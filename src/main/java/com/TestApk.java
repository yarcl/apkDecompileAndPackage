package com;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/13.
 */
public class TestApk {

    public static PropertiesFileUtil propertiesFileUtil;

    public static void main(String[] args) throws Exception{
        System.out.println(args[0]);
        propertiesFileUtil = new PropertiesFileUtil(args[0]);
        // 获取所有的文件名称
        List<String> fileNames = TestApk.readFileAllApkName();
        // 做所有文件的反编译
        TestApk.decompackApk(fileNames);
        // 做所有的apk的打包
        TestApk.packageAllApk(fileNames);
        // 做所有的apk的签名
        TestApk.signAllApkFiles(fileNames);

        System.out.println("The task is over!!!");
    }

    // copy文件
    public static void copyFile(File sourceFile,File targetFile){
         if(!sourceFile.canRead()){
             System.out.println("SourceFile:" + sourceFile.getAbsolutePath() + "is not able to read, copy failure!");
                 return;
         }else{
             System.out.println("Start copy file:" + sourceFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath());
             FileInputStream fis = null;
             BufferedInputStream bis = null;
             FileOutputStream fos = null;
             BufferedOutputStream bos = null;
             try{
                 fis = new FileInputStream(sourceFile);
                 bis = new BufferedInputStream(fis);
                 fos = new FileOutputStream(targetFile);
                 bos = new BufferedOutputStream(fos);
                 int len = 0;
                 while((len = bis.read()) != -1){
                         bos.write(len);
                     }
                 bos.flush();
             }catch(FileNotFoundException e){
                 e.printStackTrace();
             }catch(IOException e){
                 e.printStackTrace();
             }finally{
                 try{
                     if(fis != null){
                             fis.close();
                         }
                     if(bis != null){
                             bis.close();
                         }
                     if(fos != null){
                             fos.close();
                         }
                     if(bos != null){
                             bos.close();
                         }
                     System.out.println("Copy file:" + sourceFile.getAbsolutePath() + " to " + targetFile.getAbsolutePath() + " complement!");
                 }catch(IOException e){
                     e.printStackTrace();
                 }
             }
         }
    }

    // 拷贝目录到指定目录
    public static void copyDirectory(String sourcePathString, String targetPathString){
        if(!new File(sourcePathString).canRead()){
             System.out.println("Source directory:" + sourcePathString + "is not able to read, copy failure!");
             return;
        }else{
             (new File(targetPathString)).mkdirs();
             System.out.println("Start copy directory file:" + sourcePathString + " to " + targetPathString+"!");
             File[] files = new File(sourcePathString).listFiles();
             for(int i = 0; i < files.length; i++){
                 if(files[i].isFile()){
                     copyFile(new File(sourcePathString + File.separator + files[i].getName()),new File(targetPathString + File.separator + files[i].getName()));
                 }else if(files[i].isDirectory()){
                     copyDirectory(sourcePathString + File.separator + files[i].getName(),targetPathString + File.separator + files[i].getName());
                 }
             }
             System.out.println("Copy directory:" + sourcePathString + " to " + targetPathString + " end!");
         }
    }

    //
    public static void insertStrToFile(String cmdName, String insertStr) throws IOException {
        System.out.println("File insert start==============");
        String str1 = cmdName.replace('.', '/');
        File file = new File(str1+propertiesFileUtil.endName);

        StringBuilder result = new StringBuilder();
        String resultStr = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            Pattern returnPattern = Pattern.compile(propertiesFileUtil.returnReg);
            Pattern startPattern = Pattern.compile(propertiesFileUtil.startReg);
            Pattern endPattern = Pattern.compile(propertiesFileUtil.endReg);
            boolean flag1 = false;
            boolean flag2 = false;
            String s = null;
            String bufferStr = "";
            int i = 0;
            // 临界最后一行问题
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                i++;
                Matcher returnM = returnPattern.matcher(bufferStr);
                Matcher startM = startPattern.matcher(s);
                Matcher endM = endPattern.matcher(s);

                if(startM.find()){
                    flag1 = true;
                }
                if(flag1&&flag2&&endM.find()&&returnM.find()){
                    flag1 = false;
                    result.append(System.lineSeparator()+insertStr);
                    result.append(System.lineSeparator()+bufferStr);
                    result.append(System.lineSeparator()+s);
                } else if(flag1&&returnPattern.matcher(s).find()){
                    flag2 = true;
                } else {
                    flag2 = false;
                    if(i==1) {
                        result.append(s);
                    } else {
                        result.append(System.lineSeparator() + s);
                    }
                }
                bufferStr = s;
            }
            br.close();
            resultStr = result.toString();

