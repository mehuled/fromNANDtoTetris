package com.dflipflop.fromnandtotetris;

import java.io.*;

public class Main {

    static String fileName ;

    public static void main(String[] args) throws IOException, InvalidOperationException {


        String inputFilePath = args[0] ;
        String outputFilePath = inputFilePath.replace(".vm",".asm") ;
        fileName = inputFilePath.split("/")[inputFilePath.split("/").length-1].split("\\.")[0] ;
        System.out.println("Input file name is : " + fileName);
        File file = new File(outputFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        VMParser vmParser = new VMParser(new FileInputStream(inputFilePath)) ;
        CodeWriter codeWriter = new CodeWriter(new FileOutputStream(outputFilePath)) ;

        while (vmParser.hasMoreCommands())
        {
            vmParser.advance();
            VMCommand currentCommandType = vmParser.commandType() ;
            if(currentCommandType.getClass().getSimpleName().equals("C_ARITHMETIC")) {
                C_ARITHMETIC command = (C_ARITHMETIC)vmParser.currentCommand ;
                codeWriter.writeArithmetic(command.operation);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_PUSH"))
            {
                C_PUSH command = (C_PUSH)vmParser.currentCommand ;
                codeWriter.writePushPop(command);
            }

            else if(currentCommandType.getClass().getSimpleName().equals("C_POP"))
            {
                C_POP command = (C_POP) vmParser.currentCommand ;
                codeWriter.writePushPop(command);
            }
        }

        codeWriter.close();
    }
}
