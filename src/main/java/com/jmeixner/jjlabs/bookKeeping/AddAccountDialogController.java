package com.jmeixner.jjlabs.bookKeeping;

import dataStore.SimpleDbInteraction;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class AddAccountDialogController {

	@FXML TextField nameField;
	@FXML TextField amountField;
	@FXML Button okButton;
	private Stage self;
	
	private double initAmount;

	@FXML public void okButtonClick() {
		String accountName = nameField.getText();
		boolean accountNameIsValid = nameIsValid(accountName);
		boolean startingAmountIsValid = amountIsValid(amountField.getText());
		double accountStartAmount = getInitAmount();
		
		System.out.println("account name is: " + accountName);
		System.out.println("with starting amount: " + amountField.getText());

		if (accountNameIsValid && startingAmountIsValid){
			SimpleDbInteraction.AddAccount(accountName, accountStartAmount);
			self.close();
		}
	}
	
	
	private boolean nameIsValid(String possibleName){
		boolean result = possibleName.length() != 0;
		if (!result){
			System.err.println("The account Name must not be empty");
			Alert errorDialog = new Alert(AlertType.ERROR,"The Account Name must not be empty");
			errorDialog.showAndWait();
		}
		return result;
	}
		
	private boolean explicitAmountIsValid;
	private boolean amountIsValid(String possibleDouble){
		if (possibleDouble.equals("")){
			explicitAmountIsValid = false;
			String message = "if you leave the amount field blank it will default to '0.00' is this alright?";
			Alert confirmDialog = new Alert(AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.OK);
			final boolean result;
			confirmDialog.showAndWait().filter(x -> x == ButtonType.OK).ifPresent(x -> {
				explicitAmountIsValid = true; 
				initAmount = 0;
			});
			return explicitAmountIsValid;

		}
		try {
			initAmount = Double.parseDouble(possibleDouble);
			return true;
		} catch (NumberFormatException e){
			System.err.println("Unable to parse the startingAmount for this account");
			Alert errorDialog = new Alert(AlertType.ERROR, "You mus enter a number for the starting amount");
			errorDialog.showAndWait();
			return false;
		} 
	}
	
	private double getInitAmount(){
		return initAmount;
	}
	
	public void initData(Stage self){
		this.self = self;
	}

}
