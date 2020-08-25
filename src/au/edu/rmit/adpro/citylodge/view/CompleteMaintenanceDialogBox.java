package au.edu.rmit.adpro.citylodge.view;

import java.time.LocalDate;

import au.edu.rmit.adpro.citylodge.controller.RoomController;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.Suite;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import au.edu.rmit.adpro.citylodge.util.DateTime;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for Completing Maintenance
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class CompleteMaintenanceDialogBox {

	public static void showDialogBox(Room room) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(CityLodgeUtil.COMPLETE_MAINTENANCE.toUpperCase());
		stage.setMinWidth(300);

		Label returnLabel = new Label(Suite.LAST_MAINTENANCE_DATE);

		DatePicker maintenanceCompletionDatePicker = new DatePicker();
		maintenanceCompletionDatePicker.setValue(LocalDate.now());
		maintenanceCompletionDatePicker.setDayCellFactory(value -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		// Checks validity of return date and performs necessary operations to return a vehicle
		Button completeButton = new Button(CityLodgeUtil.COMPLETE);
		completeButton.setOnAction(event -> {
			LocalDate returnDate = maintenanceCompletionDatePicker.getValue();
			DateTime returnDateFormatted = new DateTime(returnDate.getDayOfMonth(), 
					returnDate.getMonthValue(), returnDate.getYear());
			try {
				new RoomController().completeMaintenance(room, returnDateFormatted);
			} catch (Exception ex) {
				UserAlertDialogBox.showDialogBox((CityLodgeUtil.COMPLETE_MAINTENANCE + CityLodgeUtil.SPACE + 
						CityLodgeUtil.ERROR).toUpperCase(), ex.getMessage());
			}
			stage.close();
		});


		Button closeButton = new Button(CityLodgeUtil.CLOSE);
		closeButton.setOnAction(event -> stage.close());
		
		ColumnConstraints columnOne = new ColumnConstraints();
		columnOne.setHgrow(Priority.ALWAYS);

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20,20,20,20));
		gridPane.getColumnConstraints().addAll(new ColumnConstraints(220), columnOne);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		gridPane.setHgap(35);
		gridPane.add(returnLabel, 0,0);
		gridPane.add(maintenanceCompletionDatePicker, 1, 0);

		HBox hBox = new HBox(15);
		hBox.getChildren().addAll(completeButton, closeButton);

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
