/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static vehiclerental.DateTime.setRequiredDate;
import static vehiclerental.GeneralValidation.validateConfirm;
import static vehiclerental.GeneralValidation.validateDoubleInput;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateMultiInteger;
import static vehiclerental.GeneralValidation.validateOptionRange;
import static vehiclerental.VehicleRental.printConstantList;

/**
 *
 * @author kelvin
 */
public class RentingInfo implements AvailableLocation {

    private String location;
    private double rentRate;
    private static List<String> featureList;
    private List<String> availableFeature;
    private List<String> availableStartDate;
    private List<String> availableEndDate;

    public RentingInfo() {
        availableFeature = new ArrayList<>();
    }

    public RentingInfo(String[] str) {

        location = str[0];
        rentRate = Double.parseDouble(str[1]);
        availableFeature = new ArrayList<>(Arrays.asList(str[2].trim().replace("[", "").replace("]", "").split(",")));
        availableStartDate = new ArrayList<>(Arrays.asList(str[3].trim().replace("[", "").replace("]", "").split(",")));
        availableEndDate = new ArrayList<>(Arrays.asList(str[4].trim().replace("[", "").replace("]", "").split(",")));

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFeatureList(List<String> featureList) {
        RentingInfo.featureList = featureList;
    }

    public List<String> getAvailableStartDate() {
        return availableStartDate;
    }

    public List<String> getAvailableEndDate() {
        return availableEndDate;
    }

    public double getRentRate() {
        return rentRate;
    }

    public void setRentRate(double rentRate) {
        this.rentRate = rentRate;
    }

    public List<String> getAvailableFeature() {
        return availableFeature;
    }

    public void addRentInfo() {
        System.out.printf("%30s Renting Info\n", "");

        //get location
        location = getLocationString();
System.out.println("");
        // get rent rate
        double rate = validateDoubleInput("Please enter the Rent Rate of your vehicle : ");
        setRentRate(rate);

        System.out.println("");
        // get feature
        setFeature();

        // get availabledate
        setAvailableDate();

    }

    public void setAvailableDate(RentingInfo data) {

        LocalDate date = LocalDate.now();

        LocalDate startDate = setRequiredDate("Starting date that your vehicle is available for reserved/rent (dd/mm/yyyy) : ",
                date, 1);
        LocalDate endDate = setRequiredDate("Ending date that your vehicle is available for reserved/rent (dd/mm/yyyy) : ",
                LocalDate.parse(data.availableStartDate.get(0)), 7);

        data.availableStartDate.set(0, startDate.toString());

        data.availableEndDate.set(0, endDate.toString());

    }

    public void setAvailableDate() {
        availableStartDate = new ArrayList<>();
        availableEndDate = new ArrayList<>();
        LocalDate date = LocalDate.now();
        
        System.out.println("");
        availableStartDate.add(setRequiredDate("Starting date that your vehicle is available for reserved/rent (dd/mm/yyyy) : ",
                date, 0).toString());
        
        System.out.println("");
        availableEndDate.add(setRequiredDate("Ending date that your vehicle is available for reserved/rent (dd/mm/yyyy) : ",
                LocalDate.parse(availableStartDate.get(0)), 7).toString());

    }

    @Override
    public String toString() {
        return String.format("%s|%.2f|%s|%s|%s",
                location, rentRate, availableFeature.toString(), availableStartDate.toString(), availableEndDate.toString());
    }

    public void setFeature(RentingInfo data) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%30s Select your vehicle's feature\n", "");
        printConstantList(featureList.toArray(new String[0]));

        System.out.printf("%30s You may select more than 1 feature. Please separate your selection with comma. IE: 1,2", "");
        System.out.printf("%30s Your selection : ", "");
        String userSelection = scanner.nextLine().replaceAll("\\s", "");
        String[] selection = userSelection.trim().split(",");

        for (String option : selection) {
            int userOption = Integer.parseInt(option) - 1;
            if (!validateOptionRange(0, featureList.size(), userOption)) {
                System.out.printf("%30s Invalid input entered\n", "");
            }
        }

        for (String option : selection) {
            int userOption = Integer.parseInt(option) - 1;
            data.availableFeature.add(featureList.get(userOption));
        }
    }

