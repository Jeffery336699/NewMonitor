package com.vito.newmonitor;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by zhaojie on 2016/7/18 0018.
 */
public class FileUtil {

    public static void save(String dir, String content) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            File target = new File(dir);
            int position = 0;
            int position1 = target.getAbsolutePath().lastIndexOf("/");
            int position2 = target.getAbsolutePath().lastIndexOf("\\");
            position = position1 != -1 ? position1 : position2;
            File folder = new File(target.getAbsolutePath().substring(0, position));
            if (!folder.exists()) {
                folder.mkdirs();
            }
            out = new FileOutputStream(target);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String filePath) {
        return readFile(new File(filePath));
    }

    public static String readFile(File file){
        return readFile(file, "UTF-8");
    }

    public static String readFile(File file, String fileEncode) {
        String str = null;
        try {
            if(file.isFile() && file.exists()) {
                FileInputStream in = new FileInputStream(file);
                // size  为字串的长度 ，这里一次性读完
                int size = in.available();
                byte[] buffer = new byte[size];
                in.read(buffer);
                in.close();
                str = new String(buffer, fileEncode);
            }else {
                Log.d("FileUtil","文件不存在.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return str;
    }
}
