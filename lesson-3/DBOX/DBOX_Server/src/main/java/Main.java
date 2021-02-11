import com.geekbrains.dbox.WorkFiles.WorkDir;
import com.geekbrains.dbox.server.comands.Comands;

public class Main {
    public static void main(String[] args) {
//        WorkDir dir = new WorkDir();
//        dir.setDirRoot("C:\\Users\\admin\\IdeaProjects\\DBOX");
        Comands cm = new Comands("C:\\Users\\admin\\IdeaProjects\\DBOX");
        String[] str = cm.comand_Ls("");

        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }
        System.out.println("");

//        boolean res = cm.comand_Rename("test", "test1");
//        if (res) System.out.println("Rename ok");
//        else  System.out.println("Rename false");

//        boolean res = cm.comand_Delete("111.txt");
//        if (res) System.out.println("Delete ok");
//        else  System.out.println("Delete false");

//        boolean res = cm.comand_NewDir("test");
//        if (res) System.out.println("Create Dir ok");
//        else  System.out.println("Create Dir false");

    }
}
