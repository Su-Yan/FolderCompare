import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FolderInformation {

    private String basePath = "";       //基版路径
    private List<String> targetPathList = new ArrayList<>(); //目标路径
    private String blackListPath = "";  //黑名单路径
    private String outputPath="";       //输出文件目录

    public void init(){
        Scanner sc = new Scanner(System.in);
        String temp = "";
        File file = null;
        //基版路径
        while(true){
            System.out.println("请输入基版路径：");
            basePath = sc.next();
            file = new File(basePath);
            if (file.isDirectory()){
                break;
            }else {
                System.out.println("输入路径有误！请重新输入！！！");
            }
        }
        //目标目录
        System.out.println("请输入目标路径（输入0结束）：");
        temp = sc.next();
        while (!temp.equals("0")){
            file = new File(temp);
            if (!file.isDirectory()||basePath.equals(temp)){
                System.out.println("路径有误！且不要输入与基版路径相同路径！请重新输入！！！");
                temp = sc.next();
                continue;
            }
            targetPathList.add(temp);
            temp = sc.next();
        }
        //输出目录
        System.out.println("请输入差异输出路径（输入0为默认目录）：");
        temp = sc.next();
        while (!temp.equals("0")){
            file = new File(temp);
            if (!file.isDirectory()){
                System.out.println("输入路径有误！请重新输入！！！");
                temp = sc.next();
                continue;
            }
            outputPath = temp;
        }
        if (temp.equals("0")){
            outputPath = "";
        }
        //黑名单列表目录
//        while(true){
//            System.out.println("请输入黑名单txt路径：");
//            outputPath = sc.next();
//            file = new File(outputPath);
//            if (file.isDirectory()){
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

    public String getOutputPath() {
        return outputPath;
    }
}
