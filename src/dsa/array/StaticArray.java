package dsa.array;

import java.util.Arrays;

// https://neetcode.io/courses/dsa-for-beginners/2
// DSA functions for Static Arrays
public class StaticArray {

    // Traverse array: O(n)
    private static void traverseArr(int[] arr) {
        // Using index
        System.out.print("Index-loop: ");
        for (int i = 0; i < arr.length; i++) System.out.print(arr[i] + " ");

        System.out.println();

        // Using for-each
        System.out.print("For-each-loop: ");
        for (int num : arr) System.out.print(num + " ");
    }

    // Delete from end: O(1)
    private static void deleteEnd(int[] arr, int size) {
        if (size == 0) return;

        size--; // Removes 'ability' to query arr[size-1]
        arr[size] = 0; // Clear the value under-the-hood for clarity. Optional.
    }

    // Delete at n: O(n)
    // The trick here is that you start at the deletion idx and iteratively shift values 'left'
    private static void deleteAt(int[] arr, int deleteAt) {
        // Out of bounds checks
        if (deleteAt < 0 || deleteAt > arr.length-1) {
            System.out.println("Index to delete at is out of bounds");
            return;
        }

        System.out.println("Before: " + Arrays.toString(arr));

        int prev = deleteAt;
        for (int next = deleteAt + 1; next < arr.length; next++) {
            arr[prev++] = arr[next];
        }
        arr[arr.length-1] = 0; // Remove the last value (since deleting always leaves the end unchanged from the loop)

        System.out.println("After: " + Arrays.toString(arr));
        System.out.println();
    }

    // Insert at end: O(1)
    private static void append(int[] arr, int val, int len, int cap) {
        if (len >= cap) {
            System.out.println("Array is full");
            return;
        }

        arr[len-1] = val;
        len++; // This has no side effects, just here to show we need to move the pointer forward
    }

    // Insert at n: O(n)
    // The trick here is that you need to shift all values 'right' to create the extra space
    //    and then set the right value
    private static void insertAt(int[] arr, int insertAt, int val) {
        if (insertAt < 0 || insertAt >= arr.length) {
            System.out.println();
        }

        System.out.println("Before: " + Arrays.toString(arr));

        for (int idx = arr.length-1; idx > insertAt; idx--) {
            arr[idx] = arr[idx-1];
        }

        arr[insertAt] = val;

        System.out.println("After: " + Arrays.toString(arr));
        System.out.println();
    }

    static void main() {
        traverseArr(new int[]{1, 2, 3});

        deleteAt(new int[]{1,2,3,4}, -1);
        deleteAt(new int[]{1,2,3,4}, 0);
        deleteAt(new int[]{1,2,3,4}, 1);
        deleteAt(new int[]{1,2,3,4}, 2);
        deleteAt(new int[]{1,2,3,4}, 3);
        deleteAt(new int[]{1,2,3,4}, 4);

        insertAt(new int[]{1,3,5}, 0, 9);
        insertAt(new int[]{1,3,5}, 1, 9);
        insertAt(new int[]{1,3,5}, 2, 9);
    }
}
