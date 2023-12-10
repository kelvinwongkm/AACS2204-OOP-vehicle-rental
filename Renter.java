package vehiclerental;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Character.isLetter;
import static java.lang.Character.isSpace;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateOptionRange;
import static vehiclerental.GeneralValidation.validateStringInput;
import static vehiclerental.VehicleRental.timeCountDown;

/**
 *
 * @author Vickham Foo
 */
public class Renter extends User implements AvailableLocation {

    private String renterID;
    private String username;
    private String password;
    private int choiceSecurityQues;
    private String securityAnswer;
    private static int numberOfUsers = 0;
    
 public Renter() {
        this("", "", "", 0, "", "", "", "", "", "", "");
    }
 
    public Renter(String renterID, String username, String password, int choiceSecurityQues, String securityAnswer, String userID, String name, String address, String icNum, String email, String phoneNumber) {
        super(userID, name, address, icNum, email, phoneNumber);
        this.renterID = renterID;
        this.username = username;
        this.password = password;
        this.choiceSecurityQues = choiceSecurityQues;
        this.securityAnswer = securityAnswer;
    }

    public String getRenterID() {
        return renterID;
    }

    @Override
    public void loginMenu() {
        int option = 0;
        do {

            System.out.printf("\n%38s %15s\n", " ", "Renter");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "1. Login", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "2. Register", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "0. Return", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 3, option));

