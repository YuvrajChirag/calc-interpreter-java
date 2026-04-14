package environment;

import java.util.HashMap;
import java.util.Map;

// Stores variable names and their values during execution
public class Environment {

    // Map holding variable-value pairs
    private final Map<String, Object> values = new HashMap<>();

    // Assigns value to a variable
    public void set(String name, Object value) {
        values.put(name, value);
    }

    // Retrieves value of a variable
    public Object get(String name) {
        if (!values.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return values.get(name);
    }
}