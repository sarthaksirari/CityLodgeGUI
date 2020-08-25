package au.edu.rmit.adpro.citylodge.model;

/**
 * An enum class containing the main menu items
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public enum MainMenu {

	ADD_ROOM("Add Room"),
	RENT_ROOM("Rent Room"),
	ETURN_ROOM("Return Room"),
	ROOM_MAINTENANCE("Room Maintenance"),
	COMPLETE_MAINTENANCE("Complete Maintenance"),
	DISPLAY_ALL_ROOMS("Display All Rooms"),
	DISPLAY_EXIT_PROGRAM("Exit Program");

	private final String name;

	private MainMenu(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
