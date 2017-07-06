package com.vito.newmonitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by $tangj$ on 2017/7/6.
 */

public class FileUtils {
    /**
     *
     * @param filePath 读取指定的文本文件
     * @return 读取的文本
     */
    public static String read(String filePath) {
        StringBuilder str = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String s;
            try {
                while ((s = in.readLine()) != null)
                    str.append(s + '\n');
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     *
     * @param filePath 写入指定的文本文件
     * @param append true表示追加，false表示重头开始写
     * @param text 文本字符串，text为null时直接返回
     */
    public static void write(String filePath, boolean append, String text) {
        if (text == null)
            return;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath,
                    append));
            try {
                out.write(text);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
