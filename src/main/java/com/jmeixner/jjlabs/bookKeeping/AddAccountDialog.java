package com.jmeixner.jjlabs.bookKeeping;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddAccountDialog extends Dialog<ButtonType> {
	final TextField nameField = new TextField();
	final TextField startAmountBox = new TextField();
	
	
	AddAccountDialog() {
		DialogPane node = new DialogPane();
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		nameField.setPromptText("Account Name");
		startAmountBox.setPromptText("set start amount");

		ButtonType okButtonType =  new ButtonType("OK");
		node.getButtonTypes().add(okButtonType);

		Button okButton = (Button) node.lookupButton(okButtonType);
		okButton.setText("ok");
		okButton.setOnAction(submitHandler);

		// build ui
		gridPane.add(nameField, 0, 0);
		gridPane.add(startAmountBox, 0, 1);
		gridPane.add(okButton, 0, 2);
		node.setContent(gridPane);
		setDialogPane(node);
			
	}
	
	private EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			String accountName = nameField.getText();
			boolean accountNameIsValid = nameIsValid(accountName);
			double accountStartAmount = 0;
			boolean startingAmountIsValid = amountIsValid(startAmountBox.getText());
			if (startingAmountIsValid)
				accountStartAmount = Double.parseDouble(startAmountBox.getText());

			System.out.println("account name is: " + accountName);
			System.out.println("with starting amount: " + startAmountBox.getText());

			if (accountNameIsValid && startingAmountIsValid){
				AppMain.AddAccount(accountName, accountStartAmount);
			}
		}
	};

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
	
}
