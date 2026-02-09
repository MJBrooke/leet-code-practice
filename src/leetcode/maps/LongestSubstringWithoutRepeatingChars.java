package leetcode.maps;

import java.util.HashSet;
import java.util.Set;

/*
Given a string s, find the length of the longest substring without duplicate characters.
A substring is a contiguous sequence of characters within a string.

Example 1:
    Input: s = "zxyzxyz"
    Output: 3
    Explanation: The string "xyz" is the longest without duplicate characters.

Example 2:
    Input: s = "xxxx"
    Output: 1

Constraints:
    0 <= s.length <= 1000
    s may consist of printable ASCII characters.
 */
public class LongestSubstringWithoutRepeatingChars {

    /*
    Okay, so this is a sliding window problem.
    We want to be able to iterate over the entire String only once.
    So, we can start by using 2 pointers: left and right
    If the current window has no duplicates, we increase right+1 to increase scope.
    If we find a duplicate, we need to move the left+1 to start a new string without duplicates.
    The biggest this window gets is our answer.

    We can use Set to continuously detect duplicate characters in any given window.

    Complexity:
        Time: O(2n) since left and right can both move n times
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) return s.length();

        Set<Character> charactersSeen = new HashSet<>();

        int left = 0;
        int right = 0;
        int max = 0;

        while (right != s.length()) {
            char newChar = s.charAt(right);

            // We have hit a duplicate
            if (charactersSeen.contains(newChar)) {
                charactersSeen.remove(s.charAt(left));
                left++; // Shorten the current window
                // Note that we leave right as-is to be re-evaluated again
            } else { // Have not seen this char before in the current window
                charactersSeen.add(newChar);
                right++; // Increase current window

                // At any point where we add a new unique char to the current window's substring,
                //  we want to see if it is a new largest substring so far
                max = Math.max(max, right - left);
            }
        }

        return max;
    }

    static void main() {
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // Output: 3 ('kew' is longest)
        System.out.println(lengthOfLongestSubstring("pwwkew")); // Output: 3 ('kew' is longest)
        System.out.println(lengthOfLongestSubstring("zxyzxyz")); // Output: 3 ('xyz' is longest)
        System.out.println(lengthOfLongestSubstring("xxxx")); // Output: 1
    }
}
