package com.jmeixner.jjlabs.bookKeeping;

import javafx.fxml.Initializable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

import dataStore.SimpleDbInteraction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import models.Account;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuBar;

public class MainAppController implements Initializable {
	@FXML private MenuBar menuBar;

	@FXML public void handleKeyInput() {
		System.out.println("keyInputJustHappened");
	}

	@FXML public void handleAboutAction() {
		System.out.println("handleAboutNOW");
	}
	
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1){
		menuBar.setFocusTraversable(true);
	}

	@FXML public void takeBackup() {
		System.out.println("going to take a backup");
		SimpleDbInteraction.takeBackup();
		
	}


}
