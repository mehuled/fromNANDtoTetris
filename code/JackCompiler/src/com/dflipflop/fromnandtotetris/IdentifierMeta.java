package com.dflipflop.fromnandtotetris;

public class IdentifierMeta {

    enum IDENTIFIERKIND {STATIC, FIELD, ARG, VAR, NONE} ;

    String type ;
    String name ;
    IDENTIFIERKIND kind ;
    int index ;

    IdentifierMeta(String name, String type, IDENTIFIERKIND kind, int index)
    {
        this.name = name ;
        this.type = type ;
        this.kind = kind ;
        this.index = index ;
    }

}
