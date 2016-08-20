package com.jmeixner.jjlabs.bookKeeping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import models.Account;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.MenuBar;
import javafx.fxml.Initializable;

public class MainContentController implements Initializable{
	long when;
	@FXML private TableView<Account> accountList;
	@FXML MenuBar menuBar;
	
	@FXML protected void createAccount(ActionEvent event){
		System.out.println("will add an account");
		System.out.println("Hi brenna");
				
		// TODO could probably reuse this stage but there seems to be minimal memory impact
		Stage addAccountStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAccountWindow.fxml"));
		Parent view;
		try {
			view = loader.load();
			Scene scene = new Scene(view, 350, 200);
			AddAccountDialogController controller = loader.getController();
			controller.initData(addAccountStage);
			addAccountStage.setScene(scene);
			addAccountStage.initModality(Modality.APPLICATION_MODAL);
			addAccountStage.show();
			addAccountStage.setOnHidden(new EventHandler<WindowEvent>(){
				@Override
				public void handle(WindowEvent event) {
					try {
						AppMain.updateAccountTable(accountList);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void addTransaction(ActionEvent event) {
		// TODO could probably reuse this stage but there seems to be minimal memory impact
		
		
		int accountNumber = accountList.getSelectionModel().getFocusedIndex();

		final Stage addTransactionStage = new Stage();
		Parent exampleContent = null;
		try {
			URL viewUrl = getClass().getResource("/AddTransactionWindow.fxml");
			System.out.println("view for 'dialog' " + viewUrl);
			FXMLLoader loader = new FXMLLoader(viewUrl);
			exampleContent = loader.load();
			AddTransactionDialogController controller = loader.getController();
			controller.initData(addTransactionStage, accountNumber + 1);
			Scene theContent = new Scene(exampleContent, 300,300);
			addTransactionStage.setScene(theContent);
			addTransactionStage.initModality(Modality.APPLICATION_MODAL);
			addTransactionStage.show();
			addTransactionStage.setOnHidden(new EventHandler<WindowEvent>(){

				@Override
				public void handle(WindowEvent event) {
					try {
						AppMain.updateAccountTable(accountList);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	@FXML public void accountDetails(MouseEvent event) {
		if (new DateTime().getMillis() - when < 500){
			System.out.println("doing something interesting");
			int accountNumber = accountList.getSelectionModel().getFocusedIndex();
			Dialog transactionLog = new AppMain.AccountTransactionLogDialog(accountNumber + 1);
			transactionLog.showAndWait();
		} else {
			when = new DateTime().getMillis();
		}
	}

	@FXML public void handleAboutAction() {}

	@FXML public void handleKeyInput() {}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			AppMain.updateAccountTable(accountList);
		} catch (SQLException e) { //TODO again probably not the right place to do this
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
