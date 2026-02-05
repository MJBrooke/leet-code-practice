package leetcode.array.anagram;

public class Anagram {

    /*
    We can reuse the smart array trick from GroupAnagrams here too.
    However, since we don't need to track anything in a Map or List and just give a true/false,
    we can go through one string incrementing each letter.
    Then go through the other decrementing.
    If all elements in the array are 0, it is an anagram or else not.

    a = length of String a
    b = length of String b

    Time Complexity: O(a + b)
    Space Complexity: O(1)
     */
    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) return false;

        int[] letterFrequency = new int[26];

        // Nit: Since we know the strings are of equal length, we can iterate once to that length
        //          and use the same for-loop counter as the index for both
        for (char ch : a.toCharArray())
            letterFrequency[ch - 'a']++;

        for (char ch : b.toCharArray())
            letterFrequency[ch - 'a']--;

        for (int freq : letterFrequency)
            if (freq != 0) return false;

        return true;
    }

    static void main() {
        System.out.println(isAnagram("pot", "top")); // true
        System.out.println(isAnagram("racecar", "carrace")); // true
        System.out.println(isAnagram("tops", "top")); // false
    }
}
