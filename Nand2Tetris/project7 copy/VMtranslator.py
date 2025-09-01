import sys

def translate_push(command):
    parts = command.split()
    segment = parts[1]
    index = parts[2]

    # Traduire la commande push en fonction du segment
    if segment == "constant":
        return f"@{index}\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"
    elif segment == "local":
        return f"@LCL\nD=M\n@{index}\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"
    elif segment == "argument":
        return f"@ARG\nD=M\n@{index}\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"
    elif segment == "this":
        return f"@THIS\nD=M\n@{index}\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"
    elif segment == "that":
        return f"@THAT\nD=M\n@{index}\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"
    else:
        raise ValueError("Unsupported segment")

def translate_pop(command):
    parts = command.split()
    segment = parts[1]
    index = parts[2]

    if segment == "local":
        return f"@LCL\nD=M\n@{index}\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n"
    elif segment == "argument":
        return f"@ARG\nD=M\n@{index}\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n"
    elif segment == "this":
        return f"@THIS\nD=M\n@{index}\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n"
    elif segment == "that":
        return f"@THAT\nD=M\n@{index}\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n"
    else:
        raise ValueError("Unsupported segment")

def translate_arithmetic(command):
    if command == "add":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nM=M+D\n"
    elif command == "sub":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n"
    elif command == "neg":
        return "@SP\nA=M-1\nM=-M\n"
    elif command == "eq":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n@EQUAL\nD;JEQ\n@SP\nA=M-1\nM=0\n(EQUAL)\n"
    elif command == "gt":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n@GREATER\nD;JGT\n@SP\nA=M-1\nM=0\n(GREATER)\n"
    elif command == "lt":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n@LESS\nD;JLT\n@SP\nA=M-1\nM=0\n(LESS)\n"
    elif command == "and":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D\n"
    elif command == "or":
        return "@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D\n"
    elif command == "not":
        return "@SP\nA=M-1\nM=!M\n"
    else:
        raise ValueError(f"Unsupported arithmetic command: {command}")

def process_vm(input_file):
    with open(input_file, 'r') as file:
        lines = file.readlines()
    
    output = []

    for line in lines:
        line = line.strip()
        if line.startswith("push"):
            output.append(translate_push(line))
        elif line.startswith("pop"):
            output.append(translate_pop(line))
        elif line in ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"]:
            output.append(translate_arithmetic(line))

    output_file = input_file.replace('.vm', '.asm')
    with open(output_file, 'w') as f:
        f.write("\n".join(output))

def main():
    if len(sys.argv) != 2:
        print("Usage: python3 VMtranslator.py <path_to_vm_file_or_directory>")
    else:
        process_vm(sys.argv[1])

if __name__ == "__main__":
    main()
