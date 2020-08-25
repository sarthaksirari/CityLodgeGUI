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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for Return Room
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class RoomReturnDialogBox {

	public static void showDialogBox(Room room) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(CityLodgeUtil.RETURN_ROOM.toUpperCase());
		stage.setMinWidth(300);

		Label returnLabel = new Label(HiringRecord.ACTUAL_RETURN_DATE);

		DatePicker returnDatePicker = new DatePicker();
		returnDatePicker.setValue(LocalDate.now());
		returnDatePicker.setDayCellFactory(value -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		// Checks validity of return date and performs necessary operations to return a vehicle
		Button returnButton = new Button(CityLodgeUtil.RETURN);
		returnButton.setOnAction(event -> {
			LocalDate returnDate = returnDatePicker.getValue();
			DateTime returnDateFormatted = new DateTime(returnDate.getDayOfMonth(), 
					returnDate.getMonthValue(), returnDate.getYear());
			try {
				new RoomController().returnRoom(room, returnDateFormatted);
			} catch (Exception ex) {
				UserAlertDialogBox.showDialogBox((CityLodgeUtil.RETURN_ROOM + CityLodgeUtil.SPACE + 
						CityLodgeUtil.ERROR).toUpperCase(), ex.getMessage());
			}
			stage.close();
		});


		Button closeButton = new Button(CityLodgeUtil.CLOSE);
		closeButton.setOnAction(event -> stage.close());

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20,20,20,20));
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(35);
		gridPane.add(returnLabel, 0,0);
		gridPane.add(returnDatePicker, 1, 0);

		HBox hBox = new HBox(15);
		hBox.getChildren().addAll(returnButton, closeButton);

		VBox vBox = new VBox(25);
		vBox.setMinWidth(300);
		vBox.setMinHeight(150);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(gridPane, hBox);
		vBox.setPadding(new Insets(20,20,20,20));

		stage.setScene(new Scene(vBox));
		stage.showAndWait();
	}
}
