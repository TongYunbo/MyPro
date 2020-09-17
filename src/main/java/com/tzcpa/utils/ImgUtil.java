package com.tzcpa.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
/**
 * @ClassName ImgUtil
 * @Description
 * @Author hanxf
 * @Date 2019/5/27 18:05
 * @Version 1.0
 **/
@Component
public class ImgUtil {

    /**
     * 读数据库，获取图片输入流
     * @param adress
     * @return
     */
    public static FileInputStream query_getPhotoImageBlob(String adress) {
        FileInputStream is = null;
        File filePic = new File(adress);
        try {
            is = new FileInputStream(filePic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return is;

    }
    /**
     * 获取图片并显示在页面
     *
     * @Author hanxf
     * @Date 18:08 2019/5/27
     * @param
     * @return void
    **/
    public static void queryPic(String address,HttpServletRequest request, HttpServletResponse response) throws IOException  {

        if (address != null){
            try(FileInputStream is = query_getPhotoImageBlob(address))
            {
                response.setContentType("image/png");

                if (is != null){
                    // 得到文件大小

                    int i = is.available();
                    byte data[] = new byte[i];
                    // 读数据
                    is.read(data);
                    is.close();
                    // 设置返回的文件类型
                    response.setContentType("image/png");
                    // 得到向客户端输出二进制数据的对象
                    OutputStream toClient = response.getOutputStream();
                    // 输出数据
                    toClient.write(data);
                    toClient.close();
                }

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
