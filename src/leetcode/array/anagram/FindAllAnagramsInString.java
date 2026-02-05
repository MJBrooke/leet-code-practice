package leetcode.array.anagram;

import java.util.ArrayList;
import java.util.List;

/*
Given two strings s and p, return an array of all the start indices of p's anagrams in s.
You may return the answer in any order.

Example 1:
    Input: s = "cbaebabacd", p = "abc"
    Output: [0,6]
    Explanation:
        The substring with start index = 0 is "cba", which is an anagram of "abc".
        The substring with start index = 6 is "bac", which is an anagram of "abc".

Example 2:
    Input: s = "abab", p = "ab"
    Output: [0,1,2]
    Explanation:
        The substring with start index = 0 is "ab", which is an anagram of "ab".
        The substring with start index = 1 is "ba", which is an anagram of "ab".
        The substring with start index = 2 is "ab", which is an anagram of "ab".


Constraints:
    1 <= s.length, p.length <= 3 * 10^4
    s and p consist of lowercase English letters. (set of 26 possibilities)
 */
public class FindAllAnagramsInString {

    /*
    Option 1:
        We could create an array[26] 'key' of p that can be used to compare if any piece of s is an anagram.
        We then use the length of p to create 2 pointers (lhs and rhs) that can be used to slide along s.
        For each increment of 1 for lhs/rhs, we compare to the key of p.

        I am not totally convinced this will be an optimal solution though.
        Although we slide along p once, we are indeed doing a lot of duplicate work re-checking each combination.
        Let's implement for practice and then see what we can improve.

        Time complexity is O(s.len * p.len)
     */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> anagramLocations = new ArrayList<>();

        if (p.length() > s.length()) return anagramLocations;

        int lhs = 0;
        int rhs = lhs + p.length();

        while (rhs != s.length()+1) {
            if (isAnagram(s.substring(lhs, rhs), p))
                anagramLocations.add(lhs);

            lhs++;
            rhs++;
        }

        return anagramLocations;
    }

    /*
    This comparison runs in O(n) time complexity where n is the length of the longest word.
    Space complexity runs in O(1) since we always use a fixed-size array of 26 length.
     */
    public static boolean isAnagram(String s, String p) {
        if (s.length() != p.length()) return false;

        int[] pFreqCounter = new int[26];
        for (int i = 0; i < s.length(); i++) {
            pFreqCounter[p.charAt(i) - 'a']++;
            pFreqCounter[s.charAt(i) - 'a']--;
        }

        for (int count : pFreqCounter) if (count != 0) return false;
        return true;
    }

    static void main() {
        System.out.println(
                findAnagrams("cbaebabacd", "abc")
        ); // Output: [0,6]

        System.out.println(
                findAnagrams("abab", "ab")
        ); // Output: [0,1,2]
    }
}
