package au.edu.rmit.adpro.citylodge.exception;

import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;

/**
 * This class is used to throw Custom Exception when room maintenance is not completed
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomMaintenanceCompletionException extends Exception {

	public RoomMaintenanceCompletionException(String roomId) {
		super(CityLodgeUtil.EXCEPTION_MAINTENANCE_NOT_COMPLETED + roomId);
	}
}
