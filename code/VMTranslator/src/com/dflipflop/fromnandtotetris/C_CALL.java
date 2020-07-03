package com.dflipflop.fromnandtotetris;

public class C_CALL extends VMCommand {
    String functionName ;
    int noOfArgs ;

    C_CALL(String functionName, String noOfArgs)
    {
        this.functionName = functionName ;
        this.noOfArgs = Integer.parseInt(noOfArgs) ;
    }
}
