package pm;

public class UI {
    private static String displayName = "Pounter";

    public static void printMain() {
        System.out.printf("%s Password Manager [Version 0.1]\n(c) %s. All rights reserved.\n\nYou need help? You must run 'help' command.\n", displayName, displayName);
    }
    
    public static void columnPointer() {
    	System.out.printf("\n(%s)-[~%s]\n$-> ", AppPreferences.userName, Process.currentCommand);
    }
    
    public static void printCommands() {
        StringBuilder printResult = new StringBuilder();
        int counter = 0;
        
        for (var commandItem : Process.commands) {
            counter++;
            printResult.append(commandItem[0]);
            printResult.append("\t- ");
            printResult.append(commandItem[1]);
            if (counter != Process.commands.length) {
            	printResult.append("\n");
            }
        }

        System.out.println(printResult.toString());
    }
}