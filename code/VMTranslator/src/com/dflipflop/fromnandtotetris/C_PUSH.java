package com.dflipflop.fromnandtotetris;


public class C_PUSH extends VMCommand {
    String segment;
    int index;

    C_PUSH(String segment, int index) throws InvalidOperationException {
        this.segment = segment;
        this.index = index;
    }

}
