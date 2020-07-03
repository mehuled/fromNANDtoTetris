package com.dflipflop.fromnandtotetris;

public class C_FUNCTION extends VMCommand {
    String functionName ;
    int localVariables ;
    C_FUNCTION(String functionName, String localVariables)
    {
        this.functionName = functionName ;
        this.localVariables = Integer.parseInt(localVariables) ;
    }
}
