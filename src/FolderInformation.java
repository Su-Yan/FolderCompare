import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FolderInformation {

    private int threadNums = 0;             //线程数量
    private String basePath = "";       //基版路径
    private List<String> targetPathList = new ArrayList<>(); //目标路径
    private String blackListPath = "";  //黑名单路径
    private File outputDirectory;       //所有输出文件父目录
    private String fileDetailsPath = "";//详细文件差异目录

    private List<String> fileDetailsPathList = null;    //详细差异文件绝对路径
    private String diffFileOutputPath = ""; //差异文件本体输出目录


    FolderInformation(){
        Scanner sc = new Scanner(System.in);

        //设置线程数，默认可用核心数+1
//        System.out.println("请输入线程数（请输入1~20，否则使用默认）：");
//        threadNums = sc.nextInt();
//        if (threadNums <= 0 || threadNums >20){
//            threadNums = Runtime.getRuntime().availableProcessors() + 1;
//        }

        String tempString = "";
        File tempFile = null;
        //基版路径
        while(true){
            System.out.println("请输入基版路径：");
            basePath = sc.next();
            tempFile = new File(basePath);
            if (tempFile.isDirectory()){
                break;
            }else {
                System.out.println("输入路径有误！请重新输入！！！");
            }
        }
        //目标目录
        System.out.println("请输入目标路径（输入0结束）：");
        tempString = sc.next();
        while (!tempString.equals("0")){
            tempFile = new File(tempString);
            if (!tempFile.isDirectory()||basePath.equals(tempString)){
                System.out.println("路径有误！且不要输入与基版路径相同路径！请重新输入！！！");
                tempString = sc.next();
                continue;
            }
            targetPathList.add(tempString);
            tempString = sc.next();
        }
        //输出目录
        System.out.println("请输入差异输出路径（输入0为默认目录）：");
        tempString = sc.next();
        while (!tempString.equals("0")){
            tempFile = new File(tempString);
            if (!tempFile.isDirectory()){
                System.out.println("输入路径有误！请重新输入！！！");
                tempString = sc.next();
                continue;
            }
        }
        if (tempString.equals("0")){
            outputDirectory = new File(System.getProperty("user.dir")+"\\record\\");
        }else{
            outputDirectory = new File(tempString+"\\record\\");
        }
        if (!outputDirectory.exists()){
            outputDirectory.mkdirs();
        }
        //黑名单列表目录
//        while(true){
//            System.out.println("请输入黑名单txt路径：");
//            outputPath = sc.next();
//            tempFile = new File(outputPath);
//            if (tempFile.isDirectory()){
//                break;
//            }else {
//                System.out.println("输入路径有误！请重新输入！！！");
//            }
//        }
    }

    public String getBasePath() {
        return basePath;
    }

    public List<String> getTargetPathList() {
        return targetPathList;
    }

    public String getBlackListPath() {
        return blackListPath;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public String getFileDetailsPath() {
        return fileDetailsPath;
    }

    public List<String> getFileDetailsPathList() {
        return fileDetailsPathList;
    }

    public void setFileDetailsPath(String fileDetailsPath) {
        this.fileDetailsPath = fileDetailsPath;
    }

    public void setTargetPathList(List<String> targetPathList) {
        this.targetPathList = targetPathList;
    }

    public void setFileDetailsPathList(List<String> fileDetailsPathList) {
        this.fileDetailsPathList = fileDetailsPathList;
    }

    public String getDiffFileOutputPath() {
        return diffFileOutputPath;
    }

    public void setDiffFileOutputPath(String diffFileOutputPath) {
        this.diffFileOutputPath = diffFileOutputPath;
    }
}
