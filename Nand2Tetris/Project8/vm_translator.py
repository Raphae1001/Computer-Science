class VmTranslator:
    def __init__(self):
        self.label_count = 0
        self.return_count = 0

    def generate_unique_label(self, prefix="LABEL"):
        self.label_count += 1
        return f"{prefix}_{self.label_count}"

    def write_bootstrap(self):
        """
        Writes the bootstrap code for initializing the VM translator.
        This code sets SP = 256 and calls Sys.init.
        """
        return [
            "// Bootstrap code",
            "@256",
            "D=A",
            "@SP",
            "M=D",
            "// Call Sys.init",
            *self.write_call("Sys.init", 0)
        ]

    def process_line(self, line):
        """
        Processes a single line of VM code and returns the equivalent assembly code.
        """
        line_parts = line.split()
        command = line_parts[0]

        if command in {"push", "pop"}:
            segment, index = line_parts[1], line_parts[2]
            if command == "push":
                return self.write_push(segment, index)
            elif command == "pop":
                return self.write_pop(segment, index)
        elif command in {"add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"}:
            return self.write_arithmetic(command)
        elif command == "label":
            return self.write_label(line_parts[1])
        elif command == "goto":
            return self.write_goto(line_parts[1])
        elif command == "if-goto":
            return self.write_if(line_parts[1])
        elif command == "call":
            function_name, num_args = line_parts[1], int(line_parts[2])
            return self.write_call(function_name, num_args)
        elif command == "function":
            function_name, num_locals = line_parts[1], int(line_parts[2])
            return self.write_function(function_name, num_locals)
        elif command == "return":
            return self.write_return()
        else:
            raise ValueError(f"Unsupported command: {command}")

    def write_arithmetic(self, command):
        if command == "add":
            return self._binary_op("M=M+D")
        elif command == "sub":
            return self._binary_op("M=M-D")
        elif command == "neg":
            return self._unary_op("M=-M")
        elif command == "eq":
            return self._compare_op("JEQ")
        elif command == "gt":
            return self._compare_op("JGT")
        elif command == "lt":
            return self._compare_op("JLT")
        elif command == "and":
            return self._binary_op("M=M&D")
        elif command == "or":
            return self._binary_op("M=M|D")
        elif command == "not":
            return self._unary_op("M=!M")

    def write_push(self, segment, index):
        if segment == "constant":
            return [
                f"@{index}",
                "D=A",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1"
            ]
        elif segment in ("local", "argument", "this", "that"):
            base = self._get_segment_base(segment)
            return [
                f"@{index}",
                "D=A",
                f"@{base}",
                "A=M+D",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1"
            ]
        elif segment == "temp":
            return [
                f"@{5 + int(index)}",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1"
            ]
        elif segment == "pointer":
            pointer = "THIS" if index == "0" else "THAT"
            return [
                f"@{pointer}",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1"
            ]
        elif segment == "static":
            return [
                f"@static.{index}",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1"
            ]

    def write_pop(self, segment, index):
        if segment in ("local", "argument", "this", "that"):
            base = self._get_segment_base(segment)
            return [
                f"@{index}",
                "D=A",
                f"@{base}",
                "D=M+D",
                "@R13",
                "M=D",
                "@SP",
                "M=M-1",
                "A=M",
                "D=M",
                "@R13",
                "A=M",
                "M=D"
            ]
        elif segment == "temp":
            return [
                "@SP",
                "M=M-1",
                "A=M",
                "D=M",
                f"@{5 + int(index)}",
                "M=D"
            ]
        elif segment == "pointer":
            pointer = "THIS" if index == "0" else "THAT"
            return [
                "@SP",
                "M=M-1",
                "A=M",
                "D=M",
                f"@{pointer}",
                "M=D"
            ]
        elif segment == "static":
            return [
                "@SP",
                "M=M-1",
                "A=M",
                "D=M",
                f"@static.{index}",
                "M=D"
            ]

    def write_label(self, label):
        return [f"({label})"]

    def write_goto(self, label):
        return [f"@{label}", "0;JMP"]

    def write_if(self, label):
        return [
            "@SP",
            "M=M-1",
            "A=M",
            "D=M",
            f"@{label}",
            "D;JNE"
        ]

    def write_call(self, function_name, num_args):
        return_label = self.generate_unique_label("RETURN")
        return [
            f"@{return_label}",
            "D=A",
            "@SP",
            "A=M",
            "M=D",
            "@SP",
            "M=M+1",
            *self._save_segment("LCL"),
            *self._save_segment("ARG"),
            *self._save_segment("THIS"),
            *self._save_segment("THAT"),
            "@SP",
            "D=M",
            f"@{num_args + 5}",
            "D=D-A",
            "@ARG",
            "M=D",
            "@SP",
            "D=M",
            "@LCL",
            "M=D",
            f"@{function_name}",
            "0;JMP",
            f"({return_label})"
        ]

    def write_function(self, function_name, num_locals):
        return [
            f"({function_name})",
            *sum([self.write_push("constant", 0) for _ in range(num_locals)], [])
        ]

    def write_return(self):
        return [
            "@LCL", "D=M", "@R13", "M=D", "@5", "A=D-A", "D=M", "@R14", "M=D",
            "@SP", "M=M-1", "A=M", "D=M", "@ARG", "A=M", "M=D", "@ARG",
            "D=M+1", "@SP", "M=D", "@R13", "D=M-1", "AM=D", "D=M", "@THAT",
            "M=D", "@R13", "D=M-1", "AM=D", "D=M", "@THIS", "M=D", "@R13",
            "D=M-1", "AM=D", "D=M", "@ARG", "M=D", "@R13", "D=M-1", "AM=D",
            "D=M", "@LCL", "M=D", "@R14", "A=M", "0;JMP"
        ]

    def _binary_op(self, operation):
        return ["@SP", "M=M-1", "A=M", "D=M", "@SP", "M=M-1", "A=M", operation, "@SP", "M=M+1"]

    def _unary_op(self, operation):
        return ["@SP", "M=M-1", "A=M", operation, "@SP", "M=M+1"]

    def _compare_op(self, jump_command):
        label_true = self.generate_unique_label("TRUE")
        label_end = self.generate_unique_label("END")
        return [
            "@SP", "M=M-1", "A=M", "D=M", "@SP", "M=M-1", "A=M", "D=M-D",
            f"@{label_true}", f"D;{jump_command}", "@SP", "A=M", "M=0",
            f"@{label_end}", "0;JMP", f"({label_true})", "@SP", "A=M",
            "M=-1", f"({label_end})", "@SP", "M=M+1"
        ]

    def _save_segment(self, segment):
        return [f"@{segment}", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"]

    def _get_segment_base(self, segment):
        return {"local": "LCL", "argument": "ARG", "this": "THIS", "that": "THAT"}[segment]
