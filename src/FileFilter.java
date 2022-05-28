import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {

        if (new File(dir.getAbsoluteFile()+"\\"+name).isDirectory()){
            return true;
        }
        return false;
    }
}
