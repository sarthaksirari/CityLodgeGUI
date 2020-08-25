package au.edu.rmit.adpro.citylodge.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class is used to generate a Dialog Box for Error Messages
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class ErrorDialogBox {

	// change names
	public static void showDialogBox(Exception ex) {
	
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Something went wrong !!");
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
}
