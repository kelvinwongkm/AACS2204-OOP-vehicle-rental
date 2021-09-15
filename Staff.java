/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;


import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateOptionRange;

/**
 *
 * @author GhostGod
 */
public class Staff extends User {

    private String staffID;
    private String staffPassword;
    private String position;
    private String activityPerformed;

    public Staff() {
        this("", "", "", "", "", "", "", "", "", "");
    }

    public Staff(String staffID, String staffPassword, String position, String activityPerformed, String userID, String name, String address, String icNum, String email, String phoneNumber) {
        super(userID, name, address, icNum, email, phoneNumber);
        this.staffID = staffID;
        this.staffPassword = staffPassword;
        this.position = position;
        this.activityPerformed = activityPerformed;
    }

    @Override
    public void loginMenu() {//staff login menu
        int option = 0;
        System.out.printf("\n%-37s %15s\n", " ", "Staff");
        System.out.printf("%30s %35s\n\n", "", "=========================================");
        System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
        System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "1.Login", "");
        System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
        System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "0.Return", "");
        System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

        do {
            option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
        } while (!validateOptionRange(0, 1, option));

        switch (option) {
            case 1:
                staffLogin();
                break;
            case 0:
                break;
        }

    }

    public void staffLogin() {//staff login method

        String tempStaffID = "";
        String tempStaffPw = "";
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.printf("\n%30s %10s", "", "Staff ID : ");
            tempStaffID = scanner.nextLine();
            System.out.printf("\n%30s %10s", "", "Password : ");
            tempStaffPw = scanner.nextLine();

            fileReaderStaff(tempStaffID);
            if (staffID.equals(tempStaffID) && staffPassword.equals(tempStaffPw)) {
                welcomeMessage(getName());
                mainMenu();
            } else if (staffID.equals(tempStaffID) && !staffPassword.equals(tempStaffPw)) {

                System.out.printf("\n%30s %10s\n", "", "Incorrect user password.");

            } else {
                System.out.printf("\n%30s %10s\n", "", "Staff ID not found.");
            }
        } while (!staffID.equals(tempStaffID) || !staffPassword.equals(tempStaffPw));
    }

    @Override
    public void mainMenu() {//staff main menu

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        do {
            System.out.printf("\n%-37s %15s\n", " ", "Staff");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-9s |\n", "", "", "1. Manage Vehicle", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-13s |\n", "", "", "2. My Profile", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-13s |\n", "", "", "3. View Report", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %-10s %-12s %-13s |\n", "", "", "0. Log Out", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 3, option));

            switch (option) {
                case 1:

                    try {
                        manageVehicleMenu(); // manage vehicle 
                    } catch (IOException ex) {
                        Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case 2:
                    myAccount();// view profile
                    break;

                case 3:
                    //view report
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

    public void manageVehicleMenu() throws IOException {
        int option = 0;
        do {
            do {
                System.out.printf("\n%37s %15s\n", " ", "Staff");
                System.out.printf("%30s %35s\n\n", "", "=========================================");
                System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
                System.out.printf("%30s | %10s %-12s %-13s |\n", "", "", "1. Car", "");
                System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
                System.out.printf("%30s | %10s %-12s %-13s |\n", "", "", "2. Motorbike", "");
                System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
                System.out.printf("%30s | %10s %-12s %1s |\n", "", "", "3. Recreational Vehicle", "");
                System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
                System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "0. Return", "");
                System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

                option = validateIntegerInput("Please select the vehicle type you want to manage : ");
            } while (!validateOptionRange(0, 3, option));

            switch (option) {
                case 0:
                    return;

                case 1:
                    vehicleMenuDetails("Car", new Car());
                    break;

                case 2:
                    vehicleMenuDetails("Motorbike", new Motorbike());
                    break;

                case 3:
                    vehicleMenuDetails("Recreational Vehicle", new RecreationalVehicle());
                    break;

            }

        } while (option != 0);
    }

    public void vehicleMenuDetails(String vehicleType, Object o) throws IOException {
        int option = 0;
        do {
            System.out.printf("\n%37s %15s\n", " ", "Staff");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %-10s |\n", "", "", "1. Add " + vehicleType, "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %-10s |\n", "", "", "2. View " + vehicleType, "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %13s %-12s %11s |\n", "", "", "0. Return", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

            do {
                option = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            } while (!validateOptionRange(0, 3, option));

            switch (option) {
                case 0:
                    return;

                case 1:
                    if (o instanceof Car) {
                        ((Car) o).addVehicle();

                    } else if (o instanceof Motorbike) {
                        ((Motorbike) o).addVehicle();

                    } else if (o instanceof RecreationalVehicle) {
                        ((RecreationalVehicle) o).addVehicle();
                    }
                    break;

                case 2:
                    ((Vehicle) o).viewVehicle(o);
                    updateDeleteVehicle();
                    break;

            }
        } while (option != 0);

    }

    public static int updateDeleteVehicle() {
        int option = 0;

        System.out.printf("\n%37s %15s\n", " ", "Staff");
        System.out.printf("%30s %35s\n\n", "", "=========================================");
        System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
        System.out.printf("%30s | %13s %-12s %-10s |\n", "", "", "1. Update", "");
        System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
        System.out.printf("%30s | %13s %-12s %-10s |\n", "", "", "2. Delete", "");
        System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
        System.out.printf("%30s | %13s %-12s %10s |\n", "", "", "0. Return", "");
        System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");

        do {
            option = validateIntegerInput("Please select the action you want to perform : ");
        } while (!validateOptionRange(0, 3, option));
        return option;
    }

    @Override
    public void myAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%26s|%-19s %-15s %-13s|\n", " ", "", "Profile", "");
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Staff ID", staffID);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Position", position);
        super.myAccount();
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Activity Performed", activityPerformed);
        System.out.printf("%25s %35s\n\n", "", "+-------------------------------------------------+");
        System.out.printf("%30s %10s", "", "Press any key to continue : ");
        scanner.nextLine();
    }

    public void fileReaderStaff(String tempStaffID) {
        try {
            try (FileReader fr = new FileReader("staffAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(Pattern.quote("|"));
                    if (lineArr[1].equals(tempStaffID)) {
                        setUserID(lineArr[0]);
                        staffID = lineArr[1];
                        staffPassword = lineArr[2];
                        setName(lineArr[3]);
                        setAddress(lineArr[4]);
                        setIcNum(lineArr[5]);
                        setEmail(lineArr[6]);
                        setPhoneNumber(lineArr[7]);
                        position = lineArr[8];
                        activityPerformed = lineArr[9];
                    }
                }
                fr.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }

    }

    public void updateActivityPerformed(int actPerformed) {
        String tempActPerformed = "";
        switch (actPerformed) {
            case 1:
                tempActPerformed = "Add Vehicle";
                break;
            case 2:
                tempActPerformed = "Delete Vehicle";
                break;
            case 3:
                tempActPerformed = "Modify Vehicle";
                break;
            default:
                break;
        }
        modifyFileActPerformed(tempActPerformed);
        fileReaderStaff(staffID);
    }

    public void modifyFileActPerformed(String newActPerformed) {
        ArrayList<String> tempArray = new ArrayList<>();
        try {
            try (FileReader fr = new FileReader("staffAccount.txt")) {
                Scanner reader = new Scanner(fr);
                String line;
                String[] lineArr;
                while ((line = reader.nextLine()) != null) {
                    lineArr = line.split(",");
                    if (lineArr[1].equals(staffID)) {
                        tempArray.add(
                                lineArr[0] + ","
                                + lineArr[1] + ","
                                + lineArr[2] + ","
                                + lineArr[3] + ","
                                + lineArr[4] + ","
                                + lineArr[5] + ","
                                + lineArr[6] + ","
                                + lineArr[7] + ","
                                + lineArr[8] + ","
                                + lineArr[9] + ","
                                + newActPerformed);
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
            try (PrintWriter pr = new PrintWriter("staffAccount.txt")) {
                for (String str : tempArray) {
                    pr.println(str);
                }
                pr.close();
            } catch (Exception e) {
            }

        } catch (Exception e) {
        }

    }

}
