package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room is not rented for under some maximum number of days 
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class MaxDaysRentException extends Exception {

	public MaxDaysRentException(int maxRentDays) {
		super(CityLodgeUtil.EXCEPTION_ROOM_RENT_DAYS_MAXIMUM + maxRentDays);
	}
}
