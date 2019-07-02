package com.cn.net;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Bookdig {
    public static void main(String[] args) {
        String urlStr = "http://category.dangdang.com/cp01.03.00.00.00.00-f0%7C0%7C1%7C0%7C0%7C0%7C.html";

        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            PrintWriter printWriter = new PrintWriter(new File("dangdang1.txt"));
            while((line=bufferedReader.readLine())!=null){
                //System.out.println(line);
                printWriter.println(line);
            }
            printWriter.close();
            bufferedReader.close();;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
