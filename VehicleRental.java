/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author GhostGod
 */
public class VehicleRental {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        new VehicleRental();
//        Reservation r = new Reservation();
//        String renterID = "R00002";
//        r.onGoingReservation(renterID);

        Report r = new Report();
        r.assignVehiclesReserved();
        r.displayReport();
        r.displayChart();
        

    }

    public VehicleRental() {
	int option = 0, confirm;
	Renter renter = new Renter();
	Staff staff = new Staff();
	Scanner scanner = new Scanner(System.in);
	do {
	    System.out.printf("\n%42s %15s\n", " ", "Vehicle Rental");
	    System.out.printf("%30s %35s\n\n", "", "=========================================");
	    System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
	    System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "1.Renter", "");
	    System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
	    System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "2.Staff", "");
	    System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
	    System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "3.Exit", "");
	    System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");
	    do {
		System.out.printf("\n%30s %10s", "", "Your Selection |> ");
		String input = scanner.nextLine();
		option = Renter.validationOptionNum(input, 3);
	    } while (option == 0);
	    switch (option) {
		case 1:
		    renter.loginMenu();
		    break;
		case 2:
		    staff.loginMenu();
		    break;
		case 3:

		    confirm = askConfirmation("Exit");
		    if (confirm == 1) {
			thankyouMessage("ShutDown");
			option = 3;
			break;
		    } else {
			option = 0;
			break;
		    }
	    }

	} while (option != 3);

    }

    public static int askConfirmation(String str) {
	Scanner scanner = new Scanner(System.in);
	String option;
	int confirm;
	do {
	    System.out.printf("\n%30s %10s", "", "Are you sure you want to " + str + "(Y/N)? ");
	    option = scanner.nextLine();
	} while (validationConfirmation(option) == false);
	if (option.charAt(0) == ('Y') || option.charAt(0) == ('y')) {
	    return confirm = 1;
	}
	return confirm = 0;
    }

    public static boolean validationConfirmation(String option) {
	if (option.length() > 1) {
	    System.out.printf("\n%30s %10s\n", "", "Invalid Input");
	    return false;
	} else if (option.charAt(0) == ('Y') || option.charAt(0) == ('y') || option.charAt(0) == ('N') || option.charAt(0) == ('n')) {
	    return true;
	} else {
	    System.out.printf("\n%30s %10s\n", "", "Invalid Input");
	    return false;
	}
    }

    public static void thankyouMessage(String str) {
	if (str.equals("LogOut")) {
	    System.out.printf("\n%30s %10s\n", "", "You have successfully logged out");
	    System.out.printf("\n%30s %10s\n", "", "Thanks for using MyKL.");
	    System.out.printf("\n%30s %10s\n", "", "Wish you have a nice day.");
	} else if (str.equals("ShutDown")) {
	    System.out.printf("\n%30s %10s\n", "", "System successfully shut down.");
	    System.out.printf("\n%30s %10s\n\n", "", "Brought to you by Buddhism Spirit Team.");
	}

    }

    public static void sleep(int ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException ex) {
	    Thread.currentThread().interrupt();
	}
    }

    public static void timeCountDown() throws InterruptedException {
	clearme();
	for (int i = 10; i >= 0; i--) {
	    System.out.printf("\n\n\n\n%35s %s %d %s\n", "", "Please wait ", i, " seconds to try again.");
	    Thread.sleep(1000);
	    clearme();
	}
	System.out.printf("\n\n\n\n%45s %10s\n", "", "Redirecting...");
	Thread.sleep(1000);
	clearme();
    }

    public static void clearme() { //clear screen
	try {
	    Robot pressbot = new Robot();
	    pressbot.keyPress(17); // Holds CTRL key.
	    pressbot.keyPress(76); // Holds L key.
	    pressbot.keyRelease(17); // Releases CTRL key.
	    pressbot.keyRelease(76); // Releases L key.
	} catch (AWTException ex) {
	}
    }

    public static int getLatestID(String fileName, int IDIndex) throws IOException {
	String line;
	int id = 1; // default is 1

	File fName = new File(fileName);
	if (!fName.exists()) {
	    fName.createNewFile();
	}

	BufferedReader br = new BufferedReader(new FileReader(fName));
	while ((line = br.readLine()) != null) {
	    String[] values = line.split(Pattern.quote("|"));
	    id = Integer.parseInt(values[IDIndex].replaceAll("[^0-9]", ""));
	}

	return id;
    }

    public static void printConstantList(List<String> list) {
	System.out.printf("%30s ", "");
	for (int i = 0, x = 1; i < list.size(); i++, x++) {
	    System.out.printf("%d) %-20s", x, list.get(i));
	    if (x == list.size()) {
		System.out.printf("\n");
	    } else if (x % 3 == 0) {
		System.out.printf("\n");
		System.out.printf("%30s ", "");
	    } else {
		System.out.printf("\t\t");
	    }
	}
    }

    public static void printConstantList(String[] list) {
	System.out.printf("%30s ", "");

	for (int i = 0, x = 1; i < list.length; i++, x++) {
	    System.out.printf("%d) %-20s", x, list[i]);
	    if (x == list.length) {
		System.out.printf("\n");
	    } else if (x % 3 == 0) {
		System.out.printf("\n");
		System.out.printf("%30s ", "");
	    } else {
		System.out.printf("\t\t");
	    }
	}
    }

}
