
ASM_FILE = test.asm
HACK_FILE = $(ASM_FILE:.asm=.hack)

MAIN = Main.py

all:
	chmod a+x Assembler

run:
	./Assembler $(ASM_FILE)

clean:
	rm -f $(HACK_FILE)
