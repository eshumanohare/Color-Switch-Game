package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javax.naming.ldap.Control;

public class Ball {
    @FXML
    private static Circle gameBall;
    private static double posX;
    private static double posY;
    private static double speedY;

    public static Circle getGameBall() {
        return gameBall;
    }

    public static void setGameBall(Circle gameBall) {
        Ball.gameBall = gameBall;
    }

    public static double getPosX() {
        return posX;
    }

    public static void setPosX(double posX) {
        Ball.posX = posX;
    }

    public static double getPosY() {
        return posY;
    }

    public static void setPosY(double posY) {
        Ball.posY = posY;
    }

    public static double getSpeedY() {
        return speedY;
    }

    public static void setSpeedY(double speedY) {
        Ball.speedY = speedY;
    }

    public static void startGame(){


    }
    /*
    public static void applyGravity(){
//        System.out.println(anchorPane.getHeight());
        double maxY = Controller.getAnchorPane().getHeight();

//        System.out.println(Controller.getHand().getLayoutY());

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame applyGravity = new KeyFrame(Duration.millis(10), event -> {
            if(gameBall.getCenterY() <= Controller.getHandView().getY()-25) {
                gameBall.setCenterY(gameBall.getCenterY() + Ball.getSpeedY());
                updateVelocity();
            }
        });


        timeline.getKeyFrames().add(applyGravity);
        timeline.play();
    }

    private static void takeJump() {

        Controller.getAnchorPane().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event1 -> {
            if(event1.getCode() == KeyCode.SPACE){
                Timeline timeline1 = new Timeline();
                timeline1.setCycleCount(25);
                KeyFrame moveDown = new KeyFrame(Duration.millis(5.3), event -> {
                    gameBall.setCenterY(gameBall.getCenterY() - Ball.getSpeedY());
                });
                timeline1.getKeyFrames().add(moveDown);
                timeline1.play();
            }
        });

     */
}


