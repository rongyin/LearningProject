package com.cn.test.ag;

import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;

public class SortTest {
    static int swapCount = 0;
    int[] data = {120,-1, 0, 11, 9, 18, 6, 4, 1, 13, 3, 5, 8, 2, 88, 99, 10,-22};

    private static void swap(int a, int b, int[] data) {
        //int temp = data[a];
        //data[a] = data[b];
        //data[b] = temp;
        data[a] = data[a] ^ data[b];
        data[b] = data[a] ^ data[b];
        data[a] = data[a] ^ data[b];
        swapCount++;
    }

    private static void print(int[] data) {
        Arrays.stream(data).forEach(d -> {
            System.out.print(d + " ");
        });
        System.out.println(" swap count "+swapCount);
    }

    @Test
    public void swapTest() {
        int a = 2;
        int b = 4;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a + "," + b);
    }

    @Test
    public void selectionSort() {

        int min = 0, count = 0;

        for (int i = 0; i < data.length - 1; i++) {
            count++;
            min = i;
            for (int j = i + 1; j < data.length; j++) {
                count++;
                min = data[j] < data[min] ? j : min;
            }
            //System.out.println(min);
            if (min != i)
                swap(i, min, data);
        }

        print(data);
        System.out.println(":" + count);
    }

    @Test
    public void selectionSort2() {
        int min = 0, count = 0, max = data.length - 1;
        int len = data.length;
        for (int i = 0; i < len - 1; i++) {
            count++;
            min = i;
            for (int j = i + 1; j < len; j++) {
                count++;
                min = data[j] < data[min] ? j : min;
                max = data[j] < data[max] ? max : j;
            }
            //System.out.println(min);
            if (min != i)
                swap(i, min, data);

            if (max == min)
                max = min;

            if (max != len - 1)
                swap(len - 1, max, data);

            len--;
        }

        print(data);
        System.out.println(":" + count);
    }

    @Test
    public void bubbleTest1() {
        int count = 0;
        for (int i = 0; i < data.length - 1; i++) {
            count++;
            for (int j = 0; j < data.length - i - 1; j++) {
                count++;
                if (data[j] > data[j + 1]) {
                    swap(j, j + 1, data);
                }
            }
        }
        print(data);
        System.out.println(":" + count); // 119
    }

    @Test
    public void bubbleTest2() {
        int count = 0;
        boolean sorted = false;
        for (int i = 0; (i < data.length - 1) && !sorted; i++) {
            sorted = true;
            count++;
            for (int j = 0; j < data.length - i - 1; j++) {
                count++;
                if (data[j] > data[j + 1]) {
                    swap(j, j + 1, data);
                    sorted = false;
                }
            }
        }
        print(data);
        System.out.println(":" + count); // 105
    }

    @Test
    public void bubbleTest3() {
        int count = 0;
        int k, j, m;
        m = data.length - 1;
        while (m > 0) {
            count++;
            for (k = j = 0; j < m; j++) {
                count++;
                if (data[j] > data[j + 1]) {
                    swap(j, j + 1, data);
                    k = j;
                }
            }
            m = k;
        }
        print(data);
        System.out.println(":" + count); // 87
    }

    @Test
    public void bubbleTest4() {
        print(data);
        System.out.println("");
        int lower = 0;
        int top = data.length - 1;
        int index = 0;
        int count = 0;
        while (top > lower) {
            for (int i = lower; i < top; i++) {
                count++;
                if (data[i] > data[i + 1]) {
                    swap(i, i + 1, data);
                    index = i;
                }
            }
            top = index;

            for (int i = top; i > lower; i--) {
                count++;
                if (data[i] < data[i - 1]) {
                    swap(i, i - 1, data);
                    index = i;
                }
            }

            lower = index;
        }
        print(data);
        System.out.println(":" + count); // 58

    }

    @Test
    public void insertionSort1() {
        int count = 0;
        for (int i = 0; i < data.length - 1; i++) {
            count++;
            for (int j = i + 1; j > 0 && data[j] < data[j - 1]; j--) {
                swap(j, j - 1, data);
                count++;
            }
        }
        print(data); //23
        System.out.println(":" + count);
    }

    @Test
    public void insertionSort2() {
        int count = 0;
        for (int i = 0; i < data.length - 1; i++) {
            count++;
            int j = i + 1;
            int temp = data[j];
            boolean isChanged = false;

            for (; j > 0; j--) {
                count++;
                if (temp < data[j - 1]) {
                    data[j] = data[j - 1];
                    isChanged = true;
                } else {
                    if (isChanged)
                        data[j] = temp;

                    break;
                }

            }

        }
        print(data); //66
        System.out.println(":" + count);
    }

    @Test
    public void binaryInsertionTest(){

        for (int i = 1; i < data.length; i++) {
            int low = 0;
            int height = i-1;
            int temp = data[i];
            int middle = (low+height)/2;
            while (low<=height){
                if (temp<data[middle]){
                    height = middle -1;
                }else{
                    low = middle+1;
                }
                middle = (low + height)/2;
            }
            System.out.println(" start swap from "+i +" to "+low);
            for (int j = i; j>low; j--) {
                swap(j, j - 1, data);
            }
        }
        print(data);

    }

    @Test
    public void shellSort() {
        int h = 1;
        while (h <= data.length / 3) {
            h = h * 3 + 1;
        }

        for (int gap = h; gap > 0; gap = (gap - 1) / 3) {
            for (int i = gap; i < data.length; i++) {
                for (int j = i; j > gap - 1; j -= gap) {
                    if (data[j] < data[j - gap]) {
                        swap(j, j - gap, data);
                    }
                }
            }
        }

        print(data);
    }

    @Test
    public void shellSort2(){
        int count = 0;
        int gap = data.length/3;
        while(gap>0) {
            for (int i = 0; (i+gap) < data.length; i ++) {
                count++;
                if (data[i] > data[i + gap]) {
                    swap(i, i + gap, data);
                }
            }
            gap--;
        }
        print(data);
        System.out.println(" count "+count);
    }
    @Test
    public void mergeTest() {
        //mergeSort(data,0,data.length);
        int[] newNums = mergeSort2(data, 0, data.length - 1);
        print(newNums);
    }

    public static int[] mergeSort2(int[] nums, int l, int h) {
        if (l == h) {
            System.out.println(" return " + l);
            return new int[]{nums[l]};
        }
        int mid = l + (h - l) / 2;
        int[] leftArr = mergeSort2(nums, l, mid); //左有序数组
        int[] rightArr = mergeSort2(nums, mid + 1, h); //右有序数组
        int[] newNum = new int[leftArr.length + rightArr.length]; //新有序数组

        int m = 0, i = 0, j = 0;
        while (i < leftArr.length && j < rightArr.length) {
            newNum[m++] = leftArr[i] < rightArr[j] ? leftArr[i++] : rightArr[j++];
        }
        while (i < leftArr.length)
            newNum[m++] = leftArr[i++];
        while (j < rightArr.length)
            newNum[m++] = rightArr[j++];
        return newNum;
    }

    @Test
    public void quickSearch(){

        quickSearch(0,data.length-1);
        print(data);
    }
    private void quickSearch(int left ,int right){
        int temp = data[right];
        int i = left;
        int j = right-1;
        int swapPoint =right;
        if(left ==right)
            return;
        if (i==j ){
            if( data[i]>data[j])
                swap(left,right,data);

            return;
        }
        while(i<j) {
            while (data[i]<=temp && i<j){
                i++;
            }
            while (data[j]>temp && j>i){
                j--;
            }
            if(i<j ){
                //System.out.println(" right is "+right+" switch i "+i+" j "+j);
                swap(i,j,data);
            }
            i++;j--;
        }



        System.out.println("left is "+left+":"+data[left]+",before swap point " + swapPoint + " is " + data[swapPoint] + ", right " + right + ":" + data[right]);
        print(data);

        for (int k = left; k < right; k++) {
            if(data[k]>temp){
                swapPoint = k;
                break;
            }
        }
        if(swapPoint!=right)
            swap(swapPoint, right, data);

        print(data);
        System.out.println("-----");


        if(swapPoint>0)
            quickSearch(left,swapPoint-1);
        if(swapPoint<right)
            quickSearch(swapPoint,right);

    }
}
