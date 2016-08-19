package com.jmeixner.jjlabs.bookKeeping;

import java.net.URL;
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

import dataStore.SimpleDbInteraction;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import models.Transaction;

public class AppMain extends Application {

	public static void main(String[] args) {
		System.out.println("HI brenna");
		SimpleDbInteraction.runsql();
		launch(args);

	}

	@Override
	public void start(Stage firstStage) throws Exception {
		final TableView<Account> table = new TableView<>();
		TableColumn<Account, String> nameColumn = new TableColumn("Account Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("name"));
		TableColumn<Account, Double> amountColumn = new TableColumn("Current Balence");
		amountColumn.setCellValueFactory(new PropertyValueFactory<Account, Double>("initAmount"));
		table.getColumns().add(nameColumn);
		table.getColumns().add(amountColumn);
		try{
			updateAcountTable(table);
		}catch (SQLException e){
			System.err.println(e);
//			e.printStackTrace();
		}

		table.setOnMousePressed(new EventHandler<MouseEvent>(){

			long when;
			@Override
			public void handle(MouseEvent arg0) {
				if (new DateTime().getMillis() - when < 500){
					System.out.println("doing something interesting");
				int accountNumber = table.getSelectionModel().getFocusedIndex();
					Dialog transactionLog = new AccountTransactionLogDialog(accountNumber + 1);
					transactionLog.showAndWait();
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
		
		Parent mainContent = new GridPane();
		boolean usingFXML = true;

		if (usingFXML){
			Class<? extends AppMain> myAppsClass = getClass();
			System.out.println(myAppsClass.getClassLoader());
			System.out.println(myAppsClass.getPackage());
			URL place = myAppsClass.getResource("/MainApp.fxml");
			System.out.println("where is this: " + place);
			mainContent = FXMLLoader.load(place);

		} else {
			GridPane gridPane = (GridPane) mainContent;
			gridPane.setAlignment(Pos.CENTER);
			gridPane.setHgap(10);
			gridPane.setVgap(10);

			gridPane.add(table,0, 0);
			gridPane.add(btn, 0, 1);
			gridPane.add(addTransactionButton, 1, 1);
		}
		
		Scene scene = new Scene(mainContent, 500, 500);
		firstStage.setScene(scene);
		firstStage.setTitle("booKeeping");
		firstStage.show();
	}
	
	class AccountTransactionLogDialog extends Dialog<ButtonType> {
		AccountTransactionLogDialog(int accountId) {
			DialogPane node = new DialogPane();
			GridPane content = new GridPane();
			content.setAlignment(Pos.CENTER);
			content.setHgap(10);
			content.setVgap(10);
			
			TableView<Transaction> table = new TableView();
			TableColumn<Transaction, String> nameColumn = new TableColumn("Name");
			nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
			TableColumn<Transaction, Double> amountColumn = new TableColumn("Amount");
			amountColumn.setCellValueFactory(new PropertyValueFactory("amount"));
			table.getColumns().add(nameColumn);
			table.getColumns().add(amountColumn);


			try {
				ObservableList<Transaction> transactionList = SimpleDbInteraction.getTransactionList(accountId);
				table.setItems(transactionList);
			} catch (SQLException e) {
				System.err.println(e);
//				e.printStackTrace();
			}
			
			ButtonType okButtonType =  new ButtonType("OK");
			node.getButtonTypes().add(okButtonType);

			Button okButton = (Button) node.lookupButton(okButtonType);
			okButton.setText("ok");	
			okButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					
				}
			});
			
			content.add(table, 0, 0);
			content.add(okButton, 0, 1);
			
			node.setContent(content);
			setDialogPane(node);
		}


	}

	void updateAcountTable(TableView<Account> table) throws SQLException {
		ObservableList<Account> accountsList = SimpleDbInteraction.getAccountList();
		table.setItems(accountsList);
	}


}




