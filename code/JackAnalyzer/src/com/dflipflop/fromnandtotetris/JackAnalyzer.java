package com.dflipflop.fromnandtotetris;

import java.io.*;

public class JackAnalyzer {

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
                    CompilationEngine compilationEngine = new CompilationEngine(new FileInputStream(file.getAbsolutePath()),new FileOutputStream(inputFilePath + "/Jack" + file.getName().replace(".jack",".xml"))) ;
//                    JackTokenizer jackTokenizer = new JackTokenizer(new FileInputStream(file.getAbsolutePath())) ;
//                    jackTokenizer.writeTokensXML(new FileOutputStream(inputFilePath + "/Tokens" + file.getName().replace(".jack",".xml")));
                }
            }
        }

        else {
            String outputFilePath = inputFilePath.replace(".jack",".xml") ;
            currentClass = inputFilePath;
            CompilationEngine compilationEngine = new CompilationEngine(new FileInputStream(inputFilePath),new FileOutputStream(outputFilePath)) ;
//
//            JackTokenizer jackTokenizer = new JackTokenizer(new FileInputStream(inputFilePath)) ;
//            jackTokenizer.writeTokensXML(new FileOutputStream(outputFilePath));
        }
    }


}
