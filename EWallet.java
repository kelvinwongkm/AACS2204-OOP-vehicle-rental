package vehiclerental;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateStringInput;

/**
 *
 * @author Melvin
 */
public class EWallet extends Payment {

    private String eWalletTelNo;
    private String eWalletType;
    private String eWalletPIN;
    private String eWalletPayerName;

    public EWallet(Reservation reservation) {
        super(reservation);
    }

    @Override
    public void paymentInfo() {
        System.out.printf("\n%30s Please enter the information below:-\n", "");
        int eWalletSelection;
        do {
            System.out.printf("%30s Which E-Wallet platform are you using?\n", "");
            System.out.printf("%30s 1. Touch 'n Go\n", "");
            System.out.printf("%30s 2. GrabPay\n", "");
            System.out.printf("%30s 3. Boost\n", "");
            eWalletSelection = validateIntegerInput(
                    "Please enter your selection: ");
        } while (!assignEWalletType(eWalletSelection));
        
        System.out.println("");
        //get payer name
        eWalletPayerName = validateStringInput(
                "Please enter E-Wallet account's owner name: ");

        System.out.println("");
        do {
            eWalletTelNo = validateStringInput(
                    "Please enter E-Wallet account's owner phone number: ");
        } while (!verifyEWalletPhoneNum(eWalletTelNo));

        System.out.println("");
        //not sure whether able to display "******" to avoid the actual PIN from being revealed
        do {
            eWalletPIN = validateStringInput(
                    "Please enter the E-Wallet verification PIN to complete your transaction: ");
        } while (!verifyEWalletPIN(eWalletPIN));

    }

    public boolean assignEWalletType(int eWalletSelection) {
        switch (eWalletSelection) {
            case 1:
                eWalletType = "Touch 'n Go";
                break;
            case 2:
                eWalletType = "GrabPay";
                break;
            case 3:
                eWalletType = "Boost";
                break;
            default:
                System.out.printf("%30s Invalid number entered. Please try again.\n\n","");
                return false;
        }

        return true;
    }

    public boolean verifyEWalletPIN(String eWalletPIN) {
        if (eWalletPIN.length() != 6) {
            System.out.printf("%30s E-Wallet PIN must have only 6 digits! Please try again.\n\n","");
            return false;
        } else {
            for (int i = 0; i < eWalletPIN.length(); i++) {
                if (!Character.isDigit(eWalletPIN.charAt(i))) {
                    System.out.printf("%30s E-Wallet PIN should only digits! Please try again.\n\n","");

                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verifyEWalletPhoneNum(String input) {
        String regex = "^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches() == false) {
            System.out.printf("%30s Invalid Input.\n\n","");
        }
        return matcher.matches();
    }

}
