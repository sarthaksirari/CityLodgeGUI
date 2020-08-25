package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when Room maintenance is not performed
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomMaintenanceException extends Exception {

	public RoomMaintenanceException(String roomId) {
		super(CityLodgeUtil.EXCEPTION_MAINTENANCE_NOT_PERFORMED + roomId);
	}
}
