package com.dflipflop.fromnandtotetris;

import java.io.*;

public class JackCompiler {

    static String currentClass ;

    public static void main(String[] args) throws IOException, InvalidOperationException {

        String inputFilePath = args[0] ;

        if(new File(inputFilePath).isDirectory())
        {
            File[] files = new File(inputFilePath).listFiles() ;
            assert files != null;
            for (File file : files)
            {
                if(file.getName().endsWith(".jack")) {
                    currentClass = file.getAbsolutePath() ;
                    CompilationEngine compilationEngine = new CompilationEngine(new FileInputStream(file.getAbsolutePath()),new FileOutputStream(inputFilePath + "/" + file.getName().replace(".jack",".vm"))) ;
                }
            }
        }

        else {
            String outputFilePath = inputFilePath.replace(".jack",".vm") ;
            currentClass = inputFilePath;
            CompilationEngine compilationEngine = new CompilationEngine(new FileInputStream(inputFilePath),new FileOutputStream(outputFilePath)) ;

        }
    }


}
