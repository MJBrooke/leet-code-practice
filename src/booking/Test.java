package booking;

import java.util.*;

public class Test {

    public static String getShiftedString(String s, int leftShifts, int rightShifts) {
        // If there is no String value, or it is just one character, there is no shifting needed
        if (s == null || s.length() < 2)
            return s;

        // For any set of shifts larger than the length of the string, the output ends up as it originally was.
        // So we can remove all extra ones with the modulo operation to keep only the effective number of shifts per side.
        int actualLeftShifts = leftShifts % s.length();
        int actualRightShifts = rightShifts % s.length();

        // We perform the set of leftShifts
        for (int i = 0; i < actualLeftShifts; i++)
            s = s.substring(1) + s.charAt(0); // String manipulation to build output

        // We perform the set of rightShifts
        for (int i = 0; i < actualRightShifts; i++)
            s = s.charAt(s.length()-1) + s.substring(0, s.length()-1);

        return s;
    }

    static void main() {
        System.out.println(getShiftedString("a", 2, 4)); // Expect "fgabcde"
        System.out.println(getShiftedString("abcdefg", 2, 4)); // Expect "fgabcde"
    }

    public static int minSum(List<Integer> num, int k) {
        if (num == null || num.isEmpty()) return 0;

        // Build our max-heap using a simple Comparator to ensure the largest is prioritised
        PriorityQueue<Integer> largestNumberQueue = new PriorityQueue<>(
                (a, b) -> b.compareTo(a)
        );
        largestNumberQueue.addAll(num);

        // Get the total sum of the input as-is
        int totalSum = 0;
        for (int n : num)
            totalSum += n;

        for (int i = 0; i < k; i++) {
            // Get the current largest and remove it from our total
            int currentLargest = largestNumberQueue.remove();
            totalSum -= currentLargest;

            // Perform the element math and add the new value back to the total
            int newValue = (int) Math.ceil(currentLargest / 2.0);
            totalSum += newValue;

            // Ensure that we put this value back in the Queue since it might still be the
            // largest number after the element math is done.
            largestNumberQueue.add(newValue);
        }

        return totalSum;
    }

    public static List<Integer> awardTopKHotels(String positiveKeywords, String negativeKeywords, List<Integer> hotelIds, List<String> reviews, int k) {
        Map<Integer, Integer> hotelScores = new HashMap<>();

        // Create sets of the keywords so that we have fast lookup when parsing reviews
        String[] tokenisedWords = positiveKeywords.toLowerCase().split(" ");
        Set<String> positiveWordSet = new HashSet<>(Arrays.asList(tokenisedWords));

        tokenisedWords = negativeKeywords.toLowerCase().split(" ");
        Set<String> negativeWordSet = new HashSet<>(Arrays.asList(tokenisedWords));

        for (int i = 0; i < reviews.size(); i++) {
            String currReview = reviews.get(i);
            int currHotel = hotelIds.get(i);

            int score = 0;
            String[] tokenisedReview = currReview.toLowerCase().replaceAll("[^a-z]", "").split("\\s+");
            for (String word : tokenisedReview) {
                if (negativeWordSet.contains(word)) score -= 1;
                else if (positiveWordSet.contains(word)) score += 3;
            }

            int hotelScore = hotelScores.getOrDefault(currHotel, 0) + score;
            hotelScores.put(currHotel, hotelScore);
        }

        PriorityQueue<Map.Entry<Integer, Integer>> highestScores = new PriorityQueue<>(
                (a, b) -> b.getValue().compareTo(a.getValue())
        );
        highestScores.addAll(hotelScores.entrySet());

        List<Integer> output = new ArrayList<>();
        for (int i = 0; i < Math.min(k, hotelIds.size()); i++)
            output.add(highestScores.remove().getKey());

        return output;
    }
}
