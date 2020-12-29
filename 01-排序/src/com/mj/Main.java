package com.mj;

import com.mj.tool.Integers;
import com.mj.tool.Times;

import java.util.Arrays;

public class Main {

    public static void main(String args[]) {
        Integer[] array1 = Integers.random(10000, 1, 100000);
        Integer[] array2 = Integers.copy(array1);
        Integer[] array3 = Integers.copy(array1);

        Integer[] array4 = Integers.random(10, 1, 100);
        Integers.println(array4);
        selectionSort(array4);
        Integers.println(array4);

//        Times.test("bubbleSort1", () -> {
//            bubbleSort1(array1);
//        });
//
//        Times.test("bubbleSort2", () -> {
//            bubbleSort2(array2);1
//        });
//
//        Times.test("bubbleSort3", () -> {
//            bubbleSort3(array3);
//        });
    }

    // 最普通的冒泡
    static void bubbleSort1(Integer[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            for (int begin = 1; begin <= end; begin++) {
                if (array[begin] < array[begin - 1]) {
                    int temp = array[begin - 1];
                    array[begin - 1] = array[begin];
                    array[begin] = temp;
                }
            }
        }
    }

    // 考虑了途中数据已经排序完成后的冒泡
    static void bubbleSort2(Integer[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            boolean sorted = true;
            for (int begin = 1; begin <= end; begin++) {
                if (array[begin] < array[begin - 1]) {
                    int temp = array[begin - 1];
                    array[begin - 1] = array[begin];
                    array[begin] = temp;
                    sorted = false;
                }
            }
            if (sorted) break;
        }
    }

    // 终极版冒泡。。
    static void bubbleSort3(Integer[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            // sortedIndex的初始值在数组完全有序的时候有用
            int sortedIndex = 1;
            for (int begin = 1; begin <= end; begin++) {
                if (array[begin] < array[begin - 1]) { // 注意这边的<，如果用的是<=，那么会变成不稳定算法
                    int temp = array[begin - 1];
                    array[begin - 1] = array[begin];
                    array[begin] = temp;
                    sortedIndex = begin;
                }
            }
            end = sortedIndex;
        }
    }

    // 选择排序
    static void selectionSort(Integer[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            int maxIndex = 0;
            for (int begin = 1; begin <= end; begin++) {
                if (array[maxIndex] <= array[begin]) { // 注意<=，如果没有=，会变成不稳定算法
                    maxIndex = begin;
                }
            }
            int temp = array[end];
            array[end] = array[maxIndex];
            array[maxIndex] = temp;
        }
    }
}
