package au.edu.rmit.adpro.citylodge.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import au.edu.rmit.adpro.citylodge.exception.InvalidRoomReturnDateException;
import au.edu.rmit.adpro.citylodge.exception.PastDateException;
import au.edu.rmit.adpro.citylodge.exception.RoomMaintenanceException;
import au.edu.rmit.adpro.citylodge.exception.RoomUnavailableException;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * This abstract class is used to contain the details of a Room
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public abstract class Room implements IRoom {

	public static final String ROOM_ID = "Room ID";
	public static final String NUMBER_OF_BEDS = "Number of Beds";
	public static final String ROOM_TYPE = "Room Type";
	public static final String ROOM_STATUS = "Room Status";
	public static final String FEATURE_SUMMARY = "Feature Summary";
	public static final String RENTAL_RECORD = "Rental Record";
	public static final String NUMBER_OF_DAYS = "Number of Days";

	public static final int FEATURE_SUMMARY_MAX_WORDS = 20;
	public static final int HIRING_RECORDS_MAX_SIZE = 10;
	public static final int MIN_NUMBER_OF_DAYS_TO_RENT = 1;

	private String roomId;
	private int numberOfBeds;
	private RoomType roomType;
	private RoomStatus roomStatus;
	private String featureSummary;
	private String imagePath;

	private List<HiringRecord> hiringRecords;

	/**
	 * Constructor of class Room
	 * 
	 * @param roomId a String containing the room ID
	 * @param numberOfBeds an integer value containing the number of beds of room 
	 * @param roomType a RoomType enum object
	 * @param roomStatus a RoomStatus enum object
	 * @param featureSummary a String containing the features of a room
	 */
	protected Room(String roomId, int numberOfBeds, RoomType roomType, RoomStatus roomStatus, String featureSummary, String imagePath) {
		this.roomId = roomId;
		this.numberOfBeds = numberOfBeds;
		this.roomType = roomType;
		this.roomStatus = roomStatus;
		this.featureSummary = featureSummary;
		this.imagePath = imagePath;
		this.hiringRecords = new ArrayList<HiringRecord>(HIRING_RECORDS_MAX_SIZE);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public RoomStatus getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(RoomStatus roomStatus) {
		this.roomStatus = roomStatus;
	}

	public String getFeatureSummary() {
		return featureSummary;
	}

	public void setFeatureSummary(String featureSummary) {
		this.featureSummary = featureSummary;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<HiringRecord> getHiringRecords() {
		return hiringRecords;
	}
	
	public HiringRecord getLatestHiringRecords() {
		return hiringRecords.get(0);
	}

	public void setHiringRecords(List<HiringRecord> hiringRecords) {
		this.hiringRecords = hiringRecords;
	}

	/**
	 * This method is used to add a new Hiring Record to Room
	 * 
	 * @param hiringRecord a HiringRecord object
	 */
	public void addHiringRecord(HiringRecord hiringRecord) {
		if(hiringRecords.size() == HIRING_RECORDS_MAX_SIZE) {
			hiringRecords.remove(HIRING_RECORDS_MAX_SIZE - 1);
		}
		hiringRecords.add(0, hiringRecord);
	}

	/**
	 * This method is used to represent the Room as a String
	 */
	@Override
	public String toString() {
		return roomId + CityLodgeUtil.DETAILS_SEPARATOR +
				numberOfBeds + CityLodgeUtil.DETAILS_SEPARATOR +
				roomType + CityLodgeUtil.DETAILS_SEPARATOR +
				roomStatus + CityLodgeUtil.DETAILS_SEPARATOR +
				featureSummary + CityLodgeUtil.DETAILS_SEPARATOR +
				imagePath;
	}

	/**
	 * This method is used to perform maintenance work on a Room
	 */
	@Override
	public void performMaintenance() throws Exception {
		if (isRoomAvailable()) {
			setRoomStatus(RoomStatus.MAINTENANCE);
		} else {
			throw new RoomMaintenanceException(roomId);
		}
	}

	/**
	 * This method is used to check if the room is available
	 * 
	 * @return a boolean which tells if the room is available 
	 */
	protected boolean isRoomAvailable() {
		return roomStatus == RoomStatus.AVAILABLE;
	}

	/**
	 * This method is used to check if the room is rented
	 * 
	 * @return a boolean which tells if the room is rented 
	 */
	protected boolean isRoomRented() {
		return roomStatus == RoomStatus.RENTED;
	}

	/**
	 * This method is used to check if the room is under maintenance
	 * 
	 * @return a boolean which tells if the room is under maintenance 
	 */
	protected boolean isRoomUnderMaintenance() {
		return roomStatus == RoomStatus.MAINTENANCE;
	}

	/**
	 * This method is used to validate a room rent request by the user
	 * 
	 * @param rentDate a DateTime object containing the rent date
	 * @return a boolean to tell if the room rent request could be processed further
	 */
	protected boolean validateRoomRentRequest(DateTime rentDate) throws Exception {
		if (!isRoomAvailable()) {
			throw new RoomUnavailableException(roomId);
		} else if (DateTime.diffDays(rentDate, new DateTime()) < -1) {
			throw new PastDateException();
		}
		return true;
	}

	/**
	 * This method is used to validate a room return request by the user
	 * 
	 * @param returnDate a DateTime object containing the return date
	 * @return a boolean to tell if the return room request could be processed further
	 */
	protected boolean validateRoomReturnRequest(DateTime returnDate) throws Exception {
		int numberOfDays = DateTime.diffDays(returnDate, getLatestHiringRecords().getRentDate());
		if (numberOfDays < MIN_NUMBER_OF_DAYS_TO_RENT) {
			throw new InvalidRoomReturnDateException();
		} else if (DateTime.diffDays(returnDate, new DateTime()) < -1) {
			throw new PastDateException();
		}
		return true;
	}
	
	/**
	 * This method is used to validate a maintenance completion request by the user
	 * 
	 * @param completionDate a DateTime object containing the completion date
	 * @return a boolean to tell if the maintenance completion request could be processed further
	 */
	protected boolean validateMaintenanceCompletionRequest(DateTime completionDate) throws Exception {
		if (DateTime.diffDays(completionDate, new DateTime()) < -1) {
			throw new PastDateException();
		}
		return true;
	}

	/**
	 * This method is used to return the Hiring record details of a Room
	 * 
	 * @return a String containing hiring record of the room
	 */
	protected String getHiringRecordsDetails() {
		String hiringRecordsDetails = CityLodgeUtil.BLANK;
		int hiringRecordsSize = getHiringRecords().size();

		if (hiringRecordsSize > 0) {
			for (int i = 0; i < hiringRecordsSize; i++) {
				hiringRecordsDetails = hiringRecordsDetails.concat(getHiringRecords().get(i).getDetails());
				if (i < hiringRecordsSize - 1 ) {
					hiringRecordsDetails = hiringRecordsDetails.concat(CityLodgeUtil.NEW_LINE + 
							CityLodgeUtil.DASHED_LINES + CityLodgeUtil.NEW_LINE);
				}
			}
		}
		return hiringRecordsDetails;
	}
}
