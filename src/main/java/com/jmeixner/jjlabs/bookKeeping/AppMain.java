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
import javafx.scene.Node;
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

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage firstStage) throws Exception {

		Class<? extends AppMain> myAppsClass = getClass();
		System.out.println(myAppsClass.getClassLoader());
		System.out.println(myAppsClass.getPackage());
		URL place = myAppsClass.getResource("/MainApp.fxml");
		System.out.println("where is this: " + place);
		Parent mainContent = FXMLLoader.load(place);
			
		
		Scene scene = new Scene(mainContent, 500, 500);
		firstStage.setScene(scene);
		firstStage.setTitle("booKeeping");
		firstStage.show();
	}
	
	static class AccountTransactionLogDialog extends Dialog<ButtonType> {
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

//	void updateAcountTable(TableView<Account> table) throws SQLException {
//		ObservableList<Account> accountsList = SimpleDbInteraction.getAccountList();
//		table.setItems(accountsList);
//	}

	public static void updateAccountTable(TableView<Account> table) throws SQLException {
		ObservableList<Account> accountsList = SimpleDbInteraction.getAccountList();
		table.setItems(accountsList);
	}


}




