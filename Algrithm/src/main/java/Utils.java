import java.util.Arrays;

public class Utils {
    public static void swapInt(int a,int b,int[] data){
        data[a] = data[a]^data[b];
        data[b] = data[a]^data[b];
        data[a] = data[a]^data[b];
    }

    public static void print(int[] data) {
        Arrays.stream(data).forEach(d -> {
            System.out.print(d + " ");
        });
    }
}
