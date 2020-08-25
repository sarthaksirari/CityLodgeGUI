package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room is not rented for some minimum number of days
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class MinDaysRentException extends Exception {

	public MinDaysRentException(int minRentDays) {
		super(CityLodgeUtil.EXCEPTION_ROOM_RENT_DAYS_MINUMUM + minRentDays);
	}
}
