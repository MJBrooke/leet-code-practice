package leetcode.array;

import java.util.*;

/*
Given an array of strings strs, group all anagrams together into sublists.
You may return the output in any order.

An anagram is a string that contains the exact same characters as another string,
but the order of the characters can be different.

Example 1:
    Input: strs = ["act","pots","tops","cat","stop","hat"]
    Output: [["hat"],["act", "cat"],["stop", "pots", "tops"]]

Example 2:
    Input: strs = ["x"]
    Output: [["x"]]

Example 3:
    Input: strs = [""]
    Output: [[""]]

Constraints:
    1 <= strs.length <= 1000.
    0 <= strs[i].length <= 100
    strs[i] is made up of lowercase English letters.
 */
public class GroupAnagrams {

    /*
    Option 1:
        For each word, we sort it alphabetically and place it in a HashMap where the key is the sorted word and the value is a list of the actual words.
        We do this for every word, and then collect up the Values of the HashMap into a list and return it.

        Time complexity:
            n = number of words
            k = average word length

            sortWord = k log k (from sorting each word)
            groupAnagrams = n (we iterate each word once)
            Final complexity: O(n * k log k)
        Space complexity:
            n = number of words
            k = average word length

            All keys may be unique, so map has n keys
            Values can each be length k or more

            Final complexity: O(n * k)
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String str : strs) {
            var sortedStr = sortWord(str);

            var value = anagramGroups.getOrDefault(sortedStr, new ArrayList<>());
            value.add(str);
            anagramGroups.put(sortedStr, value);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    public static String sortWord(String str) {
        char[] strArr = str.toCharArray();
        Arrays.sort(strArr);
        return new String(strArr);
    }

    static void main() {
        //Expect ["hat"],["act", "cat"],["stop", "pots", "tops"]
        System.out.println(groupAnagrams(new String[]{"act","pots","tops","cat","stop","hat"}));

        // Expect [["x"]]
        System.out.println(groupAnagrams(new String[]{"x"}));

        // Expect [[""]]
        System.out.println(groupAnagrams(new String[]{""}));
    }
}
