package com.tzcpa.utils;

import java.io.*;

/**
 * 针对文件的操作工具类
 *
 * @author 田庆新
 */
public class FileOperateUtil {
    /**
     * 拷贝文件
     *
     * @param src  文件源目录
     * @param dest 文件目的目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void copyFile(String src, String dest) throws FileNotFoundException, IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            File file = new File(dest);
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file);
            int c;
            byte buffer[] = new byte[1024];
            while ((c = in.read(buffer)) != -1) {
                for (int i = 0; i < c; i++)
                    out.write(buffer[i]);
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
    }

    /**
     * 创建文件
     *
     * @param path
     * @param fileName
     * @throws IOException
     */
    public void createFile(String path, String fileName) throws IOException {
        File file = new File(path + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * 创建文件
     *
     * @param filepath
     * @throws IOException
     */
    public void createFile(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * 删除文件夹
     *
     * @param path
     */
    public void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    delDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param path     目录
     * @param filename 文件名
     */
    public void delFile(String path, String filename) {
        File file = new File(path + "/" + filename);
        if (file.exists() && file.isFile())
            file.delete();
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     */
    public void delFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile())
            file.delete();
    }

    /**
     * 读文件
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String readFile(String path) throws FileNotFoundException, IOException {
        String fileStr = "";
        BufferedReader br = null;
        try {
            File file = new File(path);
            br = new BufferedReader(new FileReader(file));
            String temp = null;
            StringBuffer sb = new StringBuffer();
            temp = br.readLine();
            while (temp != null) {
                sb.append(temp);
                temp = br.readLine();
            }
            fileStr = sb.toString();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileStr;
    }

    /**
     * 向文件里写数据
     *
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeFile(String path, String fileName, String data) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(path + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(data.getBytes());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向文件里写数据
     *
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeFile(String path, String fileName, byte[] data) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(path + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(data);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得指定文件的byte数组
     */
    public byte[] getBytesbyfile(String filePath) {
        byte[] buffer = null;
        try(FileInputStream fis = new FileInputStream(new File(filePath)))
        {
//            File file = new File(filePath);
//            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public String getFile(byte[] bfile, String filePath, String fileName) {
        FileOutputStream out = null;
        String filePathW = "";
        try {
            // System.out.println(DateFormat.getDateTime()+"：  getFile(filePath)="+filePath);
            // System.out.println(DateFormat.getDateTime()+"：  getFile(fileName)="+fileName);
            // System.out.println(DateFormat.getDateTime()+"：  getFile(filePath +File.pathSeparator + fileName)="+filePath +File.pathSeparator + fileName);
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdirs();
            filePathW = filePath + "/" + fileName;
            File file = new File(filePath + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(bfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePathW;
    }
}
