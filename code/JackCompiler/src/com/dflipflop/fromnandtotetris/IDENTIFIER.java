package com.dflipflop.fromnandtotetris;

public class IDENTIFIER extends Token{

    String identifier ;

    public IDENTIFIER(String value) throws InvalidOperationException {
        if(Character.isDigit(value.charAt(0))) throw new InvalidOperationException("Identifier name cannot start with a digit. Invalid identifier " + value) ;
        for (char c: value.toCharArray()){
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c!='_') throw new InvalidOperationException("Invalid character " + c + " for identifier " + value) ;
        }
        identifier = value ;
    }

    public String getIdentifier(){
        return identifier ;
    }

    public static boolean isAIdentifier(String string)
    {
        if(Character.isDigit(string.charAt(0))) return false ;
        if(KEYWORD.isAKeyword(string)) return false ;
        for (char c: string.toCharArray()){
            if (!Character.isDigit(c) && !Character.isLetter(c) && c!='_')  return false ;
        }
        return true ;
    }

    public String toString()
    {
        return String.valueOf(identifier);
    }

}
