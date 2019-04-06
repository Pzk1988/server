package com.homeAutomation.Server.Utilites;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static javax.swing.UIManager.put;

public class ShuntingYard {
    public static String convertToPostfix(String infix){
        return postfix(infix);
    }

    private enum Operator
    {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), AND(5), OR(6), GRT(7), LESS(8), GRT_EQU(9), LESS_EQU(10);
        final int precedence;
        Operator(int p) { precedence = p; }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
        put("&", Operator.AND);
        put("|", Operator.OR);
        put(">", Operator.GRT);
        put("<", Operator.LESS);
        put(">=", Operator.GRT_EQU);
        put("<=", Operator.LESS_EQU);
    }};

    private static boolean isHigerPrec(String op, String sub)
    {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    public static String postfix(String infix)
    {
        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<String>();

        for (String token : infix.split("\\s")) {
            // operator
            if (ops.containsKey(token)) {
                while ( ! stack.isEmpty() && isHigerPrec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);

                // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

                // right parenthesis
            } else if (token.equals(")")) {
                while ( ! stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();

                // digit
            } else {
                output.append(token).append(' ');
            }
        }

        while ( ! stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString();
    }

}
