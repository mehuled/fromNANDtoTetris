package com.dflipflop.fromnandtotetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VMParser {

    BufferedReader bufferedReader ;
    InputStream inputStream ;
    VMCommand currentCommand ;
    String currentCommandString ;
    String futureCommandString ;

    VMParser(InputStream inputStream) {
        this.inputStream = inputStream ;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream) ;
        bufferedReader = new BufferedReader(inputStreamReader) ;
    }


    public boolean hasMoreCommands() throws IOException {
        return (futureCommandString = bufferedReader.readLine())!=null ;

    }

    public void advance() throws IOException, InvalidOperationException {

        if(futureCommandString==null)
            throw new InvalidOperationException("Unable to read file. End of FileStream") ;
        currentCommandString = futureCommandString.trim() ;
        System.out.println("Current Command String is : " + currentCommandString);
        if(currentCommandString.contains("//") && !currentCommandString.startsWith("//"))
            currentCommandString = currentCommandString.split("//")[0].trim() ;
        System.out.println("Processing : " + currentCommandString);
    }

    public VMCommand commandType() throws InvalidOperationException {
        if (currentCommandString.startsWith("//") || currentCommandString.isEmpty())
        {
            currentCommand = new NoopCommand() ;
        }
        else if(!currentCommandString.contains(" "))
        {
            currentCommand = new C_ARITHMETIC(currentCommandString) ;
        }
        else {
            String[] commandElements = currentCommandString.split(" ") ;
            String memorySegment =commandElements[1] ;
            if(commandElements[0].equals("push"))
            currentCommand = new C_PUSH(memorySegment,Integer.parseInt(commandElements[2])) ;
            else if(commandElements[0].equals("pop"))
            {
                if (memorySegment.equals("constant"))
                    throw new InvalidOperationException("Pop operations cannot be performed on Constant Memory Segment");
                currentCommand = new C_POP(memorySegment,Integer.parseInt(commandElements[2])) ;
            }
        }
        return currentCommand ;
    }

    public String arg1() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("C_ARITHMETIC")) {
        C_ARITHMETIC command = (C_ARITHMETIC)currentCommand ;
        return command.operation ;}
        else throw new InvalidOperationException("Invalid operation arg1 on " + currentCommand.getClass().getSimpleName() + " command type") ;
    }

    public int arg2() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("C_POP") || currentCommand.getClass().getSimpleName().equals("C_POP"))
        {
            if(currentCommand.getClass().getSimpleName().equals("C_POP")) {
                C_POP command = (C_POP) currentCommand;
                return command.index ;
            }
            else {
                C_PUSH command = (C_PUSH) currentCommand ;
                return command.index ;
            }

        }
        else throw new InvalidOperationException("Invalid operation arg2 on "  + currentCommand.getClass().getSimpleName() + " command type") ;
    }

}
