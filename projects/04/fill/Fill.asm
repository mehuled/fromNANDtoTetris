// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.


(START)
@KBD
D=M
@BLACK
D;JNE

@SCREEN
D=A
@addr
M=D
@0
D=A
@count
M=D
(LOOPA)
@8192
D=A
@count
D=D-M
@STOP
D;JLE
@addr
A=M
M=0
@addr
D=M+1
M=D
@count
M=M+1
@LOOPA
0;JEQ
@START
0;JEQ

(BLACK)
@SCREEN
D=A
@addr
M=D
@0
D=A
@count
M=D
(LOOPB)
@8192
D=A
@count
D=D-M
@STOP
D;JLE
@addr
A=M
M=-1
@addr
D=M+1
M=D
@count
M=M+1
@LOOPB
0;JEQ
(STOP)
@START
0;JEQ