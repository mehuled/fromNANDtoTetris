package com.dflipflop.fromnandtotetris;

import java.util.ArrayList;

public class KEYWORD extends Token  {

    String keyword ;
    static ArrayList<String> keywordsList ;
    static {
        keywordsList = new ArrayList<>() ;
        keywordsList.add("class");
        keywordsList.add("method") ;
        keywordsList.add("function") ;
        keywordsList.add("constructor") ;
        keywordsList.add("int") ;
        keywordsList.add("boolean");
        keywordsList.add("char");
        keywordsList.add("void");
        keywordsList.add("var");
        keywordsList.add("static");
        keywordsList.add("field");
        keywordsList.add("let") ;
        keywordsList.add("do") ;
        keywordsList.add("if") ;
        keywordsList.add("else");
        keywordsList.add("while");
        keywordsList.add("return");
        keywordsList.add("true");
        keywordsList.add("false");
        keywordsList.add("null");
        keywordsList.add("this");
    }

    public KEYWORD(String value) throws InvalidOperationException {
        if(keywordsList.contains(value)) keyword = value;
        else throw new InvalidOperationException("Invalid Keyword " + value) ;
    }

    public String getKeyword() {
        return keyword ;
    }

    public static boolean isAKeyword(String string)
    {
        return keywordsList.contains(string) ;
    }

    public String toString()
    {

        return String.valueOf(keyword);
    }
}
