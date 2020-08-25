package au.edu.rmit.adpro.citylodge.model;

import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * Interface to contain the required methods of Room class
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public interface IRoom {

	/**
	 * This method is used to rent a Room
	 * 
	 * @param customerId a String containing the customer ID
	 * @param rentDate a DateTime object containing the rent date
	 * @param numOfRentDay an integer value containing the number of rooms
	 * @return a boolean to tell if the room is rented
	 */
	public abstract void rent(String customerId, DateTime rentDate, int numOfRentDay) throws Exception;

	/**
	 * This method is used to return the rented room
	 * 
	 * @param returnDate a DateTime object containing the return date
	 * @return a boolean to tell if the room is returned
	 */
	public void returnRoom(DateTime returnDate) throws Exception;

	/**
	 * This method is used to perform the maintenance work on the room
	 * 
	 * @return a boolean to tell if the maintenance work is started on the room
	 */
	public abstract void performMaintenance() throws Exception;

	/**
	 * This method is used to complete the maintenance work on the room
	 * 
	 * @param completionDate a DateTime object containing the completion date of maintenance work on the room
	 * @return a boolean to tell if the maintenance work is completed on the room
	 */
	public abstract void completeMaintenance(DateTime completionDate) throws Exception;

	/**
	 * This method is used to return the formatted details of the room
	 * 
	 * @return a String containing the formatted details of the room
	 */
	public abstract String getDetails();

	/**
	 * This method is used to return the rate of the room per night
	 * 
	 * @return a double value containing the rate of the room per night
	 */
	public double getRoomRatePerNight();
}
