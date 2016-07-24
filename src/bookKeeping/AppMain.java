package bookKeeping;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppMain extends Application {

	public static void main(String[] args) {
		System.out.println("HI brenna");
		launch(args);

	}

	@Override
	public void start(Stage firstStage) throws Exception {
		Button btn = new Button();
		btn.setText("Add an account");
		btn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Hi brenna");
				
//				Dialog alert = new Alert(Alert.AlertType.NONE,"some account was added",ButtonType.OK);
//				Dialog alert = new TextInputDialog("some example text");
				Dialog alert = new AddAccountDialog();
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() ) {
//						result.get() 
					System.out.println("so the account was finalized now" + result.get());
				}
			}
			
		});
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(btn);
		
		
		Scene scene = new Scene(stackPane, 300, 300);
		firstStage.setScene(scene);
		firstStage.setTitle("booKeeping");
		firstStage.show();
	}

	class AddAccountDialog extends Dialog<ButtonType> {

		AddAccountDialog() {
			DialogPane node = new DialogPane();
			GridPane gridPane = new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			gridPane.setHgap(10);
			gridPane.setVgap(10);

			Button okButton = new Button();
			okButton.setText("ok");
			okButton.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent arg0) {
					AddAccountDialog.this.hide();
				}
			});
			TextField nameField = new TextField();
			TextField startAmountBox = new TextField();
			gridPane.add(nameField, 0, 0);
			gridPane.add(startAmountBox, 0, 1);
			gridPane.add(okButton, 0, 2);
//			gridPane.getChildren().add(text);
//			gridPane.getChildren().add(okButton);
			node.setContent(gridPane);
			setDialogPane(node);
		}
		
//		@Ovrride
//		public Optional<ButtonType> showAndWait(){
//			
//		}
	}
}