            switch (option) {
                case 1:

                    try {
                        renterLogin();//renter login method
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Renter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case 2:
                    renterRegister();// renter register method
                    break;
                case 0:
                    break;

            }
        } while (option != 0);
    }

    public void renterLogin() throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        int attempt = 3, optionFP = 0;
        do {
            System.out.printf("\n%38s %15s\n", " ", "Login");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %10s", "", "Username : ");
            String tempUsername = scanner.nextLine();
            System.out.printf("%30s %10s", "", "Password : ");
            String tempPw = scanner.nextLine();

            fileReader(tempUsername);
            if (username.equals(tempUsername) && password.equals(tempPw)) {
                welcomeMessage(getName());
                mainMenu();
                break;
            } else if (username.equals(tempUsername) && !password.equals(tempPw)) {

                System.out.printf("\n%30s %10s\n", "", "Incorrect user password.");
                System.out.printf("%30s %10s\n\n", "", "Try again or click Forgot password to reset it. ");
                do {
                    System.out.printf("%30s %10s\n", "", "1. Try Again.");
                    System.out.printf("%30s %10s\n", "", "2. Forgot Password.");
                    System.out.printf("%30s %-10s\n", "", "0. Back.");
                    System.out.println("");
                    int  input = validateIntegerInput(String.format("|>"));
                    optionFP=input;
                } while (!validateOptionRange(0, 2, optionFP));
                if (optionFP == 1) {
                    System.out.printf("\n%30s %10s\n\n", "", attempt + " attempt left.");

                    if (attempt == 1) {
                        System.out.printf("%30s %10s\n", "", "After 1 more failed attempt.");
                        System.out.printf("%30s %10s\n\n", "", "You will have to wait 10 seconds to try again.");
                    } else if (attempt == 0) {
                        System.out.printf("%30s %10s\n", "", "Sorry.You have reached the maximum attempt.");
                        System.out.printf("%30s %10s\n", "", "Please wait 10 seconds to try again.");
                        timeCountDown();//countdown time 

                    }
                    attempt--;
                }
                if (optionFP == 2) {
                    forgetPassword();
                } else if(optionFP==0){
                break;
                }

            } else {
                attempt = userNotFound();
            }

        } while (attempt >= 0);

    }

    public int userNotFound() {
        int optionIDN = 0, attempt = 0, option = 0;
        System.out.printf("\n%30s %10s\n", "", "Username not found.");
        do {
            System.out.printf("%30s %10s\n", "", "1.Try Again.");
            System.out.printf("%30s %10s\n", "", "2.Register.");
            System.out.printf("%30s %-5s\n", "", "0.Back.");
            System.out.println("\n");
            option = validateIntegerInput(String.format("|>"));
        } while (!validateOptionRange(0, 2, option));

        switch (optionIDN) {
            case 1:
                attempt = 1;
                break;
            case 2:
                renterRegister();
                break;
            case 0:
                attempt = -1;
                break;
        }

        return attempt;
    }

    public void forgetPassword() throws InterruptedException {//forget password + reset password
        String newPassword = "";
        int choiceSQ = 0, counterFP = 0, optionTry = 0, optionWA, attemptSQ = 3, option = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.printf("\n%-43s %-15s\n", " ", "Forgot Password");
            System.out.printf("%30s %35s\n", "", "=========================================");
            System.out.printf("%30s %10s\n", "", "To reset your password,");
            System.out.printf("%30s %10s\n\n", "", "please provide the correct answer for the following Security Question.");
            System.out.printf("%30s %10s\n", "", "Security Question");
            System.out.printf("%30s %10s\n\n", "", "===================");
            System.out.printf("%30s %10s\n", "", "1. Your mother's name");
            System.out.printf("%30s %10s\n", "", "2. Your favourite movie");
            System.out.printf("%30s %10s\n", "", "3. Your pet's name");
            System.out.printf("%30s %5s\n", "", "0. Back");

            do {
                System.out.println("");
                option = validateIntegerInput(String.format("Your choice : "));
                choiceSQ=option;
            } while (!validateOptionRange(0, 3, option));

            if (choiceSQ == choiceSecurityQues) {
                do{
                    System.out.printf("%30s %10s", "", "Your answer : ");
                    String securityAns = scanner.nextLine();
                    if (securityAns.equals(securityAnswer)) {
                        do {
                            System.out.printf("%30s %10s", "", "Your new password : ");
                            newPassword = scanner.nextLine();
                        } while (validationPassword(newPassword) == false);
                        int confirm = VehicleRental.askConfirmation("Change Password");
                        if (confirm == 1) {
                            password = newPassword;
                            modifyFile();
                            counterFP = 1;
                        } else {
                            break;
                        }

                    } else {
                        System.out.printf("\n%30s %10s\n", "", "Answer Incorrect! ");
                        do {
                            System.out.printf("%30s %10s\n", "", "1.Try Again.");
                            System.out.printf("%30s %5s\n", "", "0.Back.");
                            System.out.println("");

                            optionWA = validateIntegerInput(String.format("|>"));
                        } while (!validateOptionRange(0, 1, optionWA));

                        if (optionWA == 1) {
                            System.out.printf("\n%30s %10s\n\n", "", attemptSQ + " attempt left.");
                            attemptSQ--;
                        } else if (optionWA == 0) {
                            counterFP = 1;
                            break;
                        }
                        if (attemptSQ < 0) {
                            timeCountDown();
                            break;
                        }
                    }
                } while (counterFP == 0);
            } else if (choiceSQ == 0) {
                break;
            } else {
                System.out.printf("\n%30s %10s\n", "", "This is not your security question.");
                do {
                    System.out.printf("%30s %10s\n", "", "1. Try Again.");
                    System.out.printf("%30s %5s\n", "", "0. Back.");
                    System.out.println("");
                    optionTry = validateIntegerInput(String.format("|>"));
                } while (!validateOptionRange(0, 1, optionTry));

                if (optionTry == 1) {
                    counterFP = 0;
                } else if (optionTry == 0) {
                    counterFP = 1;
                }

            }
        } while (counterFP == 0);

    }

    public void renterRegister() {//register account

        Scanner scanner = new Scanner(System.in);
        String regName = "";
        String regIcNum = "";
        String regEmail = "";
        String regPhoneNumber = "";
        String regUsername = "";
        String regPassword = "";
        int state;
        String city = "";
        System.out.printf("\n%41s %15s\n", " ", "Registration");
        System.out.printf("%30s %35s\n\n", "", "=========================================");
        System.out.printf("%30s %10s\n\n", "", "Personal Information");
        do {
            System.out.printf("\n%27s %10s", "", "Name : ");
            regName = scanner.nextLine();
        } while (validationOnlyAlphabet(regName) == false);
        setName(regName);
        System.out.printf("\n%30s %10s\n\n", "", "Address : ");
        printState(STATE);
        do {
            System.out.printf("\n%30s %10s", "", "Your Selection |> ");
            String tempState = scanner.nextLine();
            state = validationOptionNum(tempState, 9);
        } while (validationState(state) == false);
	
        do {
            System.out.printf("\n%27s %10s", "", "City : ");
            city = scanner.nextLine();
        } while (validationOnlyAlphabet(city) == false);
        setAddress(STATE[state - 1] + "," + city);

        do {
            System.out.printf("\n%30s %10s", "", "IC Number : ");
            regIcNum = scanner.nextLine();
        } while (validationICNum(regIcNum) == false);
        setIcNum(regIcNum);

        do {

            System.out.printf("\n%28s %10s", "", "Email : ");
            regEmail = scanner.nextLine();
        } while (validationEmail(regEmail) == false);
        setEmail(regEmail);

        do {
            System.out.printf("\n%30s %10s", "", "Phone Number : ");
            regPhoneNumber = scanner.nextLine();
        } while (validationPhoneNum(regPhoneNumber) == false);
        setPhoneNumber(regPhoneNumber);


        do {
           System.out.println("");
           regUsername = validateStringInput(String.format("Username : "));
        } while (validationUserIDExist(regUsername) == false);
        username = regUsername;

        do {
            System.out.printf("\n%30s %10s", "", "Password : ");
            regPassword = scanner.nextLine();
        } while (validationPassword(regPassword) == false);
        password = regPassword;

        displaySecurityQuestions();
        do {
            System.out.printf("\n%30s %10s", "", "Your choice : ");
            String tempChoiceSQ = scanner.nextLine();
            choiceSecurityQues = validationOptionNum(tempChoiceSQ, 3);
        } while (choiceSecurityQues == 0);
        System.out.printf("\n%30s %10s", "", "Please do no forget the answer.");
        System.out.printf("\n%30s %10s\n", "", "It would be use to reset your password in the future.");
        do {
            System.out.printf("\n%30s %10s", "", "Your answer : ");
            securityAnswer = scanner.nextLine();
        } while (validationOnlyAlphabet(securityAnswer) == false);

        int confirm = VehicleRental.askConfirmation("Register");
        if (confirm == 1) {
            iDAssign();
            fileWriter();
            System.out.printf("\n%30s %10s\n", "", "Your account have been successfully created!");
            System.out.printf("%30s %10s", "", "Press any key to continue : ");
            scanner.nextLine();
        } else {
            System.out.printf("\n%30s %10s\n", "", "Your account have not been successfully created!");
            System.out.printf("%30s %10s", "", "Press any key to continue : ");
            scanner.nextLine();
        }
    }

    public void displaySecurityQuestions() {
        System.out.printf("\n\n%30s %10s\n", "", "For security purpose,");
        System.out.printf("%30s %10s\n\n", "", "Please select one of the security question below.");
        System.out.printf("%30s %10s\n", "", "1. Your mother's name");
        System.out.printf("%30s %10s\n", "", "2. Your favourite movie");
        System.out.printf("%30s %10s\n", "", "3. Your pet's name");

    }

    public void printState(String[] list) {
        System.out.printf("%6s %35s\n", "", "+--------------------------------------------------------------------------------------------------------+");
        System.out.printf("%6s | %-46s %-6s %-48s |\n", "", "", "STATE", "");
        System.out.printf("%6s %35s\n", "", "+--------------------------------------------------------------------------------------------------------+");
        for (int i = 0, x = 1; i < list.length; i++, x++) {
            System.out.printf("%-6s | %02d) %-15s", "", x, list[i]);
            if (x % 4 == 0 || x == list.length) {
                System.out.printf("|\n");
                System.out.printf("%6s %35s\n", "", "+--------------------------------------------------------------------------------------------------------+");

            } else {
                System.out.printf("");
            }
        }
    }

    @Override
    public void mainMenu() {
        Reservation r = new Reservation(this);
        int option;
        do {
            System.out.printf("\n%40s %15s\n", " ", "Main Menu");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-10s |\n", "", "", "1. Rent Vehicle", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-4s |\n", "", "", "2. Reservation Status", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-12s |\n", "", "", "3. My Account", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-13s |\n", "", "", "0. Log Out", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 3, option));

            switch (option) {
                case 1:
                    try {
                        r.makeReservation();
                    } catch (IOException ex) {
                        Logger.getLogger(Renter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //reservation + payment
                    break;
                case 2:
                    try {
                        Reservation.onGoingReservation(renterID);
                    } catch (IOException ex) {
                        Logger.getLogger(Renter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //reservation status > can view + cancel
                    break;
                case 3:
                    myAccount(); // view account
                    modifyAccount();//modify account
                    break;
                case 0:
                    int confirm = VehicleRental.askConfirmation("Log Out");
                    if (confirm == 1) {
                        VehicleRental.thankyouMessage("LogOut");
                        return;
                    } else {
                        option = 1;
                        break;
                    }
            }
        } while (option != 0);

    }

    @Override
    public void myAccount() {// view account
        System.out.printf("\n%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%26s|%-19s %-15s %-13s|\n", " ", "", "Account", "");
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Renter ID", renterID);
        super.myAccount();
    }

    public void iDAssign() {
        fileCounter();
        numberOfUsers++;
        setUserID(String.format("UID%05d", numberOfUsers));
        renterID = String.format("R%05d", numberOfUsers);

    }

    public void fileCounter() {
        try {
            try (FileReader fr = new FileReader("renterAccount.txt")) {
                Scanner reader = new Scanner(fr);
                while ((reader.nextLine()) != null) {
                    numberOfUsers++;
                }
                fr.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

    }

    public void fileWriter() {
        try {
            try (FileWriter file = new FileWriter("renterAccount.txt", true)) {
                BufferedWriter b = new BufferedWriter(file);
                b.write(this.toString());
                b.write("\n");
                b.close();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public void fileReader(String tempUsername) {

        try {
            try (FileReader fr = new FileReader("renterAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(Pattern.quote("|"));
                    if (lineArr[7].equals(tempUsername)) {
                        setUserID(lineArr[0]);
                        setName(lineArr[1]);
                        setAddress(lineArr[2]);
                        setIcNum(lineArr[3]);
                        setEmail(lineArr[4]);
                        setPhoneNumber(lineArr[5]);
                        renterID = lineArr[6];
                        username = lineArr[7];
                        password = lineArr[8];
                        choiceSecurityQues = Integer.parseInt(lineArr[9]);
                        securityAnswer = lineArr[10];

                    }
                }
                fr.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

    }

    public static Renter getRenterData(String renterID) {
        Renter temp = new Renter();
        try {
            try (FileReader fr = new FileReader("renterAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(Pattern.quote("|"));
                    if (lineArr[6].equals(renterID)) {
                        temp.setUserID(lineArr[0]);
                        temp.setName(lineArr[1]);
                        temp.setAddress(lineArr[2]);
                        temp.setIcNum(lineArr[3]);
                        temp.setEmail(lineArr[4]);
                        temp.setPhoneNumber(lineArr[5]);
                        temp.renterID = lineArr[6];
                        temp.username = lineArr[7];
                        temp.password = lineArr[8];
                        temp.choiceSecurityQues = Integer.parseInt(lineArr[9]);
                        temp.securityAnswer = lineArr[10];
                        break;
                    }
                }
                fr.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return temp;
    }

    public static int validationOptionNum(String input, int index) {
        int option = 0;
        if (!input.matches("[1-" + index + "]+")) {
            System.out.printf("\n%30s %10s\n", "", "Invalid Input");

            option = 0;
        } else {
            option = Integer.parseInt(input);
        }
        return option;
    }

    public static boolean validationOnlyAlphabet(String input) {
        if (input.length() < 3) {
            System.out.printf("\n%30s %10s\n", "", "Invalid Format.You must at least contain 3 letter.");
            return false;
        } else {
            for (int i = 0; i < input.length(); i++) {
                if (!isLetter(input.charAt(i)) && !isSpace(input.charAt(i))) {
                    System.out.printf("\n%30s %10s\n", "", "Invalid Format.Only alphabets allowed.");
                    return false;
                }
            }
        }
        return true;

    }

    public static boolean validationICNum(String input) {
        if (input.length() != 12) {
            System.out.printf("\n%30s %10s\n", "", "Invalid Format.IC must have 12 digits");
            return false;
        } else {
            for (int i = 0; i < input.length(); i++) {
                if (isLetter(input.charAt(i)) || isSpace(input.charAt(i))) {
                    System.out.printf("\n%30s %10s\n", "", "Invalid Format.Only digits allowed.");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validationEmail(String input) {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches() == false) {
            System.out.printf("\n%30s %10s\n", "", "Invalid Input.");
        }
        return matcher.matches();

    }

    public static boolean validationPhoneNum(String input) {

        String regex = "^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches() == false) {
            System.out.printf("\n%30s %10s\n", "", "Invalid Input.");
        }
        return matcher.matches();

    }

    public static boolean validationState(int option) {
        if (option <= 0 || option > 16) {
            System.out.printf("\n%30s %10s\n", "", "Out of range.");
            return false;
        } else {
            return true;
        }

    }

    public static boolean validationUserIDExist(String userIDReg) {
        try {
            try (FileReader fr = new FileReader("renterAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(Pattern.quote("|"));
                    if (lineArr[0].equals(userIDReg)) {
                        System.out.printf("\n%30s %10s\n", "", "This Username is already existed.");
                        return false;
                    }
                }
                fr.close();
            } catch (Exception e) {

            }
        } catch (Exception e) {
        }

        return true;

    }

    public static boolean validationPassword(String input) {

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.]).{8,20}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches() == false) {
            System.out.printf("\n%30s %10s\n\n", "", "Invalid format.");
            System.out.printf("%30s %10s\n", "", "Must have at least one lowercase character");
            System.out.printf("%30s %10s\n", "", "Must have at least one uppercase character");
            System.out.printf("%30s %10s\n", "", "Must have at least one special character among @.#$%");
            System.out.printf("%30s %10s\n", "", "Password length should be between 8 and 20");
            System.out.printf("%30s %10s\n", "", "Must have at least one numeric character");
        }
        return matcher.matches();

    }

    public void modifyAccount() {
        int option = 0;
        do {
            System.out.printf("\n\n%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-9s |\n", "", "", "1.Modify Account", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-13s |\n", "", "", "0.Return", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 1, option));

            switch (option) {
                case 1:
                    modifyAccountMenu();
                    break;
                case 0:
                    return;

            }
        } while (option != 0);

    }

    public void modifyAccountMenu() {
        int option = 0;

        do {
            System.out.printf("\n\n%43s %15s\n", " ", "Modify Account");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "1.Username", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "2.Password", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "3.Name", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "4.Address", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "5.Email", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %10s |\n", "", "", "6.Phone Number", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-11s %-12s %12s |\n", "", "", "0.Return", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 6, option));

            switch (option) {
                case 1:
                    changeUsername();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    changeName();
                    break;
                case 4:
                    changeAddress();
                    break;
                case 5:
                    changeEmail();
                    break;
                case 6:
                    changePhoneNumber();
                    break;
                case 0:
                    return;

            }
        } while (option != 0);
    }

    public void successfullyModifyMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n%30s %10s\n", "", "Your account have been successfully modify!");
        System.out.printf("%30s %10s", "", "Press any key to continue : ");
        scanner.nextLine();

    }

    public void unsuccessfullyModifyMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n%30s %10s\n", "", "Your account have not been successfully modify!");
        System.out.printf("%30s %10s", "", "Press any key to continue : ");
        scanner.nextLine();

    }

    public void changeUsername() {

        Scanner scanner = new Scanner(System.in);
        String newUsername = "";
        System.out.printf("\n%30s %10s", "", "Current      : " + username);
        do {
            System.out.printf("\n%30s %10s", "", "New Username : ");
            newUsername = scanner.nextLine();
        } while (validationUserIDExist(newUsername) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            username = newUsername;
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }

    }

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        String newPassword = "";
        System.out.printf("\n%30s %10s", "", "Current      : " + password);
        do {
            System.out.printf("\n%30s %10s", "", "New Password : ");

            newPassword = scanner.nextLine();
        } while (validationPassword(newPassword) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            password = newPassword;
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }
    }

    public void changeName() {
        Scanner scanner = new Scanner(System.in);
        String newName = "";
        System.out.printf("\n%30s %10s", "", "Current  : " + getName());
        do {
            System.out.printf("\n%30s %10s", "", "New Name : ");
            newName = scanner.nextLine();
        } while (validationOnlyAlphabet(newName) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            setName(newName);
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }
    }

    public void changeAddress() {
        Scanner scanner = new Scanner(System.in);
        int state = 0;
        String city = "";
        System.out.printf("\n%30s %10s", "", "Current     : " + getAddress());
        System.out.printf("\n%30s %10s\n\n", "", "New Address :");
        printState(STATE);

        do {

            System.out.printf("\n%30s %10s", "", "Your Selection |> ");
            String tempState = scanner.nextLine();
            state = validationOptionNum(tempState, 9);
        } while (validationState(state) == false);

        do {
            System.out.printf("\n%26s %10s", "", "City :");
            city = scanner.nextLine();
        } while (validationOnlyAlphabet(city) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            setAddress(STATE[state - 1] + "," + city);
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }

    }

    public void changeEmail() {
        Scanner scanner = new Scanner(System.in);
        String newEmail = "";
        System.out.printf("\n%30s %10s", "", "Current   : " + getEmail());

        do {
            System.out.printf("\n%30s %10s", "", "New Email : ");
            newEmail = scanner.nextLine();
        } while (validationEmail(newEmail) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            setEmail(newEmail);
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }

        setEmail(newEmail);
        modifyFile();
        successfullyModifyMessage();
    }

    public void changePhoneNumber() {
        Scanner scanner = new Scanner(System.in);
        String newPhoneNumber = "";
        System.out.printf("\n%30s %10s", "", "Current          : " + getPhoneNumber());
        do {
            System.out.printf("\n%30s %10s", "", "New Phone Number : ");
            newPhoneNumber = scanner.nextLine();
        } while (validationPhoneNum(newPhoneNumber) == false);
        int confirm = VehicleRental.askConfirmation("Modify");
        if (confirm == 1) {
            setPhoneNumber(newPhoneNumber);
            modifyFile();
            successfullyModifyMessage();
        } else {
            unsuccessfullyModifyMessage();
        }
    }

    public void modifyFile() {
        ArrayList<String> tempArray = new ArrayList<>();
        try {
            try (FileReader fr = new FileReader("renterAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(Pattern.quote("|"));
                    if (lineArr[6].equals(renterID)) {
                        tempArray.add(this.toString());
                    } else {
                        tempArray.add(line);
                    }
                }
                fr.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        try {
            try (PrintWriter pr = new PrintWriter("renterAccount.txt")) {
                for (String str : tempArray) {
                    pr.println(str);
                }
                pr.close();
            } catch (Exception e) {
            }

        } catch (Exception e) {
        }

    }

    @Override
    public String toString() {
        return String.format(super.toString() + "|%s|%s|%s|%s|%s",
                renterID, username, password, choiceSecurityQues, securityAnswer);
    }

}
