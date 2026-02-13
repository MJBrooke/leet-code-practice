package leetcode.maps;

import java.util.Map;
import java.util.TreeMap;

/*
Card numbers are 16-digit numbers like 4444 4444 4444 4444.
BIN numbers are first few digits of card numbers like 4444 4444 44.
The inputs given initially are card types for the range of BIN numbers.
A cache has to be built initially and then given a card number, card type is to be returned, I assume in approximately O(1).

Example:
    Input:
        BIN range object = [
            ["4444 4444 11", "4444 4444 44", "Visa credit"],
            ["4500 0000 55", "4999 9999 00", "Visa debit"],
            ["4999 9999 99", "5555 0000 00", "Master credit"],
            ["6666 4444 11", "7777 0000 00", "Amex"]
        ].
    CardNumber: 4733 6109 7901 2139
    Output: Visa debit

    Explanation: BIN for 4733 6109 7901 2139 is 4733 6109 79
        which falls between BIN range of visa debit (4500 0000 55 to 4999 9999 00).
    Note that different card types need not have continuous BIN range.
    For example, if a cardNumber 6000 0000 0000 0000 is given,
        it does not belong to any card types and hence null is to be returned.
 */
public class TypeRegistry {

    /*
    Understanding the problem:
        We have contiguous ranges of numbers that map any value within it to a card.
        However, there are ranges of numbers in between mapped ones for which no mapping exists.

        Our natural inclination here is that we can use a TreeMap for the basic lookup.
        It allows us to query a range of keys - so we can take a number and request the key/value below it (ie. range start)
        We will have to deal with the upper bound too since we can't assume there is a mapping.
     */

    TreeMap<Long, BinRange> ranges;

    public TypeRegistry(String[][] rangesInput) {
        ranges = new TreeMap<>();
        buildCache(rangesInput);
    }

    public String getCardType(String cardNumber) {
        if (cardNumber == null || cardNumber.replaceAll("\\D", "").length() < 10)
            return null;
        /*
        Implementation:
            1. Create long BIN from card number
            2. Get the 'floor' entry (ie. the key smaller than or equal to input)
            3. Check if the key fits between start and end
                a. If yes: return value
                b. If no: return null
         */
        long cardBin = extractBin(cardNumber);
        var rangeEntry = ranges.floorEntry(cardBin);

        if (rangeEntry != null && cardBin <= rangeEntry.getValue().endOfRange) {
            return rangeEntry.getValue().value;
        } else return null;
    }

    private long extractBin(String cardNumber) {
        String bin = cardNumber.replaceAll("\\D", "").substring(0, 10);
        return Long.parseLong(bin);
    }

    private void buildCache(String[][] rangesInput) {
        for (String[] rangeInput : rangesInput) {
            var binRange = new BinRange(rangeInput);
            ranges.put(binRange.startOfRange, binRange);
        }
    }

    private static class BinRange {
        long startOfRange;
        long endOfRange;
        String value;

        public BinRange(String[] rangeInput) {
            this.startOfRange = parseNumber(rangeInput[0]);
            this.endOfRange = parseNumber(rangeInput[1]);
            this.value = rangeInput[2];
        }

        private long parseNumber(String str) {
            return Long.parseLong(str.replaceAll("\\D", ""));
        }
    }

    static void main(String[] args) {
        String[][] inputs = {
                {"4444 4444 11", "4444 4444 44", "Visa credit"},
                {"4500 0000 55", "4999 9999 00", "Visa debit"},
                {"4999 9999 99", "5555 0000 00", "Master credit"},
                {"6666 4444 11", "7777 0000 00", "Amex"}
        };

        TypeRegistry registry = new TypeRegistry(inputs);

        System.out.println(registry.getCardType("4733 6109 7901 2139")); // Output: Visa debit
        System.out.println(registry.getCardType("6000 0000 0000 0000")); // Output: null
        System.out.println(registry.getCardType("4444 4444 2200 0000")); // Output: Visa credit
    }
}
