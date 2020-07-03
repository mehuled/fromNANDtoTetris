package com.dflipflop.fromnandtotetris;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class CodeWriter {

    OutputStream outputStream ;
    BufferedWriter bufferedWriter ;
    static int trueIndex = 0 ;
    static int continueIndex = 0 ;
    static Stack<String> functionStack ;
    static int returnIndex = 0 ;

    CodeWriter(OutputStream outputStream) {
        this.outputStream = outputStream ;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream) ;
        bufferedWriter = new BufferedWriter(outputStreamWriter) ;
        functionStack = new Stack<String>();
        functionStack.push("bootstrap") ;
    }

    private void addPreOperations1Operand(ArrayList<String> byteCode)
    {
        byteCode.add("@SP") ;
        byteCode.add("M=M-1") ;
        byteCode.add("A=M") ;
        byteCode.add("D=M") ;
    }
    private void addPreOperations2Operand(ArrayList<String> byteCode)
    {
        byteCode.add("@SP") ;
        byteCode.add("M=M-1") ;
        byteCode.add("A=M") ;
        byteCode.add("D=M") ;
        byteCode.add("@SP") ;
        byteCode.add("M=M-1") ;
        byteCode.add("A=M") ;
    }

    private void writeDRegisterOnStack(ArrayList<String> byteCode){
        byteCode.add("@SP") ;
        byteCode.add("A=M") ;
        byteCode.add("M=D") ;
        byteCode.add("@SP") ;
        byteCode.add("M=M+1") ;
    }

    private void addFalseInstructions(ArrayList<String> byteCode)
    {
//        byteCode.add("D=-1") ;
        byteCode.add("@0") ;
        byteCode.add("D=A") ;
        byteCode.add("@SP") ;
        byteCode.add("A=M") ;
        byteCode.add("M=D") ;
        byteCode.add("@SP") ;
        byteCode.add("M=M+1") ;
    }

    private void addTrueInstructions(ArrayList<String> byteCode)
    {
//        byteCode.add("@0") ;
//        byteCode.add("D=A") ;
        byteCode.add("D=-1") ;
        byteCode.add("@SP") ;
        byteCode.add("A=M") ;
        byteCode.add("M=D") ;
        byteCode.add("@SP") ;
        byteCode.add("M=M+1") ;
    }

    public void writeArithmetic(String command) throws InvalidOperationException, IOException {

        bufferedWriter.write("// " + command);
        bufferedWriter.newLine();
        System.out.println("// " + command);

        ArrayList<String> byteCode = new ArrayList<>() ;

        if (command.equals("add"))
        {
            addPreOperations2Operand(byteCode);
            byteCode.add("D=D+M") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));


        }
        else if(command.equals("sub"))
        {
            addPreOperations2Operand(byteCode);
            byteCode.add("D=M-D") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.equals("neg"))
        {
            addPreOperations1Operand(byteCode) ;
            byteCode.add("D=-D") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.equals("eq"))
        {
            String trueLabel = "TRUE_" + trueIndex ;
            String continueLabel = "CONTINUE_" + continueIndex ;
            trueIndex++ ;
            continueIndex++ ;
            addPreOperations2Operand(byteCode);
            byteCode.add("D=D-M") ;
            byteCode.add("@" + trueLabel );
            byteCode.add("D;JEQ");
            addFalseInstructions(byteCode) ;
            byteCode.add("@" + continueLabel) ;
            byteCode.add("0;JMP") ;
            byteCode.add("(" + trueLabel + ")") ;
            addTrueInstructions(byteCode);
            byteCode.add("(" + continueLabel + ")") ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.equals("gt"))
        {
            String trueLabel = "TRUE_" + trueIndex ;
            String continueLabel = "CONTINUE_" + continueIndex ;
            trueIndex++ ;
            continueIndex++ ;
            addPreOperations2Operand(byteCode);
            byteCode.add("D=M-D") ;
            byteCode.add("@" + trueLabel );
            byteCode.add("D;JGT");
            addFalseInstructions(byteCode) ;
            byteCode.add("@" + continueLabel) ;
            byteCode.add("0;JMP") ;
            byteCode.add("(" + trueLabel + ")") ;
            addTrueInstructions(byteCode);
            byteCode.add("(" + continueLabel + ")") ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));


        }
        else if(command.equals("lt"))
        {
            String trueLabel = "TRUE_" + trueIndex ;
            String continueLabel = "CONTINUE_" + continueIndex ;
            trueIndex++ ;
            continueIndex++ ;
            addPreOperations2Operand(byteCode);
            byteCode.add("D=M-D") ;
            byteCode.add("@" + trueLabel );
            byteCode.add("D;JLT");
            addFalseInstructions(byteCode) ;
            byteCode.add("@" + continueLabel) ;
            byteCode.add("0;JMP") ;
            byteCode.add("(" + trueLabel + ")") ;
            addTrueInstructions(byteCode);
            byteCode.add("(" + continueLabel + ")") ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.equals("and"))
        {
            addPreOperations2Operand(byteCode);
            byteCode.add("D=D&M") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));


        }
        else if(command.equals("or"))
        {
            addPreOperations2Operand(byteCode);
            byteCode.add("D=D|M") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));


        }
        else if(command.equals("not"))
        {
            addPreOperations1Operand(byteCode);
            byteCode.add("D=!D") ;
            writeDRegisterOnStack(byteCode) ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else throw new InvalidOperationException("Invalid Arithmetic command " + command) ;

    }

    public void writePushPop(C_PUSH command) throws IOException, InvalidOperationException {

        bufferedWriter.write("// " + "push " + command.segment + " " + command.index);
        bufferedWriter.newLine();
        System.out.println("// " + "push " + command.segment + " " + command.index);


        if(command.segment.equals("local") || command.segment.equals("argument") || command.segment.equals("this") || command.segment.equals("that") ){
            ArrayList<String> byteCode = new ArrayList<String>();
            byteCode.add("@" + command.index);
            byteCode.add("D=A");
            byteCode.add("@" + VMCommand.getMemorySegmentPointerRegisterName(command.segment));
            byteCode.add("D=M+D");
            byteCode.add("A=D") ;
            byteCode.add("D=M") ;
            byteCode.add("@SP");
            byteCode.add("A=M");
            byteCode.add("M=D");
            byteCode.add("@SP");
            byteCode.add("M=M+1");
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.segment.equals("temp"))
        {
            ArrayList<String> byteCode = new ArrayList<String>();
            byteCode.add("@" + command.index);
            byteCode.add("D=A");
            byteCode.add("@5");
            byteCode.add("D=A+D");
            byteCode.add("A=D") ;
            byteCode.add("D=M");
            byteCode.add("@SP");
            byteCode.add("A=M");
            byteCode.add("M=D");
            byteCode.add("@SP");
            byteCode.add("M=M+1");
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));
        }

        else if(command.segment.equals("constant"))
        {
            ArrayList<String> byteCode = new ArrayList<String>() ;
            byteCode.add("@" + command.index) ;
            byteCode.add("D=A") ;
            byteCode.add("@SP") ;
            byteCode.add("A=M") ;
            byteCode.add("M=D");
            byteCode.add("@SP") ;
            byteCode.add("M=M+1");
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.segment.equals("static"))
        {
            ArrayList<String> byteCode = new ArrayList<String>() ;
            byteCode.add("@" + Main.fileName + "." + command.index) ;
            byteCode.add("D=M");
            byteCode.add("@SP") ;
            byteCode.add("A=M") ;
            byteCode.add("M=D") ;
            byteCode.add("@SP") ;
            byteCode.add("M=M+1") ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.segment.equals("pointer"))
        {
            if(command.index==0)
            {
                ArrayList<String> byteCode = new ArrayList<String>() ;
                byteCode.add("@THIS") ;
                byteCode.add("D=M") ;
                byteCode.add("@SP") ;
                byteCode.add("A=M" );
                byteCode.add("M=D") ;
                byteCode.add("@SP") ;
                byteCode.add("M=M+1") ;
                bufferedWriter.write(String.join("\n",byteCode));
                bufferedWriter.newLine();
                System.out.println(String.join("\n",byteCode));

            }
            else if (command.index==1)
            {
                ArrayList<String> byteCode = new ArrayList<String>() ;
                byteCode.add("@THAT") ;
                byteCode.add("D=M") ;
                byteCode.add("@SP") ;
                byteCode.add("A=M" );
                byteCode.add("M=D") ;
                byteCode.add("@SP") ;
                byteCode.add("M=M+1") ;
                bufferedWriter.write(String.join("\n",byteCode));
                bufferedWriter.newLine();
                System.out.println(String.join("\n",byteCode));

            }
            else throw new InvalidOperationException("Invalid Index for Pointer Memory Segment" + command.index);
        }
        else throw new InvalidOperationException("Invalid Command Segment" + command.segment) ;

    }

    public void writePushPop(C_POP command) throws InvalidOperationException, IOException {

        bufferedWriter.write("// " + "pop " + command.segment + " " + command.index);
        bufferedWriter.newLine();
        System.out.println("// " + "pop " + command.segment + " " + command.index);


        if(command.segment.equals("local") || command.segment.equals("argument") || command.segment.equals("this") || command.segment.equals("that")) {
            ArrayList<String> byteCode = new ArrayList<String>();
            byteCode.add("@" + command.index);
            byteCode.add("D=A");
            byteCode.add("@" + VMCommand.getMemorySegmentPointerRegisterName(command.segment));
            byteCode.add("D=M+D");
            byteCode.add("@addr");
            byteCode.add("M=D");
            byteCode.add("@SP");
            byteCode.add("M=M-1");
            byteCode.add("A=M");
            byteCode.add("D=M");
            byteCode.add("@addr");
            byteCode.add("A=M");
            byteCode.add("M=D");
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }

        else if(command.segment.equals("temp"))
        {
            ArrayList<String> byteCode = new ArrayList<String>();
            byteCode.add("@" + command.index);
            byteCode.add("D=A");
            byteCode.add("@5");
            byteCode.add("D=A+D");
            byteCode.add("@addr");
            byteCode.add("M=D");
            byteCode.add("@SP");
            byteCode.add("M=M-1");
            byteCode.add("A=M");
            byteCode.add("D=M");
            byteCode.add("@addr");
            byteCode.add("A=M");
            byteCode.add("M=D");
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));
        }
        else if(command.segment.equals("static"))
        {
            ArrayList<String> byteCode = new ArrayList<String>() ;
            byteCode.add("@SP") ;
            byteCode.add("M=M-1");
            byteCode.add("A=M") ;
            byteCode.add("D=M") ;
            byteCode.add("@" + Main.fileName + "." + command.index) ;
            byteCode.add("M=D") ;
            bufferedWriter.write(String.join("\n",byteCode));
            bufferedWriter.newLine();
            System.out.println(String.join("\n",byteCode));

        }
        else if(command.segment.equals("pointer"))
        {
            if(command.index==0)
            {
                ArrayList<String> byteCode = new ArrayList<>() ;
                byteCode.add("@SP") ;
                byteCode.add("M=M-1") ;
                byteCode.add("A=M") ;
                byteCode.add("D=M") ;
                byteCode.add("@THIS") ;
                byteCode.add("M=D") ;
                bufferedWriter.write(String.join("\n",byteCode));
                bufferedWriter.newLine();
                System.out.println(String.join("\n",byteCode));

            }
            else if (command.index==1)
            {
                ArrayList<String> byteCode = new ArrayList<>() ;
                byteCode.add("@SP") ;
                byteCode.add("M=M-1") ;
                byteCode.add("A=M") ;
                byteCode.add("D=M") ;
                byteCode.add("@THAT") ;
                byteCode.add("M=D") ;
                bufferedWriter.write(String.join("\n",byteCode));
                bufferedWriter.newLine();
                System.out.println(String.join("\n",byteCode));

            }
            else throw new InvalidOperationException("Invalid Index for Pointer Memory Segment" + command.index);
        }
        else throw new InvalidOperationException("Invalid Command Segment" + command.segment) ;

    }

    public void close() throws IOException {
        bufferedWriter.close();
    }

    public void writeInit() throws IOException {

        bufferedWriter.write("// init");
        bufferedWriter.newLine();
        System.out.println("// init" );

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("@256");
        byteCode.add("D=A");
        byteCode.add("@SP");
        byteCode.add("M=D");
        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
        writeCall("Sys.init",0);

    }

    public void writeLabel(String label) throws IOException {

        bufferedWriter.write("// " + label);
        bufferedWriter.newLine();
        System.out.println("// " + label);

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("(" + functionStack.peek() + ":" + label + ")") ;
        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
    }

    public void writeGoto(String label) throws IOException {

        bufferedWriter.write("// goto " + label);
        bufferedWriter.newLine();
        System.out.println("// goto " + label);

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("@"  + functionStack.peek() + ":" + label) ;
        byteCode.add("0;JMP") ;
        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
    }

    public void writeIf(String label) throws IOException {

        bufferedWriter.write("// if-goto " + label);
        bufferedWriter.newLine();
        System.out.println("// if-goto " + label);

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("@SP") ;
        byteCode.add("M=M-1") ;
        byteCode.add("A=M") ;
        byteCode.add("D=M") ;
        byteCode.add("@"  + functionStack.peek() + ":" + label) ;
        byteCode.add("D;JNE") ;
        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
    }

    public void writeCall(String functionName, int noOfArgs) throws IOException {

        bufferedWriter.write("// call " + functionName + " " + noOfArgs);
        bufferedWriter.newLine();
        System.out.println("// call " + functionName + " " + noOfArgs );

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("@" + functionStack.peek() + "$" + "return" + "_" + returnIndex) ;
        byteCode.add("D=A") ;
        writeDRegisterOnStack(byteCode);

        byteCode.add("@LCL") ;
        byteCode.add("D=M") ;
        writeDRegisterOnStack(byteCode);

        byteCode.add("@ARG") ;
        byteCode.add("D=M") ;
        writeDRegisterOnStack(byteCode);

        byteCode.add("@THIS") ;
        byteCode.add("D=M") ;
        writeDRegisterOnStack(byteCode);

        byteCode.add("@THAT") ;
        byteCode.add("D=M") ;
        writeDRegisterOnStack(byteCode);

        byteCode.add("@" + noOfArgs) ;
        byteCode.add("D=A") ;
        byteCode.add("@5") ;
        byteCode.add("D=D+A") ;
        byteCode.add("@SP") ;
        byteCode.add("D=M-D") ;
        byteCode.add("@ARG") ;
        byteCode.add("M=D") ;


        byteCode.add("@SP") ;
        byteCode.add("D=M") ;
        byteCode.add("@LCL") ;
        byteCode.add("M=D") ;

        byteCode.add("@"  + functionName) ;
        byteCode.add("0;JMP") ;
        byteCode.add("(" + functionStack.peek() + "$" + "return" + "_" + returnIndex + ")") ;
        returnIndex++ ;

        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));

    }

    public void writeReturn() throws IOException {
        bufferedWriter.write("// return" );
        bufferedWriter.newLine();
        System.out.println("// return" );

        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("@LCL");
        byteCode.add("D=M");
        byteCode.add("@FRAME");
        byteCode.add("M=D");

        byteCode.add("@5");
        byteCode.add("D=A");
        byteCode.add("@FRAME");
        byteCode.add("D=M-D");
        byteCode.add("A=D");
        byteCode.add("D=M");
        byteCode.add("@" + functionStack.peek() + "$return" + "_" + returnIndex);
        byteCode.add("M=D");

        byteCode.add("@SP");
        byteCode.add("M=M-1");
        byteCode.add("A=M");
        byteCode.add("D=M");
        byteCode.add("@ARG");
        byteCode.add("A=M");
        byteCode.add("M=D");

        byteCode.add("@ARG");
        byteCode.add("D=M+1");
        byteCode.add("@SP");
        byteCode.add("M=D");

        byteCode.add("@1");
        byteCode.add("D=A");
        byteCode.add("@FRAME");
        byteCode.add("D=M-D");
        byteCode.add("A=D");
        byteCode.add("D=M");
        byteCode.add("@THAT");
        byteCode.add("M=D");

        byteCode.add("@2");
        byteCode.add("D=A");
        byteCode.add("@FRAME");
        byteCode.add("D=M-D");
        byteCode.add("A=D");
        byteCode.add("D=M");
        byteCode.add("@THIS");
        byteCode.add("M=D");

        byteCode.add("@3");
        byteCode.add("D=A");
        byteCode.add("@FRAME");
        byteCode.add("D=M-D");
        byteCode.add("A=D");
        byteCode.add("D=M");
        byteCode.add("@ARG");
        byteCode.add("M=D");

        byteCode.add("@4");
        byteCode.add("D=A");
        byteCode.add("@FRAME");
        byteCode.add("D=M-D");
        byteCode.add("A=D");
        byteCode.add("D=M");
        byteCode.add("@LCL");
        byteCode.add("M=D");

        byteCode.add("@" + functionStack.peek() + "$return"+ "_" + returnIndex);
        byteCode.add("A=M") ;
        byteCode.add("0;JMP");
        returnIndex++ ;

        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
    }

    public void writeFunction(String functionName, int localvariables) throws IOException {
        bufferedWriter.write("// function " + functionName + " " + localvariables);
        bufferedWriter.newLine();
        System.out.println("// function " + functionName + " " + localvariables);


        functionStack.push(functionName) ;
        ArrayList<String> byteCode = new ArrayList<>() ;
        byteCode.add("(" + functionName + ")") ;
        byteCode.add("@" + localvariables) ;
        byteCode.add("D=A") ;
        byteCode.add("@" + functionName + "$counter" ) ;
        byteCode.add("M=D") ;
        byteCode.add("(" + functionName  + "$loop)" ) ;
        byteCode.add("@" + functionName + "$counter") ;
        byteCode.add("D=M") ;
        byteCode.add("@" + functionName  + "$end" ) ;
        byteCode.add("D;JEQ") ;
        byteCode.add("@0") ;
        byteCode.add("D=A") ;
        byteCode.add("@SP") ;
        byteCode.add("A=M") ;
        byteCode.add("M=D") ;
        byteCode.add("@SP") ;
        byteCode.add("M=M+1") ;
        byteCode.add("@" + functionName + "$counter") ;
        byteCode.add("M=M-1") ;
        byteCode.add("@" + functionName  + "$loop" ) ;
        byteCode.add("0;JMP") ;
        byteCode.add("(" + functionName  + "$end)" ) ;
        bufferedWriter.write(String.join("\n",byteCode));
        bufferedWriter.newLine();
        System.out.println(String.join("\n",byteCode));
    }

}
