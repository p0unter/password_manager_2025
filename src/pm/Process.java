package pm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import security.Calculate;
import security.FileProcess;

public class Process {
	public static String currentCommand = "home";
	public static Scanner sc = new Scanner(System.in);

	public static String[][] commands = {
			{ "help", "Displays the list of available commands." },
			{ "nick", "Sets your nickname." },
			{ "config", "Configure preferences." },
			{ "list", "List your passwords." },
			{ "search", "Search your passwords." },
			{ "create", "Generate password." },
			{ "delete", "Delete selected password." },
			{ "change", "Changes a selected password." },
			{ "save", "Saves the current state." },
			{ "load", "Loads a saved state." },
			{ "cls", "Clear terminal enters." },
			{ "quit", "Quits the application." }
	};

	public static void setCommand() {
		String input = sc.nextLine();
		for (var commandItem : commands) {
			if (commandItem[0].equals(input)) {
				currentCommand = input;
				checkCommand(input);
				return;
			}
		}
		currentCommand = "err";
		System.out.println("\nPlease enter a correct command.");
	}

	public static void checkCommand(String input) {
		switch (input) {
			case "help":
				printHelp();
				break;
			case "nick":
				setNickname();
				break;
			case "config":
				setConfig();
				break;
			case "list":
				listPasswords();
				break;
			case "create":
				createPassword();
				break;
			case "delete":
				deletePassword();
				break;
			case "change":
				changePassword();
				break;
			case "save":
				saveState();
				break;
			case "load":
				loadState();
				break;
			case "cls":
				clearConsole();
				break;
			case "quit":
				quitApplication();
				break;
			case "search":
				searchPasswords();
				break;
			default:
				System.out.println("\nPlease enter a correct command.");
				break;
		}
	}

	private static void printHelp() {
		UI.printCommands();
	}

	private static void setNickname() {
		System.out.printf("Enter your new nickname: ");
		String newNick = sc.nextLine();
		AppPreferences.userName = newNick;
		System.out.println("Nickname updated to: " + AppPreferences.userName);

		StringBuilder content = new StringBuilder();
		content.append("nickName=").append(AppPreferences.userName).append("\n");
		content.append("defaultPasswordFile=").append(FileProcess.getPasswordFileName()).append("\n");
		content.append("currentCommand=").append(Process.currentCommand).append("\n");
		content.append("includeNumbers=").append(AppPreferences.isIncludeNumbers()).append("\n");
		content.append("includeLetters=").append(AppPreferences.isIncludeLetters()).append("\n");
		content.append("includeSymbols=").append(AppPreferences.isIncludeSymbols()).append("\n");

		FileProcess.data_write(FileProcess.getMainDataFileName(), content.toString());
	}

	private static void setConfig() {
		System.out.println("\n--- Configuration Settings ---");
		System.out.println("Current Configuration:");
		System.out.println("1. Include Numbers: " + AppPreferences.isIncludeNumbers());
		System.out.println("2. Include Letters: " + AppPreferences.isIncludeLetters());
		System.out.println("3. Include Symbols: " + AppPreferences.isIncludeSymbols());

		try {
			System.out.print("\nInclude numbers in passwords? (y/n): ");
			char numChoice = sc.nextLine().toLowerCase().charAt(0);
			AppPreferences.setIncludeNumbers(numChoice == 'y');

			System.out.print("Include letters in passwords? (y/n): ");
			char letterChoice = sc.nextLine().toLowerCase().charAt(0);
			AppPreferences.setIncludeLetters(letterChoice == 'y');

			System.out.print("Include symbols in passwords? (y/n): ");
			char symbolChoice = sc.nextLine().toLowerCase().charAt(0);
			AppPreferences.setIncludeSymbols(symbolChoice == 'y');

			StringBuilder content = new StringBuilder();
			content.append("nickName=").append(AppPreferences.userName).append("\n");
			content.append("defaultPasswordFile=").append(FileProcess.getPasswordFileName()).append("\n");
			content.append("currentCommand=").append(Process.currentCommand).append("\n");
			content.append("includeNumbers=").append(AppPreferences.isIncludeNumbers()).append("\n");
			content.append("includeLetters=").append(AppPreferences.isIncludeLetters()).append("\n");
			content.append("includeSymbols=").append(AppPreferences.isIncludeSymbols()).append("\n");

			FileProcess.data_write(FileProcess.getMainDataFileName(), content.toString());

			System.out.println("\nNew Configuration:");
			System.out.println("1. Include Numbers: " + AppPreferences.isIncludeNumbers());
			System.out.println("2. Include Letters: " + AppPreferences.isIncludeLetters());
			System.out.println("3. Include Symbols: " + AppPreferences.isIncludeSymbols());

			System.out.println("\nConfiguration updated successfully!");

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("\nInvalid input! Using default values.");
		}
	}

