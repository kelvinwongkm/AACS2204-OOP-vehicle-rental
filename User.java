/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;

import java.util.Scanner;

/**
 *
 * @author GhostGod
 */
public abstract class User {

    private String userID;
    private String name;
    private String address;
    private String icNum;
    private String email;
    private String phoneNumber;

    public abstract void loginMenu();

    public abstract void mainMenu();

    public User() {
        this("", "", "", "", "", "");
    }

    public User(String userID, String name, String address, String icNum, String email, String phoneNumber) {
        this.userID = userID;
        this.name = name;
        this.address = address;
        this.icNum = icNum;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcNum() {
        return icNum;
    }

    public void setIcNum(String icNum) {
        this.icNum = icNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void welcomeMessage(String userNameInput) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n%30s %10s\n", "", "Welcome back " + name + "!");
        System.out.printf("\n%30s %10s", "", "Press any key to continue : ");
        scanner.nextLine();

    }

    public void myAccount() {
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "UID", userID);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Name", name);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "IC Number", icNum);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Address", address);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Email", email);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");
        System.out.printf("%25s | %-20s| %-25s |\n", "", "Phone Number", phoneNumber);
        System.out.printf("%25s %35s\n", "", "+-------------------------------------------------+");

    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s",
                userID, name, address, icNum, email, phoneNumber);
    }

}
