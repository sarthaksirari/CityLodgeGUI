package au.edu.rmit.adpro.citylodge.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import au.edu.rmit.adpro.citylodge.controller.RoomController;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.RoomStatus;
import au.edu.rmit.adpro.citylodge.model.RoomType;
import au.edu.rmit.adpro.citylodge.model.StandardRoom;
import au.edu.rmit.adpro.citylodge.model.Suite;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for adding a Room
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class AddRoomDialogBox {

	public static void showDialogBox(RoomType roomType) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle((CityLodgeUtil.ADD + CityLodgeUtil.SPACE + roomType.toString()).toUpperCase());
		stage.setMinWidth(500);
		stage.setMinHeight(500);

		Label headingLabel = new Label(CityLodgeUtil.ENTER_THE_FOLLOWING_DETAILS);
		Label roomIdLabel = new Label(Room.ROOM_ID + CityLodgeUtil.SPACE + 
				CityLodgeUtil.BRACKET_START + CityLodgeUtil.THREE_DIGITS_ONLY + CityLodgeUtil.BRACKET_END);
		Label numberOfBedsLabel = new Label(Room.NUMBER_OF_BEDS);
		Label lastMaintenanceDateLabel = new Label(Suite.LAST_MAINTENANCE_DATE);
		Label featureSummaryLabel = new Label(Room.FEATURE_SUMMARY);
		Label imageLabel = new Label(CityLodgeUtil.IMAGE);

		TextField roomIdTextField = new TextField();
		roomIdTextField.setPromptText(CityLodgeUtil.THREE_DIGITS_ONLY);

		ChoiceBox<String> numberOfBedsChoiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(
				Integer.toString(StandardRoom.NUMBER_OF_BEDS_ONE), 
				Integer.toString(StandardRoom.NUMBER_OF_BEDS_TWO), 
				Integer.toString(StandardRoom.NUMBER_OF_BEDS_FOUR)));
		numberOfBedsChoiceBox.getSelectionModel().selectFirst();

		DatePicker lastMaintenanceDatePicker = new DatePicker();
		lastMaintenanceDatePicker.setPromptText(CityLodgeUtil.DATE_FORMAT);
		lastMaintenanceDatePicker.setValue(LocalDate.now());
		lastMaintenanceDatePicker.setDayCellFactory(value -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		TextField featureSummaryTextField = new TextField();
		featureSummaryTextField.setPromptText(CityLodgeUtil.MAX_TWENTY_WORDS);

		TextField imageFileTextField = new TextField(CityLodgeUtil.IMAGE_NOT_SELECTED);
		imageFileTextField.setDisable(true);

		Button imageFileSelectorButton = new Button(CityLodgeUtil.IMAGE_SELECT);
		imageFileSelectorButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(CityLodgeUtil.IMAGE_FILE, 
					CityLodgeUtil.IMAGE_EXTENSION_JPG, CityLodgeUtil.IMAGE_EXTENSION_JPEG));
			File selectedFile = fileChooser.showOpenDialog(stage);     
			if (selectedFile != null ) {
				Path imageOriginalPath = selectedFile.toPath();
				imageFileTextField.setText(imageOriginalPath.toString());
			}
		});

		// Validate user inputs and book a vehicle on submit
		Button addButton = new Button(CityLodgeUtil.ADD);
		addButton.setOnAction(event -> { 
			StringBuilder textFieldErrors = new StringBuilder(CityLodgeUtil.BLANK);

			String roomIdError = CityLodgeUtil.validateUserInput(Room.ROOM_ID, roomIdTextField.getText());
			if (!roomIdError.trim().isEmpty()) {
				textFieldErrors.append(roomIdError);
				roomIdTextField.clear();
			}

			String featureSummaryError = CityLodgeUtil.validateUserInput(Room.FEATURE_SUMMARY, 
					featureSummaryTextField.getText());
			if (!featureSummaryError.trim().isEmpty()) {
				textFieldErrors.append(featureSummaryError);
			}

			if(!textFieldErrors.toString().trim().isEmpty()) {
				UserAlertDialogBox.showDialogBox((CityLodgeUtil.INPUT + CityLodgeUtil.SPACE + 
						CityLodgeUtil.ERROR).toUpperCase(), 
						textFieldErrors.toString());
			} else {
				String roomIdPrefix = roomType == RoomType.STANDARD ? 
						CityLodgeUtil.STANDARD_ROOM_ID_STARTING_CHARACTER + CityLodgeUtil.UNDERSCORE : 
							CityLodgeUtil.SUITE_ID_STARTING_CHARACTER + CityLodgeUtil.UNDERSCORE;
				String roomId = roomIdPrefix + roomIdTextField.getText();

				Path imageProjectPath = Paths.get(CityLodgeUtil.IMAGE_FOLDER_LOCATION + roomId + 
						CityLodgeUtil.IMAGE_EXTENSION);
				Path imageTempProjectPath = Paths.get(imageFileTextField.getText());
				if(!imageFileTextField.getText().equalsIgnoreCase(CityLodgeUtil.IMAGE_NOT_SELECTED)) {
					try {
						Files.copy(imageTempProjectPath, imageProjectPath, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException ex) {
						UserAlertDialogBox.showDialogBox(CityLodgeUtil.IMAGE_FILE_ERROR, CityLodgeUtil.IMAGE_IMPORT_ERROR);
						imageFileTextField.setText(CityLodgeUtil.IMAGE_NOT_SELECTED);
						return;
					}
				}

				int numberOfBeds = Integer.parseInt(numberOfBedsChoiceBox.getValue());
				LocalDate lastMaintenanceDate = lastMaintenanceDatePicker.getValue();
				DateTime lastMaintenanceDateFormatted = new DateTime(lastMaintenanceDate.getDayOfMonth(), 
						lastMaintenanceDate.getMonthValue(), lastMaintenanceDate.getYear());
				String featureSummary = featureSummaryTextField.getText();
				String imagePath = imageFileTextField.getText().equalsIgnoreCase(CityLodgeUtil.IMAGE_NOT_SELECTED) ? 
						CityLodgeUtil.IMAGE_NOT_AVAILABLE :imageProjectPath.getFileName().toString();

				Room room = null;
				if (roomType == RoomType.STANDARD) {
					room = new StandardRoom(roomId, numberOfBeds, roomType, RoomStatus.AVAILABLE, 
							featureSummary, imagePath);
				} else {
					room = new Suite(roomId, Suite.NUMBER_OF_BEDS_SIX, roomType, RoomStatus.AVAILABLE, 
							featureSummary, lastMaintenanceDateFormatted, imagePath);
				}

				try {
					new RoomController().addRoom(room);
				} catch (Exception ex) {
					UserAlertDialogBox.showDialogBox((CityLodgeUtil.ADD_ROOM + CityLodgeUtil.SPACE + 
							CityLodgeUtil.ERROR).toUpperCase(), ex.getMessage());
					return;
				}
				stage.close();
			}	
		});

		Button clearFieldsButton = new Button(CityLodgeUtil.CLEAR_FIELDS);
		clearFieldsButton.setOnAction(event -> {
			roomIdTextField.clear();
			numberOfBedsChoiceBox.getSelectionModel().selectFirst();
			lastMaintenanceDatePicker.setValue(LocalDate.now());
			featureSummaryTextField.clear();
			imageFileTextField.setText(CityLodgeUtil.IMAGE_NOT_SELECTED);
		}); 

		Button closeButton = new Button(CityLodgeUtil.CLOSE);
		closeButton.setOnAction(event -> stage.close());

		ColumnConstraints columnOne = new ColumnConstraints();
		columnOne.setHgrow(Priority.ALWAYS);
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.getColumnConstraints().addAll(new ColumnConstraints(200), columnOne);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(35);
		gridPane.add(headingLabel, 0, 0);
		gridPane.add(roomIdLabel, 0, 1);
		gridPane.add(roomIdTextField, 1, 1);
		gridPane.add(featureSummaryLabel, 0, 3);
		gridPane.add(featureSummaryTextField, 1, 3);
		gridPane.add(imageLabel, 0, 4);
		gridPane.add(imageFileTextField, 1, 4);
		gridPane.add(imageFileSelectorButton, 0, 5);

		if (roomType == RoomType.STANDARD) {
			gridPane.add(numberOfBedsLabel, 0, 2);
			gridPane.add(numberOfBedsChoiceBox, 1, 2);
		} else {
			gridPane.add(lastMaintenanceDateLabel, 0, 2);
			gridPane.add(lastMaintenanceDatePicker, 1, 2);
		}

		HBox hBox = new HBox(15);
		hBox.getChildren().addAll(clearFieldsButton, addButton, closeButton);

		VBox vBox = new VBox(25);
		vBox.setMinWidth(450);
		vBox.setMinHeight(300);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(gridPane, hBox);
		vBox.setPadding(new Insets(20,20,20,20));

		stage.setScene(new Scene(vBox));
		stage.showAndWait();
	}
}
