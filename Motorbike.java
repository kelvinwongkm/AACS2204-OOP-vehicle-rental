package vehiclerental;

import java.io.IOException;
import java.util.Arrays;
import static vehiclerental.GeneralValidation.validateConfirm;
import static vehiclerental.VehicleRental.getLatestID;
import static vehiclerental.VehicleRental.successfullyMsg;
import static vehiclerental.VehicleRental.unsuccessfullyMsg;

/**
 *
 * @author Wong Kah Ming
 */
public class Motorbike extends Vehicle {

    public Motorbike() {
	super();
	super.setTypeList(new String[]{
	    "Standard", "Touring", "Scooters", 
	    "Mopeds", "Enduro", "Others"});	
        super.setTypeList(super.DETAILEDTYPELIST[1]);
	
	super.setTransmissionList(new String[]{"Auto", "Semi-Auto", "Manual"});
	
	super.getRentInfo().setFeatureList(Arrays.asList(
		"Phone Mount/Cradle", "Hand Grips", "Crash Bars", "Exhaust",
		"USB Charging", "Tank Bag/Box", "Tail Bag/Box", "Saddle Bag/Box"));
    }

    public Motorbike(String[] str) {
	super(str);
    }

    @Override
    public void addVehicle() throws IOException {
        Staff staff = new Staff();
        int id = getLatestID(getFileName(this),0) + 1;
	setVehicleID(String.format("%s%02d", "MB", id));

	super.addVehicleAndInfo("Motorbike");

	if (validateConfirm("Do you confirm your input? (Y/N) : ")) {
	    Vehicle.appendSingleRecord(getFileName(this), super.toString());
            successfullyMsg("added a motorbike");
            staff.updateActivityPerformed(1);
	}else{
            unsuccessfullyMsg("added a motorbike");
    }
    }

}
