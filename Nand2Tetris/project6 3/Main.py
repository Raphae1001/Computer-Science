import sys

def process_asm(input_file):

    with open(input_file, 'r') as f:
        lines = f.readlines()


    binary_instructions = []

    for line in lines:
        line = line.strip()  
        if line.startswith('@'):

            value = line[1:]
            if value.isdigit():
                binary_instructions.append(format(int(value), '016b'))  
            else:

                pass
        elif '=' in line or ';' in line:

            binary_instructions.append("1110000000000000")  


    output_file = input_file.replace('.asm', '.hack')
    with open(output_file, 'w') as f:
        f.write("\n".join(binary_instructions))

if __name__ == "__main__":

    if len(sys.argv) != 2:
        print("Usage: python3 Main.py <path_to_asm_file>")
    else:

        process_asm(sys.argv[1])
