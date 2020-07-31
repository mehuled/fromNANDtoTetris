package com.dflipflop.fromnandtotetris;

import java.util.ArrayList;

public class SYMBOL extends Token{

    char symbol ;
    static ArrayList<String> symbolsList ;
    static {
        symbolsList = new ArrayList<String>();
        symbolsList.add("{");
        symbolsList.add("}");
        symbolsList.add("(");
        symbolsList.add(")");
        symbolsList.add("[");
        symbolsList.add("]");
        symbolsList.add(".");
        symbolsList.add(",");
        symbolsList.add(";");
        symbolsList.add("+");
        symbolsList.add("-");
        symbolsList.add("*");
        symbolsList.add("/");
        symbolsList.add("&");
        symbolsList.add("|");
        symbolsList.add("<");
        symbolsList.add(">");
        symbolsList.add("=");
        symbolsList.add("~");
    }

    public SYMBOL(String value) throws InvalidOperationException {
        if(symbolsList.contains(value)) symbol = value.charAt(0) ;
        else throw new InvalidOperationException("Unable to match " + value + " to any valid Symbols") ;
    }

    public char getSymbol()
    {
        return symbol ;
    }

    public static boolean isASymbol(String c)
    {
        if(symbolsList.contains(c)) return true ;
        else return false ;
    }

    public String toString()
    {

        if(symbol=='<')
            return "&lt;" ;
        else if(symbol=='>')
            return "&gt;" ;
        else if(symbol=='&')
            return "&amp;" ;
        else return String.valueOf(symbol);
    }
}
