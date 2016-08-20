package com.jmeixner.jjlabs.bookKeeping;

import dataStore.SimpleDbInteraction;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class AddAccountDialogController {

	@FXML TextField nameField;
	@FXML TextField amountField;
	@FXML Button okButton;
	private Stage self;

	@FXML public void okButtonClick() {
		String accountName = nameField.getText();
		boolean accountNameIsValid = nameIsValid(accountName);
		double accountStartAmount = 0;
		boolean startingAmountIsValid = amountIsValid(amountField.getText());
		if (startingAmountIsValid)
			accountStartAmount = Double.parseDouble(amountField.getText());
		
		System.out.println("account name is: " + accountName);
		System.out.println("with starting amount: " + amountField.getText());

		if (accountNameIsValid && startingAmountIsValid){
			SimpleDbInteraction.AddAccount(accountName, accountStartAmount);
		}
		self.close();

	}
	
	
	private boolean nameIsValid(String possibleName){
		boolean result = possibleName.length() != 0;
		if (!result)
			System.err.println("The account Name must not be empty");
		return result;
	}
		
	private boolean amountIsValid(String possibleDouble){
		try {
			Double.parseDouble(possibleDouble);
			return true;
		} catch (NumberFormatException e){
			System.err.println("Unable to parse the startingAmount for this account");
			return false;
		} 
	}
	
	public void initData(Stage self){
		this.self = self;
	}

}
