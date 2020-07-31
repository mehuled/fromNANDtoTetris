package com.dflipflop.fromnandtotetris;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.dflipflop.fromnandtotetris.IdentifierMeta.IDENTIFIERKIND ;
import javafx.util.Pair;

public class CompilationEngine {

    JackTokenizer jackTokenizer ;
    SymbolTable symbolTable ;
    VMWriter vmWriter ;
    static String currentClass ;
    static String currentSubroutine ;
    static String currentSubroutineReturnType ;
    int labelIndex ;
    static String currentSubroutineType ;

    public CompilationEngine(InputStream inputStream, OutputStream outputStream) throws IOException, InvalidOperationException {
        vmWriter = new VMWriter(outputStream) ;
        jackTokenizer = new JackTokenizer(inputStream) ;
        symbolTable = new SymbolTable() ;
        compileClass() ;
        vmWriter.close();
    }

    public void compileClass() throws IOException, InvalidOperationException {
        jackTokenizer.advance(); // class
        currentClass = jackTokenizer.identifier() ;
        jackTokenizer.advance(); // className
        jackTokenizer.advance(); // {
        while (jackTokenizer.tokenType().getClass().getSimpleName().equals("KEYWORD") && (jackTokenizer.keyWord().equals("static") || jackTokenizer.keyWord().equals("field")))
            compileClassVarDec(); // fields and static declaration
        while (jackTokenizer.tokenType().getClass().getSimpleName().equals("KEYWORD") && (jackTokenizer.keyWord().equals("constructor") || jackTokenizer.keyWord().equals("function") || jackTokenizer.keyWord().equals("method"))) compileSubroutine(); //subroutines
        jackTokenizer.advance(); // }
    }

