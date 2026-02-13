package leetcode.maps;

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

    Explanation: BIN for 4733 6109 7901 2139 is 4733 6109 79 which falls between BIN range of visa debit (4500 0000 55 to 4999 9999 00).
    Note that different card types need not have continuous BIN range.
    For example, if a cardNumber 6000 0000 0000 0000 is given, it does not belong to any card types and hence null is to be returned.
 */
public class TypeRegistry {

    public TypeRegistry(String[][] ranges) {

    }

    public String getCardType(String cardNumber) {
        return null;
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
