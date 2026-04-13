package scanner;

// Represents a token produced during lexical analysis
public class Token {
    
    private final TokenType type; // Type of token (e.g., NUMBER, IDENTIFIER)
    
    private final String value; // Actual text of the token
    
    private final int line; // Line number where token appears

    // Creates a token with type, value, and line number
    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    // Returns token type
    public TokenType getType() {
        return type;
    }

    // Returns token value
    public String getValue() {
        return value;
    }

    // Returns line number
    public int getLine() {
        return line;
    }
}
