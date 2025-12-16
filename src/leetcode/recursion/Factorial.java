package leetcode.recursion;

/*
Use recursion to calculate a given factorial, such as 5!
 */
public class Factorial {

    /*
    Since factorial features a sub-problem, recursion can be used.
    5! = 5x4x3x2x1 = 5x4!
    So we have a diminishing set. The base case is 1, since all Factorial calculations end at that number.
     */
    public static int factorial(int num) {
        if (num == 1) return 1; // Base case

        return num * factorial(num - 1); // Recursion, with a smaller subset of data each time
    }

    static void main() {
        System.out.println(factorial(5));
    }
}
