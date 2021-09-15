/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclerental;

/**
 *
 * @author kelvin
 */
public interface VehicleRelated {

    final String[] VEHICLETYPE = {
        "Car", "Motorbike", "Recreational Vehicle"
    };
    final String[][] DETAILEDTYPELIST = new String[][]{
        {"Sedan", "Coupe", "SUV", "Minivan", "Pickup Truck", "Others"}, //Car types
        {"Standard", "Touring", "Scooters", "Mopeds", "Enduro", "Others"}, //Motorbike types
        {"Class A RVs", "Class B RVs", "Class C RVs", "Truck Campers", "Others"}}; //RV types
    
    final String[] FUELTYPEOPTION = {
        "Diesel", "Petrol", "Electronic"
    };

}
