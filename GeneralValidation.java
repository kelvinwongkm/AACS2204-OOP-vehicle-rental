package vehiclerental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Wong Kah Ming
 */
public class GeneralValidation {

    public static boolean validateOptionRange(int min, int max, int option) {

        if (option >= min && option <= max) {
            return true;
        }

        System.out.printf("%30s Invalid option, please enter within the range.\n\n","");
        return false;
    }

    public static boolean validateConfirm(String message) {
        char character;

        do {
	    String temp = validateStringInput(message);
            character = temp.toUpperCase().charAt(0);

            switch (character) {
                case 'Y':
                    return true;
                case 'N':
                    break;
                default:
                    System.out.printf("\n%30s Invalid input, please enter again\n\n","");
            }
        } while (Character.compare(character, 'Y') != 0 && Character.compare(character, 'N') != 0);

        return false;
    }

    public static String validateStringInput(String requestMessage) {

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean validInput = true;
        do {
            validInput = true;

            System.out.print(String.format("%30s %s", "", requestMessage));
            input = scanner.nextLine();

            if (input.length() == 0) {
                validInput = noInput();

            }

        } while (!validInput);

        return input;
    }

    public static int validateIntegerInput(String requestMessage) {

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean validInput = true;
        do {
            validInput = true;

            System.out.print(String.format("%-30s %s", "", requestMessage));
            input = scanner.nextLine();

            if (input.length() == 0) {
                validInput = noInput();

            } else {

                for (int i = 0; i < input.length(); i++) {
                    if (!Character.isDigit(input.charAt(i))) {
                        System.out.printf("\n%30s Invalid input entered. Please ensure you only enter integer value\n\n","");
                        validInput = false;
                        break;
                    }
                }
            }

        } while (!validInput);

        return Integer.parseInt(input);
    }

    public static double validateDoubleInput(String requestMessage) {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean validInput = true;
        do {
            validInput = true;

            System.out.print(String.format("%30s %s", "", requestMessage));
            input = scanner.nextLine();

            if (input.length() == 0) {
                validInput = noInput();
            } else {
                for (int i = 0; i < input.length(); i++) {

                    if (input.charAt(i) == '.') {
                        continue;
                    }

                    if (!Character.isDigit(input.charAt(i))) {
                        System.out.printf("%30s Invalid input entered. Please ensure you only enter double value\n\n","");
                        validInput = false;
                        break;
                    }
                }
            }
        } while (!validInput);

        return Double.parseDouble(input);
    }

    public static List<String> validateMultiInteger(List<String> stringList) {
        Scanner scanner = new Scanner(System.in);
        String[] selection;
        Boolean validInput = true;

        do {
            do {
                validInput = true;
                System.out.printf(String.format("\n%30s Your selection |> ", ""));
                String userSelection = scanner.nextLine().trim().replaceAll("\\s", "");
                selection = userSelection.trim().split(",");

                // check if all input is digit
                for (String string : selection) {
                    for (int i = 0; i < string.length(); i++) {
                        if (string.charAt(i) == ',') {
                            continue;
                        } else if (!Character.isDigit(string.charAt(i))) {
                            validInput = noInput();
                            break;
                        }
                    }
                }
            } while (!validInput);

            //check if the input is within the range
            for (String option : selection) {
                int userOption = Integer.parseInt(option);
                if (!validateOptionRange(1, stringList.size(), userOption)) {
                    validInput = false;
                    break;
                }
            }
        } while (!validInput);

        List<String> returnValue = new ArrayList<>(Arrays.asList(selection));
        // clear repeated selection
        Set<String> set = new HashSet<>(returnValue);
        returnValue.clear();
        returnValue.addAll(set);

        return returnValue;
    }

    public static boolean noInput() {
        System.out.printf("\n%30s Invalid is entered, please enter again\n\n", "");
        return false;
    }

}
