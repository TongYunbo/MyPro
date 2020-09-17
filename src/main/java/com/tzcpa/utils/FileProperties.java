
package com.tzcpa.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * FileProperties.java
 *
 * @author Yanjie Gao
 * @version f-1.9-web-agent-job
 * @date 2015/8/14 16:55
 */
public class FileProperties extends Properties {
    public FileProperties(String path) {
        try {
            Resource resource = new DefaultResourceLoader().getResource(path);
            load(resource.getInputStream());
        } catch (Exception e) {
            System.err.println("Exception in FileProperties(String): " + e.toString() + " for filename=" + path);
        }
    }

    public FileProperties(Properties properties) {
        super(properties);
    }

    public static void main(String[] args) {
        FileProperties fp = new FileProperties("/config.properties");
        System.out.println(fp.getProperty("esb_sender_url", "121"));
    }
}
