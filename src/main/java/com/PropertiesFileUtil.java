package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2018/1/18.
 */
public class PropertiesFileUtil {

    // apk路径
    public static String apkPath = "";

    public static String filePath = "";

    public static String storePass = "";

    // apk output
    public static String apkOutputPath = "";

    // apk文件
    public static String apkName = "";
    // 反编译命令
    public static String disPackageCmd = "";
    // 编译命令
    public static String packageCmd = "";
    // 嵌入的sdk目录
    public static String packageSdk = "";

    public static String cmdName = "";

    public static String insertStr1 = "";

    public static String startReg = "";

    public static String returnReg = "";

    public static String copyPath = "";

    public static String endName = "";

    public static String endReg = "";

    public static String cmdPath = "";

    public PropertiesFileUtil(String cmdPath){
        File file = new File(cmdPath+"/cmd.properties");
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(is);
            filePath = properties.getProperty("filePath");
            apkPath = filePath + "apk/";
            apkOutputPath = filePath + "output/";

            storePass = properties.getProperty("storePass");

            disPackageCmd = properties.getProperty("disPackageCmd");
            packageCmd = properties.getProperty("packageCmd");
            packageSdk = properties.getProperty("packageSdk");
            insertStr1 = properties.getProperty("insertStr");
            startReg = properties.getProperty("startReg");
            returnReg = properties.getProperty("returnReg");
            copyPath = properties.getProperty("copyPath");
            endName = properties.getProperty("endName");
            endReg = properties.getProperty("endReg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
