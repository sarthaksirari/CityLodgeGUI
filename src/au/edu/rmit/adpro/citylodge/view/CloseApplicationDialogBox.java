package au.edu.rmit.adpro.citylodge.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class is used to generate a Dialog Box for Closing the Application
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class CloseApplicationDialogBox {

	private static final String CLOSE_APPLICATION = "Close Application";
	private static final String CLOSE_CITY_LODGE_APPLICATION = "Close City Lodge Application?";
	private static final String NO = "NO";
	private static final String YES = "YES";
	public static boolean showDialogBox() {
		boolean answer = false;
		Stage alertStage = new Stage();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle(CLOSE_APPLICATION);
		alert.setContentText(CLOSE_CITY_LODGE_APPLICATION);

		ButtonType yesButton = new ButtonType(YES);
		ButtonType noButton = new ButtonType(NO);
		
		alert.getButtonTypes().setAll(yesButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesButton){
			answer = true;
			alertStage.close();
		} else if (result.get() == noButton) {
			answer = false;
			alertStage.close();
		}
	
		return answer;
	}
}
