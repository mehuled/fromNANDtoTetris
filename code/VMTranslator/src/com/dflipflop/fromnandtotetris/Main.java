package com.dflipflop.fromnandtotetris;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Main {

    static String fileName ;

    public static void main(String[] args) throws IOException, InvalidOperationException {


        String inputFilePath = args[0] ;

        if(new File(inputFilePath).isDirectory())
        {
            File[] files = new File(inputFilePath).listFiles() ;
            String outputFilePath = inputFilePath + "/" + inputFilePath.split("/")[inputFilePath.split("/").length-1] + ".asm" ;
            CodeWriter codeWriter = new CodeWriter(new FileOutputStream(outputFilePath)) ;
            codeWriter.writeInit();

            assert files != null;
            for (File file : files)
            {
                if(file.getName().endsWith(".vm")) {
                    fileName = file.getName().split("\\.")[0] ;
                    System.out.println("filesname is " + fileName);
                    VMParser vmParser = new VMParser(new FileInputStream(file.getAbsolutePath()));
                    translate(vmParser, codeWriter);
                }
            }
            codeWriter.close();

        }

        else {

            String outputFilePath = inputFilePath.replace(".vm",".asm") ;
            fileName = inputFilePath.split("/")[inputFilePath.split("/").length-1].split("\\.")[0] ;
            System.out.println("Input file name is : " + fileName);
            File file = new File(outputFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            VMParser vmParser = new VMParser(new FileInputStream(inputFilePath)) ;
            CodeWriter codeWriter = new CodeWriter(new FileOutputStream(outputFilePath)) ;
            translate(vmParser,codeWriter);
            codeWriter.close();

        }

    }

    public static void translate(VMParser vmParser, CodeWriter codeWriter) throws IOException, InvalidOperationException {

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

            else if(currentCommandType.getClass().getSimpleName().equals("C_LABEL"))
            {
                C_LABEL command = (C_LABEL) vmParser.currentCommand ;
                codeWriter.writeLabel(command.labelName);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_GOTO"))
            {
                C_GOTO command = (C_GOTO) vmParser.currentCommand ;
                codeWriter.writeGoto(command.gotoLabel);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_IF"))
            {
                C_IF command = (C_IF) vmParser.currentCommand ;
                codeWriter.writeIf(command.gotoLabel);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_FUNCTION"))
            {
                C_FUNCTION command = (C_FUNCTION) vmParser.currentCommand ;
                codeWriter.writeFunction(command.functionName,command.localVariables);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_CALL"))
            {
                C_CALL command = (C_CALL) vmParser.currentCommand ;
                codeWriter.writeCall(command.functionName,command.noOfArgs);
            }
            else if(currentCommandType.getClass().getSimpleName().equals("C_RETURN"))
            {
                codeWriter.writeReturn();
            }
        }

    }

}
