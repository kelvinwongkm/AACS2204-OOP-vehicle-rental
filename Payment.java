package vehiclerental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static vehiclerental.DateTime.DATEFORMAT;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateOptionRange;
import static vehiclerental.VehicleRental.askConfirmation;
import static vehiclerental.VehicleRental.getLatestID;
import static vehiclerental.VehicleRental.unsuccessfullyMsg;

/**
 *
 * @author Melvin Wong Wai Hung
 */
public abstract class Payment {

    private final String COMPANYADDRESS[] = new String[]{
        "Level 38, East Wing Menara KL",
        "Jalan Pantai Lama",
        "50600 Kuala Lumpur"};

    private final LocalDate ISSUEDATE;
    private Reservation reservation;
    private String paymentNo;
    private String invoiceNo;
    private final double GST = 0.06;
    private double subTotal;

    public abstract void paymentInfo();


    public Payment(String[] value) throws IOException {
        paymentNo = value[0];
        invoiceNo = value[1];
        this.reservation = Reservation.getData(value[3]);
        ISSUEDATE = LocalDate.parse(value[4]);
    }

    public Payment(Reservation reservation) {
        this.ISSUEDATE = LocalDate.now();
        this.reservation = reservation;
        setSubTotal(reservation.getVehicle().getRentInfo().getRentRate() * reservation.getRentalDuration());
    }

    public Reservation getReservation() {
        return reservation;
    }
    
    public LocalDate getISSUEDATE() {
        return ISSUEDATE;
    }


    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double calculatePaymentAmount() {
        return getSubTotal() * (1 + GST);
    }

    public void displayInvoice() throws IOException {
        setPaymentNo(String.format("P%04d", (getLatestID("payment.txt", 0) + 1)));
        setInvoiceNo("Invoice #" + (getLatestID("payment.txt", 0) + 1));

        System.out.printf("\n\n%57sINVOICE\n", "");
        System.out.printf("%17s--------------------------------------------------------------------------------------------\n", "");
        System.out.printf("%17s%s\n", "", COMPANYADDRESS[0]);
        System.out.printf("%17s%s\n", "", COMPANYADDRESS[1]);
        System.out.printf("%17s%s\n\n", "", COMPANYADDRESS[2]);

        System.out.printf("%17sBill To:\n", "");
        System.out.printf("%17s%-67sInvoice No.  : %10s\n", "", reservation.getRenter().getName(), getInvoiceNo());
        System.out.printf("%17s%-67sInvoice Date : %10s\n", "", reservation.getRenter().getAddress(), ISSUEDATE.format(DATEFORMAT));
        System.out.printf("%17s%-67sPayment No.  : %10s\n", "", reservation.getRenter().getPhoneNumber(), getPaymentNo());
        System.out.printf("%17s%-63s\n\n", "", reservation.getRenter().getEmail());

        System.out.printf("%17s+--------+------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|  No.   |  Description                                               |  Total: RM         |\n", "");
        System.out.printf("%17s+--------+------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|   1.   |  Reservation ID: %-42s|  %10.2f %7s|\n", "", reservation.getReservationID(), getSubTotal(), "");
        System.out.printf("%17s|        +------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|        |  Location: %-48s|  %18s|\n", "", reservation.getLocation(), "");
        System.out.printf("%17s|        +------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|        |  Start Date: %-15s End Date: %-15s   |  %18s|\n", "", reservation.getPickUp(), reservation.getDropOff(), "");
        System.out.printf("%17s|        +------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|        |  Rental Duration: %2s Day(s)%-32s|  %18s|\n", "", reservation.getRentalDuration(), "", "");
        System.out.printf("%17s|        +------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|        |  Vehicle Plate Number: %-36s|  %18s|\n", "", reservation.getVehicle().getVehicleBasic().getPlateNum(), "");
        System.out.printf("%17s|        +------------------------------------------------------------+--------------------+\n", "");
        System.out.printf("%17s|        |  Rent Rate per Day: RM%-37s|  %18s|\n", "", reservation.getVehicle().getRentInfo().getRentRate(), "");
        System.out.printf("%17s|--------+------------------------------------------------------------+--------------------+\n", "");

        System.out.printf("%87s| Subtotal: RM%7.2f|\n", "", getSubTotal());
        System.out.printf("%87s| Tax     : RM%7.2f|\n", "", getSubTotal() * GST);
        System.out.printf("%87s| Total   : RM%7.2f|\n", "", calculatePaymentAmount());
        System.out.printf("%87s+--------------------+\n\n", "");

        System.out.printf("%42s Thanks for reserving a vehicle with us!\n\n","");

    }

    public static int makePayment(Reservation reservation) throws IOException {
        //Print out invoice and prompt which payment method to be used
        int paymentMethod = 0;

        do {
            System.out.printf("\n%30s How would you like to pay your bill?\n","");
            System.out.printf("%30s 1. Card\n","");
            System.out.printf("%30s 2. E-Wallet\n","");
            paymentMethod = validateIntegerInput(
                    "Please enter the payment method that you like: ");
            System.out.println("");
        } while (!validateOptionRange(1, 2, paymentMethod));

        Payment pay = null;
        switch (paymentMethod) {
            case 1:
                pay = new Card(reservation);
                ((Card) pay).paymentInfo();
 
                break;
            case 2:
                pay = new EWallet(reservation);
                ((EWallet) pay).paymentInfo();
                break;
        }
        int confirm=askConfirmation("pay bill");
        if (confirm==1){
        pay.paymentSuccessfulMsg();
        }else{
        unsuccessfullyMsg("pay bill");
        }
        

        return paymentMethod;
    }

    public void paymentSuccessfulMsg() throws IOException {
        System.out.printf("\n\n%30s Transaction Status: Payment successful\n","");
        displayInvoice();
        Vehicle.appendSingleRecord("payment.txt", this.toString());
        
    }

    public static List<Payment> getData() throws IOException, FileNotFoundException {
        String line;
        List<Payment> paymentList = new ArrayList<>();
        File fileName = new File("payment.txt");
        boolean noRecord = true;
        if (!fileName.exists()) {
            fileName.createNewFile();

        }
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            String[] values = line.split(Pattern.quote("|"));
            paymentList.add(new Card(values));
            noRecord = false;
        }

        if (noRecord) {
            System.out.printf("%30s No record found\n","");
            return null;
        }
        return paymentList;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s",
                paymentNo, invoiceNo, reservation.getRenter().getRenterID(), reservation.getReservationID(), ISSUEDATE);
    }
}
