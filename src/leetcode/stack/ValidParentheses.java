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

    /*
        What we want to do here is to keep track of each opening parenthesis that we come across.
        We want to ensure that there is a matching closing bracket in the same order that we have seen.
        This suits a stack structure well.

        We can simplify the problem a little by storing the expected closing bracket rather than what we have seen open.
        If at any point they don't match, or the stack is not empty by the end, it is not valid.
         */
    public static boolean isValid(String s) {
        // Based on any open bracket we see, we want to know what the expected closing bracket will look like.
        //  It will also allow us to tell if we can safely ignore a character that is not relevant.
        Map<Character, Character> openToClosingBracketMap = new HashMap<>();
        openToClosingBracketMap.put('(', ')');
        openToClosingBracketMap.put('{', '}');
        openToClosingBracketMap.put('[', ']');

        // Used to quickly identify if we have found a closing bracket in the iteration
        Set<Character> closingBrackets = new HashSet<>(openToClosingBracketMap.values());

        Deque<Character> expectedClosingBracket = new ArrayDeque<>();

        for (char ch : s.toCharArray())
            if (openToClosingBracketMap.containsKey(ch)) // We know that we have seen an opening bracket
                expectedClosingBracket.push(openToClosingBracketMap.get(ch)); // So we add an expectation for a closing equivalent
            else if (closingBrackets.contains(ch)) {
                if (expectedClosingBracket.isEmpty()) return false; // No closing bracket expected
                if (expectedClosingBracket.pop() != ch) return false; // Wrong closing bracket
            }

        return expectedClosingBracket.isEmpty();
    }

    static void main() {
        System.out.println(isValid("a[a]a"));
        System.out.println(isValid("([{s}])d"));
        System.out.println(isValid("ddd[(])"));
    }
}
