package leetcode.maps;

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
    public static int lengthOfLongestSubstring(String s) {
        return 0;
    }

    static void main() {
        System.out.println(lengthOfLongestSubstring("zxyzxyz")); // Output: 3 ('xyz' is longest)
        System.out.println(lengthOfLongestSubstring("xxxx")); // Output: 1
    }
}
