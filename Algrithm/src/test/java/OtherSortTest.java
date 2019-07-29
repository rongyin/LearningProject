import org.junit.Test;

public class OtherSortTest {

    @Test
    public void holandFlagSort(){
        int[] A = {2,1,0,2,0,1,0,1,2,0,0,1};
        int begin = 0;
        int current = 0;
        int end = A.length - 1;
        while (current<=end){
            if(A[current] == 0){
                Utils.swapInt(begin,current,A);
                begin++;
                current++;
            }
            else if(A[current] == 1)
                current++;
            else if(A[current] == 2){
                Utils.swapInt(current,end,A);
                end--;
            }
        }
        Utils.print(A);
    }
}
