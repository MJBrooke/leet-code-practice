package leetcode.recursion;

public class Fibonacci {

    private static int fibonacci(int num) {
        if (num == 0) return 0;
        if (num == 1) return 1;

        return fibonacci(num-1) + fibonacci(num-2);
    }

    static void main() {
        System.out.println(fibonacci(5));
        System.out.println(fibonacci(6));
    }
}
