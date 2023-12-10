package vehiclerental;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static vehiclerental.GeneralValidation.validateConfirm;
import static vehiclerental.GeneralValidation.validateDoubleInput;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateOptionRange;
import static vehiclerental.GeneralValidation.validateStringInput;
import static vehiclerental.VehicleRental.printConstantList;
import static vehiclerental.VehicleRental.successfullyMsg;
import static vehiclerental.VehicleRental.unsuccessfullyMsg;

/**
 *
 * @author Wong Kah Ming
 */
public abstract class Vehicle implements Comparable<Vehicle>, VehicleRelated {

    private String vehicleID;
    private VehicleBasicInfo vehicleBasic;
    private double engineSize;
    private String transmission;
    private String fuelType;
    private int seats;
    private RentingInfo rentInfo;

    private String[] typeList = {};
    private String[] transmissionList = {};

    public abstract void addVehicle() throws IOException;

    public Vehicle() {
        vehicleBasic = new VehicleBasicInfo();
        rentInfo = new RentingInfo();
    }

    public Vehicle(String[] str) {
        vehicleID = str[0];
        vehicleBasic = new VehicleBasicInfo(str[1], str[2], str[3], str[4], Integer.parseInt(str[5]));
        engineSize = Double.parseDouble(str[6]);
        transmission = str[7];
        fuelType = str[8];
        seats = Integer.parseInt(str[9]);

        String[] newStr = new String[5];
        System.arraycopy(str, 10, newStr, 0, newStr.length);
        rentInfo = new RentingInfo(newStr);

    }

    public void setTypeList(String[] typeList) {
        this.typeList = typeList;
    }

