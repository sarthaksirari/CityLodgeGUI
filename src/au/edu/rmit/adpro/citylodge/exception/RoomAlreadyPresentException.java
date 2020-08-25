package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room is already present
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomAlreadyPresentException extends Exception {

	public RoomAlreadyPresentException() {
		super(CityLodgeUtil.EXCEPTION_ROOM_ALREADY_PRESENT);
	}
}
