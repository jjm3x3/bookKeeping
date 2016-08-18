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
		AddAccountDialog() {
			DialogPane node = new DialogPane();
			GridPane gridPane = new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			gridPane.setHgap(10);
			gridPane.setVgap(10);

			final TextField nameField = new TextField();
			nameField.setPromptText("Account Name");
			final TextField startAmountBox = new TextField();
			startAmountBox.setPromptText("set start amount");

			ButtonType okButtonType =  new ButtonType("OK");
			node.getButtonTypes().add(okButtonType);

			Button okButton = (Button) node.lookupButton(okButtonType);
			okButton.setText("ok");
			okButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					String accountName = nameField.getText();
					double accountStartAmount = Double.parseDouble(startAmountBox.getText());
					System.out.println("account name is: " + accountName);
					System.out.println("with starting amount: " + startAmountBox.getText());
					AppMain.AddAccount(accountName, accountStartAmount);
					AddAccountDialog.this.hide();
				}
			});

			// build ui
			gridPane.add(nameField, 0, 0);
			gridPane.add(startAmountBox, 0, 1);
			gridPane.add(okButton, 0, 2);
//			gridPane.getChildren().add(text);
//			gridPane.getChildren().add(okButton);
			node.setContent(gridPane);
			setDialogPane(node);
			
//			onCloseRequest = new EventHandler(){
//				
//			}
		}
		
//		@Ovrride
//		public Optional<ButtonType> showAndWait(){
//			
//		}
	
}
