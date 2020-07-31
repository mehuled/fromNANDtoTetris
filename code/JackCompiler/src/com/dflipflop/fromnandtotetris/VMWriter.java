package com.dflipflop.fromnandtotetris;

import java.io.*;

public class VMWriter {
    OutputStream outputStream ;
    BufferedWriter bufferedWriter ;
    enum MEMORYSEGMENT {CONSTANT,ARGUMENT,LOCAL,STATIC,THIS,THAT,POINTER,TEMP} ;
    enum ARITHMETIC {ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT}

    public VMWriter(OutputStream outputStream) throws IOException, InvalidOperationException {
        this.outputStream = outputStream ;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream) ;
        bufferedWriter = new BufferedWriter(outputStreamWriter) ;
    }

    void writePush(MEMORYSEGMENT segment, int index) throws IOException {
        bufferedWriter.write("push " + String.valueOf(segment).toLowerCase() + " " + index) ;
        bufferedWriter.newLine();
    }

    void writePop(MEMORYSEGMENT segment, int index) throws IOException {
        bufferedWriter.write("pop " + String.valueOf(segment).toLowerCase() + " " + index) ;
        bufferedWriter.newLine();
    }

    void writeArithmetic(ARITHMETIC command) throws IOException {
        bufferedWriter.write(String.valueOf(command).toLowerCase()) ;
        bufferedWriter.newLine();
    }

    void writeLabel(String label) throws IOException {
        bufferedWriter.write("label " + label) ;
        bufferedWriter.newLine();
    }

    void writeGoto(String label) throws IOException {
        bufferedWriter.write("goto " + label) ;
        bufferedWriter.newLine();
    }

    void writeIf(String label) throws IOException {
        bufferedWriter.write("if-goto " + label) ;
        bufferedWriter.newLine();
    }

    void writeCall(String name, int nArgs) throws IOException {
        bufferedWriter.write("call " + name + " " +  nArgs) ;
        bufferedWriter.newLine();
    }

    void writeFunction(String name, int nLocals) throws IOException {
        bufferedWriter.write("function " + name + " " +  nLocals) ;
        bufferedWriter.newLine();
    }

    void writeReturn() throws IOException {
        bufferedWriter.write("return") ;
        bufferedWriter.newLine();
    }

    void close() throws IOException {
        bufferedWriter.close() ;
    }

}
