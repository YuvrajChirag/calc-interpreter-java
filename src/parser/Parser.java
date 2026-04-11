package parser;

import scanner.Token;
import scanner.TokenType;

import java.util.*;
import evaluator.AssignInstruction;
import evaluator.PrintInstruction;
import evaluator.IfInstruction;
import evaluator.RepeatInstruction;
import evaluator.Instruction;

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
 
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();
        while (!isAtEnd()) {
            instructions.add(parseStatement());
        }
        return instructions;
    }

    
   
    private Instruction parseStatement() {
        if (check(TokenType.IDENTIFIER)) {
            return parseAssignment();          
        }
        if (check(TokenType.PRINT)) {
            return parsePrint();           
        }
        if (check(TokenType.IF)) {
            return parseIf();          
        }
        if (check(TokenType.REPEAT)) {
            return parseRepeat();           
        }

        throw new RuntimeException("Invalid statement at line " + peek().getLine());
    }

    
    
    private Instruction parseAssignment() {      
        String varName = advance().getValue();       
        if (!match(TokenType.ASSIGN)) {
            throw new RuntimeException("Expected ':=' after variable");
        }       
        Expression expr = parseExpression();
        match(TokenType.NEWLINE);
        return new AssignInstruction(varName, expr);
    }

    private Instruction parsePrint() {       
        advance();
        Expression expr = parseExpression();    
        match(TokenType.NEWLINE);
        return new PrintInstruction(expr);
    }

    
    private Instruction parseIf() {
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
        List<Instruction> body = new ArrayList<>();
        while (!check(TokenType.DEDENT) && !isAtEnd()) {
            body.add(parseStatement());
        }      
        if (!match(TokenType.DEDENT)) {
            throw new RuntimeException("Expected DEDENT");
        }
        return new IfInstruction(condition, body);
    }

    
    private Instruction parseRepeat() {
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
        List<Instruction> body = new ArrayList<>();  
        while (!check(TokenType.DEDENT) && !isAtEnd()) {
            body.add(parseStatement()); // 
        }
        if (!match(TokenType.DEDENT)) {
            throw new RuntimeException("Expected DEDENT");
        }
        return new RepeatInstruction(count, body); 
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