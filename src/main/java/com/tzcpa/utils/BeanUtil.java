package com.tzcpa.utils;

import com.tzcpa.model.student.HseRequest;

import java.io.*;
import java.util.List;

/**
 * @ClassName BeanUtil
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/13 12:32
 * @Version 6.0
 **/
public class BeanUtil {

    public static <T> List<T> deepCopy(List<HseRequest> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
