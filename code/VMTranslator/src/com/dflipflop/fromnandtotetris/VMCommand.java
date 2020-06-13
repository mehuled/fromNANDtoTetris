package com.dflipflop.fromnandtotetris;

public class VMCommand {

    public static String getMemorySegmentPointerRegisterName(String segment) throws InvalidOperationException {
        switch (segment)
        {
            case "local" : return  "LCL" ;
            case "argument" : return "ARG" ;
            case "this" : return "THIS" ;
            case "that"  : return "THAT" ;
            case "temp" : return "5" ;
            default : throw new InvalidOperationException("Unable to find Memory Segment Register for " + segment ) ;
        }

    }

}
