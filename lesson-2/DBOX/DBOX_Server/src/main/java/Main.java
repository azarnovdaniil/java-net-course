import com.geekbrains.dbox.WorkFiles.WorkDir;

public class Main {
    public static void main(String[] args) {
        WorkDir dir = new WorkDir();
        dir.setDirRoot("C:\\Users\\admin\\IdeaProjects\\DBOX");
        String[] str = dir.getTreeFile_All("");
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }
    }
}
