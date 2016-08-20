package com.jmeixner.jjlabs.bookKeeping;

import java.net.URL;
import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import dataStore.SimpleDbInteraction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddTransactionDialogController implements Initializable{
	@FXML private TextField nameField;
	@FXML private Button okButton;
	@FXML private TextField amountField;
	@FXML private DatePicker dateField;
	private int accountId;
	private Stage self;

	private double amount;
	private LocalDate transactionDate;

	@FXML public void okButtonClick() {
		System.out.println("Do a thing");
		String transactionName = nameField.getText();

		if (isTransactionNameValid(transactionName) && amountIsValid() && dateFieldIsValid() && transactionDate != null){
			SimpleDbInteraction.addTransaction(transactionName, amount , accountId , transactionDate); 
			self.close();
		}
	}
		
	private boolean dateFieldIsValid() {
		java.time.LocalDate dateValue = dateField.getValue();
		if (dateValue == null){
			String message = "Wiht no entered date it is assumed that the transaction took place today. Is this correct?";
			Alert confirmAlert = new Alert(AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.OK);
			confirmAlert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
				.forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
			confirmAlert.showAndWait().filter(x -> x == ButtonType.OK).ifPresent(x -> transactionDate = new DateTime().toLocalDate());
		} else {
			transactionDate = new LocalDate(dateField.getValue().getYear(),dateField.getValue().getMonthValue(),dateField.getValue().getDayOfMonth());
		}
		return true;
	}

	private boolean amountIsValid() {
		try {
			amount = Double.parseDouble(amountField.getText());
			return true;
		} catch(NumberFormatException e){
			Alert errorDialog = new Alert(AlertType.ERROR, "The amount you entered for the transaction is not valid");
			errorDialog.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
				.forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
			errorDialog.showAndWait();
			return false;
		}
	}

	private boolean isTransactionNameValid(String transactionName) {
		if (transactionName.length() == 0){
			Alert errorDialog = new Alert(AlertType.ERROR, "The Transaction Name cannot be empty");
			errorDialog.showAndWait();
			return false;
		}
		return true;
	}

	public void initData(Stage self, int accountId){
		this.accountId = accountId;
		this.self = self;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		dateField.setPromptText(new DateTime().toString("yyyy-MM-dd"));
	}
	

	

}
