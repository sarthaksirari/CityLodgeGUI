package au.edu.rmit.adpro.citylodge.view;

import java.io.File;

import au.edu.rmit.adpro.citylodge.controller.DataTransferController;
import au.edu.rmit.adpro.citylodge.controller.DatabaseController;
import au.edu.rmit.adpro.citylodge.controller.RoomController;
import au.edu.rmit.adpro.citylodge.model.HiringRecord;
import au.edu.rmit.adpro.citylodge.model.Room;
import au.edu.rmit.adpro.citylodge.model.RoomStatus;
import au.edu.rmit.adpro.citylodge.model.RoomType;
import au.edu.rmit.adpro.citylodge.model.Suite;
import au.edu.rmit.adpro.citylodge.util.CityLodgeUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is used to generate the City Logde Application
 * 
 * @author Sarthak Sirari (S3766477)
 *
 */
public class CityLodgeView extends Application {


	private static final String CITY_LODGE = "CITY LODGE";
	
	private Stage mainStage;
	private Scene mainScene, roomDetailScene;
	private static DatabaseController databaseController;

	static {
		try {
			databaseController = new DatabaseController();
		} catch (Exception ex) {
			ErrorDialogBox.showDialogBox(ex);
		}
	}

	/**
	 * Main method of the Application
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			Application.launch(args);
		}
		catch( Exception ex)
		{
			ErrorDialogBox.showDialogBox(ex);
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.mainStage = primaryStage;
			this.mainStage.setMinWidth(1200);
			this.mainStage.setMinHeight(800);
			this.mainStage.setOnCloseRequest(event -> {
				event.consume();
				this.closeApplication();
			});

			this.createHomePage(FXCollections.observableArrayList(databaseController.getRooms()));
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (Exception ex) {
			ErrorDialogBox.showDialogBox(ex);
		}

	}

	/**
	 * This method is used to generate main Home page
	 * 
	 * @param rooms an object of Rooms 
	 */
	public void createHomePage(ObservableList<Room> rooms) {	
		try{
			VBox menuBarVBox = generateMenuBar();
			ScrollPane roomsDetail = generateRoomsDetail(rooms);
			BorderPane borderPane = new BorderPane();
			borderPane.setTop(menuBarVBox);
			borderPane.setCenter(roomsDetail);

			Scene scene = new Scene(borderPane, 1200, 800, Color.WHITE);
			this.mainScene = scene;
			this.mainStage.setTitle(CITY_LODGE);
			this.mainStage.setScene(mainScene);
			this.mainStage.show();
		}
		catch(Exception ex) {
			ErrorDialogBox.showDialogBox(ex);
		}
	}

	/**
	 * This method is used to generate the main Menu
	 * 
	 * @return an object of VBox
	 */
	public VBox generateMenuBar() {
		VBox vBox = new VBox();
		try {
			Menu addRoomMenu = generateAddRoomMenu();
			Menu dataTransferMenu = generateDataTransferMenu();

			MenuItem closeApplicationMenuItem = new MenuItem(CityLodgeUtil.CLOSE_APPLICATION);
			closeApplicationMenuItem.setOnAction(event -> this.closeApplication());
			Menu exitMenu = new Menu(CityLodgeUtil.EXIT);
			exitMenu.getItems().add(closeApplicationMenuItem);

			MenuBar menuBar = new MenuBar();
			menuBar.getMenus().addAll(addRoomMenu, dataTransferMenu, exitMenu);

			ImageView cityLodgeLogoImageView = new ImageView(new Image(new File(CityLodgeUtil.IMAGE_FOLDER_LOCATION + 
					CityLodgeUtil.IMAGE_CITY_LODGE_LOGO).toURI().toString()));
			cityLodgeLogoImageView.setFitHeight(150);
			cityLodgeLogoImageView.setFitWidth(400);


			HBox hBox = new HBox();
			hBox.setMinHeight(120);
			hBox.setStyle(CityLodgeUtil.MENU_BAR_STYLE);
			hBox.getChildren().addAll(cityLodgeLogoImageView);

			vBox.getChildren().addAll(menuBar,hBox);
		}
		catch(Exception ex) {
			ErrorDialogBox.showDialogBox(ex);
		}
		return vBox;
	}

