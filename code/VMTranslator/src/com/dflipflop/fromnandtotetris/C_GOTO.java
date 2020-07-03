package com.dflipflop.fromnandtotetris;

public class C_GOTO extends VMCommand {
    String gotoLabel ;
    C_GOTO(String gotoLabel)
    {
        this.gotoLabel = gotoLabel ;
    }
}
