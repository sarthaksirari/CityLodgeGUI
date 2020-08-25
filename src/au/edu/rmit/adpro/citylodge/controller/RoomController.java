package au.edu.rmit.adpro.citylodge.controller;

import java.util.List;

import au.edu.rmit.adpro.citylodge.exception.RoomAlreadyPresentException;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;
import au.edu.rmit.adpro.citylodge.view.CityLodgeView;

/**
 * This class is used as the Controller for Room related operations
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomController {
	
	/**
	 * This method is used to add a room
	 * 
	 * @param room
	 * @throws Exception
	 */
	public void addRoom(Room room) throws Exception {
		List<Room> rooms = CityLodgeView.getDatabaseController().getRooms();
		for(Room rm : rooms) {
			if(rm.getRoomId().equalsIgnoreCase(room.getRoomId())) {
				throw new RoomAlreadyPresentException();
			}
		}
		CityLodgeView.getDatabaseController().addRoom(room);
	}

	/**
	 * This method is used to rent a room
	 * 
	 * @param room
	 * @param customerId
	 * @param rentDate
	 * @param days
	 * @throws Exception
	 */
	public void rentRoom(Room room, String customerId, DateTime rentDate, int days) throws Exception {
		room.rent(CityLodgeUtil.CUSTOMER_ID_STARTING_CHARACTERS + customerId, rentDate, days);

		CityLodgeView.getDatabaseController().updateRoom(room);
		CityLodgeView.getDatabaseController().addHiringRecord(room.getRoomId(), room.getLatestHiringRecords());
	}

	/**
	 * This method is used to return a room
	 * 
	 * @param room
	 * @param returnDate
	 * @throws Exception
	 */
	public void returnRoom(Room room, DateTime returnDate) throws Exception {
		room.returnRoom(returnDate);

		CityLodgeView.getDatabaseController().updateRoom(room);
		CityLodgeView.getDatabaseController().updateHiringRecord(room.getRoomId(), room.getLatestHiringRecords());
	}

	/**
	 * This method is used to perform maintenance on a room
	 * 
	 * @param room
	 * @throws Exception
	 */
	public void performMaintenance(Room room) throws Exception {
		room.performMaintenance();

		CityLodgeView.getDatabaseController().updateRoom(room);
	}

	/**
	 * This method is used to complete maintenance on a room
	 * 
	 * @param room
	 * @param completionDate
	 * @throws Exception
	 */
	public void completeMaintenance(Room room, DateTime completionDate) throws Exception {
		room.completeMaintenance(completionDate);

		CityLodgeView.getDatabaseController().updateRoom(room);
	}
}
