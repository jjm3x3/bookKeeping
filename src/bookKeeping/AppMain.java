package bookKeeping;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
				
				Dialog alert = new Alert(Alert.AlertType.INFORMATION,"some account was added",ButtonType.OK);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					System.out.println("so the account was finalized now");
				}
			}
			
		});
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(btn);
		
		
		Scene scene = new Scene(stackPane, 300, 300);
		firstStage.setScene(scene);
		firstStage.setTitle("the 'say hi' to brenna app");
		firstStage.show();
	}

}
