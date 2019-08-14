package com.cn.basic;

public class Red extends Color {
    public Red(){
        System.out.println("red");
    }
    public Red(int i){
        System.out.println("red"+i);
    }

    public static void main(String[] args) {
        //Color r = new Red(2);

        Red r1 = new Red();
        //Color c = new Color(4);
    }
}
