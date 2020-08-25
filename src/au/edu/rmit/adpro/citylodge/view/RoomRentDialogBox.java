package au.edu.rmit.adpro.citylodge.view;

import java.time.LocalDate;

import au.edu.rmit.adpro.citylodge.controller.RoomController;
import au.edu.rmit.adpro.citylodge.model.HiringRecord;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for Rent Room
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomRentDialogBox {

	public static void showDialogBox(Room room) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(CityLodgeUtil.RENT_ROOM.toUpperCase());
		stage.setMinWidth(500);

		Label headingLabel = new Label(CityLodgeUtil.ENTER_THE_FOLLOWING_DETAILS);
		Label customerIdLabel = new Label(HiringRecord.CUSTOMER_ID + CityLodgeUtil.SPACE + 
				CityLodgeUtil.BRACKET_START + CityLodgeUtil.THREE_DIGITS_ONLY + CityLodgeUtil.BRACKET_END);
		Label rentDateLabel = new Label(HiringRecord.RENT_DATE);
		Label numberOfDaysLabel = new Label(Room.NUMBER_OF_DAYS);

		TextField customerIdTextField = new TextField();
		customerIdTextField.setPromptText(CityLodgeUtil.THREE_DIGITS_ONLY);

		DatePicker rentDatePicker = new DatePicker();
		rentDatePicker.setPromptText(CityLodgeUtil.DATE_FORMAT);
		rentDatePicker.setValue(LocalDate.now());
		rentDatePicker.setDayCellFactory(value -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		TextField numberOfDaysTextField = new TextField();

		// Validate user inputs and book a vehicle on submit
		Button bookButton = new Button(CityLodgeUtil.BOOK);
		bookButton.setOnAction(event -> { 
			StringBuilder textFieldErrors = new StringBuilder(CityLodgeUtil.BLANK);

			String customerIdError = CityLodgeUtil.validateUserInput(HiringRecord.CUSTOMER_ID, customerIdTextField.getText());
			if (!customerIdError.trim().isEmpty()) {
				textFieldErrors.append(customerIdError);
				customerIdTextField.clear();
			}

			String numberOfDaysError = CityLodgeUtil.validateUserInput(Room.NUMBER_OF_DAYS, numberOfDaysTextField.getText());
			if (!numberOfDaysError.trim().isEmpty()) {
				textFieldErrors.append(numberOfDaysError);
				numberOfDaysTextField.clear();
			}

			if(!textFieldErrors.toString().trim().isEmpty()) {
				UserAlertDialogBox.showDialogBox((CityLodgeUtil.INPUT + CityLodgeUtil.SPACE + 
						CityLodgeUtil.ERROR).toUpperCase(), 
						textFieldErrors.toString());
			} else {
				String customerId = customerIdTextField.getText();
				LocalDate rentDate = rentDatePicker.getValue();
				DateTime rentDateFormatted = new DateTime(rentDate.getDayOfMonth(), 
						rentDate.getMonthValue(), rentDate.getYear());
				int numberOfDays = Integer.parseInt(numberOfDaysTextField.getText());
				try {
					new RoomController().rentRoom(room, customerId, rentDateFormatted, numberOfDays);
				} catch (Exception ex) {
					UserAlertDialogBox.showDialogBox((CityLodgeUtil.RENT_ROOM + CityLodgeUtil.SPACE + 
							CityLodgeUtil.ERROR).toUpperCase(), 
							ex.getMessage());
				}
				stage.close();
			}	
		});

		// To clear all inputs in TextFields
		Button clearFieldsButton = new Button(CityLodgeUtil.CLEAR_FIELDS);
		clearFieldsButton.setOnAction(event -> {
			customerIdTextField.clear();
			numberOfDaysTextField.clear();
		}); 

		// Close the current view dialog for Booking Vehicle  
		Button closeButton = new Button(CityLodgeUtil.CLOSE);
		closeButton.setOnAction(event -> stage.close());

		ColumnConstraints columnOne = new ColumnConstraints();
		columnOne.setHgrow(Priority.ALWAYS);
		
		// Add to Layout
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		gridPane.getColumnConstraints().addAll(new ColumnConstraints(220), columnOne);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(35);
		gridPane.add(headingLabel, 0, 0);
		gridPane.add(customerIdLabel, 0, 1);
		gridPane.add(customerIdTextField, 1, 1);
		gridPane.add(rentDateLabel, 0, 2);
		gridPane.add(rentDatePicker, 1, 2);
		gridPane.add(numberOfDaysLabel, 0, 3);
		gridPane.add(numberOfDaysTextField, 1, 3);

		HBox hBox = new HBox(15);
		hBox.getChildren().addAll(clearFieldsButton, bookButton, closeButton);

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
