package au.edu.rmit.adpro.citylodge.model;

import java.text.DecimalFormat;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * This class is used to contain the details of the renting record of a Room object
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class HiringRecord implements Comparable<Object> {

	public static final String CUSTOMER_ID = "Customer ID";
	public static final String RECORD_ID = "Record ID";
	public static final String RENT_DATE = "Rent Date";
	public static final String ESTIMATED_RETURN_DATE = "Estimated Return Date";
	public static final String ACTUAL_RETURN_DATE = "Actual Return Date";
	public static final String RENTAL_FEE = "Rental Fee";
	public static final String LATE_FEE = "Late Fee";

	public static final String RECORD_ID_TABLE = "recordId";
	public static final String RENT_DATE_TABLE = "rentDate";
	public static final String ESTIMATED_RETURN_DATE_TABLE = "estimatedReturnDate";
	public static final String ACTUAL_RETURN_DATE_TABLE = "actualReturnDate";
	public static final String RENTAL_FEE_TABLE = "rentalFee";
	public static final String LATE_FEE_TABLE = "lateFee";

	public static final String DETAILS_NONE = "none";

	public static final int CUSTOMER_ID_DIGITS = 3;
	public static final double DEFAULT_FEES = -1;
	public static final String FEES_DECIMAL_FORMAT = "0.00";

	private String recordId;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee = DEFAULT_FEES;
	private double lateFee = DEFAULT_FEES;

	/**
	 * Constructor for the class HiringRecord
	 */
	public HiringRecord(String recordId, DateTime rentDate, DateTime estimatedReturnDate) {
		this.recordId = recordId;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public DateTime getRentDate() {
		return rentDate;
	}

	public void setRentDate(DateTime rentDate) {
		this.rentDate = rentDate;
	}

	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(DateTime estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	/**
	 * This method is used to represent the Hiring Record as a String
	 */
	@Override
	public String toString() {
		return recordId + CityLodgeUtil.DETAILS_SEPARATOR +
				rentDate + CityLodgeUtil.DETAILS_SEPARATOR +
				estimatedReturnDate + CityLodgeUtil.DETAILS_SEPARATOR +
				(actualReturnDate == null ? DETAILS_NONE : actualReturnDate) + CityLodgeUtil.DETAILS_SEPARATOR +
				(rentalFee == DEFAULT_FEES ? DETAILS_NONE : rentalFee) + CityLodgeUtil.DETAILS_SEPARATOR + 
				(lateFee == DEFAULT_FEES ? DETAILS_NONE : lateFee);
	}

	/**
	 * This method is used to return the formatted details
	 * 
	 * @return a String containing the formatted details
	 */
	public String getDetails() {
		return CityLodgeUtil.tabFormat(RECORD_ID + CityLodgeUtil.DETAILS_SEPARATOR, recordId) + 
				CityLodgeUtil.tabFormat(RENT_DATE + CityLodgeUtil.DETAILS_SEPARATOR, rentDate.toString()) + 
				CityLodgeUtil.tabFormat(ESTIMATED_RETURN_DATE + CityLodgeUtil.DETAILS_SEPARATOR, estimatedReturnDate.toString()) + 
				(actualReturnDate == null ? CityLodgeUtil.BLANK : CityLodgeUtil.tabFormat(ACTUAL_RETURN_DATE + CityLodgeUtil.DETAILS_SEPARATOR, actualReturnDate.toString())) +
				(rentalFee == DEFAULT_FEES ? CityLodgeUtil.BLANK : CityLodgeUtil.tabFormat(RENTAL_FEE + CityLodgeUtil.DETAILS_SEPARATOR, roundToTwoDecimalPlaces(rentalFee))) +
				(lateFee == DEFAULT_FEES ? CityLodgeUtil.BLANK : CityLodgeUtil.tabFormat(LATE_FEE + CityLodgeUtil.DETAILS_SEPARATOR, roundToTwoDecimalPlaces(lateFee)));
	}

	/**
	 * This method is used to round the double value to 2 decimal places and convert to String
	 * 
	 * @param number a double value
	 * @return String containing the double value rounded to 2 decimal places
	 */
	private String roundToTwoDecimalPlaces(double number) {
		return new DecimalFormat(FEES_DECIMAL_FORMAT).format(number);
	}

	@Override
	public int compareTo(Object object) {
		return DateTime.diffDays(((HiringRecord) object).getRentDate(), this.getRentDate());
	}
}
