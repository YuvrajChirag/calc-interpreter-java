package scanner;

/**
 * Enum representing all possible token types used by the scanner.
 * These tokens are generated during lexical analysis of the source code
 * and later used by the parser for syntax analysis.
 */
public enum TokenType {

    NUMBER,           // Literal token representing numeric values
    STRING, // Literal token representing string values
    IDENTIFIER, // Token representing variable names or identifiers

    // Arithmetic operator tokens
    PLUS,      // +
    MINUS,     // -
    STAR,      // *
    SLASH,     // /

    
    ASSIGN, // Assignment operator token (:=)

    
    PRINT, // Print operator token (>>)

   
    IF,  // Conditional keyword token (?)

   
    REPEAT,  // Loop keyword token (@)

    
    ARROW, // Arrow operator token (=>), often used in mappings or expressions

    // Comparison operator tokens
    GT,        // >
    LT,        // <
    EQEQ,      // ==

    // Parentheses tokens for grouping expressions
    LPAREN,    // (
    RPAREN,    // )

    // Structural tokens for formatting-sensitive parsing
    NEWLINE,   // Indicates end of a line
    INDENT,    // Indicates increased indentation level
    DEDENT,    // Indicates decreased indentation level

    
    EOF // End-of-file token to mark input termination
}
