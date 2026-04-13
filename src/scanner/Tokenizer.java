package scanner;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Converts source code string into a list of tokens
public class Tokenizer {
    
    private final String source; // Input source code

    // Initializes tokenizer with source text
    public Tokenizer(String source) {
        this.source = source == null ? "" : source;
    }

    // Main method to tokenize entire source
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Deque<Integer> indentLevels = new ArrayDeque<>();
        indentLevels.push(0);

        // Normalize line endings
        String normalized = source.replace("\r\n", "\n").replace('\r', '\n');
        String[] lines = normalized.split("\n", -1);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNumber = i + 1;
            boolean isBlank = line.trim().isEmpty();

            // Handle indentation for non-empty lines
            if (!isBlank) {
                int indent = countIndent(line, lineNumber);

                while (indent > indentLevels.peek()) {
                    indentLevels.push(indentLevels.peek() + 4);
                    if (indentLevels.peek() > indent) {
                        throw new RuntimeException("Invalid indentation at line " + lineNumber);
                    }
                    tokens.add(new Token(TokenType.INDENT, "", lineNumber));
                }

                while (indent < indentLevels.peek()) {
                    indentLevels.pop();
                    tokens.add(new Token(TokenType.DEDENT, "", lineNumber));
                }

                if (indent != indentLevels.peek()) {
                    throw new RuntimeException("Invalid indentation at line " + lineNumber);
                }

                // Tokenize actual line content
                tokenizeLine(line.substring(indent), lineNumber, tokens);
            }

            // Add newline token after each line
            tokens.add(new Token(TokenType.NEWLINE, "", lineNumber));
        }

        // Close remaining indent levels
        while (indentLevels.peek() > 0) {
            indentLevels.pop();
            tokens.add(new Token(TokenType.DEDENT, "", lines.length));
        }

        // Add EOF token at end
        tokens.add(new Token(TokenType.EOF, "", lines.length + 1));
        return tokens;
    }

    // Counts leading spaces and validates indentation rules
    private int countIndent(String line, int lineNumber) {
        int indent = 0;
        while (indent < line.length() && line.charAt(indent) == ' ') {
            indent++;
        }

        // Tabs not allowed
        if (indent < line.length() && line.charAt(indent) == '\t') {
            throw new RuntimeException("Tabs are not supported for indentation at line " + lineNumber);
        }

        // Only multiples of 4 spaces allowed
        if (indent % 4 != 0) {
            throw new RuntimeException("Indentation must use multiples of 4 spaces at line " + lineNumber);
        }

        return indent;
    }

    // Tokenizes a single line into tokens
    private void tokenizeLine(String line, int lineNumber, List<Token> tokens) {
        int i = 0;

        while (i < line.length()) {
            char c = line.charAt(i);

            // Skip whitespace
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Parse numbers
            if (Character.isDigit(c)) {
                int start = i;
                while (i < line.length() &&
                       (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.')) {
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, line.substring(start, i), lineNumber));
                continue;
            }

            // Parse identifiers
            if (Character.isLetter(c) || c == '_') {
                int start = i;
                while (i < line.length() &&
                       (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '_')) {
                    i++;
                }
                tokens.add(new Token(TokenType.IDENTIFIER, line.substring(start, i), lineNumber));
                continue;
            }

            // Parse string literals
            if (c == '"') {
                int start = ++i;
                StringBuilder sb = new StringBuilder();

                while (i < line.length() && line.charAt(i) != '"') {
                    char ch = line.charAt(i);

                    if (ch == '\\' && i + 1 < line.length()) {
                        i++;
                        char escaped = line.charAt(i);

                        if (escaped == 'n') sb.append('\n');
                        else if (escaped == 't') sb.append('\t');
                        else sb.append(escaped);
                    } else {
                        sb.append(ch);
                    }
                    i++;
                }

                if (i >= line.length() || line.charAt(i) != '"') {
                    throw new RuntimeException("Unterminated string at line " + lineNumber +
                                               " starting index " + start);
                }

                i++;
                tokens.add(new Token(TokenType.STRING, sb.toString(), lineNumber));
                continue;
            }

            // Multi-character operators
            if (c == ':' && i + 1 < line.length() && line.charAt(i + 1) == '=') {
                tokens.add(new Token(TokenType.ASSIGN, ":=", lineNumber));
                i += 2;
                continue;
            }

            if (c == '=' && i + 1 < line.length() && line.charAt(i + 1) == '>') {
                tokens.add(new Token(TokenType.ARROW, "=>", lineNumber));
                i += 2;
                continue;
            }

            if (c == '=' && i + 1 < line.length() && line.charAt(i + 1) == '=') {
                tokens.add(new Token(TokenType.EQEQ, "==", lineNumber));
                i += 2;
                continue;
            }

            if (c == '>' && i + 1 < line.length() && line.charAt(i + 1) == '>') {
                tokens.add(new Token(TokenType.PRINT, ">>", lineNumber));
                i += 2;
                continue;
            }

            // Single-character tokens
            switch (c) {
                case '+': tokens.add(new Token(TokenType.PLUS, "+", lineNumber)); break;
                case '-': tokens.add(new Token(TokenType.MINUS, "-", lineNumber)); break;
                case '*': tokens.add(new Token(TokenType.STAR, "*", lineNumber)); break;
                case '/': tokens.add(new Token(TokenType.SLASH, "/", lineNumber)); break;
                case '>': tokens.add(new Token(TokenType.GT, ">", lineNumber)); break;
                case '<': tokens.add(new Token(TokenType.LT, "<", lineNumber)); break;
                case '?': tokens.add(new Token(TokenType.IF, "?", lineNumber)); break;
                case '@': tokens.add(new Token(TokenType.REPEAT, "@", lineNumber)); break;
                case '(': tokens.add(new Token(TokenType.LPAREN, "(", lineNumber)); break;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")", lineNumber)); break;
                default:
                    throw new RuntimeException("Unexpected character '" + c +
                                               "' at line " + lineNumber);
            }
            i++;
        }
    }
}
