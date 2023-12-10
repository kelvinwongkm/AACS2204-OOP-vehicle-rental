package vehiclerental;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Wong Kah Ming
 */


public class DateTime {

    public static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private LocalDate date;
    private LocalTime time;

    public DateTime() {
    }

    public DateTime(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public static LocalDate setRequiredDate(String requestMessage, LocalDate cmpDate, int minAvailableDay) {
        LocalDate wantedDate;
        Scanner scanner = new Scanner(System.in);
        String string;

        do {
            do {
                System.out.print(String.format("%30s %s","", requestMessage));
                string = scanner.nextLine();
            } while (!validDate(string));

            wantedDate = LocalDate.parse(string, DateTime.DATEFORMAT);

            if (dateDiffInDays(cmpDate, wantedDate) < minAvailableDay) {
                System.out.printf("\n%30s Insufficient duration, please make sure is more than %d day from %s\n\n","", minAvailableDay, cmpDate.toString());
            }
        } while (dateDiffInDays(cmpDate, wantedDate) < minAvailableDay);

        return wantedDate;
    }

    public static void updateDate(List<String> storedStartDate, List<String> storedEndDate, LocalDate startDate, LocalDate endDate) {
        int originalSize = storedStartDate.size();

        for (int y = 0; y < originalSize; y++) {

            if ((startDate.compareTo(LocalDate.parse(storedStartDate.get(y).trim())) > 0)
                    && (endDate.compareTo(LocalDate.parse(storedEndDate.get(y).trim())) < 0)) {

                if (y != 0 && y + 1 != originalSize) {
                    storedStartDate.add(y, endDate.plusDays(1).toString());

                    storedEndDate.add(startDate.plusDays(1).toString());

                } else {
                    storedStartDate.add(endDate.plusDays(1).toString());

                    storedEndDate.add(y, startDate.plusDays(1).toString());
                }

                break;
            }

        }

    }

    public static void updateDate(List<String> storedStartDate, List<String> storedEndDate, Reservation reservation) {
        int originalSize = storedStartDate.size();

        for (int y = 0; y < originalSize; y++) {

            if ((reservation.getPickUp().getDate().minusDays(1)
                    .equals(LocalDate.parse(storedEndDate.get(y).trim())))) {
                storedEndDate.remove(y);

            }

            if ((reservation.getDropOff().getDate().plusDays(1)
                    .equals(LocalDate.parse(storedStartDate.get(y).trim())))) {
                storedStartDate.remove(y);

            }

        }

    }

    public static int dateDiffInDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static String displayDateTime(String pickUpDate, String pickUpTime, String dropOfDate, String dropOfTime) {
        return pickUpDate + ", " + pickUpTime + " - " + dropOfDate + ", " + dropOfTime;
    }

    public static boolean validDate(String date) {
        try {
            LocalDate.parse(date, DATEFORMAT);
        } catch (DateTimeParseException e) {
            System.out.printf("%30s Invalid date / date format. Please enter again\n\n","");
            return false;
        }

        return true;
    }

    public static boolean validTime(String time) {
        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            System.out.printf("%30s Invalid time / time format. Please enter again\n\n","");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("%s|%s", date.toString(), time.toString());
    }
}
