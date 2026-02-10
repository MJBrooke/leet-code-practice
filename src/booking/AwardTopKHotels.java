package booking;

import java.util.*;

public class AwardTopKHotels {

    // Note that this one did not pass all tests, so there are some remaining bugs despite being structurally a good approach.
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

            // Here was one massive mistake - my regex removes spaces as well, which wrecked the input.
            // I should have had '[^a-z ]' with the space at the end. That might have solved many of the test cases.
            String[] tokenisedReview = currReview.toLowerCase().replaceAll("[^a-z]", "").split(" ");
            for (String word : tokenisedReview) {
                if (negativeWordSet.contains(word)) score -= 1;
                else if (positiveWordSet.contains(word)) score += 3;
            }

            int hotelScore = hotelScores.getOrDefault(currHotel, 0) + score;
            hotelScores.put(currHotel, hotelScore);
        }

        PriorityQueue<Map.Entry<Integer, Integer>> highestScores = new PriorityQueue<>(
                // There was another issue here where I didn't introduce a tie-breaker for equal values.
                (a, b) -> b.getValue().compareTo(a.getValue())
        );
        highestScores.addAll(hotelScores.entrySet());

        List<Integer> output = new ArrayList<>();
        for (int i = 0; i < Math.min(k, hotelIds.size()); i++)
            output.add(highestScores.remove().getKey());

        return output;
    }
}
