//Author Name: M. Corey Glass
//Date: 6.26.22
//Program Name: Glass_primeClient
//Purpose: learn the basics of client/server architecture

package glass_primeClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Glass_primeClient extends Application {

	private static final int PORT = 8000;
	private DataOutputStream toServer = null;
	private DataInputStream fromServer = null;
	private Socket socket = null;


	@Override
	public void start(Stage primaryStage) {

		// input text box
		TextField input = new TextField();
		input.setAlignment(Pos.BOTTOM_RIGHT);
		
		BorderPane inputPane = new BorderPane();
		inputPane.setPadding(new Insets(5, 5, 5, 5)); 
		inputPane.setCenter(new Label("Please enter a whole number, then press the [Enter] key: "));
		inputPane.setRight(input);
		
		// create client status output
		TextArea output = new TextArea();
		
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(new ScrollPane(output));
		mainPane.setTop(inputPane);

		// setup GUI
		Scene scene = new Scene(mainPane, 480, 220);
		primaryStage.setTitle("Prime Client"); // set window title title
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(x -> {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		input.setOnAction(e -> {
			try {
				// get user's input from text field
				long intInput = Long.parseLong(input.getText().trim());

				// send the user's input to server
				toServer.writeLong(intInput);
				toServer.flush();

				// get response from the server
				boolean results = fromServer.readBoolean();

				// display results
				output.appendText(intInput + " is " + (results ? "" : "not ") + "a prime number.\n");
			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		});

		try {
			socket = new Socket("localhost", PORT);
			
			// open input from server
			fromServer = new DataInputStream(socket.getInputStream());

			// open output to server
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			output.appendText(ex.toString() + '\n');
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
