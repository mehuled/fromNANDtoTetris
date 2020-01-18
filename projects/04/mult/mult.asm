// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

@1
D=A
@i
M=D      // i = 1
@0
D=A
@sum
M=D      //sum = 0 

(LOOP)
@i
D=M
@R0
D=D-M
@STOP
D;JGT

@R1
D=M
@sum
M=D+M

@i
M=M+1
@LOOP
0;JEQ
(STOP)
@sum
D=M
@R2
M=D

(END)
@END
0 ; JEQ

