package au.edu.rmit.adpro.citylodge.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import au.edu.rmit.adpro.citylodge.exception.FileExportException;
import au.edu.rmit.adpro.citylodge.exception.FileImportException;
import au.edu.rmit.adpro.citylodge.model.HiringRecord;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.RoomStatus;
import au.edu.rmit.adpro.citylodge.model.RoomType;
import au.edu.rmit.adpro.citylodge.model.StandardRoom;
import au.edu.rmit.adpro.citylodge.model.Suite;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;
import au.edu.rmit.adpro.citylodge.view.CityLodgeView;

/**
 * This class is used as the Controller for Data Transfer related operations
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class DataTransferController {

	public static final String EXPORT_FILE = "export_file.txt";

	/**
	 * This method is used to import data from Text File
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static boolean importData(String filepath) throws Exception {
		try {
			Scanner scanner = new Scanner(new FileInputStream(filepath));
			String roomId = null;

			CityLodgeView.getDatabaseController().clearData();

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Room room = null;
				HiringRecord hiringRecord = null;

				String lineItem[] = line.split(Character.toString(CityLodgeUtil.DETAILS_SEPARATOR));
				for(int i = 0; i < lineItem.length; i++) {
					lineItem[i] = lineItem[i].trim();
				}

				// Create and Insert Vehicle Objects in Vehicle Database
				if(lineItem[0].length() == 5 || lineItem[2].trim().equalsIgnoreCase(RoomType.STANDARD.toString()) || 
						lineItem[2].trim().equalsIgnoreCase(RoomType.SUITE.toString())) {
					roomId = lineItem[0];
					int numberOfBeds = Integer.parseInt(lineItem[1]);
					RoomType roomType = lineItem[2].trim().equalsIgnoreCase(RoomType.STANDARD.toString()) ? 
							RoomType.STANDARD : RoomType.SUITE;
					RoomStatus roomStatus = lineItem[3].trim().equalsIgnoreCase(RoomStatus.AVAILABLE.toString()) ? 
							RoomStatus.AVAILABLE : (lineItem[3].trim().equalsIgnoreCase(RoomStatus.RENTED.toString()) ? 
									RoomStatus.RENTED : RoomStatus.MAINTENANCE);
					String featureSummary;
					DateTime lastMaintenanceDate;
					String imagepath;

					if(roomType == RoomType.STANDARD) {
						featureSummary = lineItem[4];
						imagepath = lineItem[5];
						room = new StandardRoom(roomId, numberOfBeds, roomType, roomStatus, featureSummary, imagepath);
					} else {
						lastMaintenanceDate = CityLodgeUtil.convertDateToFormat(lineItem[4].trim(), CityLodgeUtil.DATE_FORMAT);
						featureSummary = lineItem[5];
						imagepath = lineItem[6];
						room = new Suite(roomId, numberOfBeds, roomType, roomStatus, featureSummary, lastMaintenanceDate, imagepath);		
					}
					new RoomController().addRoom(room);
				}else {
					// Create and Insert RentalRecord data into Database
					String recordId = lineItem[0];
					DateTime rentDate = CityLodgeUtil.convertDateToFormat(lineItem[1].trim(), CityLodgeUtil.DATE_FORMAT);
					DateTime estimatedReturnDate = CityLodgeUtil.convertDateToFormat(lineItem[2].trim(), CityLodgeUtil.DATE_FORMAT);

					hiringRecord = new HiringRecord(recordId,rentDate, estimatedReturnDate);

					if(!(lineItem[3].trim().equalsIgnoreCase(HiringRecord.DETAILS_NONE))) {
						DateTime actualReturnDate = CityLodgeUtil.convertDateToFormat(lineItem[3].trim(), CityLodgeUtil.DATE_FORMAT);
						double rentalFee = Double.parseDouble(lineItem[4]);
						double lateFee = Double.parseDouble(lineItem[5]);

						hiringRecord.setActualReturnDate(actualReturnDate);
						hiringRecord.setRentalFee(rentalFee);
						hiringRecord.setLateFee(lateFee);
					}
					CityLodgeView.getDatabaseController().addHiringRecord(roomId, hiringRecord);
				}
			}
			scanner.close();
		}	catch(Exception ex) {
			throw new FileImportException();
		}
		return true;
	}

	/**
	 * This method is used to export data to a Text File
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public static boolean exportData(String filepath) throws Exception {
		try {
			FileWriter writer = new FileWriter(new File(filepath, EXPORT_FILE));
			ArrayList<Room> rooms = CityLodgeView.getDatabaseController().getRooms();

			// Export vehicles and records to a text file
			for(Room room : rooms) {
				writer.write(room.toString());
				writer.write(System.getProperty( "line.separator" ));

				List<HiringRecord> hiringRecords = CityLodgeView.getDatabaseController().getHiringRecordsByRoomId(
						room.getRoomId());
				for(HiringRecord hiringRecord : hiringRecords) {
					writer.write(hiringRecord.toString());
					writer.write(System.getProperty( "line.separator" ));
				}
			}		
			writer.close();
		} catch (Exception ex) {
			throw new FileExportException();
		}
		return true;
	}
}
