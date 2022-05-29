import java.io.IOException;
import java.util.Scanner;

public class FolderCompare {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        FolderInformation folderInformation =new FolderInformation();
        FolderCompareImplement folderCompareImplement =new FolderCompareImplement(folderInformation);
        folderCompareImplement.startCompare();
        folderCompareImplement.getDiffFilesDetails();
        System.out.println("已获得差异文件信息，请问还要继续吗？继续则将拷贝差异文件至指定文件夹（y/n）");
        String _continue = sc.next();
        if (_continue.equalsIgnoreCase("n")){
            System.out.println("感谢使用！再见！");
            return;
        }
        folderCompareImplement.copyFilesToTargetPath();
    }
}
