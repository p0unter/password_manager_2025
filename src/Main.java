import pm.Process;
import pm.UI;
import security.FileProcess;

public class Main {
    static String command;

    public static void main(String[] args) {
    	FileProcess.mainData_default();

        UI.printMain();
        
        while(true) {
            UI.columnPointer();
            Process.setCommand();
        }
    }
}