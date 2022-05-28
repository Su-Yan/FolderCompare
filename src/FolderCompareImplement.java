import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class FolderCompareImplement {

    private File outputDirectory;
    private FolderInformation folderInformation;

    FolderCompareImplement(FolderInformation folderInformation){
        this.folderInformation = folderInformation;
        if (folderInformation.getOutputPath().equals("")||folderInformation.getOutputPath()==null){
            outputDirectory = new File(System.getProperty("user.dir")+"\\record\\");
        }else{
            outputDirectory = new File(folderInformation.getOutputPath());
        }
        if (!outputDirectory.exists()){
            outputDirectory.mkdirs();
        }
    }

    FolderCompareImplement(){
        outputDirectory = new File(System.getProperty("user.dir")+"\\record\\");
    }

    /**
     * 获取指定目录下的所有文件夹名称并输出
     * @param path  目标路径
     * @return
     * @throws IOException
     */
    public String getDirectoryList(String path) throws IOException {
        File targetDirectory = new File(path);
        File []directoryList = targetDirectory.listFiles(new FileFilter());
        FileWriter fileWriter = new FileWriter(outputDirectory.getAbsolutePath()+"\\"+targetDirectory.getName()+"_catalog_list.txt");
        fileWriter.write("");
        LinkedList<File> list =new LinkedList();
        for (File f :
                directoryList) {
            list.add(f);
        }
        File temp;
        while (!list.isEmpty()){
            temp = list.removeFirst();
            directoryList = temp.listFiles(new FileFilter());
            for (File f :
                    directoryList) {
                list.add(f);
            }
            fileWriter.append(temp.getAbsolutePath().replace(path,""));
            fileWriter.append("\n");
        }
        fileWriter.close();
        return new File(outputDirectory.getAbsolutePath()+"\\"+targetDirectory.getName()+"_catalog_list.txt").getAbsolutePath();
    }

    /**
     * 比较两个文件目录差异，输出目标目录新增文件夹信息至指定文件
     * @param baseFilePath      基版目录清单
     * @param targetFilePath    目标目录清单
     * @return  输出差异文件
     * @throws IOException
     */
    public String compareTwoFiles(String baseFilePath, String targetFilePath) throws IOException {
        List<String> baseFile = Files.readAllLines(new File(baseFilePath).toPath());
        List<String> targetFile = Files.readAllLines(new File(targetFilePath).toPath());
        Patch<String> patch = DiffUtils.diff(baseFile,targetFile);
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(new File(baseFilePath).getName(),new File(targetFilePath).getName(),baseFile,patch,0);
        FileWriter fileWriter = new FileWriter(outputDirectory.getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName().replace(".txt","")+"_"+new File(targetFilePath).getName().replace(".txt","")+".txt");
        FileWriter fileWriterDetails = new FileWriter(outputDirectory.getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName().replace(".txt","")+"_"+new File(targetFilePath).getName().replace(".txt","")+"_details.txt");
        fileWriter.write("");
        fileWriterDetails.write("");
        String temp = "";
        fileWriterDetails.write(unifiedDiff.get(0));
        fileWriterDetails.write("\n");
        fileWriterDetails.write(unifiedDiff.get(1));
        fileWriterDetails.write("\n");
        for (int i = 2 ; i < unifiedDiff.size() ; i++) {
            fileWriterDetails.write(unifiedDiff.get(i));
            fileWriterDetails.write("\n");
            if (unifiedDiff.get(i).startsWith("+")){
                temp = unifiedDiff.get(i).replaceFirst("\\+","");
                fileWriter.append(temp);
                fileWriter.append("\n");
            }
        }
        fileWriter.close();
        return new File(outputDirectory.getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName().replace(".txt","")+"_"+new File(targetFilePath).getName().replace(".txt","")+".txt").getAbsolutePath();
    }

    public void startCompare() throws IOException {
        String baseDirectoryList = getDirectoryList(folderInformation.getBasePath());
        for (String targetPath :
                folderInformation.getTargetPathList()) {
            compareTwoFiles(baseDirectoryList,getDirectoryList(targetPath));
        }
    }

    public static void main(String[] args) throws IOException {
        new FolderCompareImplement().compareTwoFiles("E:\\Code\\IDEA\\FolderCompare\\FolderCompare\\record\\安全ctf培训_catalog_list.txt","E:\\Code\\IDEA\\FolderCompare\\FolderCompare\\record\\安全ctf培训_catalog_list2.txt");
    }
}
