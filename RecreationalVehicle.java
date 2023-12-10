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
public class RecreationalVehicle extends Vehicle {

    public RecreationalVehicle() {
	super();
	super.setTypeList(super.DETAILEDTYPELIST[2]);
	
	super.setTransmissionList(new String[]{"Auto", "Manual"});
	
	super.getRentInfo().setFeatureList(Arrays.asList(
		"Air Conditioning", "Ceiling Fan", "Refigerator",
		"Microwave", "Kitchen Sink", "Water Tank", "TV",
		"WIFI", "Pet-friendly", "Smoking allowed"));
    }

    public RecreationalVehicle(String[] str) {
	super(str);
    }

    @Override
    public void addVehicle() throws IOException {
        Staff staff = new Staff();
	int id = getLatestID(getFileName(this),0) + 1;
	setVehicleID(String.format("%s%02d", "RV", id));

	super.addVehicleAndInfo("RV");

	if (validateConfirm("Do you confirm your input? (Y/N) : ")) {

	    Vehicle.appendSingleRecord(getFileName(this), super.toString());
            successfullyMsg("added a recreational vehicle");
            staff.updateActivityPerformed(1);
	}else{
            unsuccessfullyMsg("added a recreational vehicle");
        }
    }

}