    public void setTransmissionList(String[] transmissionList) {
        this.transmissionList = transmissionList;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleBasicInfo getVehicleBasic() {
        return vehicleBasic;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public RentingInfo getRentInfo() {
        return rentInfo;
    }

    public void addVehicleAndInfo(String vehicleType) {
        int selectedOption;

        System.out.printf("\n%37s Vehicle Registration - %s\n", "", vehicleType);
        System.out.printf("%30s %35s\n\n", "", "=========================================");

        //get platenum
        String enteredPlateNum = validateStringInput(
                String.format("%s plate number : ", vehicleType));
        vehicleBasic.setPlateNum(enteredPlateNum);
        System.out.println("");
        // get make
        String enteredMake = validateStringInput(
                String.format("%s Make/Brand : ", vehicleType));
        vehicleBasic.setMake(enteredMake);
        System.out.println("");
        //get model
        String enteredModel = validateStringInput(
                String.format("%s model : ", vehicleType));
        vehicleBasic.setModel(enteredModel);
        System.out.println("");
        // get type
        do {
            System.out.printf("%30s %s type : \n", "", vehicleType);
            printConstantList(typeList);
            selectedOption = validateIntegerInput(String.format("\n%30s Please select your %s type : ", "",vehicleType.toLowerCase()));
        } while (!validateOptionRange(0, typeList.length, selectedOption));
        vehicleBasic.setType(typeList[selectedOption - 1]);
        System.out.println("");
        // get year purchase
        int yearPurchase = validateIntegerInput("Year Purchase : ");
        vehicleBasic.setYear(yearPurchase);
        System.out.println("");
        // get seats
        int availableSeats = validateIntegerInput("Available Seats : ");
        setSeats(availableSeats);
        System.out.println("");
        // get engine size
        double enteredEngineSize = validateDoubleInput("Engine Size : ");
        setEngineSize(enteredEngineSize);
        System.out.println("");
//get fuel tyype
        do {
            System.out.println(String.format("%30s Fuel Type : ", ""));
            printConstantList(FUELTYPEOPTION);

            selectedOption = validateIntegerInput(String.format("\n%30s Please select your %s's fuel type : ","", vehicleType.toLowerCase()));

        } while (!validateOptionRange(1, FUELTYPEOPTION.length, selectedOption));
        setFuelType(FUELTYPEOPTION[selectedOption - 1]);
        System.out.println("");
        //get transmission type
        do {
            System.out.println(String.format("%30s Transmission type : ", ""));
            printConstantList(transmissionList);

            selectedOption = validateIntegerInput(String.format("\n%30s Please select your %s's transmission type : ","", vehicleType.toLowerCase()));

        } while (!validateOptionRange(1, transmissionList.length, selectedOption));
        setTransmission(transmissionList[selectedOption - 1]);

        System.out.println("");
        rentInfo.addRentInfo();

        System.out.printf("\n%45s Preview\n", "");
        System.out.printf("%30s %35s\n\n", "", "=========================================");
        viewVehicleInfoFullVersion();

    }

    public void viewVehicleInfoFullVersion() {
        System.out.printf("\n%15s  -------------------------------------------------------------------------------------\n", "");
        System.out.printf("%15s  %12s %-25s | %12s %-28s \n", "", "", "Vehicle Info", "", "Rent Info");
         System.out.printf("%15s  -------------------------------------------------------------------------------------\n", "");
        System.out.printf("%15s  %-20s : %-15s | %-15s : %-23s \n", "",
                "Vehicle ID", getVehicleID(), "Location", rentInfo.getLocation());
        System.out.printf("%15s  %-20s : %-15s | %-15s : RM %-20.2f \n", "",
                "Plate Number", vehicleBasic.getPlateNum(), "Rent Rate", rentInfo.getRentRate());
        System.out.printf("%15s  %-20s : %-15s | %-15s : ", "",
                "Vehicle Type", vehicleBasic.getType(), "Feature", rentInfo.getAvailableFeature().toString().replace("[", "").replace("]", ""));

        for (int i = 0; i < rentInfo.getAvailableFeature().size(); i++) {
            System.out.print(rentInfo.getAvailableFeature().get(i).trim());

            if (i + 1 == rentInfo.getAvailableFeature().size()) {
                break;
            } else if (((i + 1) % 2) != 0) {
                System.out.print(", ");
            } else {
                System.out.printf(", \n%30s |%-20s : %-15s   ", "", "", "");
            }
        }
        System.out.println("");

          System.out.printf("%15s  %-20s : %-15s | %-15s : ", "",
                "Vehicle Make", vehicleBasic.getMake(), "Available Day");

        for (int i = 0; i < rentInfo.getAvailableStartDate().size(); i++) {
            System.out.print(LocalDate.parse(rentInfo.getAvailableStartDate().get(i).trim()).format(DateTime.DATEFORMAT)
                    + " - " + LocalDate.parse(rentInfo.getAvailableEndDate().get(i).trim()).format(DateTime.DATEFORMAT));

            if (rentInfo.getAvailableStartDate().size() != (i + 1)) {
                System.out.print(", ");
                System.out.printf("\n%30s %50s | %-23s   ", "", "", "");
            }
        }
        System.out.println("");

        System.out.printf("%15s  %-20s : %-15s | %41S \n", "", "Vehicle Model", vehicleBasic.getModel(), "");
        System.out.printf("%15s  %-20s : %-15d | %41S \n", "", "Year Purchase", vehicleBasic.getYear(), "");
        System.out.printf("%15s  %-20s : %-15.2f | %41S \n", "", "Engine Size", engineSize, "");
        System.out.printf("%15s  %-20s : %-15s | %41S \n", "", "Transmission", transmission, "");
        System.out.printf("%15s  %-20s : %-15s | %41S \n", "", "Fuel Type", vehicleBasic.getType(), "");
        System.out.printf("%15s  %-20s : %-15d | %41S \n", "", "Seats", seats, "");
        System.out.printf("%15s  -------------------------------------------------------------------------------------\n", "");
    }

    public void viewVehicleInfoReservationVersion() {

        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s | Vehicle ID         : %-20s | Rent Rate    : RM %-32.2f |\n",
                "", vehicleID, rentInfo.getRentRate());
        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s | Vehicle Make       : %-20s | Feature      : ",
                "", vehicleBasic.getMake());

        String temp = "";
        for (int i = 0, counter = 1; i < rentInfo.getAvailableFeature().size(); i++, counter++) {
            temp += rentInfo.getAvailableFeature().get(i).trim();
            if (counter % 2 == 0) {
                System.out.printf("%-35s |", temp);
                temp = "";
            } else if (i + 1 == rentInfo.getAvailableFeature().size()) {
                break;
            } else if (((i + 1) % 2) != 0) {
                temp += ", ";
            } else {
                System.out.printf("\n%15s | %41s | %-12s   ", "", "", "");
            }
        }
        System.out.println("");

        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s | Vehicle Type       : %-20s | Engine Size  : %-35.2f |\n",
                "", vehicleBasic.getType(), engineSize);
        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s | Vehicle Model      : %-20s | Transmission : %-35s |\n",
                "", vehicleBasic.getModel(), transmission);
        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");
        System.out.printf("%15s | Year Purchase      : %-20d | Fuel Type    : %-35s |\n",
                "", vehicleBasic.getYear(), fuelType.trim());
        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");

        System.out.printf("%15s | Seats              : %-20d |                                                    |\n",
                "", seats);
        System.out.printf("%15s +-------------------------------------------+----------------------------------------------------+\n", "");

    }

    public  int updateVehicleMenu() {
        int number;

        do {
            System.out.printf("\n%37s %15s\n", " ", "Staff");
            System.out.printf("%30s %35s\n\n", "", "=========================================");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-7s |\n", "", "", "1. Vehicle details", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-7s |\n", "", "", "2. Renting details", "");
            System.out.printf("%30s %35s\n", "", "+---------------------------------------+");
            System.out.printf("%30s | %10s %-12s %-13s |\n", "", "", "0. Return", "");
            System.out.printf("%30s %35s\n\n", "", "+---------------------------------------+");
            System.out.printf("%30s %10s\n", "", "Please select the category that you want to update.");
            number = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
        } while (!validateOptionRange(0, 2, number));

        return number;
    }

    public void viewVehicle(Object o) throws IOException {
        Staff staff = new Staff();
        boolean validInput = false;
        String input = new String();
        int counter;

        List<Vehicle> vehicleList = previewVehicleRecord(o);

        if (vehicleList == null) {
            System.out.printf("%30s No record founded\n", "");
            return;
        }

        do {
            System.out.println("");
            validInput = false;
            input = validateStringInput("Please enter the vehicle id you want to view : ");

            for (counter = 0; counter < vehicleList.size(); counter++) {
                if (vehicleList.get(counter).getVehicleID().equalsIgnoreCase(input)) {
                    vehicleList.get(counter).viewVehicleInfoFullVersion();
                    validInput = true;
                    break;
                }
            }

            if (!validInput) {
                System.out.printf("%30s Invalid vehicle id entered. Please enter again\n", "");
            }
        } while (!validInput);

        switch (Staff.updateDeleteVehicle()) {
            case 1:
                updateVehicle(vehicleList, vehicleList.get(counter));
                break;
            case 2:
                if (validateConfirm("Are you confirm to delete selected vehicle info? (Y/N) : ")) {
                    vehicleList.remove(counter);
                    Reservation.writeFile(getFileName(o), vehicleList);
                    successfullyMsg("delete selected vehicle info");
                    staff.updateActivityPerformed(2);
                }else{
                    unsuccessfullyMsg("delete selected vehicle info");
                }
                break;
        }

        return;
    }

    public static List<Vehicle> previewVehicleRecord(Object o) {
        List<Vehicle> vehicleList = getData(o);
        if (vehicleList == null) {
            System.out.printf("%30s No record founded\n", "");
            return null;
        }

        System.out.printf("%15s     -----------------------------------------------------------------------------------\n", "");
        for (int i = 0; i < vehicleList.size(); i++) {
            System.out.printf("%15s %02d. | %-15s : %-15s | %-20s : %-20s |\n", "",
                    i + 1, "Vehicle ID", vehicleList.get(i).getVehicleID(),
                    "Model", vehicleList.get(i).getVehicleBasic().getModel());
            System.out.printf("%15s     | %-15s : %-15s | %-20s : %-20s |\n", "",
                    "Make", vehicleList.get(i).getVehicleBasic().getMake(),
                    "Type", vehicleList.get(i).getVehicleBasic().getType());
            System.out.printf("%15s     | %-15s : %-15s | %-20s : RM %-17.2f |\n", "",
                    "Year Purchase", vehicleList.get(i).getVehicleBasic().getYear(),
                    "Rent Rate (per day)", vehicleList.get(i).getRentInfo().getRentRate());
            System.out.printf("%15s     -----------------------------------------------------------------------------------\n", "");
        }

        return vehicleList;
    }

    public void updateVehicle(List<Vehicle> vehicleList, Vehicle selectedVehicle) throws IOException {

        int selection = 0;
        do {
            boolean updateConfirm = false;
            selection = updateVehicleMenu();

            switch (selection) {

                case 1:
                    updateConfirm = updateVehicleDetails(selectedVehicle);
                    break;

                case 2:
                    updateConfirm = rentInfo.updateRentInfo(selectedVehicle.getRentInfo());
                    break;

                case 0:
                    return;
            }

            if (updateConfirm) {
                Reservation.writeFile(getFileName(selectedVehicle), vehicleList);
            }
        } while (selection != 0);
    }

    public boolean updateVehicleDetails(Vehicle data) {

        int selection;
        boolean updateConfirm = false;
        Staff staff = new Staff();
        System.out.printf("\n\n%15s ---------------------------Update Vehicle Details------------------------------\n", "");
        System.out.printf("%15s | %-23s | %-23s | %-23s |\n", "", "1. Plate Number", "2. Type", "3. Make/Brand");
        System.out.printf("%15s -------------------------------------------------------------------------------\n", "");
        System.out.printf("%15s | %-23s | %-23s | %-23s |\n", "", "4. Model", "5. Year", "6. Engine Size");
        System.out.printf("%15s -------------------------------------------------------------------------------\n", "");
        System.out.printf("%15s | %-23s | %-23s | %-23s |\n", "", "7. Transmission", "8. Fuel Type", "9. Seats");
        System.out.printf("%15s -------------------------------------------------------------------------------\n", "");
        System.out.printf("%15s | %-23s   %-23s   %-23s |\n", "", "0. Return", "", "");
        System.out.printf("%15s -------------------------------------------------------------------------------\n\n", "");

        do {
            selection = validateIntegerInput(String.format("\n%30s %10s", "", "Your Selection |> "));
            System.out.println("");
        } while (!validateOptionRange(0, 9, selection));

        switch (selection) {

            case 0:
                return false;

            case 1:

                System.out.printf("%30s Old Plate Bumber of your vehicle : %s\n", "", data.vehicleBasic.getPlateNum());
                String plateNumber = validateStringInput(
                        "Please enter new Plate Bumber :  ");
                System.out.printf("%30s Your new Plate Bumber is : %s\n", "", plateNumber);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.vehicleBasic.setPlateNum(plateNumber);
                    successfullyMsg("updated plate number");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated plate number");
                }

                break;

            case 2:
                int newType;
                do {
                    System.out.printf("%30s Old Type of your vehicle : %s\n", "", data.vehicleBasic.getType());
                    printConstantList(typeList);

                    newType = validateIntegerInput("Please enter new Type :  ");
                } while (!validateOptionRange(1, typeList.length, newType));
                String selectedType = typeList[newType - 1];
                System.out.printf("%30s Your new Type is : %s\n", "", selectedType);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.vehicleBasic.setType(selectedType);
                    successfullyMsg("updated vehicle type");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated vehicle type");
                }

                break;

            case 3:

                System.out.printf("%30s Old Make/Brand of your vehicle : %s\n", "", data.vehicleBasic.getMake());
                String newBrand = validateStringInput("Please enter new Make/Brand :  ");
                System.out.printf("%30s Your new Make/Brand is : %s\n", "", data.vehicleBasic.getMake());
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.vehicleBasic.setMake(newBrand);
                    successfullyMsg("updated vehicle make/brand");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated vehicle make/brand");
                }
                break;

            case 4:

                System.out.printf("%30s Old Model of your vehicle : %s\n", "", data.vehicleBasic.getModel());
                String newModel = validateStringInput("Please enter new Model :  ");

                System.out.printf("%30s Your new Model is : %s\n", "", newModel);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.vehicleBasic.setModel(newModel);
                    successfullyMsg("updated vehicle model");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated vehicle model");
                }
                break;

            case 5:

                System.out.printf("%30s Old Year of Purchase of your vehicle : %s\n", "", data.vehicleBasic.getYear());
                int newYear = validateIntegerInput("Please enter new Year of Purchase :  ");
                System.out.printf("%30s Your new Year of Purchase is : %s\n", "", newYear);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.vehicleBasic.setYear(newYear);
                    successfullyMsg("updated vehicle year of purchase");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated vehicle year of purchase");
                }

                break;

            case 6:

                System.out.printf("%30s Old Engine Size of your vehicle : %s\n", "", data.getEngineSize());
                double newEngineSize = validateDoubleInput("Please enter new Engine Size :  ");

                System.out.printf("%30s Your new Engine Size is : %s\n", "", data.getEngineSize());
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.setEngineSize(newEngineSize);
                    successfullyMsg("updated engine size");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated engine size");
                }
                break;

            case 7:
                int newTransmission;
                do {
                    System.out.printf("%30s Old Transmission Type of your vehicle : %s\n", "", data.getTransmission());
                    printConstantList(transmissionList);

                    newTransmission = validateIntegerInput("Please enter new Transmission Type :  ");
                } while (!validateOptionRange(1, transmissionList.length, newTransmission));

                String selectedTransmission = transmissionList[newTransmission - 1];
                System.out.printf("%30s Your new Transmission Type is : %s\n", "", selectedTransmission);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.setTransmission(selectedTransmission);
                    successfullyMsg("updated transmission");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated transmission");
                }
                break;

            case 8:
                int newFuelType = 0;

                do {
                    System.out.printf("%30s Old Fuel Type of your vehicle : %s\n", "", data.getFuelType());
                    printConstantList(FUELTYPEOPTION);

                    newFuelType = validateIntegerInput("Please enter new Fuel Type :  ");
                } while (!validateOptionRange(1, FUELTYPEOPTION.length, newFuelType));

                String selectedFuelType = FUELTYPEOPTION[newFuelType - 1];

                System.out.printf("%30s Your new Fuel Type is : %s\n", "", selectedFuelType);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.setFuelType(selectedFuelType);
                    successfullyMsg("updated fuel type");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated fuel type");
                }

                break;

            case 9:

                System.out.printf("%30s Old Seats Available of your vehicle : %s\n", "", data.getSeats());

                int newSeats = validateIntegerInput("Please enter new Seats Available :  ");
                System.out.printf("%30s Your new Seats Available is : %s\n", "", newSeats);
                updateConfirm = validateConfirm("Do you confirm your changes? (Y/N) : ");
                if (updateConfirm) {
                    data.setSeats(newSeats);
                    successfullyMsg("updated seats");
                    staff.updateActivityPerformed(3);
                } else {
                    unsuccessfullyMsg("updated seats");
                }

                break;

        }
        return updateConfirm;

    }

    public static String getFileName(Object o) {

        if (o instanceof Car) {
            return "car.txt";

        } else if (o instanceof Motorbike) {
            return "motobike.txt";

        } else if (o instanceof RecreationalVehicle) {
            return "recreationalvehicle.txt";
        }

        return null;
    }

    public static void appendSingleRecord(String fileName, String str1) throws IOException {
        try {
            File fn = new File(fileName);
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(fn, true))) {
                if (!fn.exists()) {
                    fn.createNewFile();
                    // create new file if not exist
                }
                bf.write(str1 + "\n");
                bf.flush();
                bf.close();
            }

