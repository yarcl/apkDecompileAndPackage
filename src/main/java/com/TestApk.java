package com;
/*
import java.io.IOException;

*/
import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/13.
 */
public class TestApk {

    // apk路径
    public static String apkPath = "";
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

    static {
        InputStream is = TestApk.class.getClassLoader().getResourceAsStream("cmd.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
            apkPath = properties.getProperty("apkPath");
            apkName = properties.getProperty("apkName");
            disPackageCmd = properties.getProperty("disPackageCmd")+" "+apkName;
            packageCmd = properties.getProperty("packageCmd")+ " " + apkPath + apkName.substring(0, apkName.lastIndexOf("."));
            packageSdk = properties.getProperty("packageSdk");
            cmdName = properties.getProperty("cmdName");
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



    public static void main(String[] args) throws Exception{
        // 反编译
        // TestApk.deCompile(apkPath, disPackageCmd);

        // 反编译apk到当前目录



        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec("cmd.exe /c cd "+ apkPath + " && " + disPackageCmd + " && exit && exit");

            final Process process1 = process;

            InputStream inputStream = process1.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            TestApk.outputCmdResult(bufferedReader, process1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 指定的sdk目录全路径
        String targetPath = apkPath + copyPath + packageSdk;

        // 将文件copy到指定的目录
        TestApk.copyDirectory(apkPath+packageSdk, targetPath);





        // 将指定目录文件插入指定的内容 lib.myfile.test
        TestApk.insertStrToFile(apkPath+apkName.substring(0,apkName.lastIndexOf("."))+"\\"+cmdName, insertStr1);






        // 编译apk
        // TestApk.packageApk(apkPath, apkName);
        String executeCmd = packageCmd + " " +apkName.substring(0,apkName.lastIndexOf("."));
        String executeCmdFile = "cmd.exe /c cd "+ apkPath + " && " + executeCmd + " && exit && exit";
        // 反编译apk到当前目录
        Runtime run1 = Runtime.getRuntime();
        try {
            Process process = run1.exec(executeCmdFile);

            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            TestApk.outputCmdResult(bufferedReader, process);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // copy文件
    public static void copyFile(File sourceFile,File targetFile){
         if(!sourceFile.canRead()){
             System.out.println("源文件" + sourceFile.getAbsolutePath() + "不可读，无法复制！");
                 return;
         }else{
            System.out.println("开始复制文件" + sourceFile.getAbsolutePath() + "到" + targetFile.getAbsolutePath());
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
                     System.out.println("文件" + sourceFile.getAbsolutePath() + "复制到" + targetFile.getAbsolutePath() + "完成");
                 }catch(IOException e){
                     e.printStackTrace();
                 }
             }
         }
    }

    // 拷贝目录到指定目录
    public static void copyDirectory(String sourcePathString, String targetPathString){
        if(!new File(sourcePathString).canRead()){
             System.out.println("源文件夹" + sourcePathString + "不可读，无法复制！");
             return;
        }else{
             (new File(targetPathString)).mkdirs();
             System.out.println("开始复制文件夹" + sourcePathString + "到" + targetPathString);
             File[] files = new File(sourcePathString).listFiles();
             for(int i = 0; i < files.length; i++){
                 if(files[i].isFile()){
                     copyFile(new File(sourcePathString + File.separator + files[i].getName()),new File(targetPathString + File.separator + files[i].getName()));
                 }else if(files[i].isDirectory()){
                     copyDirectory(sourcePathString + File.separator + files[i].getName(),targetPathString + File.separator + files[i].getName());
                 }
             }
             System.out.println("复制文件夹" + sourcePathString + "到" + targetPathString + "结束");
         }
    }

    //
    public static void insertStrToFile(String cmdName, String insertStr) throws IOException {
        System.out.println("文件插入开始==============");
        String str1 = cmdName.replace('.', '/');
        File file = new File(str1+endName);

        StringBuilder result = new StringBuilder();
        String resultStr = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            Pattern returnPattern = Pattern.compile(returnReg);
            Pattern startPattern = Pattern.compile(startReg);
            Pattern endPattern = Pattern.compile(endReg);
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
            System.out.println("文件插入失败==============");
        }
        System.out.println("文件插入结束==============");
    }

    // 插入字符串到文件指定位置
    public static File insertStrFile(String str1, String resultStr){
        File file1 = new File(str1+"1"+endName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            bw.write(resultStr);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(str1+"1"+endName);
    }

    // 删除文件
    public static boolean deleteFile(File file) {
        System.out.println("开始删除文件"+file.getName());
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("文件删除成功");
                return true;
            } else {
                System.out.println("文件删除失败");
                return false;
            }
        } else {
            System.out.println("文件删除失败");
            return false;
        }

    }

    // 修改文件名称
    public static void reNameFile(File sourceFile, File targetFile){
        System.out.println("文件重命名开始"+sourceFile.getName());
        if(sourceFile.exists())
        {
            sourceFile.renameTo(new File(targetFile.getAbsolutePath()));
        }
        System.out.println("文件重命名结束"+targetFile.getName());
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
            process.destroy();
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
