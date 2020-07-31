package com.dflipflop.fromnandtotetris;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Expression {

    Term term ;
    List<Pair<Operand,Term>> opterms ;

    Expression(Term term, List<Pair<Operand,Term>> opterms)
    {
        this.term = term ;
        this.opterms = opterms ;
    }


    Expression(Term term)
    {
        this.term = term ;
        this.opterms = new ArrayList<Pair<Operand, Term>>() ;
    }

    // 1 + (2*3)
    // exp1 op exp2
    // push 1
    // exp1 op exp2
    // push 2
    // push 3
    // push *
    // push +
    // = 7

//    public void writeExpression(VMWriter vmWriter) throws IOException {
//        if(this.opterms.size()==0)
//        {
//            if(term.type== Term.TERMTYPES.INTEGERCONSTANT)
//                vmWriter.writePush(VMWriter.MEMORYSEGMENT.CONST,term.integerConstant);
//
//            else if(term.type == Term.TERMTYPES.UNARYOPTERM)
//        }
//
//    }
}
