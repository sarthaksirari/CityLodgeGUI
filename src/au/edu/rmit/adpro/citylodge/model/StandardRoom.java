package au.edu.rmit.adpro.citylodge.model;

import au.edu.rmit.adpro.citylodge.exception.MaxDaysRentException;
import au.edu.rmit.adpro.citylodge.exception.MinDaysRentException;
import au.edu.rmit.adpro.citylodge.exception.RoomMaintenanceCompletionException;
import au.edu.rmit.adpro.citylodge.exception.RoomMaintenanceException;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * This class is used to contain the details of a StandardRoom
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class StandardRoom extends Room {

	public static final String AVAILABLE_NUMBER_OF_BEDS = "(1, 2 or 4)";

	public static final int NUMBER_OF_BEDS_ONE = 1;
	public static final int NUMBER_OF_BEDS_TWO = 2;
	public static final int NUMBER_OF_BEDS_FOUR = 4;
	
	private static final double SINGLE_BED_RATE = 59;
	private static final double DOUBLE_BED_RATE = 99;
	private static final double FOUR_BED_RATE = 199;
	private static final double LATE_FEE_MULTIPLIER = 1.35;

	private static final int MINIMUM_RENT_DAYS_WEEKDAYS = 2;
	private static final int MINIMUM_RENT_DAYS_WEEKENDS = 3;
	private static final int MAXIMUM_RENT_DAYS = 10;

	/**
	 * Constructor of class StandardRoom
	 * 
	 * @param roomId a String containing the room ID
	 * @param numberOfBeds an integer value containing the number of beds of room 
	 * @param roomType a RoomType enum object
	 * @param roomStatus a RoomStatus enum object
	 * @param featureSummary a String containing the features of a room
	 */
	public StandardRoom(String roomId, int numberOfBeds, RoomType roomType, RoomStatus roomStatus, String featureSummary, String imagePath) {
		super(roomId, numberOfBeds, roomType, roomStatus, featureSummary, imagePath);
	}

	@Override
	public void rent(String customerId, DateTime rentDate, int numOfRentDay) throws Exception {
		if (validateRoomRentRequest(rentDate)) {
				String rentDay = rentDate.getNameOfDay();
				if((rentDay.equalsIgnoreCase(DaysOfWeek.MONDAY.toString()) || 
						rentDay.equalsIgnoreCase(DaysOfWeek.TUESDAY.toString()) || 
						rentDay.equalsIgnoreCase(DaysOfWeek.WEDNESDAY.toString()) || 
						rentDay.equalsIgnoreCase(DaysOfWeek.THURSDAY.toString()) || 
						rentDay.equalsIgnoreCase(DaysOfWeek.FRIDAY.toString())) && 
						numOfRentDay < MINIMUM_RENT_DAYS_WEEKDAYS) {
					throw new MinDaysRentException(MINIMUM_RENT_DAYS_WEEKDAYS);
				} else if((rentDay.equalsIgnoreCase(DaysOfWeek.SATURDAY.toString()) || 
						rentDay.equalsIgnoreCase(DaysOfWeek.SUNDAY.toString())) && 
						numOfRentDay < MINIMUM_RENT_DAYS_WEEKENDS) {
					throw new MinDaysRentException(MINIMUM_RENT_DAYS_WEEKENDS);
				} else if (numOfRentDay > MAXIMUM_RENT_DAYS){
					throw new MaxDaysRentException(MAXIMUM_RENT_DAYS);
				} else {
					String recordId = getRoomId() + CityLodgeUtil.UNDERSCORE + 
							customerId + CityLodgeUtil.UNDERSCORE + 
							rentDate.getEightDigitDate();
					DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);
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
				if (getNumberOfBeds() == NUMBER_OF_BEDS_ONE) {
					rentalFee = estimatedRentDays * SINGLE_BED_RATE;
					lateFee = (actualRentDays - estimatedRentDays) * LATE_FEE_MULTIPLIER * SINGLE_BED_RATE;
				} else if (getNumberOfBeds() == NUMBER_OF_BEDS_TWO) {
					rentalFee = estimatedRentDays * DOUBLE_BED_RATE;
					lateFee = (actualRentDays - estimatedRentDays) * LATE_FEE_MULTIPLIER * DOUBLE_BED_RATE;
				} else if (getNumberOfBeds() == NUMBER_OF_BEDS_FOUR) {
					rentalFee = estimatedRentDays * FOUR_BED_RATE;
					lateFee = (actualRentDays - estimatedRentDays) * LATE_FEE_MULTIPLIER * FOUR_BED_RATE;
				}
			}
			else {
				if (getNumberOfBeds() == NUMBER_OF_BEDS_ONE) {
					rentalFee = actualRentDays * SINGLE_BED_RATE;
				} else if (getNumberOfBeds() == NUMBER_OF_BEDS_TWO) {
					rentalFee = actualRentDays * DOUBLE_BED_RATE;
				} else if (getNumberOfBeds() == NUMBER_OF_BEDS_FOUR) {
					rentalFee = actualRentDays * FOUR_BED_RATE;
				}
			}		

			latestRecord.setActualReturnDate(returnDate);
			latestRecord.setRentalFee(Math.round(rentalFee * 100.0) / 100.0);
			latestRecord.setLateFee(Math.round(lateFee * 100.0) / 100.0);
			setRoomStatus(RoomStatus.AVAILABLE);
		}
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws Exception {
		if (isRoomUnderMaintenance()) {
			setRoomStatus(RoomStatus.AVAILABLE);
		} else {
			throw new RoomMaintenanceCompletionException(getRoomId());
		}
	}

	@Override
	public String getDetails() {
		return CityLodgeUtil.tabFormat(ROOM_ID + CityLodgeUtil.DETAILS_SEPARATOR, getRoomId()) + 
				CityLodgeUtil.tabFormat(NUMBER_OF_BEDS + CityLodgeUtil.DETAILS_SEPARATOR, Integer.toString(getNumberOfBeds())) + 
				CityLodgeUtil.tabFormat(ROOM_TYPE + CityLodgeUtil.DETAILS_SEPARATOR, getRoomType().toString()) + 
				CityLodgeUtil.tabFormat(ROOM_STATUS + CityLodgeUtil.DETAILS_SEPARATOR, getRoomStatus().toString()) + 
				CityLodgeUtil.tabFormat(FEATURE_SUMMARY + CityLodgeUtil.DETAILS_SEPARATOR, getFeatureSummary()) + 
				(getHiringRecords().isEmpty() ? 
						CityLodgeUtil.tabFormat(RENTAL_RECORD + CityLodgeUtil.DETAILS_SEPARATOR, CityLodgeUtil.EMPTY) : 
							RENTAL_RECORD + CityLodgeUtil.NEW_LINE + getHiringRecordsDetails());
	}

	@Override
	public double getRoomRatePerNight() {
		return getNumberOfBeds() == NUMBER_OF_BEDS_ONE ? SINGLE_BED_RATE : (getNumberOfBeds() == NUMBER_OF_BEDS_TWO ? DOUBLE_BED_RATE : FOUR_BED_RATE);
	}
}
