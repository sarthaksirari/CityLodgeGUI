package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when invalid room return date is entered 
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class InvalidRoomReturnDateException extends Exception {

	public InvalidRoomReturnDateException() {
		super(CityLodgeUtil.EXCEPTION_INVALID_RETURN_DATE);
	}
}
