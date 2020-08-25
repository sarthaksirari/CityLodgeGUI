package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room is unavailable to rent due to maintenance work
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomMaintenaceScheduleException extends Exception {

	public RoomMaintenaceScheduleException(String roomId) {
		super(roomId + CityLodgeUtil.EXCEPTION_ROOM_RENT_UNAVAILABLE_MAINTENANCE);
	}
}
