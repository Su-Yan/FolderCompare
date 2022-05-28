import java.io.IOException;
import java.util.Scanner;

public class FolderCompare {
    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);
        int threadnums = 0;     //线程数量
        FolderInformation folderInformation =new FolderInformation();
        //设置线程数，默认可用核心数+1
        System.out.println("请输入线程数（请输入1~20，否则使用默认）：");
        threadnums = sc.nextInt();
        if (threadnums <= 0 || threadnums >20){
            threadnums = Runtime.getRuntime().availableProcessors() + 1;
        }
        folderInformation.init();
        FolderCompareImplement folderCompareImplement =new FolderCompareImplement(folderInformation);
        folderCompareImplement.startCompare();
    }
}
