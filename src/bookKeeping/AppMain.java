package bookKeeping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Account;

public class AppMain extends Application {

	private static final String DB_URL = "jdbc:h2:~/.bookKeeping/db";
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
		try{
			updateAcountTable(table);
		}catch (SQLException e){
			e.printStackTrace();
		}

		table.setOnMousePressed(new EventHandler<MouseEvent>(){

			long when;
			@Override
			public void handle(MouseEvent arg0) {
				if (new DateTime().getMillis() - when < 500){
					System.out.println("doing something interesting");
				} else {
					when = new DateTime().getMillis();
				}
			}
		});

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
					try {
						AppMain.this.updateAcountTable(table);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		});
		
		Button addTransactionButton = new Button();
		addTransactionButton.setText("Add Transaction");
		addTransactionButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				int accountNumber = table.getSelectionModel().getFocusedIndex();
				System.out.println(accountNumber + 1);
				Dialog addTransactionDialog = new AddTransactionDialog(accountNumber + 1);
				Optional<ButtonType> result = addTransactionDialog.showAndWait();
				if (result .isPresent()){
					try {
						AppMain.this.updateAcountTable(table);
					} catch(SQLException e){
						e.printStackTrace();
					}
				}
				
			}
		});
		
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(table,0, 0);
		gridPane.add(btn, 0, 1);
		gridPane.add(addTransactionButton, 1, 1);
		
		Scene scene = new Scene(gridPane, 500, 500);
		firstStage.setScene(scene);
		firstStage.setTitle("booKeeping");
		firstStage.show();
	}

	void updateAcountTable(TableView<Account> table) throws SQLException {
		Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
		Statement stmt = dbConn.createStatement();
		ResultSet result = stmt.executeQuery("select * from accounts");
		ObservableList<Account> accountsList = FXCollections.observableArrayList();
		while(result.next()){
			String name = result.getString("name");
			double accountStart = result.getDouble("initVal");
			Statement transactionStatement = dbConn.createStatement();
			ResultSet transactions = transactionStatement.executeQuery("select * from transactions where account_id = " + result.getInt("id"));
			while (transactions.next()){
				accountStart += transactions.getDouble("amount");
			}
			accountsList.add(new Account(name, accountStart));
		}
		dbConn.close();
		table.setItems(accountsList);
	}

	static void AddAccount(String accountName, double accountStartAmount) {
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
		
	}
	
		static void addTransaction(String transactionName, double amount, int account, LocalDate transactionDate){
			try {
				Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
				Statement stmt = dbConn.createStatement();
				DateTime now = new DateTime();
				System.out.println("its this time: " + now.toString().length());
				boolean createTable = stmt.execute("create table if not exists " + 
				"transactions(id identity," + 
						"name VARCHAR(100), " + 
						"amount double, " +  
						"account_id int," + 
						"transaction_date date, " + 
						"transaction_created varchar(30), " + 
						"foreign key (account_id) references public.accounts(id))");
				boolean insertRecord = stmt.execute("insert into transactions(id,name,amount,account_id,transaction_date,transaction_created) " + 
						"values(NULL, '" + transactionName + "'," + amount +", " + account + ", '" + transactionDate + "', '" + now + "')");
				dbConn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}




