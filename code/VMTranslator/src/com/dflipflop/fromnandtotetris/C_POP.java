package com.dflipflop.fromnandtotetris;


public class C_POP extends VMCommand {
    String segment;
    int index;

    C_POP(String segment, int index) throws InvalidOperationException {
        this.segment = segment;
        this.index = index;
    }

}
