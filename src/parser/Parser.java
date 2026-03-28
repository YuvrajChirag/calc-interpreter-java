
package parser;

import scanner.Token;
import scanner.TokenType;


import java.util.*;


public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }
 
    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
    }

    
   
    private void parseStatement() {

        if (check(TokenType.IDENTIFIER)) {
            parseAssignment();
            return;
        }

        if (check(TokenType.PRINT)) {
            parsePrint();
            return;
        }

        if (check(TokenType.IF)) {
            parseIf();
            return;
        }

        if (check(TokenType.REPEAT)) {
            parseRepeat();
            return;
        }

        throw new RuntimeException("Invalid statement at line " + peek().getLine());
    }

    
    
    private void parseAssignment() {

        
        String varName = advance().getValue();

        
        if (!match(TokenType.ASSIGN)) {
            throw new RuntimeException("Expected ':=' after variable");
        }
       
        Expression expr = parseExpression();

        match(TokenType.NEWLINE);

        System.out.println("Parsed Assignment: " + varName + " = " + expr);
    }

    private void parsePrint() {
        
        advance();
        Expression expr = parseExpression();    
        match(TokenType.NEWLINE);

        System.out.println("Parsed Print: " + expr);
    }

    
    private void parseIf() {

        
        advance();

        Expression condition = parseExpression();

        if (!match(TokenType.ARROW)) {
            throw new RuntimeException("Expected => after condition");
        }

        if (!match(TokenType.NEWLINE)) {
            throw new RuntimeException("Expected NEWLINE after =>");
        }

        if (!match(TokenType.INDENT)) {
            throw new RuntimeException("Expected INDENT");
        }

        while (!check(TokenType.DEDENT) && !isAtEnd()) {
            parseStatement();

        }

       
        if (!match(TokenType.DEDENT)) {
            throw new RuntimeException("Expected DEDENT");
        }

        System.out.println("Parsed If with condition: " + condition);
    }

    
    private void parseRepeat() {

       
        advance();

        
        Expression count = parseExpression();

        if (!match(TokenType.ARROW)) {
            throw new RuntimeException("Expected => after repeat count");
        }

        if (!match(TokenType.NEWLINE)) {
            throw new RuntimeException("Expected NEWLINE after =>");
        }

        if (!match(TokenType.INDENT)) {
            throw new RuntimeException("Expected INDENT");
        }

        while (!check(TokenType.DEDENT) && !isAtEnd()) {
            parseStatement();

        }

       
        if (!match(TokenType.DEDENT)) {
            throw new RuntimeException("Expected DEDENT");
        }

        System.out.println("Parsed Repeat with count: " + count);
    }


    private Expression parseExpression() {

        Expression expr = parseTerm();

        while (match(TokenType.GT) || match(TokenType.LT) || match(TokenType.EQEQ)) {
            Token operator = tokens.get(current - 1);
            Expression right = parseTerm();

            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }

        return expr;
    }

    private Expression parseTerm() {

        Expression expr = parseFactor();

        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token operator = tokens.get(current - 1);
            Expression right = parseFactor();

            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }

        return expr;
    }

    private Expression parseFactor() {

        Expression expr = parsePrimary();

        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token operator = tokens.get(current - 1);
            Expression right = parsePrimary();

            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }

        return expr;
    }

    private Expression parsePrimary() {

        Token t = advance();

        if (t.getType() == TokenType.NUMBER) {
            return new NumberNode(Double.parseDouble(t.getValue()));
        }

        if (t.getType() == TokenType.STRING) {
            return new StringNode(t.getValue());
        }

        if (t.getType() == TokenType.IDENTIFIER) {
            return new VariableNode(t.getValue());
        }

        if (t.getType() == TokenType.LPAREN) {
            Expression expr = parseExpression();

            if (!match(TokenType.RPAREN)) {
                throw new RuntimeException("Expected ')'");
            }

            return expr;
        }

        throw new RuntimeException("Invalid expression at line " + t.getLine());
    }

}  