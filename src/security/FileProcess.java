package security;

import pm.AppPreferences;
import pm.Process;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class FileProcess {
    private static String mainDataFileName = "config";
    private static String passwordFileName = "passwords";

    public static void mainData_default() {
        File file = new File(mainDataFileName);

        if (!file.exists()) {
            StringBuilder content = new StringBuilder();

            content.append("nickName=").append(AppPreferences.userName).append("\n");
            content.append("defaultPasswordFile=").append(passwordFileName).append("\n");
            content.append("currentCommand=").append("home").append("\n");
            content.append("includeNumbers=").append(AppPreferences.isIncludeNumbers()).append("\n");
            content.append("includeLetters=").append(AppPreferences.isIncludeLetters()).append("\n");
            content.append("includeSymbols=").append(AppPreferences.isIncludeSymbols()).append("\n");

            data_write(mainDataFileName, content.toString());
        } else {
            String content = data_read(mainDataFileName);
            String[] lines = content.split("\n");

            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "nickName":
                            AppPreferences.userName = value;
                            break;
                        case "defaultPasswordFile":
                            passwordFileName = value;
                            break;
                        case "currentCommand":
                            Process.currentCommand = value;
                            break;
                        case "includeNumbers":
                            AppPreferences.setIncludeNumbers(Boolean.parseBoolean(value));
                            break;
                        case "includeLetters":
                            AppPreferences.setIncludeLetters(Boolean.parseBoolean(value));
                            break;
                        case "includeSymbols":
                            AppPreferences.setIncludeSymbols(Boolean.parseBoolean(value));
                            break;
                    }
                }
            }
        }
    }

    public static void data_write(String fileName, String content) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, false))) {
                bWriter.write(content);
            }
        } catch (IOException e) {
            System.err.println("File write error: " + e.getMessage());
        }
    }

    public static String data_read(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }
        return content.toString().trim();
    }

    public static void passwordData_save(String fileName, List<List<Object>> passwords) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, false))) {
                for (List<Object> password : passwords) {
                    int id = (Integer) password.get(0);
                    String tag = (String) password.get(1);
                    String pass = (String) password.get(2);

                    String encryptedPass = Calculate.passEncryption(pass, 5);

                    bWriter.write(id + "," + tag + "," + encryptedPass);
                    bWriter.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Password save error: " + e.getMessage());
        }
    }

    public static List<List<Object>> passwordData_load(String fileName) {
        List<List<Object>> passwords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    List<Object> password = new ArrayList<>();
                    password.add(Integer.parseInt(parts[0]));
                    password.add(parts[1]);

                    String encryptedPass = parts[2];
                    String decryptedPass = Calculate.passEncryption(encryptedPass, -5);

                    password.add(decryptedPass);
                    passwords.add(password);
                }
            }
        } catch (IOException e) {
            System.err.println("Password load error: " + e.getMessage());
        }
        return passwords;
    }

    public static String getMainDataFileName() {
        return mainDataFileName;
    }

    public static String getPasswordFileName() {
        return passwordFileName;
    }
}