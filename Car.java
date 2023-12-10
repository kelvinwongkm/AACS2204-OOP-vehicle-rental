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
public class Car extends Vehicle {

    public Car() {
	super();
	super.setTypeList(super.DETAILEDTYPELIST[0]);
	
	super.setTransmissionList(new String[]{"Auto", "Manual", "Hybrid"});
	
	super.getRentInfo().setFeatureList(Arrays.asList(
		"Touch 'n Go", "Smart Tag", "Bluetooth", "GPS",
		"Child Seat", "Pet Friendly", "No Smoking", "Rear Vision Camera"));
    }

    public Car(String[] str) {
	super(str);
    }

    @Override
    public void addVehicle() throws IOException {
        Staff staff = new Staff();
	int id = getLatestID(getFileName(this),0) + 1;
	setVehicleID(String.format("%s%02d", "CA", id));
	super.addVehicleAndInfo("Car");

	if (validateConfirm("Do you confirm your input? (Y/N) : ")) {
	    Vehicle.appendSingleRecord(getFileName(this), super.toString());
            successfullyMsg("added a vehicle");
            staff.updateActivityPerformed(1);
	}else {
            unsuccessfullyMsg("added a vehicle");
        }
    }

}
