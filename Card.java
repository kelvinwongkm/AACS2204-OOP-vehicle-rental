package vehiclerental;


import java.io.IOException;
import java.util.Calendar;
import static vehiclerental.GeneralValidation.validateIntegerInput;
import static vehiclerental.GeneralValidation.validateStringInput;

/**
 *
 * @author Melvin
 */
public class Card extends Payment {

    private String cardType;
    private String cardNumber;
    private String cardExpiryYearAndMonth;
    private int cVVNumber;
    private String cardHolderName;

    public Card(Reservation reservation) {
	super(reservation);
    }

    public Card(String[] value) throws IOException {
        super(value);
    }

    public String getCardType() {
	return cardType;
    }

    @Override
    public void paymentInfo() {
	System.out.printf("\n%30s Please enter the information below:-\n","");
	int cardSelection;
	do {
	    System.out.printf("%30s What type of card are you using?\n","");
	    System.out.printf("%30s 1. Visa\n","");
	    System.out.printf("%30s 2. MasterCard\n","");
	    cardSelection = validateIntegerInput(
		    "Please enter your selection; ");
	} while (!assignCardType(cardSelection));

	// get card number
	do {
	    cardNumber = validateStringInput(
		    "Please enter the card number (Eg: 0000 0000 0000 0000): ");
	} while (!verifyBankAcc(cardNumber));

	do {
	    cardExpiryYearAndMonth = validateStringInput(
		    "Please enter the card's expiry month and year (Eg: 07/2022, 11/2024): ");
	} while (verifyCardExpiry(cardExpiryYearAndMonth));

	// get cvv number
	cVVNumber = validateIntegerInput(
		"Please enter the card's CVV number "
		+ "(Last 3 digits on back of the card): ");

	// get card holder name 
	cardHolderName = validateStringInput(
		"Please enter the card's holder name: ");

    }

    public boolean verifyCardExpiry(String expiryMonthYear) {

	if (!expiryMonthYear.contains("/") && expiryMonthYear.length() != 7) {
	    return false;
	}

	for (int i = 0; i < expiryMonthYear.length(); i++) {
	    if ((expiryMonthYear.charAt(i) != '/')) {
		continue;
	    } else if (!Character.isDigit(expiryMonthYear.charAt(i))) {
		return false;
	    }
	}

	String[] tempArray = expiryMonthYear.split("/");
	int month = Integer.parseInt(tempArray[0]);
	int year = Integer.parseInt(tempArray[1]);
	if (month < 1 || month > 12
		|| year < Calendar.getInstance().get(Calendar.YEAR)) {
	    if (year < Calendar.getInstance().get(Calendar.YEAR) && month <= Calendar.getInstance().get(Calendar.MONTH)) {
		return false;
	    }
	}

	return true;
    }

    public boolean verifyBankAcc(String cardNumber) {
	String strTemp = cardNumber.trim();
	if (getCardType().equalsIgnoreCase("Visa")) {
//Visa cards – Begin with a 4 and have 13 or 16 digits. 
//Mastercard cards – Begin with a 5 and has 16 digits
	    if ((strTemp.length() == 13 || strTemp.length() == 16) && strTemp.charAt(0) == '4') {
		return true;
	    }

	} else if (getCardType().equalsIgnoreCase("MasterCard")) {
	    if (strTemp.length() == 16 && strTemp.charAt(0) == '4') {
		return true;
	    }
	}

	System.out.printf("%30s Invalid account number. Please try again.\n\n","");
	return false;

    }

    public boolean assignCardType(int cardSelection) {
	switch (cardSelection) {
	    case 1:
		cardType = "Visa";
		break;
	    case 2:
		cardType = "MasterCard";
		break;
	    default:
		System.out.printf("%30s Invalid number entered. Please try again.\n\n","");
		return false;
	}

	return true;
    }
}