	private Menu generateDataTransferMenu() {
		MenuItem importDataMenuItem = new MenuItem(CityLodgeUtil.IMPORT_DATA);
		importDataMenuItem.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(CityLodgeUtil.DATA_TRANSFER_DIRECTORY));
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(CityLodgeUtil.TEXT_FILE, 
					CityLodgeUtil.FILE_EXTENSION_TXT));

			File selectedFile = fileChooser.showOpenDialog(this.mainStage);
			if(selectedFile == null){
				UserAlertDialogBox.showDialogBox(CityLodgeUtil.IMPORT_DATA, CityLodgeUtil.FILE_NOT_SELECTED);
			} else {
				try {
					if (DataTransferController.importData(selectedFile.getAbsolutePath())) {
						UserAlertDialogBox.showDialogBox(CityLodgeUtil.IMPORT_DATA, CityLodgeUtil.FILE_IMPORT_SUCCESS);
						this.createHomePage(FXCollections.observableArrayList(databaseController.getRooms()));
					}
				} catch (Exception ex) {
					ErrorDialogBox.showDialogBox(ex);
				}
			}
		});
		MenuItem exportDataMenuItem = new MenuItem(CityLodgeUtil.EXPORT_DATA);
		exportDataMenuItem.setOnAction(e -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setInitialDirectory(new File(CityLodgeUtil.DATA_TRANSFER_DIRECTORY));

			File selectedFile = directoryChooser.showDialog(mainStage);
			if(selectedFile == null){
				UserAlertDialogBox.showDialogBox(CityLodgeUtil.IMPORT_DATA, CityLodgeUtil.DIRECTORY_NOT_SELECTED);
			}else{
				try {
					if(DataTransferController.exportData(selectedFile.getAbsolutePath())) {
						UserAlertDialogBox.showDialogBox(CityLodgeUtil.EXPORT_DATA, CityLodgeUtil.FILE_EXPORT_SUCCESS);
					}
				} catch (Exception ex) {
					ErrorDialogBox.showDialogBox(ex);
				}
			} 
		});

		Menu dataTransferMenu = new Menu(CityLodgeUtil.DATA_TRANSFER);
		dataTransferMenu.getItems().add(importDataMenuItem);
		dataTransferMenu.getItems().add(exportDataMenuItem);
		return dataTransferMenu;
	}

	private Menu generateAddRoomMenu() {
		MenuItem addStandardRoomMenuItem = new MenuItem(CityLodgeUtil.ADD_STANDARD_ROOM);
		addStandardRoomMenuItem.setOnAction(event -> {
			AddRoomDialogBox.showDialogBox(RoomType.STANDARD);
			try {
				this.createHomePage(FXCollections.observableArrayList(databaseController.getRooms()));
			} catch (Exception ex) {
				ErrorDialogBox.showDialogBox(ex);
			}
		});
		MenuItem addSuiteMenuItem = new MenuItem(CityLodgeUtil.ADD_SUITE);
		addSuiteMenuItem.setOnAction(event -> {
			AddRoomDialogBox.showDialogBox(RoomType.SUITE);
			try {
				this.createHomePage(FXCollections.observableArrayList(databaseController.getRooms()));
			} catch (Exception ex) {
				ErrorDialogBox.showDialogBox(ex);
			}
		});

		Menu addRoomMenu = new Menu(CityLodgeUtil.ADD_ROOM);
		addRoomMenu.getItems().add(addStandardRoomMenuItem);
		addRoomMenu.getItems().add(addSuiteMenuItem);
		return addRoomMenu;
	}

	/**
	 * This method is used to generate Room Details Functionality
	 * 
	 * @param rooms an Observable List of Room
	 * @return an object of ScrollPane
	 */
	public ScrollPane generateRoomsDetail(ObservableList<Room> rooms) {
		ScrollPane mainScrollPane = new ScrollPane();
		VBox vBox = new VBox(10);

		mainScrollPane.setStyle(CityLodgeUtil.SCROLL_PANE_STYLE);
		vBox.setPadding(new Insets(10,10,10,10));

		for(int i = 0; i < rooms.size() ; i++)
		{
			vBox.getChildren().add(createRoomListView(rooms.get(i)));
		}

		mainScrollPane.setContent(vBox);	  
		mainScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		mainScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		mainScrollPane.setFitToWidth(true);
		return mainScrollPane;
	}

	/**
	 * This method is used to generate the Rooms list view
	 * 
	 * @param room an object of Room
	 * @return an object of GridPane
	 */
	public GridPane createRoomListView(Room room) {
		ImageView roomImageView = new ImageView(new Image(new File(room.getImagePath() == null ? 
				CityLodgeUtil.IMAGE_FOLDER_LOCATION + CityLodgeUtil.IMAGE_NOT_AVAILABLE : 
					CityLodgeUtil.IMAGE_FOLDER_LOCATION + room.getImagePath()).toURI().toString()));
		roomImageView.setFitHeight(150);
		roomImageView.setFitWidth(250);

		Label roomTypeLabel = new Label(room.getRoomType().toString());
		roomTypeLabel.setStyle(CityLodgeUtil.ROOM_TITLE_STYLE);

		Label numberOfBedsLabel = new Label(Room.NUMBER_OF_BEDS + CityLodgeUtil.DETAILS_SEPARATOR + 
				CityLodgeUtil.SPACE + room.getNumberOfBeds());
		Label roomStatusLabel = new Label(Room.ROOM_STATUS + CityLodgeUtil.DETAILS_SEPARATOR + 
				CityLodgeUtil.SPACE + room.getRoomStatus().toString());
		Label featureSummaryLabel = new Label(Room.FEATURE_SUMMARY + CityLodgeUtil.DETAILS_SEPARATOR + 
				CityLodgeUtil.SPACE + room.getFeatureSummary());

		Label lastMaintenanceDateLabel = new Label();
		if (room instanceof Suite) {
			lastMaintenanceDateLabel = new Label(Suite.LAST_MAINTENANCE_DATE + CityLodgeUtil.DETAILS_SEPARATOR + 
					CityLodgeUtil.SPACE + ((Suite) room).getLastMaintenanceDate().getFormattedDate());
		}

		Button roomCostButton = new Button(CityLodgeUtil.DOLLAR + (int) room.getRoomRatePerNight() + 
				CityLodgeUtil.SPACE + CityLodgeUtil.PER_NIGHT.toLowerCase());
		roomCostButton.setStyle(CityLodgeUtil.COST_BUTTON_STYLE);

		Button roomSelectButton = new Button(CityLodgeUtil.SELECT);
		roomSelectButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		roomSelectButton.setOnAction(e -> {
			this.setroomDetailsScene(generateRoomDetailPage(room));
			this.mainStage.setScene(roomDetailScene); 
		});

		ColumnConstraints columnOne = new ColumnConstraints();
		columnOne.setHgrow(Priority.ALWAYS);
		ColumnConstraints columnTwo = new ColumnConstraints();
		columnTwo.setHgrow(Priority.ALWAYS);

		GridPane gridPane = generateRoomListGridView(roomImageView, roomTypeLabel, numberOfBedsLabel, roomStatusLabel,
				featureSummaryLabel, lastMaintenanceDateLabel, roomCostButton, roomSelectButton, columnTwo);

		return gridPane;
	}

	private GridPane generateRoomListGridView(ImageView roomImageView, Label roomTypeLabel, Label numberOfBedsLabel,
			Label roomStatusLabel, Label featureSummaryLabel, Label lastMaintenanceDateLabel, Button roomCostButton,
			Button roomSelectButton, ColumnConstraints columnTwo) {
		GridPane gridPane = new GridPane();
		gridPane.getColumnConstraints().addAll(new ColumnConstraints(80), columnTwo, new ColumnConstraints(300), columnTwo);
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		gridPane.setVgap(5);
		gridPane.setHgap(10);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setStyle(CityLodgeUtil.ROOM_LIST_STYLE);	
		gridPane.add(roomImageView, 0, 0, 3, 4);
		gridPane.add(roomTypeLabel, 2, 0);
		gridPane.add(numberOfBedsLabel, 2, 1);
		gridPane.add(roomStatusLabel, 3, 1);
		gridPane.add(featureSummaryLabel, 2, 3, 1, 6);
		gridPane.add(lastMaintenanceDateLabel, 2, 2);
		gridPane.add(roomCostButton, 6, 1);
		gridPane.add(roomSelectButton, 6, 3);
		return gridPane;
	}

	/**
	 * This method is used to generate Room details Page
	 * 
	 * @param room an object of Room
	 * @return an object of Scene
	 */
	public Scene generateRoomDetailPage(Room room) {	
		VBox topMenu = generateMenuBar();
		ScrollPane detailsPage =  generateRoomDetailsView(room);
		BorderPane borderPane = new BorderPane();

		borderPane.setTop(topMenu);	
		borderPane.setCenter(detailsPage);
		Scene scene = new Scene(borderPane, 1200, 800);

		return scene;
	}

	/**
	 * This method is used to generate Room details view
	 * 
	 * @param room an object of Room
	 * @return an object of ScrollPane
	 */
	public ScrollPane generateRoomDetailsView(Room room) {
		ScrollPane scrollPane = null;
		try {		
			Button backToHomeButton = getBackToHomeButton();
			ImageView roomImageView = new ImageView(new Image(new File(room.getImagePath() == null ? CityLodgeUtil.IMAGE_FOLDER_LOCATION + CityLodgeUtil.IMAGE_NOT_AVAILABLE : 
						CityLodgeUtil.IMAGE_FOLDER_LOCATION + room.getImagePath()).toURI().toString()));
			roomImageView.setFitWidth(600);
			roomImageView.setFitHeight(300);
			Label roomTypeLabel = new Label(room.getRoomType().toString());
			roomTypeLabel.setStyle(CityLodgeUtil.ROOM_TITLE_STYLE);
			Label numberOfBedsLabel = new Label(Room.NUMBER_OF_BEDS + CityLodgeUtil.DETAILS_SEPARATOR + CityLodgeUtil.SPACE + room.getNumberOfBeds());
			Label roomStatusLabel = new Label(Room.ROOM_STATUS + CityLodgeUtil.DETAILS_SEPARATOR + CityLodgeUtil.SPACE + room.getRoomStatus().toString());
			Label featureSummaryLabel = new Label(Room.FEATURE_SUMMARY + CityLodgeUtil.DETAILS_SEPARATOR + CityLodgeUtil.SPACE + room.getFeatureSummary());
			Label lastMaintenanceDateLabel = new Label();
			if (room instanceof Suite) {
				lastMaintenanceDateLabel = new Label(Suite.LAST_MAINTENANCE_DATE + CityLodgeUtil.DETAILS_SEPARATOR + 
						CityLodgeUtil.SPACE + ((Suite) room).getLastMaintenanceDate().getFormattedDate());
			}
			Button rentRoomButton = generateRentRoomButton(room);
			Button returnRoomButton = generateReturnRoomButton(room);
			Button performMaintenanceButton = generatePerformMaintenanceButton(room);
			Button completeMaintenanceButton = generateCompleteMaintenanceButton(room);
			if (room.getRoomStatus() == RoomStatus.AVAILABLE) {
				rentRoomButton.setDisable(false);
				returnRoomButton.setDisable(true);
				performMaintenanceButton.setDisable(false);
				completeMaintenanceButton.setDisable(true);
			} else if (room.getRoomStatus() == RoomStatus.MAINTENANCE) {
				rentRoomButton.setDisable(true);
				returnRoomButton.setDisable(true);
				performMaintenanceButton.setDisable(true);
				completeMaintenanceButton.setDisable(false);
			} else {
				rentRoomButton.setDisable(true);
				returnRoomButton.setDisable(false);
				performMaintenanceButton.setDisable(true);
				completeMaintenanceButton.setDisable(true);
			}
			GridPane gridPane = generateRoomDetailsPageGridPane(roomTypeLabel, numberOfBedsLabel, roomStatusLabel,
					featureSummaryLabel, lastMaintenanceDateLabel, rentRoomButton, returnRoomButton,
					performMaintenanceButton, completeMaintenanceButton);
			HBox hBox = new HBox(10);
			hBox.setMinWidth(1200);
			hBox.getChildren().addAll(roomImageView, gridPane);
			TableView tableView = createHiringRecordTable();
			tableView.setItems(FXCollections.observableArrayList(
					databaseController.getHiringRecordsByRoomId(room.getRoomId())));
			VBox vBox = new VBox(10); 
			vBox.setPadding(new Insets(10,10,10,10));
			vBox.getChildren().addAll(backToHomeButton, hBox, tableView);
			scrollPane = new ScrollPane();
			scrollPane.setContent(vBox);
		}
		catch(Exception exception) {
			ErrorDialogBox.showDialogBox(exception);
		}
		return scrollPane;
	}

	private GridPane generateRoomDetailsPageGridPane(Label roomTypeLabel, Label numberOfBedsLabel,
			Label roomStatusLabel, Label featureSummaryLabel, Label lastMaintenanceDateLabel, Button rentRoomButton,
			Button returnRoomButton, Button performMaintenanceButton, Button completeMaintenanceButton) {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		gridPane.setVgap(15);
		gridPane.setHgap(40);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.add(roomTypeLabel, 0, 0);
		gridPane.add(numberOfBedsLabel, 0, 1);
		gridPane.add(roomStatusLabel, 1, 1);
		gridPane.add(lastMaintenanceDateLabel, 0, 2);
		gridPane.add(featureSummaryLabel, 0, 3);
		gridPane.add(rentRoomButton, 0, 5);
		gridPane.add(returnRoomButton, 1, 5);
		gridPane.add(performMaintenanceButton, 0, 6);
		gridPane.add(completeMaintenanceButton, 1, 6);
		return gridPane;
	}

	private Button generateCompleteMaintenanceButton(Room room) {
		Button completeMaintenanceButton = new Button(CityLodgeUtil.COMPLETE_MAINTENANCE);
		completeMaintenanceButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		completeMaintenanceButton.setOnAction(e -> {
			if(room instanceof Suite)
				CompleteMaintenanceDialogBox.showDialogBox(room);
			else {
				try {
					new RoomController().completeMaintenance(room, null);
				} catch (Exception ex) {
					UserAlertDialogBox.showDialogBox((CityLodgeUtil.COMPLETE_MAINTENANCE + CityLodgeUtil.SPACE + 
							CityLodgeUtil.ERROR).toUpperCase(), ex.getMessage());
				}
			}
			this.setroomDetailsScene(generateRoomDetailPage(room));
			this.mainStage.setScene(roomDetailScene);
		});
		return completeMaintenanceButton;
	}

	private Button generatePerformMaintenanceButton(Room room) {
		Button performMaintenanceButton = new Button(CityLodgeUtil.MAINTENANCE);
		performMaintenanceButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		performMaintenanceButton.setOnAction(event -> {
			try {
				new RoomController().performMaintenance(room);
			} catch (Exception ex) {
				UserAlertDialogBox.showDialogBox((CityLodgeUtil.MAINTENANCE + CityLodgeUtil.SPACE + 
						CityLodgeUtil.ERROR).toUpperCase(), ex.getMessage());
			}
			this.setroomDetailsScene(generateRoomDetailPage(room));
			this.mainStage.setScene(roomDetailScene);
		});
		return performMaintenanceButton;
	}

	private Button generateReturnRoomButton(Room room) {
		Button returnRoomButton = new Button(CityLodgeUtil.RETURN_ROOM);
		returnRoomButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		returnRoomButton.setOnAction(event -> {
			RoomReturnDialogBox.showDialogBox(room);
			this.setroomDetailsScene(generateRoomDetailPage(room));
			this.mainStage.setScene(roomDetailScene); 
		});
		return returnRoomButton;
	}

	private Button generateRentRoomButton(Room room) {
		Button rentRoomButton = new Button(CityLodgeUtil.RENT_ROOM);
		rentRoomButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		rentRoomButton.setOnAction(event -> {
			RoomRentDialogBox.showDialogBox(room);
			this.setroomDetailsScene(generateRoomDetailPage(room));
			this.mainStage.setScene(roomDetailScene); 
		});
		return rentRoomButton;
	}

	private Button getBackToHomeButton() {
		Button backToHomeButton = new Button(CityLodgeUtil.BACK_TO_HOME.toUpperCase());
		backToHomeButton.setStyle(CityLodgeUtil.BUTTON_STYLE);
		backToHomeButton.setOnAction(e -> {
			try {
				this.createHomePage(FXCollections.observableArrayList(databaseController.getRooms()));
				this.mainStage.setScene(mainScene);
			} catch (Exception ex) {
				ErrorDialogBox.showDialogBox(ex);
			}
		});
		return backToHomeButton;
	}

	/**
	 * This method is used to create Hiring Record table
	 * 
	 * @return an object of TableView
	 */
	public TableView createHiringRecordTable() {
		TableView tableView = new TableView();

		TableColumn<HiringRecord, String> recordIdColumn = new TableColumn<HiringRecord, String>(HiringRecord.RECORD_ID);		
		recordIdColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.RECORD_ID_TABLE));
		TableColumn<HiringRecord, String> rentDateColumn = new TableColumn<HiringRecord, String>(HiringRecord.RENT_DATE);	
		rentDateColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.RENT_DATE_TABLE));
		TableColumn<HiringRecord, String> estimatedDateColumn = new TableColumn<HiringRecord, String>(HiringRecord.ESTIMATED_RETURN_DATE);
		estimatedDateColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.ESTIMATED_RETURN_DATE_TABLE));
		TableColumn<HiringRecord, String> actualDateColumn = new TableColumn<HiringRecord, String>(HiringRecord.ACTUAL_RETURN_DATE);
		actualDateColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.ACTUAL_RETURN_DATE_TABLE));
		TableColumn<HiringRecord, String> rentalFeeColumn = new TableColumn<HiringRecord, String>(HiringRecord.RENTAL_FEE);
		rentalFeeColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.RENTAL_FEE_TABLE));
		TableColumn<HiringRecord, String> lateFeeColumn = new TableColumn<HiringRecord, String>(HiringRecord.LATE_FEE);
		lateFeeColumn.setCellValueFactory(new PropertyValueFactory<HiringRecord,String>(HiringRecord.LATE_FEE_TABLE));

		tableView.getColumns().addAll(recordIdColumn, rentDateColumn, estimatedDateColumn, actualDateColumn, rentalFeeColumn, lateFeeColumn);
		return tableView;
	}

	/**
	 * This method is used to close the cpplication 
	 */
	private void closeApplication() {
		if (CloseApplicationDialogBox.showDialogBox()) {
			mainStage.close();
		}
	}

	public static DatabaseController getDatabaseController() {
		return databaseController;
	}

	public void setroomDetailsScene(Scene detailPageScene) {
		this.roomDetailScene = detailPageScene;
	}
}