    public void compileClassVarDec() throws InvalidOperationException, IOException {
        String kind = jackTokenizer.keyWord() ;
        jackTokenizer.advance();// static || field
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        String type ;
        if (tokenClass.equals("IDENTIFIER")) {
            type = jackTokenizer.identifier() ;
            jackTokenizer.advance(); // type identifier
        } else if (tokenClass.equals("KEYWORD")) {
            type = jackTokenizer.keyWord() ;
            jackTokenizer.advance(); //type int || char || boolean
        } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);
        symbolTable.define(jackTokenizer.identifier(),type, IdentifierMeta.IDENTIFIERKIND.valueOf(kind.toUpperCase()));
        jackTokenizer.advance(); // identifier
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
            jackTokenizer.advance(); // ,
            symbolTable.define(jackTokenizer.identifier(),type,IdentifierMeta.IDENTIFIERKIND.valueOf(kind.toUpperCase())) ;
            jackTokenizer.advance(); // identifier
        }
        jackTokenizer.advance(); // ;
    }


    public void compileSubroutine() throws IOException, InvalidOperationException {
        int nLocals = 0 ;
        String returnType ;
        symbolTable.startSubroutine();
        labelIndex = 0 ;
        currentSubroutineType = jackTokenizer.keyWord() ;
        jackTokenizer.advance(); // method || function || constructor
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        if (tokenClass.equals("IDENTIFIER")){
            returnType = jackTokenizer.identifier() ;
            jackTokenizer.advance()  ; // returnType Identifier
        }
        else {
            returnType = jackTokenizer.keyWord() ;
            jackTokenizer.advance(); // returnType int | char | boolean | void
        }
        String subroutineName = currentClass + "." +  jackTokenizer.identifier() ;
        currentSubroutine = subroutineName ;
        currentSubroutineReturnType = returnType ;
        jackTokenizer.advance(); // subroutineName
        jackTokenizer.advance(); // (
        compileParameterList();
        jackTokenizer.advance(); // )
        jackTokenizer.advance(); // {
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") && jackTokenizer.keyWord().equals("var"))
            nLocals += compileVarDec();
        vmWriter.writeFunction(subroutineName,nLocals);
        if(currentSubroutineType.equals("constructor")){
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,symbolTable.VarCount(IDENTIFIERKIND.FIELD)) ;
            vmWriter.writeCall("Memory.alloc", 1) ;
            vmWriter.writePop(VMWriter.MEMORYSEGMENT.POINTER,0) ;
        }
        else if (currentSubroutineType.equals("method")){
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.ARGUMENT,0);
            vmWriter.writePop(VMWriter.MEMORYSEGMENT.POINTER,0) ;
        }
        compileStatements();
        jackTokenizer.advance(); // }
    }

    public void compileParameterList() throws InvalidOperationException {
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        if (tokenClass.equals("IDENTIFIER") || (tokenClass.equals("KEYWORD") && (jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean")))) {
            String identifierType ;
            if (tokenClass.equals("IDENTIFIER")) {
                identifierType = jackTokenizer.identifier() ;
                jackTokenizer.advance(); // type Identifier
            } else {
                identifierType = jackTokenizer.keyWord() ;
                jackTokenizer.advance(); // type int | char | boolean
            }
            if(currentSubroutineType.equals("method")) symbolTable.subroutineArgumentIdentifierIndex = 1;
            symbolTable.define(jackTokenizer.identifier(), identifierType, IdentifierMeta.IDENTIFIERKIND.ARG) ;
            jackTokenizer.advance();// identifier
        }
            while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
                jackTokenizer.advance() ; // ,
                String identifierType ;
                tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
                if (tokenClass.equals("IDENTIFIER")) {
                    identifierType = jackTokenizer.identifier() ;
                    jackTokenizer.advance() ;// type Identifier
                } else if (tokenClass.equals("KEYWORD")) {
                    identifierType = jackTokenizer.keyWord() ;
                    jackTokenizer.advance() ;// type int | char | boolean
                } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);

                symbolTable.define(jackTokenizer.identifier(),identifierType, IDENTIFIERKIND.ARG) ;
                jackTokenizer.advance();// identifier
            }
    }

    public int compileVarDec() throws IOException, InvalidOperationException {
        int nLocals = 0 ;
        jackTokenizer.advance() ; // var
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        String identifierType ;
        if (tokenClass.equals("IDENTIFIER")) {
            identifierType = jackTokenizer.identifier() ;
            jackTokenizer.advance() ; // type Identifier
        } else if (tokenClass.equals("KEYWORD")) {
            identifierType = jackTokenizer.keyWord() ;
            jackTokenizer.advance() ; // type int | char | bool
        } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);
        symbolTable.define(jackTokenizer.identifier(),identifierType, IDENTIFIERKIND.VAR) ;
        jackTokenizer.advance() ; // Identifier
        nLocals++ ;
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
            jackTokenizer.advance() ; // ,
            symbolTable.define(jackTokenizer.identifier(),identifierType, IDENTIFIERKIND.VAR) ;
            jackTokenizer.advance(); // identifier
            nLocals++ ;
        }
        jackTokenizer.advance(); // ;
        return nLocals ;
    }

    public void compileStatements() throws IOException, InvalidOperationException {
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") &&  (jackTokenizer.keyWord().equals("let") || jackTokenizer.keyWord().equals("if") || jackTokenizer.keyWord().equals("while") || jackTokenizer.keyWord().equals("do") || jackTokenizer.keyWord().equals("return")))
        {
            if(jackTokenizer.keyWord().equals("if")) compileIf();
            else if(jackTokenizer.keyWord().equals("let")) compileLet();
            else if (jackTokenizer.keyWord().equals("while")) compileWhile();
            else if(jackTokenizer.keyWord().equals("do")) compileDo();
            else if(jackTokenizer.keyWord().equals("return")) compileReturn();
        }
    }

    public void compileDo() throws IOException, InvalidOperationException {
        jackTokenizer.advance(); // do
        int nArgs = 0 ;
        String subroutineCall =  jackTokenizer.identifier() ;
        jackTokenizer.advance(); // functionCall
        if(jackTokenizer.symbol()=='.')
        {
            jackTokenizer.advance() ; // .
            if(symbolTable.isADefinedIdentifier(subroutineCall)) {
                String objectName = subroutineCall ;
                subroutineCall = symbolTable.TypeOf(subroutineCall) + "." + jackTokenizer.identifier();
                nArgs++ ;
                vmWriter.writePush(getSegmentForIdentifierKind(symbolTable.KindOf(objectName)),symbolTable.IndexOf(objectName)) ;
            }
            else subroutineCall = subroutineCall + "." + jackTokenizer.identifier() ;
            jackTokenizer.advance(); // Object.methodCall
        }
        else {
            nArgs++ ;
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.POINTER,0) ;
            subroutineCall = currentClass + "." + subroutineCall ;
        }
        jackTokenizer.advance(); // (
        List<Expression> expressionsList = compileExpressionList() ;
        for (Expression e : expressionsList) writeExpression(e) ;
        nArgs += expressionsList.size() ;
        jackTokenizer.advance(); // )
        jackTokenizer.advance(); // ;
        vmWriter.writeCall(subroutineCall, nArgs);
        vmWriter.writePop(VMWriter.MEMORYSEGMENT.TEMP,0);
    }

    public void compileLet() throws IOException, InvalidOperationException {
        jackTokenizer.advance(); // let
        String identifier = jackTokenizer.identifier() ;
        jackTokenizer.advance(); // identifier
        Expression exp1 = null ;
        if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()=='[')
        {
            jackTokenizer.advance(); // [
            exp1 = compileExpression();
            jackTokenizer.advance(); // ]
        }
        jackTokenizer.advance(); // =
        Expression exp2 = compileExpression();
        if(exp1==null) {
            writeExpression(exp2);
            vmWriter.writePop(getSegmentForIdentifierKind(symbolTable.KindOf(identifier)), symbolTable.IndexOf(identifier));
        }
        else{
            vmWriter.writePush(getSegmentForIdentifierKind(symbolTable.KindOf(identifier)), symbolTable.IndexOf(identifier)) ;
            writeExpression(exp1) ;
            vmWriter.writeArithmetic(VMWriter.ARITHMETIC.ADD) ;
            writeExpression(exp2) ;
            vmWriter.writePop(VMWriter.MEMORYSEGMENT.TEMP,0);
            vmWriter.writePop(VMWriter.MEMORYSEGMENT.POINTER,1);
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.TEMP,0);
            vmWriter.writePop(VMWriter.MEMORYSEGMENT.THAT,0);
        }
        jackTokenizer.advance(); // ;
    }

    public void compileWhile() throws IOException, InvalidOperationException {
        String label1 = currentSubroutine + "$WHILE$" + labelIndex ;
        labelIndex++ ;
        String label2 = currentSubroutine + "$WHILE$" + labelIndex ;
        labelIndex++ ;
        jackTokenizer.advance(); // while
        jackTokenizer.advance(); // (
        Expression exp = compileExpression();
        jackTokenizer.advance(); // )
        jackTokenizer.advance(); // {
        vmWriter.writeLabel(label1) ;
        writeExpression(exp) ;
        vmWriter.writeArithmetic(VMWriter.ARITHMETIC.NOT) ;
        vmWriter.writeIf(label2);
        compileStatements();
        vmWriter.writeGoto(label1);
        vmWriter.writeLabel(label2) ;
        jackTokenizer.advance(); // }
    }

    public void compileReturn() throws IOException, InvalidOperationException {
        jackTokenizer.advance(); // return
        Expression exp = null;
        boolean returnExpression  = false ;
        if(!(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==';'))
        {
            returnExpression = true ;
            exp = compileExpression();
        }
        jackTokenizer.advance(); // ;
        if(currentSubroutineReturnType.equals("void")) vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,0) ;
        if(returnExpression) writeExpression(exp) ;
        vmWriter.writeReturn();
    }

    public void compileIf() throws IOException, InvalidOperationException {
        String label1 = currentSubroutine + "$IF$" + labelIndex ;
        labelIndex++ ;
        String label2 = currentSubroutine + "$IF$" + labelIndex ;
        labelIndex++ ;
        jackTokenizer.advance(); // if
        jackTokenizer.advance(); // (
        Expression exp = compileExpression();
        jackTokenizer.advance(); // )
        jackTokenizer.advance(); // {
        writeExpression(exp) ;
        vmWriter.writeArithmetic(VMWriter.ARITHMETIC.NOT) ;
        vmWriter.writeIf(label1);
        compileStatements();
        vmWriter.writeGoto(label2);
        jackTokenizer.advance(); // }
        vmWriter.writeLabel(label1);
        if(jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") && jackTokenizer.keyWord().equals("else")){
            jackTokenizer.advance(); // else
            jackTokenizer.advance(); // {
            compileStatements();
            jackTokenizer.advance(); // }
        }
        vmWriter.writeLabel(label2) ;
    }

    public Expression compileExpression() throws IOException, InvalidOperationException {
        Term term = compileTerm();
        List<Pair<Operand,Term>> opterms = new ArrayList<Pair<Operand,Term>>() ;
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && (jackTokenizer.symbol()=='+' || jackTokenizer.symbol()=='+' || jackTokenizer.symbol()=='-' ||jackTokenizer.symbol()=='*' ||jackTokenizer.symbol()=='/' || jackTokenizer.symbol()=='&' || jackTokenizer.symbol()=='|'  || jackTokenizer.symbol()=='<' || jackTokenizer.symbol()=='>' || jackTokenizer.symbol()=='='))
        {
            Operand op = new Operand(jackTokenizer.symbol()) ;
            jackTokenizer.advance(); // symbol
            Term newTerm = compileTerm();
            opterms.add(new Pair<>(op, newTerm));
        }
        return new Expression(term,opterms) ;
    }

    public Term compileTerm() throws InvalidOperationException, IOException {
        Token t1 = jackTokenizer.currentToken ;
        jackTokenizer.advance();

        if(t1.getClass().getSimpleName().equals("INT_CONST")) return new Term(((INT_CONST)t1).intVal) ;
        else if(t1.getClass().getSimpleName().equals("STRING_CONST")) return new Term(((STRING_CONST)t1).stringVal) ;
        else if(t1.getClass().getSimpleName().equals("KEYWORD"))  return new Term((KEYWORD)t1) ;
        else if(t1.getClass().getSimpleName().equals("IDENTIFIER"))
        {
            if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()=='[')
                {
                    jackTokenizer.advance(); // [
                    Expression exp = compileExpression();
                    jackTokenizer.advance(); // ]
                    return new Term(((IDENTIFIER)t1).identifier,exp) ;
                }
            else if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && (jackTokenizer.symbol()=='(' || jackTokenizer.symbol()=='.'))
                {
                    String name = ((IDENTIFIER)t1).identifier ;
                    if(jackTokenizer.symbol()=='(')
                    {
                        jackTokenizer.advance(); // (
                        List<Expression> expressionsList = compileExpressionList();
                        jackTokenizer.advance(); // )
                        return new Term(name, expressionsList) ;
                    }
                    else // (jackTokenizer.symbol()=='.')
                    {
                        jackTokenizer.advance(); // .
                        String subroutineName = jackTokenizer.identifier() ;
                        jackTokenizer.advance(); // identifier
                        jackTokenizer.advance(); // (
                        List<Expression> expressionsList = compileExpressionList();
                        jackTokenizer.advance(); // )
                        return new Term(name, subroutineName, expressionsList) ;
                    }
                }
            else return new Term(((IDENTIFIER)t1).identifier, Term.TERMTYPES.VARNAME) ;

        }
        else //(t1.getClass().getSimpleName().equals("SYMBOL"))
        {
            if(((SYMBOL)t1).symbol=='(')
            {
                Expression exp = compileExpression();
                jackTokenizer.advance(); // )
                return new Term(exp) ;
            }
           else //(((SYMBOL)t1).symbol=='-' || ((SYMBOL)t1).symbol=='~')
            {
                Term term = compileTerm();
                return new Term((((SYMBOL) t1).symbol),term) ;
            }
        }
    }

    public List<Expression> compileExpressionList() throws IOException, InvalidOperationException {
        List<Expression> expressionsList = new ArrayList<Expression>();
        if(!(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==')'))
        {
            expressionsList.add(compileExpression());
            while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',')
            {
                jackTokenizer.advance(); // ,
                expressionsList.add(compileExpression());
            }
        }
        return expressionsList ;
    }

    public VMWriter.MEMORYSEGMENT getSegmentForIdentifierKind(IDENTIFIERKIND kind) throws InvalidOperationException {
        if(kind==IDENTIFIERKIND.STATIC) return VMWriter.MEMORYSEGMENT.STATIC ;
        else if(kind==IDENTIFIERKIND.VAR) return VMWriter.MEMORYSEGMENT.LOCAL ;
        else if(kind==IDENTIFIERKIND.ARG) return VMWriter.MEMORYSEGMENT.ARGUMENT ;
        else if(kind==IDENTIFIERKIND.FIELD) return VMWriter.MEMORYSEGMENT.THIS ;
        else throw new InvalidOperationException("Invalid Kind of Identifier") ;
    }

    public void writeExpression(Expression exp) throws IOException, InvalidOperationException {
        if(exp.opterms.size()==0)
        {
            if(exp.term.type== Term.TERMTYPES.INTEGERCONSTANT)
                vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,exp.term.integerConstant);

            else if(exp.term.type== Term.TERMTYPES.STRINGCONSTANT){
                String s = exp.term.stringConstant ;
                vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,s.length()) ;
                vmWriter.writeCall("String.new",1) ;
                for(char c : s.toCharArray()) {
                    vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,(int)c) ;
                    vmWriter.writeCall("String.appendChar",2) ;
                }
            }
            else if(exp.term.type== Term.TERMTYPES.VARNAME)
            {
                VMWriter.MEMORYSEGMENT memseg = getSegmentForIdentifierKind(symbolTable.KindOf(exp.term.varname)) ;
                vmWriter.writePush(memseg,symbolTable.IndexOf(exp.term.varname));
            }

            else if(exp.term.type==Term.TERMTYPES.ARRAYEXPRESSION){

                vmWriter.writePush(getSegmentForIdentifierKind(symbolTable.KindOf(exp.term.arrayName)),symbolTable.IndexOf(exp.term.arrayName)) ;
                writeExpression(exp.term.arrayExpression) ;
                vmWriter.writeArithmetic(VMWriter.ARITHMETIC.ADD) ;
                vmWriter.writePop(VMWriter.MEMORYSEGMENT.POINTER,1);
                vmWriter.writePush(VMWriter.MEMORYSEGMENT.THAT,0) ;

            }
            else if(exp.term.type == Term.TERMTYPES.UNARYOPTERM)
            {
                writeExpression(new Expression(exp.term.term)) ;
                if(exp.term.unaryOp=='-') vmWriter.writeArithmetic(VMWriter.ARITHMETIC.NEG);
                else if (exp.term.unaryOp=='~') vmWriter.writeArithmetic(VMWriter.ARITHMETIC.NOT);
            }

            else if(exp.term.type== Term.TERMTYPES.SUBROUTINECALL){
                List<Expression> expressionsList = exp.term.subroutineExpressionsList ;
                for (Expression e : expressionsList) writeExpression(e) ;
                vmWriter.writeCall(exp.term.subroutineName,expressionsList.size());
            }

            else if(exp.term.type==Term.TERMTYPES.PARATHESISEXPRESSION){
                writeExpression(exp.term.expression) ;
            }

            else if(exp.term.type==Term.TERMTYPES.METHODCALL){
                if(symbolTable.isADefinedIdentifier(exp.term.objectName)){
                    vmWriter.writePush(getSegmentForIdentifierKind(symbolTable.KindOf(exp.term.objectName)),symbolTable.IndexOf(exp.term.objectName));
                    for(Expression methodExpressions : exp.term.methodCallExpressionList) writeExpression(methodExpressions) ;
                    String qualifiedMethodName = symbolTable.TypeOf(exp.term.objectName) + "." + exp.term.methodName ;
                    vmWriter.writeCall(qualifiedMethodName,exp.term.methodCallExpressionList.size()+1) ;
                }
                else {
                    for(Expression methodExpressions : exp.term.methodCallExpressionList) writeExpression(methodExpressions) ;
                    vmWriter.writeCall(exp.term.objectName + "." + exp.term.methodName,exp.term.methodCallExpressionList.size());
                }
            }

            else if(exp.term.type==Term.TERMTYPES.KEYWORDCONSTANT){
                writeKeywordConstant(exp.term.keyword) ;
            }
        }
        else {
            Pair<Operand,Term> head = exp.opterms.get(0) ;
            List<Pair<Operand, Term>> tail ;
            if(exp.opterms.size()==1) tail = new ArrayList<Pair<Operand, Term>>() ;
            else tail = exp.opterms.subList(1,exp.opterms.size()-1) ;
            Expression remainingExpression = new Expression(head.getValue(), tail) ;
            writeExpression(new Expression(exp.term)) ;
            writeExpression(remainingExpression) ;
            writeOperand(head.getKey()) ;
        }

    }

    public void writeOperand(Operand op) throws IOException {
        if(op.symbol.equals("+")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.ADD) ;
        else if(op.symbol.equals("-")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.SUB) ;
        else if(op.symbol.equals("*")) vmWriter.writeCall("Math.multiply",2) ;
        else if (op.symbol.equals("/")) vmWriter.writeCall("Math.divide",2) ;
        else if (op.symbol.equals("&")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.AND) ;
        else if(op.symbol.equals("|")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.OR) ;
        else if(op.symbol.equals("<")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.LT) ;
        else if(op.symbol.equals(">")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.GT) ;
        else if(op.symbol.equals("=")) vmWriter.writeArithmetic(VMWriter.ARITHMETIC.EQ) ;
    }

    public void writeKeywordConstant(String keyword) throws IOException {
        if(keyword.equals("true")) {
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,1);
            vmWriter.writeArithmetic(VMWriter.ARITHMETIC.NEG) ;
        }
        else if(keyword.equals("false")) {
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,0);
        }
        else if(keyword.equals("null")) {
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONSTANT,0);
        }
        else if(keyword.equals("this")){
            vmWriter.writePush(VMWriter.MEMORYSEGMENT.POINTER,0);
        }
    }



}
