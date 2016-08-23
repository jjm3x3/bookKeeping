package com.jmeixner.jjlabs.bookKeeping;

import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

import dataStore.SimpleDbInteraction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Account;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuBar;

public class MainAppController implements Initializable {
	@FXML private MenuBar menuBar;
	private Stage self;

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
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName("*.bak");
		fileChooser.setTitle("Choose file to backup to");
		File file = fileChooser.showSaveDialog(self);
		if (file != null){
			System.out.println("Lets see whats in this thing: " + file);
			SimpleDbInteraction.takeBackup(file);
		} else {
			System.err.println("something went wrong the file trying to read in from is null");
		}
		
	}

	@FXML public void retore() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(self);
		if (file != null){
			System.out.println("Lets see whats in this thing: " + file);
			SimpleDbInteraction.restoreFromFile(file);
		}

		try {
			self.setScene(AppDriver.getMainContentScene(self, getClass()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML public void importCSV() {
		Parent thing = (Parent) self.getScene().getRoot().getChildrenUnmodifiable().get(1);
		Parent vbox =  (Parent) thing.getChildrenUnmodifiable().get(0);
		TableView table = (TableView) vbox.getChildrenUnmodifiable().get(0);

		int accountId = table.getSelectionModel().getFocusedIndex();
		SimpleDbInteraction.importCsv(accountId + 1);
	}

	protected void initData(Stage self) {
		this.self = self;
	}



}
