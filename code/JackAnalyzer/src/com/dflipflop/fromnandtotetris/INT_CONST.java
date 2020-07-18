package com.dflipflop.fromnandtotetris;

public class INT_CONST extends Token {

    int intVal ;

    public INT_CONST(String value) throws InvalidOperationException {
        if(isAIntConst(value)) intVal = Integer.parseInt(value) ;
        else throw new InvalidOperationException("Invalid value : " + value + " for Integer Constant") ;
    }

    public int getIntVal()
    {
        return intVal ;
    }

    public static boolean isAIntConst(String string)
    {
        try {
            Integer.parseInt(string) ;
        }
        catch (Exception e)
        {
           return false;
        }

        if(Integer.parseInt(string)<0 || Integer.parseInt(string) >32767) return false ;

        return true ;
    }

    public String toString()
    {

        return String.valueOf(intVal);
    }
}
