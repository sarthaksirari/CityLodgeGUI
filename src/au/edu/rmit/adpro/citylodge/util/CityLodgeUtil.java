package au.edu.rmit.adpro.citylodge.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import au.edu.rmit.adpro.citylodge.model.HiringRecord;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.RoomType;

/**
 * This class is the utility class for CityLodge application
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class CityLodgeUtil {

	public static final String STRING_FORMAT = "%-25s%s%n";
	public static final String SPACE_REGEX = "\\s+";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String SCROLL_PANE_STYLE = "-fx-background-color: #000000;";
	public static final String MENU_BAR_STYLE = "-fx-background-color: linear-gradient(#38424b, #1f2429);";
	public static final String ROOM_LIST_STYLE = "-fx-background-color: #DCDCDC; ";
	public static final String ROOM_TITLE_STYLE = "-fx-font-size:25px;-fx-font-weight: bold;";
	public static final String BUTTON_STYLE = "-fx-background-color: linear-gradient(#38424b, #1f2429);"
			+ "-fx-padding: 7 15 7 15;-fx-background-insets: 0;-fx-background-radius: 30;"
			+ "-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;";
	public static final String COST_BUTTON_STYLE = "-fx-background-color: #696969;"
			+ "-fx-text-fill: white;-fx-font-size: 15px;-fx-font-weight: bold;"
			+ "-fx-background-radius: 3,2,2,2;-fx-padding: 7 15 7 15;";

	public static final char BRACKET_START = '(';
	public static final char BRACKET_END = ')';
	public static final char DETAILS_SEPARATOR = ':';
	public static final char UNDERSCORE = '_';
	public static final String BLANK = "";
	public static final String SPACE = " ";
	public static final String DOLLAR = "$";
	public static final String NEW_LINE = "\n";
	public static final String EMPTY = "empty";
	public static final String DASHED_LINES = "--------------------------------------";

	public static final String STANDARD_ROOM_ID_STARTING_CHARACTER = "R";
	public static final String SUITE_ID_STARTING_CHARACTER = "S";
	public static final String CUSTOMER_ID_STARTING_CHARACTERS = "CUS";

	public static final String IMAGE_FOLDER_LOCATION = "images/";
	public static final String IMAGE_CITY_LODGE_LOGO = "city-lodge-logo-2.jpg";
	public static final String IMAGE_NOT_AVAILABLE = "no-image-available.jpg";
	public static final String IMAGE_EXTENSION = ".jpg";
	public static final String IMAGE_EXTENSION_JPG = "*.jpg";
	public static final String IMAGE_EXTENSION_JPEG = "*.jpeg";
	public static final String IMAGE_SELECT = "Select image";
	public static final String IMAGE_NOT_SELECTED = "No Image Selected";
	public static final String IMAGE_FILE_ERROR = "File Error";
	public static final String IMAGE_IMPORT_ERROR = "An error encountered while importing the image." +
			NEW_LINE + "Please try again.";
	
	public static final String ADD = "Add";
	public static final String BOOK = "Book";
	public static final String CLOSE = "Close";
	public static final String COMPLETE = "Complete";
	public static final String ENTER = "Enter";
	public static final String ERROR = "Error";
	public static final String IMAGE = "Image";
	public static final String INPUT = "Input";
	public static final String RETURN = "Return";
	public static final String SELECT = "Select";
	public static final String BACK_TO_HOME = "< Back To Home";
	public static final String CLEAR_FIELDS = "Clear Fields";
	public static final String PER_NIGHT = "Per Night";
	public static final String ADD_ROOM = "Add Room";
	public static final String ADD_STANDARD_ROOM = "Add Standard Room";
	public static final String ADD_SUITE = "Add Suite";
	public static final String DATA_TRANSFER = "Data Transfer";
	public static final String IMPORT_DATA = "Import Data";
	public static final String EXPORT_DATA = "Export Data";
	public static final String EXIT = "Exit";
	public static final String CLOSE_APPLICATION = "Close Application";
	public static final String MUST_BE = "must be";
	public static final String INTEGER = "integer";
	public static final String IMAGE_FILE = "Image File";
	public static final String TEXT_FILE = "Text File";
	public static final String RENT_ROOM = "Rent Room";
	public static final String RETURN_ROOM = "Return Room";
	public static final String MAINTENANCE = "Maintenance";
	public static final String COMPLETE_MAINTENANCE = "Complete Maintenance";
	public static final String ENTER_THE_FOLLOWING_DETAILS = "Enter the following details";
	public static final String THREE_DIGITS_ONLY = "3 Digits Only";
	public static final String MAX_TWENTY_WORDS = "Maximum 20 Words";
	public static final String DATA_TRANSFER_DIRECTORY = "datatransfer";
	public static final String FILE_EXTENSION_TXT = "*.txt";
	public static final String DIRECTORY_NOT_SELECTED = "No directory was selected";
	public static final String FILE_NOT_SELECTED = "No file was selected";
	public static final String FILE_IMPORT_SUCCESS = "File Imported Sucessfully!";
	public static final String FILE_EXPORT_SUCCESS = "File Exported Sucessfully!";

	public static final String EXCEPTION_MAX_ROOMS = "Can not have more than 50 rooms!";
	public static final String EXCEPTION_ROOM_ALREADY_PRESENT = "Room already exists with this Room ID!";
	public static final String EXCEPTION_INVALID_VALUE = "Invalid value! Please try again.";
	public static final String EXCEPTION_ROOM_NOT_PRESENT = "Room does not exist! Please try again.";
	public static final String EXCEPTION_ROOM_RENTED = " room is now rented by customer ";
	public static final String EXCEPTION_ROOM_NOT_AVAILABLE = " room is not available at the moment.";
	public static final String EXCEPTION_ROOM_RETURNED = " room returned successfully";
	public static final String EXCEPTION_ROOM_NOT_RENTED = " room is not being rented at the moment.";
	public static final String EXCEPTION_ROOM_UNDER_MAINTENANCE = " room is under maintenance.";
	public static final String EXCEPTION_MAINTENANCE_NOT_PERFORMED = "Can not perform maintenance work on room ";
	public static final String EXCEPTION_ROOM_NOW_AVAILABLE = " room is now available.";
	public static final String EXCEPTION_MAINTENANCE_NOT_COMPLETED = "Can not complete maintenance work on room ";
	public static final String EXCEPTION_INVALID_ROOM_ID = "Invalid Room ID! Please try again.";
	public static final String EXCEPTION_NO_PAST_DATES = "This date cannot be of past dates.";
	public static final String EXCEPTION_ROOM_RENT_UNAVAILABLE = " room is not available to rent during the period";
	public static final String EXCEPTION_ROOM_RENT_UNAVAILABLE_MAINTENANCE = " room is not available to rent during the period due to maintenance schedule";
	public static final String EXCEPTION_ROOM_RENT_DAYS_MINUMUM = "Minimum days to rent this room today is ";
	public static final String EXCEPTION_ROOM_RENT_DAYS_MAXIMUM = "Maximum days to rent this room is ";
	public static final String EXCEPTION_INVALID_RETURN_DATE = "Room cannot be returned the same day";
	public static final String EXCEPTION_FILE_IMPORT_FAILED = "File Import Failed!";
	public static final String EXCEPTION_FILE_EXPORT_FAILED = "File Export Failed!";

	/**
	 * This method is used to format the details
	 * 
	 * @param name a String containing item name
	 * @param detail a String containing item value
	 * @return a formatted item and its value
	 */
	public static String tabFormat(String name, String detail) {
		String format = STRING_FORMAT;
		return String.format(format, name, detail, NEW_LINE);
	}

	/**
	 * This method is used to validate the room ID
	 * 
	 * @param roomId a String containing room ID
	 * @return a RoomType enum value
	 */
	public static RoomType validateRoomId(String roomId) {
		if(roomId.startsWith(STANDARD_ROOM_ID_STARTING_CHARACTER + UNDERSCORE)) {
			return RoomType.STANDARD;
		} else if(roomId.startsWith(SUITE_ID_STARTING_CHARACTER + UNDERSCORE)) {
			return RoomType.SUITE;
		}
		return null;
	}

	public static String validateUserInput(String fieldName, String input) {
		String errorMessage = BLANK;
		if (input.trim().isEmpty()) {
			errorMessage = ENTER + SPACE + fieldName + NEW_LINE;
		} else if ((fieldName.equalsIgnoreCase(HiringRecord.CUSTOMER_ID) || 
				fieldName.equalsIgnoreCase(Room.ROOM_ID)) && 
				!isInteger(input.trim())) {
			errorMessage = fieldName + SPACE + MUST_BE + SPACE + INTEGER + NEW_LINE;
		} else if ((fieldName.equalsIgnoreCase(HiringRecord.CUSTOMER_ID) || 
				fieldName.equalsIgnoreCase(Room.ROOM_ID)) && 
				input.length() != HiringRecord.CUSTOMER_ID_DIGITS) {
			errorMessage = fieldName + SPACE + MUST_BE + SPACE + THREE_DIGITS_ONLY.toLowerCase() + NEW_LINE;
		} else if (fieldName.equalsIgnoreCase(Room.FEATURE_SUMMARY) && 
				input.split(SPACE_REGEX).length > Room.FEATURE_SUMMARY_MAX_WORDS) {
			errorMessage = fieldName + SPACE + MUST_BE + SPACE + MAX_TWENTY_WORDS.toLowerCase() + NEW_LINE;
		}
		return errorMessage;
	}

	public static DateTime convertDateToFormat(String date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate parsedDate = formatter.parse(date, LocalDate::from);
		return new DateTime(parsedDate.getDayOfMonth(),parsedDate.getMonthValue(),parsedDate.getYear());
	}
	
	private static boolean isInteger(String input) {
		try {		
			Integer.parseInt(input);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
}
