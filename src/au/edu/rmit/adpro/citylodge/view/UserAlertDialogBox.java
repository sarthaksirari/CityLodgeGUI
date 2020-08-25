package au.edu.rmit.adpro.citylodge.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for User Alert messgaes  
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class UserAlertDialogBox {

	private final static String OK = "OK";

	public static void showDialogBox(String header, String alertMessage) {
		Stage stage = new Stage();
		stage.setMinWidth(300);
		stage.setMinHeight(200);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(header);

		Button okButton = new Button(OK);
		okButton.setOnAction(value -> stage.close());

		VBox vBox = new VBox(25);	
		vBox.getChildren().addAll(new Label(alertMessage), okButton);
		vBox.setAlignment(Pos.CENTER);

		stage.setScene(new Scene(vBox));
		stage.showAndWait();
	}
}
