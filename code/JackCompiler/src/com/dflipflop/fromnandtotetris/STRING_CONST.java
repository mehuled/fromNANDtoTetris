package com.dflipflop.fromnandtotetris;

public class STRING_CONST extends Token {

    String stringVal ;

    public STRING_CONST(String value) throws InvalidOperationException {

        if(isAStringConst(value))
            stringVal = value.substring(1,value.length()-1) ;
        else throw new InvalidOperationException("Invalid value for String Constant" ) ;

    }

    public String getStringVal()
    {
        return stringVal ;
    }

    public static boolean isAStringConst(String string)
    {
        if(string.length()==1) return false ;
        if(!string.startsWith("\"") || !string.endsWith("\"")) return false ;
        string = string.substring(1,string.length()-1) ;
        for (char c : string.toCharArray())
        {
            if(c=='\"' || c=='\n')
            {
                return false ;
            }
        }
        return true ;
    }

    public String toString()
    {

        return String.valueOf(stringVal);
    }
}
