import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FolderCompareImplement {

    private FolderInformation folderInformation;

    FolderCompareImplement(FolderInformation folderInformation){
        //初始化文件夹信息并指定输出目录
        this.folderInformation = folderInformation;
    }

    /**
     * 获取指定目录下的所有文件夹名称并输出
     * @param path  目标路径，传目标文件绝对路径！
     * @return  返回目录txt绝对路径！
     * @throws IOException
     */
    public String getDirectoryList(String path,String filterType) throws IOException {
        //文件差异输出后缀 文件夹对应_directory_list.txt 文件对应_file_details_list.txt
        String outputFileSuffix = "_directory_list.txt";
        if(filterType.equals("File")){
            outputFileSuffix = "_list.txt";
        }

        //获取指定path路径下文件夹/文件明细
        File targetDirectory = new File(path);
        File []directoryList = null;
        String outputPath = this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\"+targetDirectory.getName()+outputFileSuffix;
        FileWriter fileWriter = null;
        //将明细写入指定文件
        switch (filterType){
            case "Directory":
                directoryList = targetDirectory.listFiles(new FileFilter(filterType));
                fileWriter = new FileWriter(outputPath);
                break;
            case "File":
                outputPath = this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\"+this.folderInformation.getFileDetailsPath()+outputFileSuffix;
                fileWriter = new FileWriter(outputPath,true);
                directoryList = targetDirectory.listFiles();
                break;
        }
        if (filterType.equals("Directory")){
            fileWriter.write("");
        }
        //初始化根目录
        LinkedList<File> list =new LinkedList();
        for (File f :
                directoryList) {
            list.add(f);
        }
        File temp;
        //遍历所有目录，如果指定输出文件夹差异，则过滤掉文件，否则不过滤文件
        while (!list.isEmpty()){
            temp = list.removeFirst();
            switch (filterType){
                case "Directory":
                    directoryList = temp.listFiles(new FileFilter(filterType));
                    break;
                case "File":
                    if (!temp.isFile()){
                        directoryList = temp.listFiles();
                    }else {
                        fileWriter.append(temp.getAbsolutePath());
                        fileWriter.append("\n");
                    }
                    break;
            }
            //如果是文件夹，则加入list，如果是文件，则文件绝对路径写入到txt中
            if (directoryList!=null){
                for (File f :
                        directoryList) {
                    list.add(f);
                }
            }
            switch (filterType){
                case "Directory":
                    fileWriter.append(temp.getAbsolutePath().replace(path+"\\",""));
                    fileWriter.append("\n");
                    break;
                case "File":
                    break;
            }
        }
        fileWriter.close();
        return outputPath;
    }

    /**
     * 获取指定目录下的所有文件并输出
     * @param path  指定目录
     * @return
     */
    public String getFileList(String path) throws IOException {
        File[] files = new File(path).listFiles();
        LinkedList<File> fileLinkedList = new LinkedList<>();
        FileWriter fileWriter = new FileWriter(this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\"+new File(path).getName()+"_file_details_list.txt");
        fileWriter.write("");
        for (File f :
                files) {
            fileLinkedList.add(f);
        }
        File tempFile;
        while(!fileLinkedList.isEmpty()){
            tempFile = fileLinkedList.removeFirst();
            files = tempFile.listFiles();
            for (File f :
                    files) {
                if (f.isDirectory()){
                    fileLinkedList.add(f);
                }else {
                    fileWriter.append(f.getAbsolutePath());
                    fileWriter.append("\n");
                }
            }
        }
        fileWriter.close();
        return new File(this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\"+new File(path).getName()+"_file_details_list.txt").getAbsolutePath();
    }

    /**
     * 比较两个文件目录txt差异，输出目标目录新增文件夹信息至指定文件，参数传txt绝对路径！
     * @param baseFilePath      基版目录清单，传txt绝对路径！
     * @param targetFilePath    目标目录清单，传txt绝对路径！
     * @return  输出差异文件
     * @throws IOException
     */
    public String compareTwoFiles(String baseFilePath, String targetFilePath) throws IOException {
        //读取基版路径和目标路径文件目录（txt文件）
        List<String> baseList = Files.readAllLines(new File(baseFilePath).toPath());
        List<String> targeList = Files.readAllLines(new File(targetFilePath).toPath());
        //比较不同
        Patch<String> patch = DiffUtils.diff(baseList,targeList);
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(new File(baseFilePath).getName(),new File(targetFilePath).getName(),baseList,patch,0);
        //fileWriter仅输出目标文件比基版文件多哪些东西
        String outputPath = this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName().replace(".txt","")+"_"+new File(targetFilePath).getName().replace(".txt","")+".txt";
        FileWriter fileWriter = new FileWriter(outputPath);
        fileWriter.write("");

        //fileWriterDetails两文件详细差异输出
        String outputDetailsPath = this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName().replace(".txt","")+"_"+new File(targetFilePath).getName().replace(".txt","")+"_details.txt";
        FileWriter fileWriterDetails = new FileWriter(outputDetailsPath);
        //由于下面for循环从第三行开始，所以手动补齐前两行
        fileWriterDetails.write("");
        if (unifiedDiff.size()!=0){
            fileWriterDetails.append(unifiedDiff.get(0));
            fileWriterDetails.append("\n");
            fileWriterDetails.append(unifiedDiff.get(1));
            fileWriterDetails.append("\n");;
        }

        //将差异输出到文件
        String temp = "";
        for (int i = 2 ; i < unifiedDiff.size() ; i++) {
            fileWriterDetails.append(unifiedDiff.get(i));
            fileWriterDetails.append("\n");
            if (unifiedDiff.get(i).startsWith("+")){
                temp = unifiedDiff.get(i).replaceFirst("\\+","");
                fileWriter.append(temp);
                fileWriter.append("\n");
            }
        }
        fileWriter.close();
        fileWriterDetails.close();
        return outputPath;
    }

    /**
     * 比较入口API
     * @throws IOException
     */
    public void startCompare() throws IOException {
        String baseDirectoryList = getDirectoryList(folderInformation.getBasePath(),"Directory");
        String diffPath = "";
        List<String> targetDirectoryList = folderInformation.getTargetPathList();
        for (String targetPath :
                targetDirectoryList) {
            diffPath = compareTwoFiles(baseDirectoryList,getDirectoryList(targetPath,"Directory"));
            System.out.println(folderInformation.getBasePath()+"与"+targetPath+"已完成对比！差异文件夹详情请见："+diffPath);
        }
    }

    /**
     * 获取基版路径与目标路径差异文件详细信息
     * @throws IOException
     */
    public List<String> getDiffFilesDetails() throws IOException {
        String baseFilePath = this.folderInformation.getBasePath();                 //基版路径
        List<String>targetFilePath = this.folderInformation.getTargetPathList();    //目标路径
        List<String> detailsListPath = new LinkedList<>();
        String outputPath = "";

        //遍历所有基版路径与目标路径比较形成的差异文件
        for (String targetTempPath :
                targetFilePath) {
            List<String> copyOriginPathList = Files.readAllLines(new File(this.folderInformation.getOutputDirectory().getAbsolutePath()+"\\Diff_"+new File(baseFilePath).getName()+"_directory_list_"+new File(targetTempPath).getName()+"_directory_list.txt").toPath());

            this.folderInformation.setFileDetailsPath("Details_file_"+new File(baseFilePath).getName()+"_"+new File(targetTempPath).getName()+"_"+System.currentTimeMillis());
            for (String path :
                    copyOriginPathList) {
                outputPath = getDirectoryList(targetTempPath+"\\"+path,"File");
            }
            System.out.println(baseFilePath+"与"+targetTempPath+"差异文件详情请见："+outputPath);
            detailsListPath.add(outputPath);
        }
        this.folderInformation.setFileDetailsPathList(detailsListPath);
        return detailsListPath;
    }

    public String copyFilesToTargetPath() throws IOException {
        Scanner sc =new Scanner(System.in);
        System.out.println("请输入目标文件夹差异文件输出路径（输入0为默认目录）：");
        String tempString = sc.next();
        File tempFile = null;
        while (!tempString.equals("0")){
            tempFile = new File(tempString);
            if (!tempFile.isDirectory()){
                System.out.println("输入路径有误！请重新输入！！！");
                tempString = sc.next();
                continue;
            }
        }
        if (tempString.equals("0")){
            tempString = System.getProperty("user.dir")+"\\diff_file_record";
        }
        this.folderInformation.setDiffFileOutputPath(tempString);

        List<String> fileDetailsPathList = this.folderInformation.getFileDetailsPathList();
        List<String> details;
        String tempPath = "";
        for (String filePath :
                fileDetailsPathList) {
            tempPath = filePath.split("_")[3];
            details = Files.readAllLines(new File(filePath).toPath());
            for (String realPath :
                    details) {
                tempFile = new File(realPath);
                int endIndex = tempFile.getParentFile().getAbsolutePath().indexOf(tempPath) + tempPath.length();
                tempString = this.folderInformation.getDiffFileOutputPath() + tempFile.getParentFile().getAbsolutePath().substring(endIndex);
                tempFile = new File(tempString);
                if (!tempFile.exists()){
                    tempFile.mkdirs();
                }
                System.out.println(tempFile.getAbsolutePath()+"\\"+new File(realPath).getName());
                OutputStream outputStream = new FileOutputStream(tempFile.getAbsolutePath()+"\\"+new File(realPath).getName());
                Files.copy(new File(realPath).toPath(),outputStream);
            }
        }
        System.out.println("感谢使用！再见！");
        return this.folderInformation.getDiffFileOutputPath();
    }
}
