package vehiclerental;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Report implements VehicleRelated {

    private static int totalVehiclesReserved[] = new int[3];   //1 is car, 2 is motorbike, 3 is RV (stands for total cars reserved for that category)
    private static int vehiclesReserved[][] = new int[3][6];
    private static double revenueEarned[] = new double[3];

    public void displayReport() throws IOException {
        Calendar cal = Calendar.getInstance();
        System.out.printf("%30sVehicle Type Ranking Report in %s 2021\n", "", new SimpleDateFormat("MMMM").format(cal.getTime()));
        System.out.printf("%5s-------------------------------------------------------------------------------------------------\n", "");
        System.out.printf("%5s%-35s %-42s %-20s\n", "", "Vehicle Type", "No. of Vehicles Reserved", "Revenue Earned(RM)");
        System.out.printf("%5s-------------------------------------------------------------------------------------------------", "");

        for (int i = 0; i < 3; i++) {
            System.out.printf("\n%5s%-35s %24d %18s %17.2f\n", " ", VEHICLETYPE[i], totalVehiclesReserved[i], " ", revenueEarned[i]);
            System.out.printf("%5s--------------------%36s --- %18s------------------\n", " ", " ", " ");

            for (int j = 0; j < 6; j++) { //the first condition is to not show the vehicle type if 0 reserved
                if (vehiclesReserved[i][j] != 0 || !(i == 2 && j == 5)) {   //remove the comment to test out after done assigning vehiclesReserved values method
                    System.out.printf("%5s%-35s %24d\n", " ", DETAILEDTYPELIST[i][j], vehiclesReserved[i][j]);
                    //}
                }
            }
        }
        System.out.println(java.time.LocalDate.now());

    }

    public void displayChart() throws IOException {
        Calendar cal = Calendar.getInstance();
        System.out.printf("\n\n%20sSimple Bar Chart for Vehicle Type Ranking Report in %s 2021\n\n", "", new SimpleDateFormat("MMMM").format(cal.getTime()));
        System.out.printf("%-11s No. of Vehicles Reserved\n", "");
        System.out.printf("%14s/|\\\n", " ");

        int maxValue = bigger(
                bigger(totalVehiclesReserved[0],
                        totalVehiclesReserved[1]),
                totalVehiclesReserved[2]) + 2;
        int temp = maxValue;

        for (int i = 0; i < temp; i++) {  //Rows
            System.out.printf("%11s %2d |     ", "", maxValue);

            for (int j = 0; j < 3; j++) {     //Columns
                if (totalVehiclesReserved[j] >= maxValue) {
                    System.out.printf("* * *              ");
                } else {
                    System.out.printf("                   ");
                }
            }

            maxValue--;
            System.out.printf("\n", ""); //move on to next row
        }

        System.out.printf("%15s+--------------------------------------------------------------> Vehicle Type\n", "");
        System.out.printf("%14s        Car             Motorbike       Recreational Vehicle\n", "");
    }

    public void assignVehiclesReserved() throws IOException {
        //read from txt file and initialize the vehiclesReserved array
        //add up the values in vehiclesReserved[0][j] to totalVehiclesReserved accordingly

        //read file method
        List<Payment> paymentRecord = Payment.getData();
        List<Reservation> reservationRecord = new ArrayList<>();

        for (Payment payment : paymentRecord) {
            if (payment.getISSUEDATE().getMonth().equals(LocalDate.now().getMonth())
                    && payment.getISSUEDATE().getYear() == (LocalDate.now().getYear())) {
                reservationRecord.add(payment.getReservation());
            }
        }

        for (Reservation reservation : reservationRecord) {
            if (reservation == null) {
                break;
            }

            if (reservation.getVehicle() instanceof Car) {
                double subtotal = reservation.getRentalDuration() * reservation.getVehicle().getRentInfo().getRentRate();

                totalVehiclesReserved[0]++;
                revenueEarned[0] += subtotal;
                for (int i = 0; i < DETAILEDTYPELIST[0].length; i++) {
                    if (DETAILEDTYPELIST[0][i]
                            .equals(reservation.getVehicle()
                                    .getVehicleBasic().getType())) {
                        vehiclesReserved[0][i]++;
                    }
                }

            } else if (reservation.getVehicle() instanceof Motorbike) {
                double subtotal = reservation.getRentalDuration() * reservation.getVehicle().getRentInfo().getRentRate();

                totalVehiclesReserved[1]++;
                revenueEarned[1] += subtotal;
                for (int i = 0; i < DETAILEDTYPELIST[1].length; i++) {
                    if (DETAILEDTYPELIST[1][i]
                            .equals(reservation.getVehicle()
                                    .getVehicleBasic().getType())) {
                        vehiclesReserved[1][i]++;
                    }
                }

            } else if (reservation.getVehicle() instanceof RecreationalVehicle) {
                double subtotal = reservation.getRentalDuration() * reservation.getVehicle().getRentInfo().getRentRate();

                totalVehiclesReserved[2]++;
                revenueEarned[2] += subtotal;

                for (int i = 0; i < DETAILEDTYPELIST[2].length; i++) {
                    if (DETAILEDTYPELIST[2][i]
                            .equals(reservation.getVehicle()
                                    .getVehicleBasic().getType())) {
                        vehiclesReserved[2][i]++;
                    }
                }
            }
        }
    }

    public int bigger(int a, int b) {
        return (a > b) ? a : b;
    }

}
