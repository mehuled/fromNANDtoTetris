package com.dflipflop.fromnandtotetris;

import java.util.List;

public class Term {

    TERMTYPES type ;

    int integerConstant ;
    String stringConstant ;
    String varname ;
    String keyword ;
    String arrayName ;
    Expression arrayExpression ;
    String subroutineName ;
    List<Expression> subroutineExpressionsList ;
    List<Expression> methodCallExpressionList ;
    String objectName ;
    String methodName ;
    Expression expression ;
    char unaryOp ;
    Term term ;


    static enum TERMTYPES {INTEGERCONSTANT, STRINGCONSTANT, KEYWORDCONSTANT, VARNAME, ARRAYEXPRESSION, SUBROUTINECALL, METHODCALL, PARATHESISEXPRESSION, UNARYOPTERM }

    Term(int integerConstant){
        this.type = TERMTYPES.INTEGERCONSTANT ;
        this.integerConstant = integerConstant ;
    }

    Term(String stringConstant){
        this.type = TERMTYPES.STRINGCONSTANT ;
        this.stringConstant = stringConstant ;
    }

    Term(KEYWORD keyword){
        this.type = TERMTYPES.KEYWORDCONSTANT ;
        this.keyword = keyword.keyword ; // true | false | null | this
    }

    Term(String arrayName, Expression exp){
        this.type = TERMTYPES.ARRAYEXPRESSION ;
        this.arrayName = arrayName ;
        this.arrayExpression = exp ;
    }

    Term(String subroutineName, List<Expression> subroutineExpressionsList){
        this.type = TERMTYPES.SUBROUTINECALL ;
        this.subroutineName = subroutineName ;
        this.subroutineExpressionsList = subroutineExpressionsList ;
    }

    Term(String objectName, String methodName, List<Expression> methodCallExpressionList){
        this.type = TERMTYPES.METHODCALL ;
        this.objectName = objectName;
        this.methodName = methodName ;
        this.methodCallExpressionList = methodCallExpressionList ;
    }

    Term (String name, TERMTYPES termType){
        this.type = termType ;
        this.varname = name ;
    }

    Term(Expression exp){
        this.type = TERMTYPES.PARATHESISEXPRESSION ;
        this.expression = exp ;
    }

    Term(char c, Term term){
        this.type = TERMTYPES.UNARYOPTERM ;
        this.unaryOp = c ;
        this.term = term ;
    }


}
