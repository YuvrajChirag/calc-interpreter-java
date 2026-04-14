package parser; // Parser class belongs to parser package

import scanner.Token;
import scanner.TokenType;

import java.util.*;
import evaluator.*;

// Parser class: converts list of tokens into executable instructions (AST)
public class Parser {
    // List of tokens received from scanner
    private final List<Token> tokens;
    // Pointer to current token
    private int current = 0;

    // Constructor: initialize parser with tokens
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

     // Returns current token without consuming it
    private Token peek() {
        return tokens.get(current);
    }

    // Returns previous token
    private Token previous() {
        return tokens.get(current - 1);
    }

    // Move to next token and return previous one
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    // Check if we reached end of tokens
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    // Check if current token matches given type
    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }
        return peek().getType() == type;
    }

     // Check next token without consuming
    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size()) {
            return false;
        }
        return tokens.get(current + 1).getType() == type;
    }


    // Match current token with any of given types
    // If matched → consume it
    private boolean match(TokenType... types) {
        for(TokenType type : types){
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // Skip newline tokens
    private void skipNewlines() {
        while (match(TokenType.NEWLINE)) {
        // skip
        }
    }

    // Generate parse error with line info
    private RuntimeException error(Token token, String message) {
        return new RuntimeException("Parse error at line " + token.getLine() + ": " + message);
    }

    // Ensure expected token is present, else throw error
    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }
 
    // Main parse method → returns list of instructions
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();
        // Loop until end of tokens
        while (!isAtEnd()) {
            skipNewlines();
            if(isAtEnd()){
                break;
            }
             // Parse each instruction
            instructions.add(parseInstruction());
            skipNewlines();
        }
        return instructions;
    }

     // Parse a single instruction (if / repeat / assignment)
    private Instruction parseInstruction() {
        if (match(TokenType.IF)) {
            Expression condition = parseExpression();
            consume(TokenType.ARROW, "Expected '=>' after if condition.");
            consume(TokenType.NEWLINE, "Expected newline after if header.");
            consume(TokenType.INDENT, "Expected indented block after if.");

            // Parse body of if block
            List<Instruction> body = parseBlock();
            return new IfInstruction(condition, body);
        }
        // REPEAT loop
        if (match(TokenType.REPEAT)) {
            Expression countExpr = parseExpression();
            consume(TokenType.ARROW, "Expected '=>' after repeat count.");
            consume(TokenType.NEWLINE, "Expected newline after repeat header.");
            consume(TokenType.INDENT, "Expected indented block after repeat.");
            // Parse body of loop
            List<Instruction> body = parseBlock();
            return new RepeatInstruction(countExpr, body);
        }
        if (match(TokenType.PRINT)) {
            Expression expression = parseExpression();
            return new PrintInstruction(expression);
        }
        // Assignment: variable = expression
        if (check(TokenType.IDENTIFIER) && checkNext(TokenType.ASSIGN)) {
            String name = consume(TokenType.IDENTIFIER, "Expected variable name.").getValue();
            consume(TokenType.ASSIGN, "Expected '=' after variable name.");
            Expression expression = parseExpression();
            return new AssignInstruction(name, expression);
            }
            // If no valid instruction found → error
            throw error(peek(), "Unknown instruction starting with token " + peek().getType());
    }


    // Parse block of instructions (used in IF and REPEAT)
    private List<Instruction> parseBlock() {
        List<Instruction> body = new ArrayList<>();
        while (!check(TokenType.DEDENT) && !isAtEnd()) {
            skipNewlines();
            if (check(TokenType.DEDENT) || isAtEnd()) {
                break;
            }
            body.add(parseInstruction());
            skipNewlines();
        }
        // Ensure block ends properly
        consume(TokenType.DEDENT, "Expected end of indented block.");

        return body;
    }


    // Entry point for expression parsing
    private Expression parseExpression() {
        return parseComparison();
    }

     // Handle comparison operators (>, <, ==)
    private Expression parseComparison() {
        Expression expr = parseTermExpression();
        while (match(TokenType.GT, TokenType.LT, TokenType.EQEQ)) {
            Token operator = previous();
            Expression right = parseTermExpression();
            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }
        return expr;
    }


    // Handle + and - operations
    private Expression parseTermExpression() {
        Expression expr = parseFactor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = parseFactor();
            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }
        return expr;
    }


     // Handle * and / operations
    private Expression parseFactor() {
        Expression expr = parsePrimary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = parsePrimary();
            expr = new BinaryOpNode(expr, operator.getValue(), right);
        }
        return expr;
    }


    // Parse basic units: number, string, variable, parentheses
    private Expression parsePrimary() {
        // Number
        if (match(TokenType.NUMBER)) {
            return new NumberNode(Double.parseDouble(previous().getValue()));
        }
        // String
        if (match(TokenType.STRING)) {
            return new StringNode(previous().getValue());
        }
        // Variable
        if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(previous().getValue());
        }
        // Parentheses expression
        if (match(TokenType.LPAREN)) {
            Expression expression = parseExpression();
            consume(TokenType.RPAREN, "Expected ')' after expression.");
            return expression;
        }
        // Error if nothing matches
        throw error(peek(), "Expected expression.");
    }

}  