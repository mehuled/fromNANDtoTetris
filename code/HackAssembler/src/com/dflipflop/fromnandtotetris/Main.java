package com.dflipflop.fromnandtotetris;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InvalidOperationException {
	// write your code here

        String inputFilePath = args[0] ;
        String outputFilePath = inputFilePath.replace(".asm",".hack") ;
        File file = new File(outputFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile()) ;
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter) ;

        SymbolTable table = new SymbolTable() ;
        Parser firsPassParser = new Parser(new FileInputStream(inputFilePath)) ;
        int ROMINSTRUCTIONNUMBER = 0;
        int RAMALLOCATIONINDEX = 16 ;
        while (firsPassParser.hasMoreCommands())
        {
            firsPassParser.advance();
            if(firsPassParser.commandType().getClass().getSimpleName().equals("A_Command"))
            {
//                A_Command aCommand = (A_Command) firsPassParser.currentCommand;
//                String symbol = aCommand.symbol ;
//                try {
//                    Integer.parseInt(symbol) ;
//                } catch (NumberFormatException nfe)
//                {
//                    System.out.println("Found Symbol : " + symbol);
//                    if(!table.contains(symbol)) {
//                        table.addEntry(symbol,RAMALLOCATIONINDEX);
//                        RAMALLOCATIONINDEX++ ;
//                    }
//                }
                ROMINSTRUCTIONNUMBER++ ;
            }
            else if(firsPassParser.commandType().getClass().getSimpleName().equals("L_Command"))
            {
                L_Command lCommand = (L_Command) firsPassParser.currentCommand;
                System.out.println("Found Label : " + lCommand.symbol);
                table.addEntry(lCommand.symbol,ROMINSTRUCTIONNUMBER);
            }
            else if(firsPassParser.commandType().getClass().getSimpleName().equals("C_Command"))
            {
                ROMINSTRUCTIONNUMBER++ ;
            }
        }



        Parser parser = new Parser(new FileInputStream(inputFilePath));
        while(parser.hasMoreCommands())
        {
            parser.advance();
            System.out.println(parser.commandType().getClass().getSimpleName());
            if(parser.commandType().getClass().getSimpleName().equals("C_Command"))
            {
                StringBuilder binaryString = new StringBuilder("111") ;
                binaryString.append(Code.comp(parser.comp())) ;
                binaryString.append(Code.dest(parser.dest())) ;
                binaryString.append(Code.jump(parser.jump())) ;
                System.out.println(binaryString.toString());
                bufferedWriter.write(binaryString.toString());
                bufferedWriter.newLine();
            }
            else if(parser.commandType().getClass().getSimpleName().equals("A_Command"))
            {
                StringBuilder binaryString = new StringBuilder("0");
                String symbol = parser.symbol() ;
                String qualifiedSymbol ;
                try{
                    qualifiedSymbol = Integer.toBinaryString(Integer.parseInt(symbol)) ;
                } catch (NumberFormatException nfe)
                {
                    if(!table.contains(symbol))
                    {
                        table.addEntry(symbol,RAMALLOCATIONINDEX);
                        RAMALLOCATIONINDEX++ ;
                    }
                    qualifiedSymbol = Integer.toBinaryString(table.getAddress(symbol)) ;
                }
                String paddedBinaryString = String.format("%15s",qualifiedSymbol).replace(" ","0");
                binaryString.append(paddedBinaryString) ;
                System.out.println(binaryString.toString());
                bufferedWriter.write(binaryString.toString());
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
