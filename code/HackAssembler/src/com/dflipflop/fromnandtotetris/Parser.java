package com.dflipflop.fromnandtotetris;

import java.io.*;
import java.util.Map;

public class Parser {

    BufferedReader bufferedReader ;
    InputStream inputStream ;
    Command currentCommand ;
    String currentCommandString ;
    String futureCommandString ;

    Parser(InputStream inputStream) {
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
            if(currentCommandString.contains("//"))
                currentCommandString = currentCommandString.split("//")[0].trim() ;
            System.out.println("Processing : " + currentCommandString);
    }

    public Command commandType()
    {
        if(currentCommandString.startsWith("@")) currentCommand =  new A_Command(currentCommandString.split("@")[1]) ;
        else if (currentCommandString.startsWith("(") && currentCommandString.endsWith(")")) currentCommand =  new L_Command(currentCommandString.substring(1,currentCommandString.length()-1)) ;
        else if (currentCommandString.startsWith("//") || currentCommandString.isEmpty()) currentCommand = new NoopCommand() ;
        else {
            if(currentCommandString.contains("="))
            {
                String dest = currentCommandString.split("=")[0] ;
                if(currentCommandString.split("=")[1].contains(";"))
                {
                    String comp = currentCommandString.split("=")[1].split(";")[0] ;
                    String jump = currentCommandString.split("=")[1].split(";")[1] ;
                    currentCommand = new C_Command(dest,comp,jump) ;
                }
                else {
                    String comp = currentCommandString.split("=")[1] ;
                    currentCommand = new C_Command(dest,comp,"") ;
                }
            }
            else if(currentCommandString.contains(";"))
            {
                String comp = currentCommandString.split(";")[0] ;
                String jump = currentCommandString.split(";")[1] ;
                currentCommand =  new C_Command("",comp,jump) ;
            }
            else {
                currentCommand =  new C_Command("",currentCommandString,"") ;
            }
        }
        return currentCommand ;
    }

    public String symbol() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("A_Command"))
        {
            A_Command command = (A_Command)currentCommand ;
            return command.symbol ;
        }
        else if(currentCommand.getClass().getSimpleName().equals("L_Command")){
            L_Command command = (L_Command)currentCommand ;
            return command.symbol ;
        }
        else throw new InvalidOperationException("symbol is only callable for A_Command or L_Command types") ;
    }

    public String dest() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("C_Command"))
        {
            C_Command command = (C_Command)currentCommand ;
            return  command.dest;
        }
        else throw new InvalidOperationException("dest is only callable for C_Command") ;
    }

    public String comp() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("C_Command"))
        {
            C_Command command = (C_Command)currentCommand ;
            return  command.comp;
        }
        else throw new InvalidOperationException("comp is only callable for C_Command") ;
    }

    public String jump() throws InvalidOperationException {
        if(currentCommand.getClass().getSimpleName().equals("C_Command"))
        {
            C_Command command = (C_Command)currentCommand ;
            return  command.jump;
        }
        else throw new InvalidOperationException("jump is only callable for C_Command") ;
    }

}
