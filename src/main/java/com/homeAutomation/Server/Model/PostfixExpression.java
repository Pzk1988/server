package com.homeAutomation.Server.Model;

import com.homeAutomation.Server.Interface.ILogicExpResult;

import java.util.AbstractCollection;
import java.util.Stack;

public class PostfixExpression {
    public PostfixExpression(ILogicExpResult out, AbstractCollection<Token> postfixExpression){
        this.out = out;
        this.postfixExpression = postfixExpression;
    }

    @Override
    public String toString(){
        return "Postfix: " + out + "=" + postfixExpression + "\r\n";
    }

    public void Evaluate(){
        Stack<Integer> stack = new Stack();
        for(Token token : postfixExpression){
            if(token.getType() == Token.TokenType.OPERAND){
                stack.push(getTokenValueForEvaluation(token));
            }else{
                int operand2 = stack.pop();
                int operand1 = stack.pop();

                switch(OperatoryType.valueOf(token.getValue()))
                {
                    case AND:
                        stack.push( operand1 & operand2);
                        break;
                    case OR:
                        stack.push( operand1 | operand2);
                        break;
                    case GRT:
                        stack.push( (operand1 > operand2) ? 1 : 0);
                        break;
                    case GRT_EQ:
                        stack.push( (operand1 >= operand2) ? 1 : 0);
                        break;
                    case LESS:
                        stack.push( (operand1 < operand2) ? 1 : 0);
                        break;
                    case LESS_EQ:
                        stack.push( (operand1 <= operand2) ? 1 : 0);
                        break;
                        default:
                }
            }
        }
        assert( stack.size() == 1);
        out.setValue(stack.pop());
        out.setOutOfControl( postfixExpression.stream().anyMatch(e -> e.getOutOfControl() == true));
    }
    private Integer getTokenValueForEvaluation(Token token) {
        if(token.getActivity() == Token.TokenActivity.HIGH){
            if(token.getOutOfControl() == false) {
                return token.getValue();
            }else{
                return 0;
            }
        }else if(token.getActivity() == Token.TokenActivity.LOW) {
            if(token.getValue() == 0 || token.getOutOfControl() == true){
                return 1;
            }else{
                return  0;
            }
        } else if(token.getActivity() == Token.TokenActivity.FALLING_EDGE){
            if( token.getPrevValue() == 1 && token.getValue() == 0 && token.getOutOfControl() == false){
                return 1;
            } else{
                return 0;
            }
        } else if(token.getActivity() == Token.TokenActivity.RISING_EDGE){
            if( token.getPrevValue() == 0 && token.getValue() == 1 && token.getOutOfControl() == false){
                return 1;
            } else{
                return 0;
            }
        } else{
            assert (false);
            return 0;
        }
    }
    private ILogicExpResult out;
    private AbstractCollection<Token> postfixExpression;
}
