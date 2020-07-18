package com.dflipflop.fromnandtotetris;

import java.io.*;

public class CompilationEngine {

    InputStream inputStream ;
    OutputStream outputStream ;
    BufferedWriter bufferedWriter ;
    JackTokenizer jackTokenizer ;

    public CompilationEngine(InputStream inputStream, OutputStream outputStream) throws IOException, InvalidOperationException {
        this.inputStream = inputStream ;
        this.outputStream = outputStream ;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream) ;
        bufferedWriter = new BufferedWriter(outputStreamWriter) ;
        jackTokenizer = new JackTokenizer(inputStream) ;
        compileClass() ;
        bufferedWriter.close();
    }

    public void compileClass() throws IOException, InvalidOperationException {
        writeCompileStart("class");
        assert jackTokenizer.keyWord().equals("class");
        writeKeyword();
        writeIdentifier();
        assert jackTokenizer.symbol()=='{' ;
        writeSymbol();

        while (jackTokenizer.tokenType().getClass().getSimpleName().equals("KEYWORD") && (jackTokenizer.keyWord().equals("static") || jackTokenizer.keyWord().equals("field")))                      compileClassVarDec();

        while (jackTokenizer.tokenType().getClass().getSimpleName().equals("KEYWORD") && (jackTokenizer.keyWord().equals("constructor") || jackTokenizer.keyWord().equals("function") || jackTokenizer.keyWord().equals("method"))) compileSubroutine();

        assert jackTokenizer.symbol()=='}'  ;
        writeSymbol();
        writeCompileEnd("class");
    }

    public void compileClassVarDec() throws InvalidOperationException, IOException {
            writeCompileStart("classVarDec");
            assert jackTokenizer.keyWord() == "static" || jackTokenizer.keyWord() == "field";
            writeKeyword();
            String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
            if (tokenClass.equals("IDENTIFIER")) {
                writeIdentifier();
            } else if (tokenClass.equals("KEYWORD")) {
                assert jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean") ;
                writeKeyword();
            } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);

            writeIdentifier();
            while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
                writeSymbol();
                writeIdentifier();
            }

        assert jackTokenizer.symbol()==';' ;
        writeSymbol();
        writeCompileEnd("classVarDec");

        }


    public void compileSubroutine() throws IOException, InvalidOperationException {
        writeCompileStart("subroutineDec");
        writeKeyword();
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        if (tokenClass.equals("IDENTIFIER")) {
            writeIdentifier();
        } else if (tokenClass.equals("KEYWORD")) {
            assert jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean") || jackTokenizer.keyWord().equals("void");
            writeKeyword();
        } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);

        writeIdentifier();
        assert jackTokenizer.symbol()=='(' ;
        writeSymbol();
        compileParameterList();
        assert jackTokenizer.symbol()==')' ;
        writeSymbol();

        writeCompileStart("subroutineBody");
        assert jackTokenizer.symbol()=='{' ;
        writeSymbol();
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") && jackTokenizer.keyWord().equals("var"))
            compileVarDec();
        compileStatements();
        assert jackTokenizer.symbol()=='}' ;
        writeSymbol();
        writeCompileEnd("subroutineBody");
        writeCompileEnd("subroutineDec");
    }

    public void compileParameterList() throws IOException, InvalidOperationException {
        writeCompileStart("parameterList");
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        if (tokenClass.equals("IDENTIFIER") || (tokenClass.equals("KEYWORD") && (jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean")))) {
            if (tokenClass.equals("IDENTIFIER")) {
                writeIdentifier();
            } else if (tokenClass.equals("KEYWORD")) {
                writeKeyword();
            }
            writeIdentifier();
        }
            while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
                writeSymbol();

                tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
                if (tokenClass.equals("IDENTIFIER")) {
                    writeIdentifier();
                } else if (tokenClass.equals("KEYWORD")) {
                    assert jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean") || jackTokenizer.keyWord().equals("void");
                    writeKeyword();
                } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);

                writeIdentifier();
            }

        writeCompileEnd("parameterList");
    }

    public void compileVarDec() throws IOException, InvalidOperationException {
        writeCompileStart("varDec");
        assert jackTokenizer.keyWord().equals("var") ;
        writeKeyword();
        String tokenClass = jackTokenizer.tokenType().getClass().getSimpleName();
        if (tokenClass.equals("IDENTIFIER")) {
            writeIdentifier();
        } else if (tokenClass.equals("KEYWORD")) {
            assert jackTokenizer.keyWord().equals("int") || jackTokenizer.keyWord().equals("char") || jackTokenizer.keyWord().equals("boolean") || jackTokenizer.keyWord().equals("void");
            writeKeyword();
        } else throw new InvalidOperationException("Invalid Token for 'type' " + tokenClass);
        writeIdentifier();
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',') {
            writeSymbol();
            writeIdentifier();
        }

        assert jackTokenizer.symbol() ==';' ;
        writeSymbol();
        writeCompileEnd("varDec");
    }

    public void compileStatements() throws IOException, InvalidOperationException {
        writeCompileStart("statements");
        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") &&  (jackTokenizer.keyWord().equals("let") || jackTokenizer.keyWord().equals("if") || jackTokenizer.keyWord().equals("while") || jackTokenizer.keyWord().equals("do") || jackTokenizer.keyWord().equals("return")))
        {
            if(jackTokenizer.keyWord().equals("if")) compileIf();
            else if(jackTokenizer.keyWord().equals("let")) compileLet();
            else if (jackTokenizer.keyWord().equals("while")) compileWhile();
            else if(jackTokenizer.keyWord().equals("do")) compileDo();
            else if(jackTokenizer.keyWord().equals("return")) compileReturn();
        }
        writeCompileEnd("statements");
    }

    public void compileDo() throws IOException, InvalidOperationException {
        writeCompileStart("doStatement");
        assert jackTokenizer.keyWord().equals("do") ;
        writeKeyword();
        writeIdentifier();
        if(jackTokenizer.symbol()=='.')
        {
            writeSymbol();
            writeIdentifier();
        }
        assert jackTokenizer.symbol()=='(' ;
        writeSymbol();
        compileExpressionList();
        assert jackTokenizer.symbol()==')' ;
        writeSymbol();
        assert jackTokenizer.symbol()==';' ;
        writeSymbol();
        writeCompileEnd("doStatement");
    }

    public void compileLet() throws IOException, InvalidOperationException {
        writeCompileStart("letStatement");
        assert jackTokenizer.keyWord().equals("let") ;
        writeKeyword();
        writeIdentifier();
        if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()=='[')
        {
            assert jackTokenizer.symbol()=='[' ;
            writeSymbol();
//            writeIdentifier();
            compileExpression();
            assert jackTokenizer.symbol()==']' ;
            writeSymbol();
        }
        assert jackTokenizer.symbol()=='=' ;
        writeSymbol();