	private static void listPasswords() {
		String passwordList = Calculate.listRAMPasswordsString();
		if (passwordList.isEmpty()) {
			System.out.println("No passwords stored.");
		} else {
			System.out.printf(passwordList);
		}
	}

	private static void searchPasswords() {
		System.out.printf("Enter search term: ");
		String searchTerm = sc.nextLine().toLowerCase();

		List<List<Object>> passwords = Calculate.getPasswords();
		List<List<Object>> searchResults = new ArrayList<>();

		for (List<Object> entry : passwords) {
			if (entry.size() >= 3) {
				int id = (int) entry.get(0);
				String tag = ((String) entry.get(1)).toLowerCase();
				String pass = ((String) entry.get(2)).toLowerCase();

				try {
					int searchId = Integer.parseInt(searchTerm);
					if (id == searchId) {
						searchResults.add(entry);
						continue;
					}
				} catch (NumberFormatException e) {
				}

				if (tag.contains(searchTerm) || pass.contains(searchTerm)) {
					searchResults.add(entry);
				}
			}
		}

		if (searchResults.isEmpty()) {
			System.out.println("No passwords found matching the search term.");
		} else {
			System.out.println("\n--- Search Results ---");
			for (List<Object> entry : searchResults) {
				System.out.printf("ID:\t%s\nTag:\t%s\nPass:\t%s\n\n", entry.get(0), entry.get(1), entry.get(2));
			}
		}
	}

	private static void createPassword() {
		boolean validInput = false;
		while (!validInput) {
			try {
				System.out.printf("Tag: ");
				String passTag = sc.nextLine();

				System.out.printf("Password length (2-50): ");

				String passLengthStr = sc.nextLine();
				int passLength = Integer.parseInt(passLengthStr);

				if (passLength < 2 || passLength > 50) {
					System.out.println("Please enter a value between 2 and 50.");
					continue;
				}

				String generatedPassword = Calculate.passGenerate(passLength);

				List<Object> addedPass = new ArrayList<>();
				addedPass.add(Calculate.listLength() + 1);
				addedPass.add(passTag);
				addedPass.add(generatedPassword);

				Calculate.addPassword(addedPass);
				System.out.println("\n-Generated Password-\nGenerated Password: " + generatedPassword + " (Tag: " + passTag + ")");
				validInput = true;
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer value.");
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("Please enter a correct value.");
				sc.nextLine();
			}
		}
	}

	private static void deletePassword() {
		processPasswordAction("delete", Calculate::deletePassword);
	}

	private static void changePassword() {
		boolean validInput = false;
		while (!validInput) {
			try {
				System.out.println("Password ID: ");
				int passID = sc.nextInt();
				sc.nextLine();

				if (passID < 1 || passID > Calculate.listLength()) {
					System.out.println("Invalid password ID.");
					continue;
				}

				System.out.println("New tag: ");
				String newPassword = sc.nextLine();

				System.out.println("You want change password? (y/n): ");

				char askReply_1 = sc.next().charAt(0);
				sc.nextLine();

				if (askReply_1 == 'y') {
					System.out.println("Password length: ");
					int passLength = sc.nextInt();
					sc.nextLine();

					List<Object> entry = Calculate.getPasswords().get(passID - 1);
					entry.set(2, Calculate.passGenerate(passLength));
				}

				System.out.printf("Are you sure for changes? (y/n): ");

				char askReply_2 = sc.next().charAt(0);
				sc.nextLine();

				if (askReply_2 == 'y') {
					Calculate.changePassword(passID, newPassword);
					System.out.println("Password tag " + passID + " changed successfully.");
					validInput = true;
				} else {
					System.out.println("Process cancel.");
					validInput = true;
				}
			} catch (Exception e) {
				System.out.println("Please enter a correct value.");
				sc.nextLine();
			}
		}
	}

	private static void processPasswordAction(String actionName, PasswordAction action) {
		boolean validInput = false;
		while (!validInput) {
			try {
				System.out.println("Password ID: ");
				int passID = sc.nextInt();
				sc.nextLine();

				if (passID < 1 || passID > Calculate.listLength()) {
					System.out.println("Invalid password ID.");
					continue;
				}

				System.out.printf("Are you sure? (y/n): ");
				char askReply = sc.next().charAt(0);
				sc.nextLine();

				if (askReply == 'y') {
					action.execute(passID);
					System.out.println("Password " + actionName + "d successfully.");
					validInput = true;
				} else {
					System.out.println("Process cancel.");
					validInput = true;
				}
			} catch (Exception e) {
				System.out.println("Please enter a correct value.");
				sc.nextLine();
			}
		}
	}

	private static void saveState() {
		System.out.println("Current state saved.");
	}

	private static void loadState() {
		System.out.println("Previous state loaded.");
	}

	private static void quitApplication() {
		System.out.println("Exiting application...");
		System.exit(0);
	}

	private static void clearConsole() {
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error clearing console: " + e.getMessage());
		}
	}

	@FunctionalInterface
	private interface PasswordAction {
		void execute(int passwordID);
	}
}