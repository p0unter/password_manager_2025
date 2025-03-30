package pm;

public class AppPreferences {
    private static boolean includeNumbers = true;
    private static boolean includeLetters = true;
    private static boolean includeSymbols = true;

    public static String mainDataFileName = "config";
    public static String defaultPassFile = "pass";
    public static String userName = "user";

    public AppPreferences(boolean includeNumbers, boolean includeLetters, boolean includeSymbols) {
        AppPreferences.includeNumbers = includeNumbers;
        AppPreferences.includeLetters = includeLetters;
        AppPreferences.includeSymbols = includeSymbols;
    }

    public static boolean isIncludeNumbers() {
        return includeNumbers;
    }

    public static void setIncludeNumbers(boolean includeNumbers) {
        AppPreferences.includeNumbers = includeNumbers;
    }

    public static boolean isIncludeLetters() {
        return includeLetters;
    }

    public static void setIncludeLetters(boolean includeLetters) {
        AppPreferences.includeLetters = includeLetters;
    }

    public static boolean isIncludeSymbols() {
        return includeSymbols;
    }

    public static void setIncludeSymbols(boolean includeSymbols) {
        AppPreferences.includeSymbols = includeSymbols;
    }
}