//        writeIdentifier();
        compileExpression();
        assert jackTokenizer.symbol()==';' ;
        writeSymbol();
        writeCompileEnd("letStatement");
    }

    public void compileWhile() throws IOException, InvalidOperationException {
        writeCompileStart("whileStatement");
        assert jackTokenizer.keyWord().equals("while") ;
        writeKeyword();
        assert jackTokenizer.symbol()=='(' ;
        writeSymbol();
//        writeIdentifier();
        compileExpression();
        assert jackTokenizer.symbol()==')' ;
        writeSymbol();
        assert jackTokenizer.symbol()=='{' ;
        writeSymbol();
        compileStatements();
        assert jackTokenizer.symbol()=='}' ;
        writeSymbol();
        writeCompileEnd("whileStatement");
    }

    public void compileReturn() throws IOException, InvalidOperationException {
        writeCompileStart("returnStatement");
        assert jackTokenizer.keyWord().equals("return") ;
        writeKeyword();

        if(!(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==';'))
//            writeIdentifier();
            compileExpression();

        assert jackTokenizer.symbol() == ';';
        writeSymbol();
        writeCompileEnd("returnStatement");
    }

    public void compileIf() throws IOException, InvalidOperationException {
        writeCompileStart("ifStatement");
        assert jackTokenizer.keyWord().equals("if") ;
        writeKeyword();
        assert jackTokenizer.symbol() == '(';
        writeSymbol();
//        writeIdentifier();
        compileExpression();
        assert jackTokenizer.symbol() == ')';
        writeSymbol();
        assert jackTokenizer.symbol() == '{';
        writeSymbol();
        compileStatements();
        assert jackTokenizer.symbol() == '}';
        writeSymbol();

        if(jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD") && jackTokenizer.keyWord().equals("else")){
            assert jackTokenizer.keyWord().equals("else") ;
            writeKeyword();
            assert jackTokenizer.symbol() == '{';
            writeSymbol();
            compileStatements();
            assert jackTokenizer.symbol() == '}';
            writeSymbol();
        }
        writeCompileEnd("ifStatement");
    }

    public void compileExpression() throws IOException, InvalidOperationException {
        writeCompileStart("expression");
        compileTerm();

        while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && (jackTokenizer.symbol()=='+' || jackTokenizer.symbol()=='+' || jackTokenizer.symbol()=='-' ||jackTokenizer.symbol()=='*' ||jackTokenizer.symbol()=='/' || jackTokenizer.symbol()=='&' || jackTokenizer.symbol()=='|'  || jackTokenizer.symbol()=='<' || jackTokenizer.symbol()=='>' || jackTokenizer.symbol()=='='))
        {
            writeSymbol();
            compileTerm();
        }

            writeCompileEnd("expression");
    }

    public void compileTerm() throws InvalidOperationException, IOException {
        writeCompileStart("term");
        Token t1 = jackTokenizer.currentToken ;
        jackTokenizer.advance();

        if(t1.getClass().getSimpleName().equals("INT_CONST"))
        {
            bufferedWriter.write("<integerConstant> ");
            System.out.print("<integerConstant>");
            System.out.print(((INT_CONST)t1).intVal + "");
            bufferedWriter.write(((INT_CONST)t1).intVal + "");
            bufferedWriter.write(" </integerConstant>");
            System.out.print("<integerConstant>");
            System.out.println();
            bufferedWriter.newLine();
        }
        else if(t1.getClass().getSimpleName().equals("STRING_CONST"))
        {
            bufferedWriter.write("<stringConstant> ");
            System.out.print("<stringConstant>");
            System.out.print(((STRING_CONST)t1).stringVal);
            bufferedWriter.write(((STRING_CONST)t1).stringVal);
            bufferedWriter.write(" </stringConstant>");
            System.out.print("<stringConstant>");
            System.out.println();
            bufferedWriter.newLine();
        }
        else if(t1.getClass().getSimpleName().equals("KEYWORD"))
        {
            KEYWORD k1 = (KEYWORD)t1 ;
            if(k1.keyword.equals("true") || k1.keyword.equals("false") || k1.keyword.equals("null") || k1.keyword.equals("this"))
            {
                bufferedWriter.write("<keyword> ");
                System.out.print("<keyword>");
                System.out.print(((KEYWORD)t1).keyword);
                bufferedWriter.write(((KEYWORD)t1).keyword);
                bufferedWriter.write(" </keyword>");
                System.out.print("<keyword>");
                System.out.println();
                bufferedWriter.newLine();
            }
            else throw new InvalidOperationException("Invalid Keyword " + k1.keyword + " for Term.") ;
        }
        else if(t1.getClass().getSimpleName().equals("IDENTIFIER"))
        {
            if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()=='[')
                {

                    bufferedWriter.write("<identifier> ");
                    System.out.print("<identifier>");
                    System.out.print(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(" </identifier>");
                    System.out.print("<identifier>");
                    System.out.println();
                    bufferedWriter.newLine();

                    writeSymbol();

                    compileExpression();

                    assert jackTokenizer.symbol()==']' ;
                    writeSymbol();

                }
            else if(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && (jackTokenizer.symbol()=='(' || jackTokenizer.symbol()=='.'))
                {
                    bufferedWriter.write("<identifier> ");
                    System.out.print("<identifier>");
                    System.out.print(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(" </identifier>");
                    System.out.print("<identifier>");
                    System.out.println();
                    bufferedWriter.newLine();

                    if(jackTokenizer.symbol()=='(')
                    {
                        writeSymbol();
                        compileExpressionList();
                        assert jackTokenizer.symbol()==')' ;
                        writeSymbol();

                    }
                    else if (jackTokenizer.symbol()=='.')
                    {
                        writeSymbol();
                        writeIdentifier();
                        assert jackTokenizer.symbol()=='(' ;
                        writeSymbol();
                        compileExpressionList();
                        assert jackTokenizer.symbol()==')' ;
                        writeSymbol();
                    }
                }

                else
                {
                    bufferedWriter.write("<identifier> ");
                    System.out.print("<identifier>");
                    System.out.print(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(((IDENTIFIER)t1).identifier);
                    bufferedWriter.write(" </identifier>");
                    System.out.print("<identifier>");
                    System.out.println();
                    bufferedWriter.newLine();
                }
        }
        else if(t1.getClass().getSimpleName().equals("SYMBOL"))
        {
            if(((SYMBOL)t1).symbol=='(')
            {
                bufferedWriter.write("<symbol>");
                System.out.print("<symbol>");
                bufferedWriter.write((((SYMBOL) t1).symbol));
                System.out.print((((SYMBOL) t1).symbol));
                bufferedWriter.write("</symbol>");
                System.out.print("</symbol>");
                System.out.println();
                bufferedWriter.newLine();

                compileExpression();

                assert jackTokenizer.symbol()==')' ;
                writeSymbol();
            }
           else if(((SYMBOL)t1).symbol=='-' || ((SYMBOL)t1).symbol=='~')
            {
                bufferedWriter.write("<symbol>");
                System.out.print("<symbol>");
                bufferedWriter.write((((SYMBOL) t1).symbol));
                System.out.print((((SYMBOL) t1).symbol));
                bufferedWriter.write("</symbol>");
                System.out.print("</symbol>");
                System.out.println();
                bufferedWriter.newLine();
                compileTerm();
            }
        }

        writeCompileEnd("term");
    }

    public void compileExpressionList() throws IOException, InvalidOperationException {
        writeCompileStart("expressionList");

        if(!(jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==')'))
        {
            compileExpression();
//            if(jackTokenizer.currentToken.getClass().getSimpleName().equals("IDENTIFIER"))
//            writeIdentifier();
//            else if (jackTokenizer.currentToken.getClass().getSimpleName().equals("KEYWORD")) writeKeyword();
//

            while (jackTokenizer.currentToken.getClass().getSimpleName().equals("SYMBOL") && jackTokenizer.symbol()==',')
            {
                writeSymbol();
//                writeIdentifier();
                compileExpression();

            }
        }
        writeCompileEnd("expressionList");
    }


    private void writeKeyword() throws InvalidOperationException, IOException {
        bufferedWriter.write("<keyword> ");
        System.out.print("<keyword>");
        bufferedWriter.write(jackTokenizer.keyWord());
        System.out.print(jackTokenizer.keyWord());
        jackTokenizer.advance();
        bufferedWriter.write(" </keyword>");
        System.out.print("<keyword>");
        System.out.println();
        bufferedWriter.newLine();
    }

    private void writeSymbol() throws IOException, InvalidOperationException {
        bufferedWriter.write("<symbol> ");
        System.out.print("<symbol>");
        bufferedWriter.write(xmlFixedSymbol(jackTokenizer.symbol()));
        System.out.print(xmlFixedSymbol(jackTokenizer.symbol()));
        jackTokenizer.advance();
        bufferedWriter.write(" </symbol>");
        System.out.print("</symbol>");
        System.out.println();
        bufferedWriter.newLine();
    }

    private void writeIdentifier() throws InvalidOperationException, IOException {
        bufferedWriter.write("<identifier> ");
        System.out.print("<identifier>");
        bufferedWriter.write(jackTokenizer.identifier());
        System.out.print(jackTokenizer.identifier());
        jackTokenizer.advance();
        bufferedWriter.write(" </identifier>");
        System.out.print("</identifier>");
        System.out.println();
        bufferedWriter.newLine();
    }

    private void writeIntConstant() throws InvalidOperationException, IOException {
        bufferedWriter.write("<intConstant> ");
        System.out.print("<intConstant>");
        bufferedWriter.write(jackTokenizer.intVal());
        System.out.print(jackTokenizer.intVal());
        jackTokenizer.advance();
        bufferedWriter.write(" </intConstant>");
        System.out.print("</intConstant>");
        System.out.println();
        bufferedWriter.newLine();

    }

    private void writeStringConstant() throws IOException, InvalidOperationException {
        bufferedWriter.write("<stringConstant> ");
        System.out.print("<stringConstant>");
        bufferedWriter.write(jackTokenizer.stringVal());
        System.out.print(jackTokenizer.stringVal());
        jackTokenizer.advance();
        bufferedWriter.write(" </stringConstant>");
        System.out.print("</stringConstant>");
        System.out.println();
        bufferedWriter.newLine();
    }

    private void writeCompileStart(String tag) throws IOException {
        bufferedWriter.write("<" +  tag + ">");
        System.out.print("<" +tag+ ">");
        System.out.println() ;
        bufferedWriter.newLine();
    }


    private void writeCompileEnd(String tag) throws IOException {
        bufferedWriter.write("</" + tag + "> ");
        System.out.print(" </" +tag+ ">");
        System.out.println() ;
        bufferedWriter.newLine();
    }

    private String xmlFixedSymbol(char symbol)
    {
        if(symbol=='<')
            return "&lt;" ;
        else if(symbol=='>')
            return "&gt;" ;
        else if(symbol=='&')
            return "&amp;" ;
        else return String.valueOf(symbol);
    }



}
