package vehiclerental;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import static vehiclerental.DateTime.DATEFORMAT;
import static vehiclerental.GeneralValidation.validateConfirm;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateMultiInteger;
import static vehiclerental.GeneralValidation.validateOptionRange;
import static vehiclerental.VehicleRelated.VEHICLETYPE;
import static vehiclerental.VehicleRental.askConfirmation;
import static vehiclerental.VehicleRental.getLatestID;
import static vehiclerental.VehicleRental.printConstantList;
import static vehiclerental.VehicleRental.printType;
import static vehiclerental.VehicleRental.successfullyMsg;
import static vehiclerental.VehicleRental.unsuccessfullyMsg;

/**
 *
 * @author Wong Kah Ming
 */

public class Reservation implements VehicleRelated {

    private Renter renter;
    private Vehicle vehicle;
    private String reservationID;
    private DateTime pickUp;
    private DateTime dropOff;
    private String location;
    private int rentalDuration;

    public Reservation(Renter renter) {
        this.renter = renter;
        pickUp = new DateTime();
        dropOff = new DateTime();
    }

    public Reservation(String[] str) throws IOException {
        reservationID = str[0];
        pickUp = new DateTime(LocalDate.parse(str[1]), LocalTime.parse(str[2]));
        dropOff = new DateTime(LocalDate.parse(str[3]), LocalTime.parse(str[4]));
        location = str[5];

        if (str[6].matches("CA.*")) {
            vehicle = Vehicle.getData(new Car(), str[6]);
        } else if (str[6].matches("MB.*")) {
            vehicle = Vehicle.getData(new Motorbike(), str[6]);

        } else if (str[6].matches("RV.*")) {
            vehicle = Vehicle.getData(new RecreationalVehicle(), str[6]);
        }

        renter = Renter.getRenterData(str[7]);
        rentalDuration = DateTime.dateDiffInDays(pickUp.getDate(), dropOff.getDate());
    }

    public Renter getRenter() {
        return renter;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public String getLocation() {
        return location;
    }

    public String getReservationID() {
        return reservationID;
    }

    public DateTime getPickUp() {
        return pickUp;
    }

    public DateTime getDropOff() {
        return dropOff;
    }

    public int getRentalDuration() {
        return rentalDuration;
    }

    public String[] inputSearchInfo() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int id = getLatestID("reservation.txt", 0) + 1;
        reservationID = String.format("%s%02d", "RS", id);
        String time, vehicleType;
        int reserveType;

        do {
            System.out.printf("\n%30s Available Vehicle Type : \n", "");
            printType(VEHICLETYPE);

            reserveType = validateIntegerInput(String.format("\n%30s Please select the vehicle type you want to reserve : ", ""));
        } while (!validateOptionRange(1, VEHICLETYPE.length, reserveType));
        vehicleType = VEHICLETYPE[reserveType - 1];

        System.out.println("");

        pickUp.setDate(DateTime.setRequiredDate(
                "Please enter your desired pickup date (DD/MM/YYYY): ",
                LocalDate.now(), 1));

        System.out.println("");

        do {
            System.out.printf("%30s Please enter your desired pickup time (HH:MM): ", "");
            time = scanner.nextLine();
        } while (!DateTime.validTime(time));
        pickUp.setTime(LocalTime.parse(time));

        System.out.println("");

        dropOff.setDate(DateTime.setRequiredDate("Please enter your desired dropoff date (DD/MM/YYYY): ",
                pickUp.getDate(), 1));

        System.out.println("");

        do {
            System.out.printf("%30s Please enter your desired dropoff time (HH:MM): ", "");
            time = scanner.nextLine();
        } while (!DateTime.validTime(time));

        dropOff.setTime(LocalTime.parse(time));

        System.out.println("");
        //Code to display available locations
        //...
        System.out.printf("%30s Please enter your desired pickup and dropoff city in Kuala Lumpur: \n", "");
        location = RentingInfo.getLocationString();
        //Code to display available car types
        //...
        System.out.println("");

        rentalDuration = DateTime.dateDiffInDays(pickUp.getDate(), dropOff.getDate());
        //Code to display information entered
        //...
        displayRequirementInfo(vehicleType);

        boolean confirm = validateConfirm("Please confirm the above information are correct. (Y/N): ");
        System.out.println("");
        String[] returnValue = {Boolean.toString(confirm), vehicleType};
        return returnValue;
    }

