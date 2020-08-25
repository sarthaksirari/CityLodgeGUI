package au.edu.rmit.adpro.citylodge.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import au.edu.rmit.adpro.citylodge.model.HiringRecord;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.RoomStatus;
import au.edu.rmit.adpro.citylodge.model.RoomType;
import au.edu.rmit.adpro.citylodge.model.StandardRoom;
import au.edu.rmit.adpro.citylodge.model.Suite;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;

/**
 * This class is used as the Controller for Database related operations
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class DatabaseController {

	Connection con = null;
	Statement stmt = null;
	int result = 0;

	public DatabaseController() {	 	
		final String DB_NAME = "citylodgedb";

		try {
			con = getConnection(DB_NAME);
			System.out.println("Connection to database " + DB_NAME + " created successfully");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * This method is used to get Connection object
	 * 
	 * @param dbName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
		final String driver = "org.hsqldb.jdbc.JDBCDriver";
		final String url = "jdbc:hsqldb:file:database/";
		final String username = "user";
		final String password = "";

		Class.forName(driver);
		return DriverManager.getConnection(url + dbName, username, password);
	}

	/**
	 * This method is used to insert a room to database
	 * 
	 * @param room
	 * @throws Exception
	 */
	public void addRoom(Room room) throws Exception {
		String addRoomQuery = CityLodgeUtil.BLANK;
		stmt = con.createStatement();

		if(room instanceof StandardRoom) {
			addRoomQuery = "INSERT INTO ROOM (\"ROOMID\", \"NUMBEROFBEDS\", \"ROOMTYPE\", " +
					"\"ROOMSTATUS\", \"FEATURESUMMARY\", \"IMAGEPATH\") " + 
					"VALUES('" +
					room.getRoomId() + "', '" +
					room.getNumberOfBeds() + "', '" +
					room.getRoomType() + "', '" +
					room.getRoomStatus() + "', '" +
					room.getFeatureSummary() + "', '" +
					room.getImagePath() + "')";
		} else {
			addRoomQuery = "INSERT INTO ROOM (\"ROOMID\", \"NUMBEROFBEDS\", \"ROOMTYPE\", " +
					"\"ROOMSTATUS\", \"FEATURESUMMARY\", \"LASTMAINTENANCEDATE\", \"IMAGEPATH\") " + 
					"VALUES('" +
					room.getRoomId() + "', '" +
					room.getNumberOfBeds() + "', '" +
					room.getRoomType() + "', '" +
					room.getRoomStatus() + "', '" +
					room.getFeatureSummary() + "', " +
					"TO_DATE('"+ ((Suite) room).getLastMaintenanceDate().getFormattedDate() + "', 'DD/MM/YYYY'), '" +
					room.getImagePath() + "')";
		}	
		result = stmt.executeUpdate(addRoomQuery);
	}

	/**
	 * This method is used to update a room in database
	 * 
	 * @param room
	 * @throws Exception
	 */
	public void updateRoom(Room room) throws Exception {
		Statement stmt = con.createStatement();
		int result = 0;
		String updateRoomQuery = "UPDATE ROOM SET "
				+ "NUMBEROFBEDS = '"+ room.getNumberOfBeds() + "', "
				+ "ROOMTYPE = '"+ room.getRoomType() + "', "
				+ "ROOMSTATUS = '"+ room.getRoomStatus() + "', "
				+ "FEATURESUMMARY = '" + room.getFeatureSummary() + "' ";

		if(room instanceof Suite) {
			updateRoomQuery = updateRoomQuery + ", LASTMAINTENANCEDATE = TO_DATE('" + 
					((Suite) room).getLastMaintenanceDate().getFormattedDate() + "', 'DD/MM/YYYY') ";
		}

		updateRoomQuery = updateRoomQuery + "WHERE ROOMID = '" + room.getRoomId() + "'";

		result = stmt.executeUpdate(updateRoomQuery);
		if(result != 1) {
			throw new Exception("Room details not updated");
		}
	}

	/**
	 * This method is used to get all rooms in database
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Room> getRooms() throws Exception {
		ArrayList<Room> rooms  = new ArrayList<Room>();
		stmt = con.createStatement();
		String getRoomsQuery = "SELECT * FROM ROOM"; 
		ResultSet roomsResultSet = stmt.executeQuery(getRoomsQuery);

		while (roomsResultSet.next()) {
			String roomID = roomsResultSet.getString("ROOMID");
			int numberOfBeds = roomsResultSet.getInt("NUMBEROFBEDS");
			RoomType roomType = roomsResultSet.getString("ROOMTYPE").equalsIgnoreCase(
					RoomType.STANDARD.toString()) ? RoomType.STANDARD : RoomType.SUITE;
			RoomStatus roomStatus = roomsResultSet.getString("ROOMSTATUS").equalsIgnoreCase(
					RoomStatus.AVAILABLE.toString()) ? RoomStatus.AVAILABLE : 
						(roomsResultSet.getString("ROOMSTATUS").equalsIgnoreCase(RoomStatus.RENTED.toString()) ? 
								RoomStatus.RENTED : RoomStatus.MAINTENANCE);
			String featureSummary = roomsResultSet.getString("FEATURESUMMARY");
			String imagePath = roomsResultSet.getString("IMAGEPATH");

			Room room = null;
			if (roomType == RoomType.STANDARD) {
				room = new StandardRoom(
						roomID, 
						numberOfBeds,
						roomType,
						roomStatus,
						featureSummary,
						imagePath);
			}
			else {
				DateTime lastMaintenanceDate = CityLodgeUtil.convertDateToFormat(roomsResultSet.getDate("LASTMAINTENANCEDATE").toString(), "yyyy-MM-dd");
				room = new Suite(
						roomID, 
						numberOfBeds,
						roomType, 
						roomStatus,
						featureSummary,
						lastMaintenanceDate,
						imagePath);
			}
			room.setHiringRecords(getHiringRecordsByRoomId(room.getRoomId()));
			rooms.add(room);
		}

		return rooms;
	}

	/**
	 * This method is used to insert a hiring record in database
	 * 
	 * @param roomId
	 * @param hiringRecord
	 * @throws Exception
	 */
	public void addHiringRecord(String roomId, HiringRecord hiringRecord) throws Exception {
		stmt = con.createStatement();
		String insertQuery = CityLodgeUtil.BLANK;
		if (hiringRecord.getActualReturnDate() != null) {
			insertQuery = "INSERT INTO HIRINGRECORD ( \"RECORDID\", \"ROOMID\", \"RENTDATE\", " + 
					"\"ESTIMATEDRETURNDATE\", \"ACTUALRETURNDATE\", \"RENTALFEE\", \"LATEFEE\" ) " + 
					"VALUES ('" + 
					hiringRecord.getRecordId() + "', '" + 
					roomId + "', " + 
					"TO_DATE('" + hiringRecord.getRentDate().getFormattedDate() + "','dd/MM/yyyy') , " + 
					"TO_DATE('" + hiringRecord.getEstimatedReturnDate().getFormattedDate() + "','dd/MM/yyyy'), " + 
					"TO_DATE('" + hiringRecord.getActualReturnDate().getFormattedDate() + "','dd/MM/yyyy'), '" + 
					hiringRecord.getRentalFee() + "', '" + 
					hiringRecord.getLateFee() + "')";
		}
		else {
			insertQuery = "INSERT INTO HIRINGRECORD ( \"RECORDID\", \"ROOMID\", \"RENTDATE\", " + 
					"\"ESTIMATEDRETURNDATE\" ) " + 
					"VALUES ('" +
					hiringRecord.getRecordId() + "', '" +
					roomId + "', " + 
					"TO_DATE('" + hiringRecord.getRentDate().getFormattedDate() + "','dd/MM/yyyy'), " +
					"TO_DATE('" + hiringRecord.getEstimatedReturnDate().getFormattedDate() + "','dd/MM/yyyy'))";
		}
		result = stmt.executeUpdate(insertQuery);
	}

	/**
	 * This method is used to update a hiring record in database
	 * 
	 * @param roomId
	 * @param hiringRecord
	 * @throws Exception
	 */
	public void updateHiringRecord(String roomId, HiringRecord hiringRecord) throws Exception {
		stmt = con.createStatement();
		String updateQuery = "UPDATE HIRINGRECORD SET " +
				"ACTUALRETURNDATE = TO_DATE('" + hiringRecord.getActualReturnDate().getFormattedDate() + "', 'DD/MM/YYYY'), " +
				"RENTALFEE = '" + hiringRecord.getRentalFee() + "', " +
				"LATEFEE = '" + hiringRecord.getLateFee() + "' " +
				"WHERE RECORDID = '" + hiringRecord.getRecordId() + "'";

		result = stmt.executeUpdate(updateQuery);
	}

	/**
	 * This method is used to get all hiring record in database
	 * 
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HiringRecord> getHiringRecordsByRoomId(String roomId) throws Exception {
		ArrayList<HiringRecord> hiringRecordList = new ArrayList<HiringRecord>();
		stmt = con.createStatement();
		ResultSet hiringRecordResultSet = stmt.executeQuery("SELECT * FROM HIRINGRECORD WHERE ROOMID = '" + roomId.toUpperCase() +"'" );
		while (hiringRecordResultSet.next()) {
			HiringRecord hiringRecord = new HiringRecord(
					hiringRecordResultSet.getString("RECORDID"),
					CityLodgeUtil.convertDateToFormat(hiringRecordResultSet.getDate("RENTDATE").toString(), "yyyy-MM-dd"),
					CityLodgeUtil.convertDateToFormat(hiringRecordResultSet.getDate("ESTIMATEDRETURNDATE").toString(), "yyyy-MM-dd"));

			if (hiringRecordResultSet.getDate("ACTUALRETURNDATE") != null) {
				hiringRecord.setActualReturnDate(CityLodgeUtil.convertDateToFormat(hiringRecordResultSet.getDate("ACTUALRETURNDATE").toString(), "yyyy-MM-dd"));
			}
			hiringRecord.setRentalFee(hiringRecordResultSet.getDouble("RENTALFEE"));
			hiringRecord.setLateFee(hiringRecordResultSet.getDouble("LATEFEE"));

			hiringRecordList.add(hiringRecord);
			Collections.sort(hiringRecordList);
		}
		return hiringRecordList;
	}

	/**
	 * This method is used to delete all tables in database
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean clearData() throws Exception {
		stmt = con.createStatement();
		int roomResult = stmt.executeUpdate("DELETE from ROOM" );
		int hiringRecordResult = stmt.executeUpdate("DELETE from HIRINGRECORD" );
		return roomResult > 0 && hiringRecordResult > 0;
	}
}
