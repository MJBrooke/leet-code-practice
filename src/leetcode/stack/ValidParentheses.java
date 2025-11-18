package leetcode.stack;

import java.util.*;

/*
You are given a string s consisting of the following characters:
    '(', ')', '{', '}', '[' and ']'.

The input string s is valid if and only if:
    - Every open bracket is closed by the same type of close bracket.
    - Open brackets are closed in the correct order.
    - Every close bracket has a corresponding open bracket of the same type.

Return true if s is a valid string, and false otherwise.
 */
public class ValidParentheses {

    public static boolean isValid(String s) {
        // We want to push each open bracket onto the stack
        // For each closing bracket, we pop off the stack
        // If the types are not the same, false
        // If the stack is not empty by the end, false

        Map<Character, Character> brackets = new HashMap<>(Map.of(
                ')', '(',
                '}', '{',
                ']', '['
        ));

        // Only necessary if there can be other characters in the string.
        Set<Character> openBrackets = new HashSet<>(brackets.values());

        Deque<Character> stack = new ArrayDeque<>();

        // Iterate over entire string
        for (int i = 0; i < s.length(); i++) {
            // Get current character
            char ch = s.charAt(i);

            // Is an open bracket?
            if (openBrackets.contains(ch)) {
                // Put the open bracket into the stack
                stack.push(ch);
                // Continue to next character
                continue;
            }

            // Is a closed bracket?
            if (brackets.containsKey(ch)) {
                // Check that the last bracket seen matches the expected closed bracket
                if (stack.isEmpty() || stack.pop() != brackets.get(ch)) return false;
            }
        }

        // If the stack is not empty, there were no equal numbers of open/closed brackets
        return stack.isEmpty();
    }

    static void main() {
        System.out.println(isValid("a[a]a"));
        System.out.println(isValid("([{s}])d"));
        System.out.println(isValid("ddd[(])"));
    }
}
