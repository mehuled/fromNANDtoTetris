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
            if(currentCommandString.equals("return"))
            {
              currentCommand = new C_RETURN() ;
            }
            else
            currentCommand = new C_ARITHMETIC(currentCommandString) ;
        }
        else {
            String[] commandElements = currentCommandString.split(" ") ;

            if(commandElements[0].equals("label"))
            {
                currentCommand = new C_LABEL(commandElements[1]) ;
            }
            else if(commandElements[0].equals("goto"))
            {
                currentCommand = new C_GOTO(commandElements[1]) ;
            }
            else if(commandElements[0].equals("if-goto"))
            {
                currentCommand = new C_IF(commandElements[1]) ;
            }
            else if(commandElements[0].equals("call"))
            {
                currentCommand = new C_CALL(commandElements[1],commandElements[2]) ;
            }
            else if(commandElements[0].equals("function"))
            {
                currentCommand = new C_FUNCTION(commandElements[1],commandElements[2]) ;
            }
            else if (commandElements[0].equals("push"))
                    currentCommand = new C_PUSH(commandElements[1], Integer.parseInt(commandElements[2]));

            else if (commandElements[0].equals("pop")) {
                    if (commandElements[1].equals("constant"))
                        throw new InvalidOperationException("Pop operations cannot be performed on Constant Memory Segment");
                    currentCommand = new C_POP(commandElements[1], Integer.parseInt(commandElements[2]));
                }
            else {
                throw new InvalidOperationException("Unable to parse command : " + currentCommandString) ;
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
