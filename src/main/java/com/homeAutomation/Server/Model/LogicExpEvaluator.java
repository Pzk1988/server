package com.homeAutomation.Server.Model;

import com.homeAutomation.Server.Exceptions.InfixPostfixParsException;
import com.homeAutomation.Server.Exceptions.PostfixParseException;
import com.homeAutomation.Server.Exceptions.UnknowResultException;
import com.homeAutomation.Server.Exceptions.UnknownTokenException;
import com.homeAutomation.Server.Interface.ILogicExpResult;
import com.homeAutomation.Server.Utilites.ShuntingYard;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class LogicExpEvaluator {

    public LogicExpEvaluator(){
        this.postfixExps = new ArrayList();
    }
    public void setInputs(AbstractCollection<Token> tokenList) {
        this.tokenList = tokenList;
        tokenList.add( new Operator(OperatoryType.AND));
        tokenList.add( new Operator(OperatoryType.OR));
        tokenList.add( new Operator(OperatoryType.GRT));
        tokenList.add( new Operator(OperatoryType.GRT_EQ));
        tokenList.add( new Operator(OperatoryType.LESS));
        tokenList.add( new Operator(OperatoryType.LESS_EQ));
    }
    public void setLogicExpResults(AbstractCollection<ILogicExpResult> logicExpResults) {
        this.logicExpResults = logicExpResults;
    }
    public void parsInfixToPostfix(InfixExpressions infixExpressions) {
        for(InfixExpression infixExpression : infixExpressions){
            String postfixExpression = ShuntingYard.convertToPostfix(infixExpression.getExpression());
            String out = infixExpression.getOut();
            try {
                addExpresstion(out, postfixExpression);
            }catch (InfixPostfixParsException e){
                System.out.println(e.getStackTrace());
                return;
            }
        }
    }
    public void evaluate() {
        for(PostfixExpression exp : postfixExps){
            exp.Evaluate();
        }
    }

    private void addExpresstion(String out, String postFixExp) throws InfixPostfixParsException {
        ILogicExpResult result;
        AbstractCollection<Token> tokenList;

        try{
            result = findOutput(out);
            tokenList = findTokens(postFixExp);
        }catch (UnknowResultException e){
            System.out.println("Unknown exp result: " + e.getOutput());
            throw new InfixPostfixParsException(postFixExp);

        }catch (PostfixParseException e){
            System.out.println("Unknown logic exp: " + e.getExp());
            throw new InfixPostfixParsException(postFixExp);
        }
        postfixExps.add( new PostfixExpression(result, tokenList));
    }
    private AbstractCollection<Token> findTokens(String postFixExp) throws  PostfixParseException{
        String[] postfixExpParts = postFixExp.split(" ");
        AbstractCollection<Token> exp = new ArrayList();

        try {
            for (String expToken : postfixExpParts) {
                Token.TokenType type = getType(expToken);
                if (type == Token.TokenType.OPERATOR) {
                    exp.add(findOperator(expToken));
                } else if (type == Token.TokenType.OPERAND) {
                    exp.add(findOperand(expToken));
                }
            }
        }catch(UnknownTokenException e){
            throw new PostfixParseException(postFixExp);
        }
        return exp;
    }
    private Token findOperand(String operand) throws UnknownTokenException{
        Token.TokenActivity activity = getActivity(operand);
        if(operand.charAt(0) == '+' || operand.charAt(0) == '-' || operand.charAt(0) == '!'){
            operand = operand.substring(1);
        }
        for(Token token : tokenList){
            if(token.getType() == Token.TokenType.OPERAND){
                if(token.getName().equals(operand)){
                    token.setActivity(activity);
                    return token;
                }
            }
        }
        try {
            Float decimalValue = Float.parseFloat(operand);
            return new Constant(decimalValue);
        }catch(Exception e)
        {

        }
        throw new UnknownTokenException(operand);
    }
    private Token findOperator(String operator) throws UnknownTokenException{
        for(Token token : tokenList){
            if(token.getName().equals(operator)){
                return token;
            }
        }
        throw new UnknownTokenException(operator);
    }
    private Token.TokenActivity getActivity(String expToken) {
        Token.TokenActivity activity = Token.TokenActivity.HIGH;
        if(expToken.charAt(0) == '-'){
            activity = Token.TokenActivity.FALLING_EDGE;
        } else if(expToken.charAt(0) == '+'){
            activity = Token.TokenActivity.RISING_EDGE;
        } else if(expToken.charAt(0) == '!'){
            activity = Token.TokenActivity.LOW;
        }
        return activity;
    }
    private Token.TokenType getType(String token) {
        if( token.equals("&") || token.equals("|") || token.equals(">") ||
            token.equals(">=") || token.equals("<") || token.equals("<=")){
            return Token.TokenType.OPERATOR;
        }else{
            return Token.TokenType.OPERAND;
        }
    }
    private ILogicExpResult findOutput(String out) throws UnknowResultException{
        ILogicExpResult result = null;
        for(ILogicExpResult logicExpResult : logicExpResults){
            if(logicExpResult.getName().equals(out)){
                result = logicExpResult;
            }
        }
        if(result == null){
            throw new UnknowResultException(out);
        }
        return  result;
    }


    private AbstractCollection<Token> tokenList;
    private AbstractCollection<ILogicExpResult> logicExpResults;
    private AbstractCollection<PostfixExpression> postfixExps;
}
