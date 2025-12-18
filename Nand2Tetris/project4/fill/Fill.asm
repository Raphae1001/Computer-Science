// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen.
// When no key is pressed, the program clears the screen.

(RESTART)
    @SCREEN
    D=A
    @ptr
    M=D     // ptr = adresse écran

    @KBD
    D=M     // Lire clavier
    @SET_WHITE
    D;JEQ   // Si pas de touche, blanc

(SET_BLACK)
    @value
    M=-1    // Noir
    @DRAW
    0;JMP

(SET_WHITE)
    @value
    M=0     // Blanc

(DRAW)
    @value
    D=M
    
    @ptr
    A=M
    M=D     // Colorier pixel

    @ptr
    M=M+1   // Pixel suivant

    @ptr
    D=M
    @24576
    D=D-A   
    @RESTART
    D;JEQ   // Si fin écran, recommencer

    @DRAW
    0;JMP   // Sinon continuer