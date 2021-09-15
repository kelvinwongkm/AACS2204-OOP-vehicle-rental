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
public class VehicleBasicInfo {

    private String plateNum;
    private String type;
    private String make;
    private String model;
    private int year;

    public VehicleBasicInfo() {
    }

    public VehicleBasicInfo(String plateNum, String type, String make, String model, int year) {
	this.plateNum = plateNum;
	this.type = type;
	this.make = make;
	this.model = model;
	this.year = year;
    }

    public String getPlateNum() {
	return plateNum;
    }

    public void setPlateNum(String plateNum) {
	this.plateNum = plateNum;
    }


    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getMake() {
	return make;
    }

    public void setMake(String make) {
	this.make = make;
    }

    public String getModel() {
	return model;
    }

    public void setModel(String model) {
	this.model = model;
    }

    public int getYear() {
	return year;
    }

    public void setYear(int year) {
	this.year = year;
    }

    @Override
    public String toString() {
	return String.format("%s|%s|%s|%s|%d", plateNum,type, make, model, year);
    }
}
