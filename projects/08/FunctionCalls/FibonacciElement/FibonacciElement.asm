// init
@256
D=A
@SP
M=D
// call Sys.init 0
@bootstrap$return_0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(bootstrap$return_0)
// function Main.fibonacci 0
(Main.fibonacci)
@0
D=A
@Main.fibonacci$counter
M=D
(Main.fibonacci$loop)
@Main.fibonacci$counter
D=M
@Main.fibonacci$end
D;JEQ
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@Main.fibonacci$counter
M=M-1
@Main.fibonacci$loop
0;JMP
(Main.fibonacci$end)
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
// lt
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=M-D
@TRUE_0
D;JLT
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@CONTINUE_0
0;JMP
(TRUE_0)
D=-1
@SP
A=M
M=D
@SP
M=M+1
(CONTINUE_0)
// if-goto IF_TRUE
@SP
M=M-1
A=M
D=M
@Main.fibonacci:IF_TRUE
D;JNE
// goto IF_FALSE
@Main.fibonacci:IF_FALSE
0;JMP
// IF_TRUE
(Main.fibonacci:IF_TRUE)
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
// return
@LCL
D=M
@FRAME
M=D
@5
D=A
@FRAME
D=M-D
A=D
D=M
@Main.fibonacci$return_1
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@1
D=A
@FRAME
D=M-D
A=D
D=M
@THAT
M=D
@2
D=A
@FRAME
D=M-D
A=D
D=M
@THIS
M=D
@3
D=A
@FRAME
D=M-D
A=D
D=M
@ARG
M=D
@4
D=A
@FRAME
D=M-D
A=D
D=M
@LCL
M=D
@Main.fibonacci$return_1
A=M
0;JMP
// IF_FALSE
(Main.fibonacci:IF_FALSE)
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
// call Main.fibonacci 1
@Main.fibonacci$return_2
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Main.fibonacci
0;JMP
(Main.fibonacci$return_2)
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
// call Main.fibonacci 1
@Main.fibonacci$return_3
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Main.fibonacci
0;JMP
(Main.fibonacci$return_3)
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
// return
@LCL
D=M
@FRAME
M=D
@5
D=A
@FRAME
D=M-D
A=D
D=M
@Main.fibonacci$return_4
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@1
D=A
@FRAME
D=M-D
A=D
D=M
@THAT
M=D
@2
D=A
@FRAME
D=M-D
A=D
D=M
@THIS
M=D
@3
D=A
@FRAME
D=M-D
A=D
D=M
@ARG
M=D
@4
D=A
@FRAME
D=M-D
A=D
D=M
@LCL
M=D
@Main.fibonacci$return_4
A=M
0;JMP
// function Sys.init 0
(Sys.init)
@0
D=A
@Sys.init$counter
M=D
(Sys.init$loop)
@Sys.init$counter
D=M
@Sys.init$end
D;JEQ
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@Sys.init$counter
M=M-1
@Sys.init$loop
0;JMP
(Sys.init$end)
// push constant 4
@4
D=A
@SP
A=M
M=D
@SP
M=M+1
// call Main.fibonacci 1
@Sys.init$return_5
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Main.fibonacci
0;JMP
(Sys.init$return_5)
// WHILE
(Sys.init:WHILE)
// goto WHILE
@Sys.init:WHILE
0;JMP
