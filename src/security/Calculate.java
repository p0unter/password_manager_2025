package security;

import pm.AppPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Calculate {
    private static List<List<Object>> ramPasswords = new ArrayList<>();

    public static int listLength() {
        return ramPasswords.size();
    }

    public static void addPassword(List<Object> addedPassword) {
        ramPasswords.add(addedPassword);
    }

    public static void deletePassword(int passwordID) {
        ramPasswords.remove(passwordID - 1);
    }

    public static void deleteAllPassword() {
        ramPasswords.clear();
    }

    public static void changePassword(int passwordID, String tag) {
        List<Object> entry = ramPasswords.get(passwordID - 1);
        if (entry.size() >= 3) {
            entry.set(1, tag);
        } else {
            throw new IndexOutOfBoundsException("Password ID is invalid");
        }
    }

    public static List<List<Object>> getPasswords() {
        return ramPasswords;
    }

    public static String listRAMPasswordsString() {
        StringBuilder printResult = new StringBuilder();
        for (List<Object> entry : ramPasswords) {
            if (entry.size() >= 3) {
                printResult.append(String.format("ID:\t%s\nTag:\t%s\nPass:\t%s\n\n", entry.get(0), entry.get(1), entry.get(2)));
            }
        }

        return printResult.toString();
    }

    public static String passGenerate(int passLength) {
        Random random = new Random();

        StringBuilder characters = new StringBuilder();

        if (AppPreferences.isIncludeNumbers()) {
            characters.append("0123456789");
        }
        if (AppPreferences.isIncludeLetters()) {
            characters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }
        if (AppPreferences.isIncludeSymbols()) {
            characters.append("!@#$%^&*()-_=+[]{}|;:',.<>?/");
        }

        if (characters.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be included.");
        }

        if (passLength < 2) {
            System.out.println("Enter minimum 2 value.");
            throw new IllegalArgumentException("Enter minimum 2 value.");
        }

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    public static String passEncryption(String password, int shift) {
        StringBuilder encryptedPassword = new StringBuilder();

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + shift) % 26 + base);
            }
            encryptedPassword.append(c);
        }

        return encryptedPassword.toString();
    }
}