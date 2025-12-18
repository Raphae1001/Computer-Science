// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

    @R2     
    M=0     // R2 = 0

    @R0
    D=M     
    @END
    D;JEQ   // Si R0 == 0, fini

    @R1
    D=M     
    @END
    D;JEQ   // Si R1 == 0, fini

(LOOP)      
    @R0
    D=M     
    
    @R2
    M=D+M   // R2 = R2 + R0

    @R1
    M=M-1   // R1 = R1 - 1

    D=M     
    @LOOP
    D;JGT   // Si R1 > 0, on boucle

(END)
    @END
    0;JMP