package com.jmeixner.jjlabs.bookKeeping;

import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import models.Account;

public class MainAppController {
	@FXML private TableView<Account> accountList;
	
	@FXML protected void createAccount(ActionEvent event){
		System.out.println("will add an account");
		System.out.println("Hi brenna");
				
		Dialog<ButtonType> alert = new AddAccountDialog();
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() ) {
			System.out.println("so the account was finalized now with result: " + result.get());
			try {
				AppMain.updateAccountTable(accountList);
			} catch (SQLException e) { // TODO ::: this is probably not correct
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
	}

	@FXML public void addTransaction(ActionEvent event) {
		int accountNumber = accountList.getSelectionModel().getFocusedIndex();
		System.out.println(accountNumber + 1);
		Dialog<ButtonType> addTransactionDialog = new AddTransactionDialog(accountNumber + 1);
		Optional<ButtonType> result = addTransactionDialog.showAndWait();
		if (result .isPresent()){
			try {
				AppMain.updateAccountTable(accountList);
			} catch(SQLException e){
				e.printStackTrace();
			}
		}
				
	}

}