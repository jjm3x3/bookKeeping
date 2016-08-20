package com.jmeixner.jjlabs.bookKeeping;


import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import dataStore.SimpleDbInteraction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

	class AddTransactionDialog extends Dialog<ButtonType>{

		AddTransactionDialog(final int accountNumber){
			DialogPane node = new DialogPane();
			GridPane content = new GridPane();
			content.setAlignment(Pos.CENTER);
			content.setHgap(10);
			content.setVgap(10);
			
			final TextField nameField = new TextField();
			nameField.setPromptText("Transaction Name");
			final TextField amountField = new TextField();
			amountField.setPromptText("Transaction Amount");
			final DatePicker dateField = new DatePicker();
			dateField.setPromptText(new DateTime().toString("yyyy-MM-dd"));
			
			ButtonType okButtonType =  new ButtonType("OK");
			node.getButtonTypes().add(okButtonType);

			Button okButton = (Button) node.lookupButton(okButtonType);
			okButton.setText("ok");	
			okButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Do a thing");
					double amount;
					try {
						amount = Double.parseDouble(amountField.getText());
					} catch(NumberFormatException e){
						amount = 0;
					}
					LocalDate transactionDate = new LocalDate(dateField.getValue().getYear(),dateField.getValue().getMonthValue(),dateField.getValue().getDayOfMonth());
					SimpleDbInteraction.addTransaction(nameField.getText(), amount , accountNumber, transactionDate);
					
				}
			});
			
			content.add(nameField, 0, 0);
			content.add(amountField, 0, 1);
			content.add(dateField, 0, 2);
			content.add(okButton, 0, 3);
			
			node.setContent(content);
			setDialogPane(node);
		}
		
		
	}