    public void displayRequirementInfo(String selectedType) {
        System.out.printf("\n%-30s %15s %s\n", "", "", "Reservation Information");
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "Vehicle Type", selectedType);
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "PickUp Date", pickUp.getDate());
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "PickUp Time", pickUp.getTime());
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "DropOff Date", dropOff.getDate());
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "DropOff Time", dropOff.getTime());
        System.out.printf("%30s ------------------------------------------------------------\n", "");
        System.out.printf("%30s | %-26s : %-27s |\n", "", "location", location);
        System.out.printf("%30s ------------------------------------------------------------\n", "");
    }

    public void makeReservation() throws IOException {
        boolean hasVehicle = false;
        do {
            hasVehicle = false;

            Object o;
            List<Vehicle> tempVehicle;
            String[] returnValue = inputSearchInfo();
            if (!Boolean.parseBoolean(returnValue[0])) {
                return;
            }

            if (returnValue[1].equalsIgnoreCase("Car")) {
                o = new Car();
            } else if (returnValue[1].equalsIgnoreCase("Motorbike")) {
                o = new Motorbike();
            } else {
                o = new RecreationalVehicle();
            }

            tempVehicle = Vehicle.getData(o);

            if (tempVehicle == null) {
                System.out.printf("%30s No vehicle founded.\n", "");
                return;
            }

            List<Vehicle> suitableVehicleList = meetsRequirementList(tempVehicle);

            if (suitableVehicleList.isEmpty()) {
                System.out.printf("%30s No suitable vehicle founded.\n", "");
                hasVehicle = true;
            } else {

                filterListMenu(suitableVehicleList);

                vehicle = displayPreviewAndSelect(suitableVehicleList);

                if (vehicle != null) {
                    // store reservation
                    // paymentthing
                    Payment.makePayment(this);
                    writeFile(Vehicle.getFileName(o), tempVehicle);
                    Vehicle.appendSingleRecord("reservation.txt", this.toString());
                    return;	// if selected the vehicle, end function
                }
            }
        } while (validateConfirm("Do you want to change your requirement? (Y/N) : ") && hasVehicle);

    }

    public List<Vehicle> meetsRequirementList(List<Vehicle> tempVehicle) {
        List<Vehicle> suitableVehicle = new ArrayList<>();
        if (tempVehicle.isEmpty()) {
            return null;
        }

        for (Vehicle vehicle : tempVehicle) {

            for (int y = 0; y < vehicle.getRentInfo().getAvailableStartDate().size(); y++) {

                LocalDate availableStartDate = LocalDate.parse(vehicle.getRentInfo().getAvailableStartDate().get(y).trim());
                LocalDate availableEndDate = LocalDate.parse(vehicle.getRentInfo().getAvailableEndDate().get(y).trim());
                String tempLocation = vehicle.getRentInfo().getLocation();

                if (pickUp.getDate().compareTo(availableStartDate) > 0
                        && dropOff.getDate().compareTo(availableEndDate) < 0
                        && getLocation().equalsIgnoreCase(tempLocation)) {

                    suitableVehicle.add(vehicle);

                }

            }
        }
        return suitableVehicle;
    }

    public void filterListMenu(List<Vehicle> suitableVehicleList) {
        int selection;
        List<String> makeList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        updateMakeType(suitableVehicleList, makeList, typeList);

        System.out.printf("%30s There are total %d suitable vehicle been founded.\n",
                "", suitableVehicleList.size());

        do {

            boolean validateConfirm = validateConfirm("Do you want to filter the list? (Y/N) : ");
            if (!validateConfirm) {
                return;
            }

            System.out.printf("\n%30s Please select the action that you would like to perform\n", "");
            System.out.printf("%30s 1. Filter the result to specified brand(s)\n", "");
            System.out.printf("%30s 2. Filter the result to specified type(s)\n", "");
            System.out.printf("%30s 3. Display record according price's accending order\n", "");
            System.out.printf("%30s 4. Display record according price's decending order\n", "");
            System.out.printf("%30s 0. Return\n", "");

            do {
                selection = validateIntegerInput("Please enter your selection : ");
            } while (!validateOptionRange(0, 4, selection));

            switch (selection) {
                case 0:
                    return;
                case 1:
                    filterList(suitableVehicleList, makeList, "brand/make(s)");
                    break;
                case 2:
                    filterList(suitableVehicleList, typeList, "type(s)");
                    break;
                case 3:
                    Collections.sort(suitableVehicleList);
                    break;
                case 4:
                    Collections.sort(suitableVehicleList, Collections.reverseOrder());
                    break;
            }

            updateMakeType(suitableVehicleList, makeList, typeList);
            System.out.printf("\n%30s The searched record has been updated\n", "");
            System.out.printf("%30s There are total %d suitable vehicle remaining.\n",
                    "", suitableVehicleList.size());

        } while (selection != 0);

    }

    public void updateMakeType(List<Vehicle> suitableVehicleList, List<String> makeList, List<String> typeList) {
        Set<String> makeSet = new HashSet<>();
        Set<String> typeSet = new HashSet<>();

        for (Vehicle vehicle : suitableVehicleList) {
            makeSet.add(vehicle.getVehicleBasic().getMake());
            typeSet.add(vehicle.getVehicleBasic().getType());
        }

        makeList.clear();
        typeList.clear();

        makeList.addAll(makeSet);
        typeList.addAll(typeSet);
    }

    public void filterList(List<Vehicle> suitableVehicleList, List<String> returnSelectedList, String message) {

        List<Vehicle> temp = new ArrayList<>();

        System.out.printf("\n%30s The following %s has/have been founded\n", "", message);
        printConstantList(returnSelectedList);

        System.out.printf("%15s You may select more than 1 %s. Please separate your selection with comma. IE: 1,2\n", "", message);

        List<String> selection = validateMultiInteger(returnSelectedList);

        List<String> selectedFilterItem = new ArrayList<>();
        for (String option : selection) {
            int selectedOption = Integer.parseInt(option) - 1;
            selectedFilterItem.add(returnSelectedList.get(selectedOption));
        }

        // update the list with filtered value
        if (message.equalsIgnoreCase("brand/make(s)")) {

            for (String filterItem : selectedFilterItem) {
                for (Vehicle vehicle : suitableVehicleList) {
                    if (vehicle.getVehicleBasic().getMake().equalsIgnoreCase(filterItem)) {
                        temp.add(vehicle);
                    }
                }
            }

        } else if (message.equalsIgnoreCase("type(s)")) {

            for (String filterItem : selectedFilterItem) {
                for (Vehicle vehicle : suitableVehicleList) {
                    if (vehicle.getVehicleBasic().getType().equalsIgnoreCase(filterItem)) {
                        temp.add(vehicle);
                    }
                }
            }
        }

        suitableVehicleList.clear();
        suitableVehicleList.addAll(temp);
    }

    public Vehicle displayPreviewAndSelect(List<Vehicle> vehicleList) {
        int selection;
        Boolean selectConfirm, continueSelect;

        do {
            System.out.printf("\n%30s The following vehicle(s） meet your needs.\n", "");

            System.out.printf("\n%15s +------+--------------+-------------------------------+-----------------------+------------------------+\n", "");
            System.out.printf("%15s |  No. |  Make        |  Model                        |  Type                 |   Rent Rate / Day (RM) |\n", "");
            System.out.printf("%15s +------+--------------+-------------------------------+-----------------------+------------------------+\n", "");

            for (int i = 0; i < vehicleList.size(); i++) {
                System.out.printf("%15s | %3d. |  %-11s |  %-28s |  %-19s  |  %-20.2f  |\n", "",
                        i + 1, vehicleList.get(i).getVehicleBasic().getMake(),
                        vehicleList.get(i).getVehicleBasic().getModel(),
                        vehicleList.get(i).getVehicleBasic().getType(),
                        vehicleList.get(i).getRentInfo().getRentRate());
                System.out.printf("%15s +------+--------------+-------------------------------+-----------------------+------------------------+\n", "");
            }

            do {
                selection = validateIntegerInput("Please select required vehicle to view more information : ");
            } while (!validateOptionRange(1, vehicleList.size(), selection));

            System.out.println("");
            vehicleList.get(selection - 1).viewVehicleInfoReservationVersion();
            selectConfirm = validateConfirm("Do you want to rent this vehicle? (Y/N) : ");

            if (selectConfirm) {
                DateTime.updateDate(
                        vehicleList.get(selection - 1).getRentInfo().getAvailableStartDate(),
                        vehicleList.get(selection - 1).getRentInfo().getAvailableEndDate(),
                        pickUp.getDate(), dropOff.getDate());

                return vehicleList.get(selection - 1);
            }

            continueSelect = validateConfirm("Do you want to continue view other vehicle ? (Y/N) : ");
        } while (continueSelect);

        return null;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s",
                reservationID, pickUp.toString(),
                dropOff.toString(), location, vehicle.getVehicleID(), renter.getRenterID());
    }

    public static void onGoingReservation(String renterID) throws IOException {
        List<Reservation> allReservationRecord = getData();
        List<Reservation> matchedRecord = new ArrayList();

        if (allReservationRecord == null) {
            System.out.printf("%30s Null record\n", "");
            return;
        }

        for (Reservation reservation : allReservationRecord) {
            if (reservation.getRenter().getRenterID().equalsIgnoreCase(renterID)) {
                matchedRecord.add(reservation);
            }
        }

        if (matchedRecord.isEmpty()) {
            System.out.printf("%30s No on-going reservation record is founded.\n", "");
            return;
        }

        System.out.printf("\n%15s +------+--------------------+----------------------+---------------------------+-------------------------------------------+\n", "");
        System.out.printf("%15s |  No. |  Reservation ID    |  Location            |  Reserved Vehicle ID      |   Reservation Duration                    |\n", "");
        System.out.printf("%15s +------+--------------------+----------------------+---------------------------+-------------------------------------------+\n", "");

        for (int i = 0; i < matchedRecord.size(); i++) {
            String reservationDuration = DateTime.displayDateTime(
                    matchedRecord.get(i).getPickUp().getDate().format(DATEFORMAT),
                    matchedRecord.get(i).getPickUp().getTime().toString(),
                    matchedRecord.get(i).getDropOff().getDate().format(DATEFORMAT),
                    matchedRecord.get(i).getDropOff().getTime().toString()
            );

            System.out.printf("%15s | %3d. |  %-17s |  %-19s |  %-24s |  %-40s |\n", "",
                    i + 1, matchedRecord.get(i).getReservationID(),
                    matchedRecord.get(i).getLocation(),
                    matchedRecord.get(i).getVehicle().getVehicleID(),
                    reservationDuration);
            System.out.printf("%15s +------+--------------------+----------------------+---------------------------+-------------------------------------------+\n", "");
        }

        int selection = 0;

        do {
            System.out.println("");
            selection = validateIntegerInput("Please select required vehicle to view more information : ");
        } while (!validateOptionRange(1, matchedRecord.size(), selection));
	
        Reservation selectedReservation = matchedRecord.get(selection - 1);
        displayReservationInfo(selectedReservation);

        int option = 0;
        System.out.printf("%30s Please select the action you want to perform.\n", "");
        System.out.printf("%30s 1. Cancel reservation\n", "");
        System.out.printf("%30s 0. Return\n", "");
        do {
            option = validateIntegerInput("Please enter your selection : ");
        } while (!validateOptionRange(0, 1, option));

        switch (option) {
            case 0:
                return;
            case 1:
                int confirm = askConfirmation("cancel reservation");
                if (confirm == 1) {
                    deleteReservation(allReservationRecord, selectedReservation);
                    successfullyMsg("cancel reservation");
                    break;
                } else {
                    unsuccessfullyMsg("cancel reservation");
                    break;
                }

        }

    }

    public static void deleteReservation(List<Reservation> reservationRecord, Reservation selectedReservation) throws IOException {
        Object o = null;
        if (selectedReservation.getVehicle().getVehicleID().contains("CA")) {
            o = new Car();
        } else if (selectedReservation.getVehicle().getVehicleID().contains("MB")) {
            o = new Motorbike();
        } else if (selectedReservation.getVehicle().getVehicleID().contains("RV")) {
            o = new RecreationalVehicle();
        }

        List<Vehicle> tempVehicle = Vehicle.getData(o);

        if (tempVehicle.isEmpty()) {
            System.out.printf("%30s No vehicle founded.\n", "");
            return;
        }

        for (Vehicle vehicle : tempVehicle) {
            if (vehicle.getVehicleID().equals(selectedReservation.getVehicle().getVehicleID())) {
                DateTime.updateDate(
                        vehicle.getRentInfo().getAvailableStartDate(),
                        vehicle.getRentInfo().getAvailableEndDate(),
                        selectedReservation);

                break;
            }

        }

        //update file
        reservationRecord.remove(reservationRecord.indexOf(selectedReservation));
        writeFile(Vehicle.getFileName(o), tempVehicle);
        writeFile("reservation.txt", reservationRecord);

    }

    public static void displayReservationInfo(Reservation reservation) {

        System.out.println("");
        System.out.printf("%15s +--------------------+----------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s |  Reservation ID    | Location             |   Duration                                         |\n", "");
        System.out.printf("%15s +--------------------+----------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s |  %-17s | %-19s  |   %-48s |\n",
                "", reservation.getReservationID(), reservation.getLocation(),
                DateTime.displayDateTime(
                        reservation.getPickUp().getDate().format(DateTime.DATEFORMAT),
                        reservation.getPickUp().getTime().toString(),
                        reservation.getDropOff().getDate().format(DateTime.DATEFORMAT),
                        reservation.getDropOff().getTime().toString()));
        System.out.printf("%15s +--------------------+----------------------+----------------------------------------------------+\n", "");
        System.out.println("");
        reservation.vehicle.viewVehicleInfoReservationVersion();
        System.out.println("");
    }

    public static void writeFile(String fileName, List<?> objectList) throws IOException {
        File filen = new File(fileName);
        FileWriter file = new FileWriter(filen);
        try (BufferedWriter output = new BufferedWriter(file)) {
            if (!filen.exists()) {
                filen.createNewFile();
            }

            for (int i = 0; i < objectList.size(); i++) {
                output.write(objectList.get(i).toString() + "\n");
                output.flush();
            }
            output.close();
        }
    }

    public static List<Reservation> getData() throws IOException, FileNotFoundException {
        String line;
        List<Reservation> reservationList = new ArrayList<>();
        File fileName = new File("reservation.txt");
        boolean noRecord = true;
        if (!fileName.exists()) {
            fileName.createNewFile();

        }
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            String[] values = line.split(Pattern.quote("|"));
            reservationList.add(new Reservation(values));
            noRecord = false;
        }

        if (noRecord) {
            System.out.printf("%30s No record found\n", "");
            return null;
        }
        return reservationList;
    }

    public static Reservation getData(String reservationID) throws IOException, FileNotFoundException {
        String line;
        File fileName = new File("reservation.txt");
        boolean noRecord = true;
        if (!fileName.exists()) {
            fileName.createNewFile();

        }
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            String[] values = line.split(Pattern.quote("|"));
            if (values[0].equalsIgnoreCase(reservationID)) {
                Reservation reservation = new Reservation(values);
                return reservation;
            }
        }
        return null;
    }

}
