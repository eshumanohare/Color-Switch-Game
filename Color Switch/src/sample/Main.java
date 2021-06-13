package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public class Main extends Application implements Serializable {

    private static Stage mainStage = new Stage();
    private static Controller gameController = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception{
        runGame();
    }

    public static void initializeGame() throws IOException {

        gameController.setGameBallX(400);
        gameController.setGameBallY(700);
        gameController.setCircleObstacleX(400);
        gameController.setCircleObstacleY(350);
        gameController.setStarY(gameController.getCircleObstacleY() - 40);
        gameController.setLineObstacleStartY(gameController.getCircleObstacleY() - 400);
        gameController.setLineObstacleEndY(gameController.getCircleObstacleY() - 400);
        gameController.setStar2Y(gameController.getLineObstacleStartY() - 160);
        gameController.setPlusHorizontalStartY(gameController.getLineObstacleStartY() - 500);
        gameController.setPlusHorizontalEndY(gameController.getLineObstacleStartY() - 500);
        gameController.setPlusVerticalStartY((gameController.getPlusHorizontalStartY() + gameController.getPlusHorizontalEndY())/2 - 100);
        gameController.setPlusVerticalEndY((gameController.getPlusHorizontalStartY() + gameController.getPlusHorizontalEndY())/2 + 100);
        gameController.setStar3Y(gameController.getPlusVerticalStartY() - 160);
        gameController.setColorChangerY(gameController.getStar3Y() - 400);
        gameController.setRectObstacleY(gameController.getColorChangerY() - 500);
        gameController.setStar4Y(gameController.getRectObstacleY() + 125 - 40);
        gameController.setScore(0);
        gameController.setCounter(0);
        gameController.setFileCounter(1);
    }

    public static void runGame() throws IOException {
        initializeGame();
        gameController.renderMainWindow();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src\\sample\\sound.mp3").toURI().toString()));
        mediaPlayer.setAutoPlay(true);
        launch(args);
    }

    public static Controller getGameController() {
        return gameController;
    }

    public static void setGameController(Controller gameController) {
        Main.gameController = gameController;
    }
}
