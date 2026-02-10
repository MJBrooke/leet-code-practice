package booking;

public class ShiftedString {
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
}
