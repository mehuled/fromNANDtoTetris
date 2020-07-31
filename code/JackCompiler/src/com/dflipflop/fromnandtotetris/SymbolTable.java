package com.dflipflop.fromnandtotetris;

import java.util.HashMap;
import java.util.Map;
import com.dflipflop.fromnandtotetris.IdentifierMeta.IDENTIFIERKIND ;

public class SymbolTable {

    Map<String,IdentifierMeta> classMap ;
    Map<String,IdentifierMeta> subroutineMap ;
    int classStaticIdentifierIndex ;
    int classFieldIdentifierIndex ;
    int subroutineArgumentIdentifierIndex ;
    int subroutineVarIdentifierIndex ;

    SymbolTable()
    {
        classMap = new HashMap<String,IdentifierMeta>();
        subroutineMap = new HashMap<String,IdentifierMeta>();
        classStaticIdentifierIndex = 0;
        classFieldIdentifierIndex = 0 ;
    }

    void startSubroutine()
    {
        subroutineMap = new HashMap<String,IdentifierMeta>();
        subroutineArgumentIdentifierIndex = 0 ;
        subroutineVarIdentifierIndex = 0 ;
    }

    void define(String name, String type,IDENTIFIERKIND kind) throws InvalidOperationException {

        if(kind.equals(IDENTIFIERKIND.FIELD)) {
            IdentifierMeta meta = new IdentifierMeta(name, type, kind, classFieldIdentifierIndex);
            classMap.put(name, meta);
            classFieldIdentifierIndex++ ;
        }
        else if(kind.equals(IDENTIFIERKIND.STATIC)) {
            IdentifierMeta meta = new IdentifierMeta(name, type, kind, classStaticIdentifierIndex);
            classMap.put(name, meta);
            classStaticIdentifierIndex++ ;
        }
        else if(kind.equals(IDENTIFIERKIND.ARG)) {
            IdentifierMeta meta = new IdentifierMeta(name, type, kind, subroutineArgumentIdentifierIndex) ;
            subroutineMap.put(name,meta) ;
            subroutineArgumentIdentifierIndex++ ;
        }
        else if(kind.equals(IDENTIFIERKIND.VAR))
        {
            IdentifierMeta meta = new IdentifierMeta(name, type, kind, subroutineVarIdentifierIndex) ;
            subroutineMap.put(name,meta) ;
            subroutineVarIdentifierIndex++ ;
        }
        else throw new InvalidOperationException("Invalid kind " + kind + " for identifier") ;
    }

    int VarCount(IDENTIFIERKIND kind) throws InvalidOperationException {
        if(kind.equals(IDENTIFIERKIND.FIELD)) return classFieldIdentifierIndex ;
        else if (kind.equals(IDENTIFIERKIND.STATIC)) return classStaticIdentifierIndex ;
        else if(kind.equals(IDENTIFIERKIND.ARG)) return subroutineArgumentIdentifierIndex ;
        else if(kind.equals(IDENTIFIERKIND.VAR)) return subroutineVarIdentifierIndex ;
        else throw new InvalidOperationException("Invalid kind " + kind + " for identifier") ;
    }

    IDENTIFIERKIND KindOf(String name){
        if(classMap.containsKey(name)) return classMap.get(name).kind ;
        else if(subroutineMap.containsKey(name)) return subroutineMap.get(name).kind ;
        else return IDENTIFIERKIND.NONE ;
    }

    String TypeOf(String name) throws InvalidOperationException {
        if(classMap.containsKey(name)) return classMap.get(name).type ;
        else if(subroutineMap.containsKey(name)) return subroutineMap.get(name).type ;
        else throw new InvalidOperationException("Identifier not found " + name) ;
    }

    int IndexOf(String name) throws InvalidOperationException {
        if(classMap.containsKey(name)) return classMap.get(name).index ;
        else if(subroutineMap.containsKey(name)) return subroutineMap.get(name).index ;
        else throw new InvalidOperationException("Identifier not found " + name) ;
    }

    boolean isADefinedIdentifier(String name){
        return this.classMap.containsKey(name) || this.subroutineMap.containsKey(name) ;
    }

}
