package au.edu.rmit.adpro.citylodge.model;

import au.edu.rmit.adpro.citylodge.exception.RoomMaintenaceScheduleException;
import au.edu.rmit.adpro.citylodge.exception.RoomMaintenanceCompletionException;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * This class is used to contain the details of a Suite
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class Suite extends Room {

	public static final String LAST_MAINTENANCE_DATE = "Last Maintenance Date";
	public static final String MAINTENANCE_COMPLETION_DATE = "Maintenance Completion Date";

	public static final int NUMBER_OF_BEDS_SIX = 6;
	private static final int LAST_MAINTENANCE_DATE_DETAIL_INSERT_POSITION = 4;

	private static final double SUITE_RATE = 999;
	private static final double LATE_FEE_RATE = 1099;
	private static final int MAINTENANCE_INTERVAL = 10;

	private DateTime lastMaintenanceDate;

	/**
	 * /**
	 * Constructor of class Suite
	 * 
	 * @param roomId a String containing the room ID
	 * @param numberOfBeds an integer value containing the number of beds of room 
	 * @param roomType a RoomType enum object
	 * @param roomStatus a RoomStatus enum object
	 * @param featureSummary a String containing the features of a room
	 * @param lastMaintenanceDate a DateTime object containing the date when the room was last maintained
	 */
	public Suite(String roomId, int numberOfBeds, RoomType roomType, RoomStatus roomStatus, String featureSummary, DateTime lastMaintenanceDate, String imagePath) {
		super(roomId, numberOfBeds, roomType, roomStatus, featureSummary, imagePath);
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	public void setLastMaintenanceDate(DateTime lastMaintenanceDate) {
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDay) throws Exception {
		if (validateRoomRentRequest(rentDate)) {
			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);
			if (DateTime.diffDays(rentDate, lastMaintenanceDate) > MAINTENANCE_INTERVAL - 1 || 
					DateTime.diffDays(estimatedReturnDate, lastMaintenanceDate) > MAINTENANCE_INTERVAL - 1) {
				throw new RoomMaintenaceScheduleException(getRoomId());
			} else {
				String recordId = getRoomId() + CityLodgeUtil.UNDERSCORE + 
						customerId + CityLodgeUtil.UNDERSCORE + 
						rentDate.getEightDigitDate();
				HiringRecord hiringRecord = new HiringRecord(recordId, rentDate, 
						estimatedReturnDate);
				addHiringRecord(hiringRecord);
				setRoomStatus(RoomStatus.RENTED);
			}
		}
	}

	@Override
	public void returnRoom(DateTime returnDate) throws Exception {
		if (validateRoomReturnRequest(returnDate)) {
			HiringRecord latestRecord = getHiringRecords().get(0);

			int actualRentDays = DateTime.diffDays(returnDate, latestRecord.getRentDate());
			int estimatedRentDays = DateTime.diffDays(latestRecord.getEstimatedReturnDate(), latestRecord.getRentDate());

			double rentalFee = 0.0;
			double lateFee = 0.0;

			if (actualRentDays >= estimatedRentDays) {
				rentalFee = actualRentDays * SUITE_RATE;
				lateFee = (actualRentDays - estimatedRentDays) * LATE_FEE_RATE;
			}
			else {
				rentalFee = actualRentDays * SUITE_RATE;
			}		

			latestRecord.setActualReturnDate(returnDate);
			latestRecord.setRentalFee(Math.round(rentalFee * 100.0) / 100.0);
			latestRecord.setLateFee(Math.round(lateFee * 100.0) / 100.0);

			setRoomStatus(RoomStatus.AVAILABLE);
		}
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws Exception {
		if (validateMaintenanceCompletionRequest(completionDate) && isRoomUnderMaintenance()) {
			setRoomStatus(RoomStatus.AVAILABLE);
			lastMaintenanceDate = completionDate;
		} else {
			throw new RoomMaintenanceCompletionException(getRoomId());
		}
	}

	/**
	 * This method is used to represent the Room as a String
	 */
	@Override
	public String toString() {
		return addSuiteDetails(super.toString());
	}

	@Override
	public String getDetails() {
		return CityLodgeUtil.tabFormat(ROOM_ID + CityLodgeUtil.DETAILS_SEPARATOR, getRoomId()) + 
				CityLodgeUtil.tabFormat(NUMBER_OF_BEDS + CityLodgeUtil.DETAILS_SEPARATOR, Integer.toString(getNumberOfBeds())) + 
				CityLodgeUtil.tabFormat(ROOM_TYPE + CityLodgeUtil.DETAILS_SEPARATOR, getRoomType().toString()) + 
				CityLodgeUtil.tabFormat(ROOM_STATUS + CityLodgeUtil.DETAILS_SEPARATOR, getFeatureSummary()) +
				CityLodgeUtil.tabFormat(LAST_MAINTENANCE_DATE + CityLodgeUtil.DETAILS_SEPARATOR, lastMaintenanceDate.toString()) + 
				CityLodgeUtil.tabFormat(FEATURE_SUMMARY + CityLodgeUtil.DETAILS_SEPARATOR, getFeatureSummary()) + 
				(getHiringRecords().isEmpty() ? 
						CityLodgeUtil.tabFormat(RENTAL_RECORD + CityLodgeUtil.DETAILS_SEPARATOR, CityLodgeUtil.EMPTY) : 
							RENTAL_RECORD + CityLodgeUtil.NEW_LINE + getHiringRecordsDetails());
	}

	/**
	 * This method is used to add the last maintenance date in the room details
	 * 
	 * @param roomDetails a String containing Room details
	 * @return a String containing Suite details
	 */
	private String addSuiteDetails(String roomDetails) {
		String suiteDetails = CityLodgeUtil.BLANK;
		int roomDetailsLength = roomDetails.length();
		System.out.println(roomDetails);
		int detailsSeparatorCount = 0;
		for (int i = 0; i < roomDetailsLength; i++) {
			if (roomDetails.charAt(i) == CityLodgeUtil.DETAILS_SEPARATOR) {
				detailsSeparatorCount++;
			}
			System.out.println(detailsSeparatorCount);
			if (detailsSeparatorCount == LAST_MAINTENANCE_DATE_DETAIL_INSERT_POSITION) {
				suiteDetails = roomDetails.substring(0, i) + CityLodgeUtil.DETAILS_SEPARATOR + 
						lastMaintenanceDate + roomDetails.substring(i, roomDetailsLength);
				System.out.println(suiteDetails);
				break;
			}
		}
		return suiteDetails;
	}

	@Override
	public double getRoomRatePerNight() {
		return SUITE_RATE;
	}
}