    public void setFeature() {
        Scanner scanner = new Scanner(System.in);

        System.out.printf("%30s Select your vehicle's feature\n", "");
        printConstantList(featureList.toArray(new String[0]));
        System.out.printf("%30s You may select more than 1 feature. Please separate your selection with comma. IE: 1,2\n", "");
        List<String> selection = validateMultiInteger(featureList);

        for (String option : selection) {
            int userOption = Integer.parseInt(option) - 1;
            availableFeature.add(featureList.get(userOption));
        }

    }

    public boolean updateRentInfo(RentingInfo data) {
        int number;
        boolean updateConfirm = false;

        do {

            updateConfirm = false;

            System.out.printf("\n%30s --------------------------Update Renting Details------------------------------\n", "");
            System.out.printf("%30s | %-23s | %-23s | %-23s |\n", "1. Location", "2. Rent Rate", "3. Available Feature");
            System.out.printf("%30s -------------------------------------------------------------------------------\n", "");
            System.out.printf("%30s | %-23s | %-23s | %-23s |\n", "4. Available Date", "0. Return", "");
            System.out.printf("%30s -------------------------------------------------------------------------------\n", "");

            do {
                number = validateIntegerInput("Please enter your selection : ");
                System.out.println("");
            } while (!validateOptionRange(0, 4, number));

            switch (number) {
                case 0:
                    return false;

                case 1:
                    System.out.printf("%30s Old Location : %s\n", "", data.getLocation());
                    String enteredLocation = getLocationString();
                    System.out.printf("%30s Your new area is : %s\n", "", enteredLocation);
                    updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                    if (updateConfirm) {
                        data.setLocation(enteredLocation);
                    }
                    break;

                case 2:

                    System.out.printf("%30s Old rent rate : %.2f\n", "", data.getRentRate());
                    double enteredRentRate = validateDoubleInput("Please enter new rent rate :  ");
                    System.out.printf("%30s Your new rent rate is : %.2f\n", "", enteredRentRate);
                    updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                    if (updateConfirm) {
                        data.setRentRate(enteredRentRate);
                    }
                    break;

                case 3:
                    List<String> tempFeature = new ArrayList<>();
                    tempFeature.addAll(data.availableFeature);
                    data.availableFeature.clear();

                    System.out.printf("%30s Old feature of your vehicle : %s\n", "", tempFeature
                            .toString().replace("[", "").replace("]", ""));

                    setFeature(data);
                    System.out.printf("%30s Your new feature is : %s\n", "", data.getAvailableFeature()
                            .toString().replace("[", "").replace("]", ""));
                    updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                    if (!updateConfirm) {
                        // revert to orignal data if user does not want to change the feature
                        data.availableFeature.clear();
                        data.availableFeature.addAll(tempFeature);
                    }

                    break;

                case 4:
                    List<String> tempStartDate = new ArrayList<>();
                    List<String> tempEndDate = new ArrayList<>();

                    if (data.availableStartDate.size() > 1) {
                        System.out.printf("%30s Your are only able to edit the available date without having any reservation activity\n", "");
                        System.out.printf("%30s The current vehicle is getting involve in %d reservation.\n", "",
                                +(data.availableStartDate.size() - 1));
                        break;
                    }

                    System.out.printf("%30s You will have to update for start and end date of renting or reservation\n", "");

                    System.out.printf("%30s Your old available start date : %s\n", "", data.getAvailableStartDate().toString()
                            .replace("[", "").replace("]", ""));
                    System.out.printf("%30s Your old available end date : %s\n", "", data.getAvailableEndDate().toString()
                            .replace("[", "").replace("]", ""));

                    setAvailableDate(data);

                    System.out.printf("%30s Your new available start date : %s\n", "", data.getAvailableStartDate().toString()
                            .replace("[", "").replace("]", ""));
                    System.out.printf("%30s Your new available end date : %s\n", "", data.getAvailableEndDate().toString()
                            .replace("[", "").replace("]", ""));
                    updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                    if (!updateConfirm) {
                        data.availableStartDate.clear();
                        data.availableEndDate.clear();

                        data.availableStartDate.addAll(tempStartDate);
                        data.availableEndDate.addAll(tempEndDate);
                    }
                    break;

            }
        } while (number != 0);

        return updateConfirm;
    }

    public static String getLocationString() {
        int selectedOption;
        do {
            System.out.printf("\n%30s Available City: \n", "");
            printConstantList(CITY);

            selectedOption = validateIntegerInput("Please select required city : ");
        } while (!validateOptionRange(1, CITY.length, selectedOption));

        return CITY[selectedOption - 1];
    }
}
