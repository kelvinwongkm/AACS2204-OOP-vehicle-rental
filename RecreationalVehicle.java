/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;

import java.io.IOException;
import java.util.Arrays;
import static vehiclerental.GeneralValidation.validateConfirm;
import static vehiclerental.VehicleRental.getLatestID;

/**
 *
 * @author kelvin
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

	int id = getLatestID(getFileName(this),0) + 1;
	setVehicleID(String.format("%s%02d", "RV", id));

	super.addVehicleAndInfo("RV");

	if (validateConfirm("Do you confirm your input? (Y/N) : ")) {

	    Vehicle.appendSingleRecord(getFileName(this), super.toString());

	}
    }

}
