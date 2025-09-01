import sys
import os
from vm_translator import VmTranslator  # Ensure this class is in vm_translator.py


def main():
    if len(sys.argv) != 2:
        print("Usage: VMtranslator <path-to-file-or-directory>")
        sys.exit(1)

    input_path = sys.argv[1]

    if not os.path.exists(input_path):
        print(f"Error: Path '{input_path}' does not exist.")
        sys.exit(1)

    # Initialize the translator
    translator = VmTranslator()

    if os.path.isfile(input_path):
        # If it's a single VM file
        if not input_path.endswith(".vm"):
            print("Error: The input file must have a .vm extension.")
            sys.exit(1)

        output_path = input_path.replace(".vm", ".asm")
        print(f"Processing file: {input_path} -> {output_path}")
        translate_file(translator, input_path, output_path)

    elif os.path.isdir(input_path):
        # If it's a directory containing VM files
        output_path = os.path.join(input_path, os.path.basename(input_path) + ".asm")
        print(f"Processing directory: {input_path} -> {output_path}")

        vm_files = [os.path.join(input_path, f) for f in os.listdir(input_path) if f.endswith(".vm")]
        if not vm_files:
            print(f"Error: No .vm files found in directory '{input_path}'.")
            sys.exit(1)

        # Create or overwrite the output ASM file
        with open(output_path, "w") as output_file:
            # Add the bootstrap code at the beginning
            bootstrap_code = translator.write_bootstrap()
            for line in bootstrap_code:
                output_file.write(line + "\n")

        # Translate each VM file in the directory
        for vm_file in vm_files:
            print(f"Processing file: {vm_file}")
            translate_file(translator, vm_file, output_path)

    else:
        print("Error: The input must be a .vm file or a directory containing .vm files.")
        sys.exit(1)


def translate_file(translator, input_file, output_file):
    """
    Translates a .vm file and appends the result to the .asm file.
    """
    with open(input_file, "r") as vm_file:
        lines = vm_file.readlines()

    with open(output_file, "a") as asm_file:  # Append translated lines to the output ASM file
        for line in lines:
            line = line.strip()
            if line and not line.startswith("//"):  # Skip comments and empty lines
                try:
                    translated_lines = translator.process_line(line)
                    for translated_line in translated_lines:
                        asm_file.write(translated_line + "\n")
                except ValueError as e:
                    print(f"Error processing line '{line}': {e}")


if __name__ == "__main__":
    main()
