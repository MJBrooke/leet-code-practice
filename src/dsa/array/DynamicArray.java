package dsa.array;

import java.util.Arrays;

public class DynamicArray {

    private int length = 0; // Current number of stored elements
    private int capacity = 1; // Size of the current underlying array
    private int[] arr = new int[capacity]; // Actual underlying array to store data

    public void push(int n) {
        // We want to insert at end, unless we have reached capacity.
        if (length == capacity) resize();

        arr[length++] = n;
    }

    private void resize() {
        capacity *= 2;

        int[] newArr = new int[capacity];
        for (int i = 0; i < length; i++) newArr[i] = arr[i];
        arr = newArr;
    }

    public void print() {
        System.out.println(Arrays.toString(arr));
    }

    static void main() {
        var dynArr = new DynamicArray();
        dynArr.push(1);
        dynArr.push(2);
        dynArr.print();
    }

}
