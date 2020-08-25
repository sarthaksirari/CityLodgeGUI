package au.edu.rmit.adpro.citylodge.model;

/**
 * An enum class containing all the room status
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public enum RoomStatus {

	AVAILABLE("Available"),
	RENTED("Rented"),
	MAINTENANCE("Maintenance");

	private final String name;

	private RoomStatus(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
