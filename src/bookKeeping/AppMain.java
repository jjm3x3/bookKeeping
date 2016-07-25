package bookKeeping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AppMain extends Application {

	private static final String DB_URL = "jdbc:h2:~/bookKeeping";
	private static final String DB_PASSWORD = "secret";
	private static final String DB_USERNAME = "accountant";

	public static void main(String[] args) {
		System.out.println("HI brenna");
		launch(args);

	}

	@Override
	public void start(Stage firstStage) throws Exception {
		TableView<Account> table = new TableView<>();
		TableColumn<Account, String> nameColumn = new TableColumn("Account Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("name"));
		TableColumn<Account, Double> amountColumn = new TableColumn("Current Balence");
		amountColumn.setCellValueFactory(new PropertyValueFactory<Account, Double>("initAmount"));
		table.getColumns().add(nameColumn);
		table.getColumns().add(amountColumn);
		Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
		Statement stmt = dbConn.createStatement();
		ResultSet result = stmt.executeQuery("select * from accounts");
		ObservableList<Account> accountsList = FXCollections.observableArrayList();
		while(result.next()){
			String name = result.getString("name");
			double accountStart = result.getDouble("initVal");
			accountsList.add(new Account(name, accountStart));
		}
		dbConn.close();
		table.setItems(accountsList);
		Button btn = new Button();
		btn.setText("Add an account");
		btn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Hi brenna");
				
//				Dialog alert = new Alert(Alert.AlertType.NONE,"some account was added",ButtonType.OK);
//				Dialog alert = new TextInputDialog("some example text");
				Dialog alert = new AddAccountDialog();
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() ) {
//						result.get() 
					System.out.println("so the account was finalized now with result: " + result.get());
				}
			}
			
		});
		GridPane gridPane = new GridPane();
		gridPane.add(table,0, 0);
		gridPane.add(btn, 0, 1);
		
		Scene scene = new Scene(gridPane, 300, 300);
		firstStage.setScene(scene);
		firstStage.setTitle("booKeeping");
		firstStage.show();
	}

	class AddAccountDialog extends Dialog<ButtonType> {

		AddAccountDialog() {
			DialogPane node = new DialogPane();
			GridPane gridPane = new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			gridPane.setHgap(10);
			gridPane.setVgap(10);

			TextField nameField = new TextField();
			nameField.setPromptText("Account Name");
			TextField startAmountBox = new TextField();
			startAmountBox.setPromptText("set start amount");

			ButtonType okButtonType =  new ButtonType("OK");
			node.getButtonTypes().add(okButtonType);

			Button okButton = (Button) node.lookupButton(okButtonType);
//			 = new Button();
			okButton.setText("ok");
			okButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					String accountName = nameField.getText();
					double accountStartAmount = Double.parseDouble(startAmountBox.getText());
					System.out.println("account name is: " + accountName);
					System.out.println("with starting amount: " + startAmountBox.getText());
					try {
						Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
						Statement stmt = dbConn.createStatement();
						boolean tableCreated = stmt.execute("create table if not exists accounts(id identity,name VARCHAR(50),initVal double)");
						boolean entryAdded = stmt.execute("insert into accounts(id,name,initVal) values(NULL, '" + accountName + "'," + accountStartAmount + ")");
						dbConn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AddAccountDialog.this.hide();
				}
			});

			// build ui
			gridPane.add(nameField, 0, 0);
			gridPane.add(startAmountBox, 0, 1);
			gridPane.add(okButton, 0, 2);
//			gridPane.getChildren().add(text);
//			gridPane.getChildren().add(okButton);
			node.setContent(gridPane);
			setDialogPane(node);
			
//			onCloseRequest = new EventHandler(){
//				
//			}
		}
		
//		@Ovrride
//		public Optional<ButtonType> showAndWait(){
//			
//		}
	}
	
	public class Account {
		private final SimpleStringProperty _name;
		private final SimpleDoubleProperty _initAmount;
		
		Account(String name, double initAmount){
			_name = new SimpleStringProperty(name);
			_initAmount = new SimpleDoubleProperty(initAmount);
		}
		
		public String getName(){
			return _name.get();
		}
		
		public double getInitAmount(){
			return _initAmount.get();
		}
	}
}




