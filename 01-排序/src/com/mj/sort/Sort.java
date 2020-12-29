package com.mj.sort;

public abstract class Sort {
    protected Integer[] array;
    protected int cmpCount;
    protected int swapCount;

    public void sort(Integer[] array) {
        if (array == null || array.length < 2) return;
        this.array = array;
        sort();
    }

    protected abstract void sort();

    protected int cmp(int i1, int i2) {
        cmpCount++;
        return array[i1] - array[i2];
    }

    protected void swap(int i1, int i2) {
        swapCount++;
        int tmp = array[i1];
        array[i1] = array[i2];
        array[i2] = tmp;
    }
}
