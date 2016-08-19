package com.jmeixner.jjlabs.bookKeeping;

import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class MainAppController {
	@FXML private TableView accountList;
	
	@FXML protected void createAccount(ActionEvent event){
		System.out.println("will add an account");
		System.out.println("Hi brenna");
				
		Dialog alert = new AddAccountDialog();
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() ) {
			System.out.println("so the account was finalized now with result: " + result.get());
			try {
				AppMain.updateAcountTable(accountList);
			} catch (SQLException e) { // TODO ::: this is probably not correct
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
	}

}
