// push argument 1
@1
D=A
@ARG
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// pop pointer 1
@SP
M=M-1
A=M
D=M
@THAT
M=D
// push constant 0
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
// pop that 0
@0
D=A
@THAT
D=M+D
@addr
M=D
@SP
M=M-1
A=M
D=M
@addr
A=M
M=D
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// pop that 1
@1
D=A
@THAT
D=M+D
@addr
M=D
@SP
M=M-1
A=M
D=M
@addr
A=M
M=D
// push argument 0
@0
D=A
@ARG
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 2
@2
D=A
@SP
A=M
M=D
@SP
M=M+1
// sub
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=M-D
@SP
A=M
M=D
@SP
M=M+1
// pop argument 0
@0
D=A
@ARG
D=M+D
@addr
M=D
@SP
M=M-1
A=M
D=M
@addr
A=M
M=D
// MAIN_LOOP_START
(main:MAIN_LOOP_START)
// push argument 0
@0
D=A
@ARG
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// if-goto COMPUTE_ELEMENT
@SP
M=M-1
A=M
D=M
@main:COMPUTE_ELEMENT
D;JNE
// goto END_PROGRAM
@main:END_PROGRAM
0;JMP
// COMPUTE_ELEMENT
(main:COMPUTE_ELEMENT)
// push that 0
@0
D=A
@THAT
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push that 1
@1
D=A
@THAT
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=D+M
@SP
A=M
M=D
@SP
M=M+1
// pop that 2
@2
D=A
@THAT
D=M+D
@addr
M=D
@SP
M=M-1
A=M
D=M
@addr
A=M
M=D
// push pointer 1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=D+M
@SP
A=M
M=D
@SP
M=M+1
// pop pointer 1
@SP
M=M-1
A=M
D=M
@THAT
M=D
// push argument 0
@0
D=A
@ARG
D=M+D
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// push constant 1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
// sub
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=M-D
@SP
A=M
M=D
@SP
M=M+1
// pop argument 0
@0
D=A
@ARG
D=M+D
@addr
M=D
@SP
M=M-1
A=M
D=M
@addr
A=M
M=D
// goto MAIN_LOOP_START
@main:MAIN_LOOP_START
0;JMP
// END_PROGRAM
(main:END_PROGRAM)
