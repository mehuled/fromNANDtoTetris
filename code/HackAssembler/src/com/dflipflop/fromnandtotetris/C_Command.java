package com.dflipflop.fromnandtotetris;

public class C_Command extends Command {

    String dest ;
    String comp ;
    String jump ;

    C_Command(String dest, String comp, String jump)
    {
        this.dest = dest ;
        this.comp = comp ;
        this.jump = jump ;
    }
}
