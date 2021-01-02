package com.mj;

import com.mj.sort.Sort;

// 计数排序，只针对整数
public class CountingSort extends Sort<Integer> {
    @Override
    protected void sort() {
        // 找出最大值
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        // 开辟内存空间，存储每个整数出现的次数
        int[] counts = new int[1 + max];
        for (int i = 0; i < array.length; i++) {
            // 统计每个整数出现的次数
            counts[array[i]]++;
        }
        // 根据整数出现次数，对整数进行排序
        int index = 0;
        for (int i = 0; i < counts.length; i++) {
            while (counts[i]-- > 0) {
                array[index++] = i;
            }
        }
    }
}