            File file1 = TestApk.insertStrFile(str1, resultStr);
            TestApk.deleteFile(file);

            TestApk.reNameFile(file1, file);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("File insert failure==============");
        }
        System.out.println("File insert end==============");
    }

    // 插入字符串到文件指定位置
    public static File insertStrFile(String str1, String resultStr){
        File file1 = new File(str1+"1"+propertiesFileUtil.endName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            bw.write(resultStr);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(str1+"1"+propertiesFileUtil.endName);
    }

    // 删除文件
    public static boolean deleteFile(File file) {
        System.out.println("Start delete file:"+file.getName());
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("Success to delete file");
                return true;
            } else {
                System.out.println("Delete file failure");
                return false;
            }
        } else {
            System.out.println("Delete file failure");
            return false;
        }

    }

    // 修改文件名称
    public static void reNameFile(File sourceFile, File targetFile){
        System.out.println("File rename start:"+sourceFile.getName());
        if(sourceFile.exists())
        {
            sourceFile.renameTo(new File(targetFile.getAbsolutePath()));
        }
        System.out.println("File rename over:"+targetFile.getName());
    }

    public static void outputCmdResult(BufferedReader bufferedReader, Process process){
        try {
            String line1 = null;
            while ((line1 = bufferedReader.readLine()) != null) {
                if (line1 != null){
                    System.out.println(line1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            try {
                process.waitFor();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List readFileAllApkName(){
        File file = new File(propertiesFileUtil.apkPath);
        List<String> fileNames = new ArrayList<String>();
        File[] files = file.listFiles();

        for(File file1 : files){
            if(file1.isDirectory()){
                continue;
            } else if(file1.getName().endsWith(".apk")){
                fileNames.add(file1.getName());
            }
        }
        return fileNames;
    }


    // 一次性做所有apk的签名
    public static void signAllApkFiles(List<String> fileNames){
        int fileCount = 0;
        int fileSizes = fileNames.size();
        while(fileCount < fileSizes) {
            String fileName =  fileNames.get(fileCount);
            if(null != fileName && !"".equals(fileName)){
                propertiesFileUtil.apkName = fileName;
            } else {
                fileCount++;
                continue;
            }
            fileCount++;
            String signerExecuteCmd = "cmd.exe /c cd " + propertiesFileUtil.apkOutputPath + propertiesFileUtil.apkName.substring(0, propertiesFileUtil.apkName.lastIndexOf("."))
                    + "/dist/" + " && " + " jarsigner -storepass "+ propertiesFileUtil.storePass +" -verbose -keystore demo.keystore "
                    + propertiesFileUtil.apkName + " "+"demo.keystore " + " && exit && exit";
            // 反编译apk到当前目录
            Runtime run2 = Runtime.getRuntime();
            try {
                Process process = run2.exec(signerExecuteCmd);
                final Process process2 = process;
                InputStream inputStream = process2.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                TestApk.outputCmdResult(bufferedReader, process2);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    // 一次性做所有的打包
    public static void packageAllApk(List<String> fileNames){
        int fileCount = 0;
        int fileSizes = fileNames.size();
        String packageCmdTmp = "";
        while(fileCount < fileSizes) {
            String fileName =  fileNames.get(fileCount);
            if(null != fileName && !"".equals(fileName)){
                propertiesFileUtil.apkName = fileName;
                packageCmdTmp =  propertiesFileUtil.packageCmd + " " + propertiesFileUtil.apkOutputPath
                        + propertiesFileUtil.apkName.substring(0, propertiesFileUtil.apkName.lastIndexOf("."));
            } else {
                fileCount++;
                continue;
            }
            fileCount++;
            // 打包apk
            String executeCmd = packageCmdTmp + " " + propertiesFileUtil.apkName.substring(0, propertiesFileUtil.apkName.lastIndexOf("."));
            String executeCmdFile = "cmd.exe /c cd " + propertiesFileUtil.apkOutputPath + " && " + executeCmd + " && exit && exit";
            Runtime run1 = Runtime.getRuntime();
            try {
                Process process = run1.exec(executeCmdFile);
                InputStream inputStream = process.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TestApk.outputCmdResult(bufferedReader, process);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            // 将签名文件发送到每个apk包所在的目录
            copyFile(new File(propertiesFileUtil.filePath + "demo.keystore"),new File(propertiesFileUtil.apkOutputPath
                    + propertiesFileUtil.apkName.substring(0, propertiesFileUtil.apkName.lastIndexOf(".")) + "/dist/demo.keystore"));
        }
    }


    // 一次性做所有的反编译+sdk嵌入和插码
    public static void decompackApk(List<String> fileNames){
        int fileCount = 0;
        int fileSizes = fileNames.size();
        String disPackageCmdTmp = "";
        while(fileCount < fileSizes){
            // 获取文件名并且设置相关命令
            String fileName =  fileNames.get(fileCount);
            if(null != fileName && !"".equals(fileName)){
                propertiesFileUtil.apkName = fileName;
                disPackageCmdTmp = propertiesFileUtil.disPackageCmd + " "+propertiesFileUtil.apkName + " -o "
                        + propertiesFileUtil.apkOutputPath+propertiesFileUtil.apkName.substring(0, propertiesFileUtil.apkName.lastIndexOf("."));
                propertiesFileUtil.copyPath = fileName.substring(0, propertiesFileUtil.apkName.lastIndexOf(".")) + "/smali/";
            } else {
                fileCount++;
                continue;
            }
            fileCount++;

            // 反编译apk到当前目录
            Runtime run = Runtime.getRuntime();
            try {
                Process process = run.exec("cmd.exe /c cd "+ propertiesFileUtil.apkPath + " && " + disPackageCmdTmp + " && exit && exit");
                final Process process1 = process;
                InputStream inputStream = process1.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                TestApk.outputCmdResult(bufferedReader, process1);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            try {
                propertiesFileUtil.cmdName = "smali." + TestApk.parseXmlFile(fileName);
                System.out.println("the package directory is "+propertiesFileUtil.cmdName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            // 指定的sdk目录全路径
            // 将文件copy到指定的目录
            // 将指定目录文件插入指定的内容
            String targetPath = propertiesFileUtil.apkOutputPath + propertiesFileUtil.copyPath + propertiesFileUtil.packageSdk;
            TestApk.copyDirectory(propertiesFileUtil.filePath + propertiesFileUtil.packageSdk, targetPath);
            try {
                TestApk.insertStrToFile(propertiesFileUtil.apkOutputPath+propertiesFileUtil.apkName.substring(0,propertiesFileUtil.apkName.lastIndexOf("."))+"\\"+propertiesFileUtil.cmdName, propertiesFileUtil.insertStr1);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static String parseXmlFile(String fileName) throws FileNotFoundException {
        String androidFileName = propertiesFileUtil.apkOutputPath+fileName.substring(0, propertiesFileUtil.apkName.lastIndexOf("."))+"/AndroidManifest.xml";
        String packageName = "";
        String classPackageName = "";
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        Document document;
        try {
            document = reader.read(new File(androidFileName));
            //document = reader.read(new File("src/AndroidManifest.xml"));
            Element root = document.getRootElement();//得到xml跟标签，此处是<mainfest></mainfest>
            packageName = root.attribute("package").getValue();
            Element applications = (Element)(root.elements("application").get(0));
            Attribute androidName = applications.attribute("name");
            if(null != androidName){
                classPackageName = androidName.getValue();
                if(null != classPackageName && classPackageName.startsWith(".")){
                    classPackageName = packageName + classPackageName;
                }
            } else {
                List<Element> activitys = applications.elements("activity");
                for (Element activity : activitys){
                    Element intentFilter = activity.element("intent-filter");
                    if(null == intentFilter){
                        continue;
                    } else {
                        Attribute activityName = activity.attribute("name");
                        String activityNameString = activityName.getValue();
                        if(null != activityNameString && activityNameString.startsWith(".")){
                            classPackageName = packageName + activityNameString;
                            break;
                        } else if(null != activityNameString){
                            classPackageName = activityNameString;
                            break;
                        } else {
                            System.out.println("Can't find the main class for insert text!");
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            System.out.println("parse xml file is error, please check your xml files");
        }
        return classPackageName;
    }
}
