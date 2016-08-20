package com.jmeixner.jjlabs.bookKeeping;

import java.net.URL;
import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import dataStore.SimpleDbInteraction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTransactionDialogController implements Initializable{
		@FXML private TextField nameField;
		@FXML private Button okButton;
		@FXML private TextField amountField;
		@FXML private DatePicker dateField;
		private int accountId;
		private Stage self;

		@FXML public void okButtonClick() {
					System.out.println("Do a thing");
					double amount;
					try {
						amount = Double.parseDouble(amountField.getText());
					} catch(NumberFormatException e){
						amount = 0;
					}
					LocalDate transactionDate = new LocalDate(dateField.getValue().getYear(),dateField.getValue().getMonthValue(),dateField.getValue().getDayOfMonth());
					SimpleDbInteraction.addTransaction(nameField.getText(), amount , accountId , transactionDate); 
					self.close();
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
