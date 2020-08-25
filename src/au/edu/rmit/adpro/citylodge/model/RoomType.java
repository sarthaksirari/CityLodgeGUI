package au.edu.rmit.adpro.citylodge.model;

/**
 * An enum class containing all the room types
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public enum RoomType {

	STANDARD("Standard Room"),
	SUITE("Suite");

	private final String name;

	private RoomType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