//            System.out.printf("%30s Your data have been stored.\n","");
        } catch (HeadlessException | IOException e) {
            System.out.printf("%30s Error occur while writting file to %s\n", "", fileName);
        }
    }

    public static List<Vehicle> getData(Object o) {
        String line;
        List<Vehicle> vehicleList = new ArrayList<>();
        File fileName = new File(getFileName(o));
        boolean noRecord = true;
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(Pattern.quote("|"));
                    if (o instanceof Car) {
                        vehicleList.add(new Car(values));
                    } else if (o instanceof Motorbike) {
                        vehicleList.add(new Motorbike(values));
                    } else {
                        vehicleList.add(new RecreationalVehicle(values));
                    }

                    noRecord = false;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (noRecord) {
            System.out.printf("%30s No record found\n", "");
            return null;
        }
        return vehicleList;
    }

    public static Vehicle getData(Object o, String vehicleID) throws FileNotFoundException, IOException {
        String line;
        Vehicle vehicle;
        File fileName = new File(getFileName(o));
        boolean noRecord = true;
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            String[] values = line.split(Pattern.quote("|"));

            if (o instanceof Car) {
                vehicle = new Car(values);
            } else if (o instanceof Motorbike) {
                vehicle = new Motorbike(values);
            } else {
                vehicle = new RecreationalVehicle(values);
            }

            if (vehicle.getVehicleID().equalsIgnoreCase(vehicleID)) {
                return vehicle;
            }

            noRecord = false;
        }

        if (noRecord) {
            System.out.printf("%30s No record found\n", "");
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%f|%s|%s|%d|%s",
                vehicleID, vehicleBasic.toString(), engineSize, transmission, fuelType, seats, rentInfo.toString());
    }

    @Override
    public int compareTo(Vehicle that) {
        if (this.rentInfo.getRentRate() > that.rentInfo.getRentRate()) {
            return 1;
        } else if (this.rentInfo.getRentRate() < that.rentInfo.getRentRate()) {
            return -1;
        }
        return 0;

    }
}
