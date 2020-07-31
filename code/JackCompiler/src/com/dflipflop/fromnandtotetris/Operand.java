package com.dflipflop.fromnandtotetris;

public class Operand {

    String symbol ;

    Operand(char c) throws InvalidOperationException {
        if(c=='+' || c== '-' || c=='*' || c=='/' || c=='&' || c=='|' || c=='<' || c=='>' || c=='=')
            symbol = String.valueOf(c);
        else throw new InvalidOperationException("Invalid Operand " + c) ;
    }
}
