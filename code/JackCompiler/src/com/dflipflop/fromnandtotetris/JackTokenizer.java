package com.dflipflop.fromnandtotetris;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JackTokenizer {

    BufferedReader bufferedReader ;
    InputStream inputStream ;
    Token currentToken ;
    ArrayList<Token> tokenList ;
    Map<String,String> stringConstsMap ;
    static int tokenIterator ;

    public JackTokenizer(InputStream inputStream) throws IOException, InvalidOperationException {
        tokenIterator = 0 ;
        tokenList = new ArrayList<>() ;
        stringConstsMap = new HashMap<>() ;
        this.inputStream = inputStream ;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream) ;
        bufferedReader = new BufferedReader(inputStreamReader) ;
        buildTokenList();
        advance();
    }

    private void buildTokenList() throws IOException, InvalidOperationException {
        ArrayList<String> fileLines = new ArrayList<>() ;
        String line = bufferedReader.readLine() ;
        while (line!=null)
        {
            fileLines.add(line.trim()) ;
            line = bufferedReader.readLine() ;
        }
        String fileContent = String.join("\n",fileLines) ;

        Pattern stringConstPatttern = Pattern.compile("\".*\"") ;
        Matcher stringConstMatches = stringConstPatttern.matcher(fileContent) ;

        while (stringConstMatches.find())
        {
            String match = stringConstMatches.group() ;
            String stringConstKey = "STRCONST$" + UUID.randomUUID().toString().substring(5).replaceAll("-","") ;
            stringConstsMap.put(stringConstKey,match) ;
        }

        for(String key : stringConstsMap.keySet())
        {
            fileContent = fileContent.replace(stringConstsMap.get(key),key) ;
        }

        fileContent = fileContent.replaceAll("(?s)/\\*\\*.*?\\*/","") ;
        fileContent = fileContent.replaceAll("//.*\n"," ") ;
        fileContent = fileContent.replaceAll("/\\*\\*.*\\*/","") ;
        fileContent = fileContent.replaceAll("/\\*.*\\*/","") ;
        fileContent = fileContent.replaceAll("\n"," ") ;
        fileContent = fileContent.replaceAll("\n *\n"," ") ;

        for (String symbol : SYMBOL.symbolsList)
        {
            fileContent = fileContent.replaceAll(Pattern.quote(symbol)," " + symbol + " ");
        }

        String[] tokens = fileContent.split(" ") ;
        for(String s : tokens)
        {
            identifyAndAddToken(tokenList,s) ;
        }
    }

    public boolean identifyAndAddToken(ArrayList<Token> tokenList, String lexicon) throws InvalidOperationException {

        if(lexicon.equals("") || lexicon.equals("\n")){
            return false ;
        }

        else if(stringConstsMap.containsKey(lexicon))
        {
            tokenList.add(new STRING_CONST(stringConstsMap.get(lexicon))) ;
            return true ;
        }

        else if(SYMBOL.isASymbol(lexicon)) {
            tokenList.add(new SYMBOL(lexicon)) ;
            return true ;
        }
        else if(KEYWORD.isAKeyword(lexicon)) {
            tokenList.add(new KEYWORD(lexicon));
            return true ;
        }
        else if(IDENTIFIER.isAIdentifier(lexicon)) {
            tokenList.add(new IDENTIFIER(lexicon));
            return true;
        }
        else if(STRING_CONST.isAStringConst(lexicon)){
            tokenList.add(new STRING_CONST(lexicon)) ;
            return true ;
        }
        else if(INT_CONST.isAIntConst(lexicon)) {
            tokenList.add(new INT_CONST(lexicon)) ;
            return true ;
        }
        else return false ;
    }

    public boolean hasMoreTokens() {
        return !(tokenIterator==tokenList.size()) ;
    }

    public void advance() throws InvalidOperationException {
//        if (!hasMoreTokens()) throw new InvalidOperationException("Unable to advance since no tokens are available");
        if(tokenIterator<=tokenList.size()-1)
        currentToken = tokenList.get(tokenIterator) ;
        tokenIterator++ ;
    }

    public Token tokenType()
    {
        return currentToken ;
    }

    public String keyWord() throws InvalidOperationException {
        if(!tokenType().getClass().getSimpleName().equals("KEYWORD")) throw new InvalidOperationException("Cannot return Keyword for non-keyword token type") ;
        KEYWORD keyword = (KEYWORD)currentToken ;
        return keyword.getKeyword() ;
    }

    public char symbol() throws InvalidOperationException {
        if(!tokenType().getClass().getSimpleName().equals("SYMBOL")) throw new InvalidOperationException("Cannot return Symbol for non-symbol token type") ;
        SYMBOL symbol = (SYMBOL) currentToken;
        return symbol.getSymbol() ;
    }

    public String identifier() throws InvalidOperationException {
        if(!tokenType().getClass().getSimpleName().equals("IDENTIFIER")) throw new InvalidOperationException("Cannot return identifier for non-identifier token type") ;
        IDENTIFIER identifier = (IDENTIFIER) currentToken ;
        return identifier.getIdentifier() ;
    }

    public int intVal() throws InvalidOperationException {
        if(tokenType().getClass().getSimpleName()!="INT_CONST") throw new InvalidOperationException("Cannot return intval for non-intval token type") ;
        INT_CONST intConst = (INT_CONST) currentToken ;
        return intConst.getIntVal() ;
    }

    public String stringVal() throws InvalidOperationException {
        if(tokenType().getClass().getSimpleName()!="STRING_CONST") throw new InvalidOperationException("Cannot return string val for non-stringval token type") ;
        STRING_CONST stringConst = (STRING_CONST) currentToken ;
        return stringConst.getStringVal() ;

    }

    public void writeTokensXML(OutputStream outputStream) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream) ;
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter) ;
        bufferedWriter.write("<tokens>");
        System.out.println("<tokens>");
        bufferedWriter.newLine();
        for(Token t : tokenList)
        {
            String tag ;

            if(t.getClass().getSimpleName().equals("INT_CONST"))
                tag = "integerConstant" ;
            else if(t.getClass().getSimpleName().equals("STRING_CONST")) tag = "stringConstant" ;
            else tag = t.getClass().getSimpleName().toLowerCase() ;

            bufferedWriter.write("<" + tag + "> ");
            bufferedWriter.write(String.valueOf(t));
            bufferedWriter.write(" </" + tag + ">");
            bufferedWriter.newLine();

            System.out.print("<" + tag + "> ");
            System.out.print(String.valueOf(t));
            System.out.print(" </" + tag + ">");
            System.out.println();

        }
        bufferedWriter.write("</tokens>");
        System.out.println("</tokens>");
        bufferedWriter.newLine();
        bufferedWriter.close();
    }



}
