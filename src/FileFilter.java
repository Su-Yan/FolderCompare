import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {
    private String type;

    FileFilter(String type){
        this.type = type;
    }

    @Override
    public boolean accept(File dir, String name) {
        switch (type){
            case "Directory":
                if (new File(dir.getAbsoluteFile()+"\\"+name).isDirectory()){
                    return true;
                }
                break;
            case "File":
                if (new File(dir.getAbsoluteFile()+"\\"+name).isFile()){
                    return true;
                }
                break;
        }
        return false;
    }
}
