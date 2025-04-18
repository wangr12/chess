package edu.guilford;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

/**
 * This program runs a standard game of chess.
 * @author Ray Wang
 * @version 1.0
 */
public class ChessDisplay extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        BoardPane boardPane = new BoardPane();

        Image image = new Image(this.getClass().getResource("/edu/guilford/chess_board.png").toExternalForm());

        BackgroundImage chessBoard = new BackgroundImage(image,
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.DEFAULT,
        new BackgroundSize(800, 800, false, false, false, false));
        
        boardPane.setBackground(new Background(chessBoard));

        scene = new Scene(boardPane, 800, 800);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessDisplay.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}