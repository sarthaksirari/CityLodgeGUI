package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room is Unavailable
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomUnavailableException extends Exception {

	public RoomUnavailableException(String roomId) {
		super(roomId + CityLodgeUtil.EXCEPTION_ROOM_NOT_AVAILABLE);
	}
}
