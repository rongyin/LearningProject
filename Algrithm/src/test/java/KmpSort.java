import org.junit.Test;

public class KmpSort {
    String str = "abcxabcdabcdabcy";
    String subString = "abcdabcy";

    @Test
    public  void volientWay(){
        char[] str_array = str.toCharArray();
        char[] subString_array = subString.toCharArray();
        int i =0;
        int j =0;
        int k =0;
        while (i<str_array.length && j<subString_array.length){
            if(str_array[i]==subString_array[j]){
                i++;
                j++;
            }else{
                k++;
                j=0;
                i=k;
            }

        }
        System.out.println(j+":"+k);
    }

    /**
     * prefix table 最长公共前缀array
     * @param pattern
     * @return
     */
    private int[] computeTemporaryArray(char pattern[]){
        int [] lps = new int[pattern.length];
        int index =0;
        for(int i=1; i < pattern.length;){
            if(pattern[i] == pattern[index]){
                lps[i] = index + 1;
                index++;
                i++;
            }else{
                if(index != 0){
                    //index = lps[index-1];
                    index = 0;
                }else{
                    lps[i] =0;
                    i++;
                }
            }
        }
        return lps;
    }
    public boolean KMP(char []text, char []pattern){

        int lps[] = computeTemporaryArray(pattern);
        int i=0;
        int j=0;
        while(i < text.length && j < pattern.length){
            if(text[i] == pattern[j]){
                i++;
                j++;
            }else{
                if(j!=0){
                    j = lps[j-1];
                }else{
                    i++;
                }
            }
        }
        if(j == pattern.length){
            return true;
        }
        return false;
    }

    @Test
    public void calc(){
        char[] str_array = str.toCharArray();
        char[] subString_array = subString.toCharArray();
        char[] d = {'2','3','4','2','3','3','6','2','3'};
        int[] r = computeTemporaryArray(d);
        for (int i = 0; i < r.length; i++) {
            System.out.print(r[i]+",");
        }
    }
}
