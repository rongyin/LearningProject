import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<int[]> {
    int start;
    int end;
    int[] data;

    public ForkJoinMergeSort(int[] data,int start, int end){
        this.data=data;
        this.start=start;
        this.end=end;
    }
    @Override
    protected int[] compute() {
        if( data.length <4 ){
           return sort(data);
        } else {
            int middle = data.length/2;

            ForkJoinMergeSort forkJoinMergeSortLeft = new ForkJoinMergeSort(Arrays.copyOfRange(data,start,middle),0,middle);
            forkJoinMergeSortLeft.fork();
            ForkJoinMergeSort forkJoinMergeSortRight = new ForkJoinMergeSort(Arrays.copyOfRange(data,middle+1,data.length),0,end);
            forkJoinMergeSortRight.fork();

            return merge(forkJoinMergeSortLeft.join(),forkJoinMergeSortRight.join());

        }
    }
    int[] sort(int[] small){
        for (int i = 0; i < small.length; i++) {
            for (int j = i; j <small.length-1; j++) {
                if(small[j]>small[j+1]){
                    swap(j,j+1,small);
                }
            }
        }
        return small;
    }
    void swap(int a,int b,int[] data){
        data[a] = data[a]^data[b];
        data[b] = data[a]^data[b];
        data[a] = data[a]^data[b];
    }
    int[] merge(int[] leftArr,int[] rightArr){
        int[] newNum = new int[leftArr.length + rightArr.length];
        int i =0,j=0;
        int m =0;
        while (i < leftArr.length && j < rightArr.length) {
            newNum[m++] = leftArr[i] < rightArr[j] ? leftArr[i++] : rightArr[j++];
        }
        while (i < leftArr.length)
            newNum[m++] = leftArr[i++];
        while (j < rightArr.length)
            newNum[m++] = rightArr[j++];
        return newNum;
    }



}
