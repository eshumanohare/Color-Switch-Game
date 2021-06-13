package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Controller extends AnimationTimer implements Serializable {

//    Vars for serialization
    private int randVal;
    private double gameBallX;
    private double gameBallY;
    private double circleObstacleX;
    private double circleObstacleY;
    private double lineObstacleStartY;
    private double lineObstacleEndY;
    private double plusHorizontalStartY;
    private double plusHorizontalEndY;
    private double plusVerticalStartY;
    private double plusVerticalEndY;
    private double rectObstacleY;
    private double starY;
    private double star2Y;
    private double star3Y;
    private double star4Y;
    private double colorChangerY;
    private int score;
    private int counter;
    private int fileCounter;

    public int getFileCounter() {
        return fileCounter;
    }

    public void setFileCounter(int fileCounter) {
        this.fileCounter = fileCounter;
    }

    //    First Game Window buttons
    @FXML
    private static Polygon startGame = new Polygon();
    @FXML
    private static Polygon resumeGame = new Polygon();
    @FXML
    private static Circle exitGame = new Circle();
    @FXML
    private static Rectangle strip1 = new Rectangle();
    @FXML
    private static Rectangle strip2 = new Rectangle();

//    Game play window stuff
    @FXML
    private static Circle gameBall = new Circle();
    @FXML
    private static AnchorPane anchorPane = new AnchorPane();
    private static AnchorPane pauseLayout = new AnchorPane();

    @FXML
    private static Image hand = new Image(new File("src/Images/hand.png").toURI().toString());
    private static ImageView handView = new ImageView();
    private static Image star = new Image(new File("src/Images/star-solid.png").toURI().toString());
    private static ImageView starView = new ImageView();
    private static ImageView starView2 = new ImageView();
    private static ImageView starView3 = new ImageView();
    private static ImageView starView4 = new ImageView();

    private static Image colorChange = new Image(new File("src/Images/color-changer.png").toURI().toString());
    private static ImageView colorChanger = new ImageView();

    //    FPS Rate of game
    transient final Duration fps = Duration.millis(1000/60);

//    Stuff needed for creating obstacle 1 -- circle with different colors
    @FXML
    private static Circle circleObstacle90 = new Circle();

//  Obstacle 2
    private static Line lineObstacle = new Line();

//    Obstacle 3
    private static Line plusVertical = new Line();
    private static Line plusHorizontal = new Line();

//     Obstacle 4 -> Square
    private static Rectangle rectangleObstacle = new Rectangle();

//    Score Label
    private static Label scoreLabel = new Label();

    @FXML
    private static JFXButton pauseButton = new JFXButton();

    @FXML
    private static JFXButton resumeButton = new JFXButton();

    @FXML
    private static JFXButton exitButton = new JFXButton();

//    Obstacle1 container -> circles with radius 90
//    private static Circle obstacles[] = new Circle[4];
//    Obstacle2 container -> straight lines
//    private static Line obstacles2[] = new Line[4];
//    ALl Colors
    private static Color colors[] = new Color[4];

    transient Group images = new Group();

    private static boolean hitsSpace;
    private static boolean mouseHit;

    private static double time = 1.0;

    private static AnimationTimer animationTimer;
    private static Timeline timelineRotation;

    private static ImageView continueImage = new ImageView();
    private static Label textLabel = new Label();

//    First game window methods

    @Override
    public void handle(long now) {

    }


    @FXML
    public void startGame() throws IOException {

//        Render ball, hand and all the obstacles -> outside frame obstacles too
        renderGamePlayWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gamePlay.fxml"));

        anchorPane = loader.load();

        Controller.setAnchorPane(anchorPane);

        if(!anchorPane.getChildren().contains(gameBall)){
            anchorPane.getChildren().add(gameBall);
        }

        if(!anchorPane.getChildren().contains(images)){
            anchorPane.getChildren().add(images);
        }

        if(!anchorPane.getChildren().contains(pauseButton)){
            anchorPane.getChildren().add(pauseButton);
        }

        if(!anchorPane.getChildren().contains(scoreLabel)){
            anchorPane.getChildren().add(scoreLabel);
        }

        Scene scene = new Scene(anchorPane,800,900);

        scene.getStylesheets().add("src/CSS/styles.css");

        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("ColorSwitch | MainWindow");
        Main.getMainStage().show();

        Controller.getAnchorPane().getScene().addEventHandler(KeyEvent.KEY_PRESSED, event ->  {
            if(event.getCode() == KeyCode.SPACE){
                hitsSpace = true;
//                System.out.println("asd");
            }
        });

        pauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            timelineRotation.stop();
            timelineRotation.getKeyFrames().removeAll(timelineRotation.getKeyFrames());
            anchorPane.getChildren().removeAll(anchorPane.getChildren());
            plusVertical.getTransforms().removeAll(plusVertical.getTransforms());
            plusHorizontal.getTransforms().removeAll(plusHorizontal.getTransforms());
            rectangleObstacle.getTransforms().removeAll(rectangleObstacle.getTransforms());
            mouseHit = true;
        });

        pauseButton.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.P){
                mouseHit = true;
            }
        });

        pauseLayout.setVisible(false);

            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    System.out.println(circleObstacle90.getCenterY());
                    System.out.println(gameBall.getCenterY());
                    double currYBall = gameBall.getCenterY();
//                Apply gravity always
                    Ball.setSpeedY(Ball.getSpeedY() + Constants.getGravity() * 0.5 * Math.pow(time, 2));
                    double deltaY = Ball.getSpeedY();
                    double newY = currYBall + deltaY;

//                To jump
                    if (hitsSpace) {
                        Ball.setSpeedY(-9.0);
                        time = 0.75;
//                    Consuming the event that just happened
                        hitsSpace = false;
                    }

//                    To pause
                    if (mouseHit) {
                        animationTimer.stop();

                        System.out.println("ssss");
                        anchorPane.setVisible(false);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseWindow.fxml"));

                        try {
                            pauseLayout = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                    anchorPane.getChildren().addAll(gameBall, images, pauseButton);

                        Scene scene = new Scene(pauseLayout, 800, 900);

                        scene.getStylesheets().add("src/CSS/styles.css");

                        Main.getMainStage().setScene(scene);
                        Main.getMainStage().setTitle("ColorSwitch | PauseWindow");
                        Main.getMainStage().show();

                        pauseLayout.setVisible(true);

                        mouseHit = false;

                    }
                    if (checkCollision()) {
                        runGameOverScreen();
                    }
                    if (gameBall.getCenterY() <= 330) {
                        moveBackground();
                    }

                    gameBall.setCenterY(newY);
                    time += 0.001;
                }
            };
            animationTimer.start();

    }

    private void runGameOverScreen() {

        anchorPane.setVisible(false);
        animationTimer.stop();
        timelineRotation.stop();

        AnchorPane exitLayout = new AnchorPane();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameOver.fxml"));

        try {
            exitLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textLabel = (Label) loader.getNamespace().get("continueLabel");
        continueImage = (ImageView) loader.getNamespace().get("continuePoints");

        if(Main.getGameController().getScore() >= 5){
            continueImage.setVisible(true);
            textLabel.setVisible(true);
            textLabel.setText("Use 5 Points to continue");
            textLabel.setTextFill(Color.rgb(255,255,255));
        }

        Scene scene = new Scene(exitLayout, 800, 900);

        scene.getStylesheets().add("src/CSS/styles.css");

        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("ColorSwitch | ExitWindow");
        Main.getMainStage().show();
    }

    @FXML
    public void resumePlayAfterBump() throws IOException {
        continueImage.setVisible(false);
        textLabel.setVisible(false);
        Main.getGameController().setScore(Main.getGameController().getScore() - 5);
        resumePlay();
        Main.getGameController().setGameBallY(gameBall.getCenterY() - 50);
    }
    private void moveBackground() {

        if(circleObstacle90.getCenterY() + circleObstacle90.getRadius() >= anchorPane.getHeight()){
            circleObstacle90.setCenterY(starView4.getY() - 500);
            starView.setY(circleObstacle90.getCenterY() - starView.getFitHeight()/2);
        }

        if(lineObstacle.getEndY() >= (anchorPane.getHeight() - 15)){
            lineObstacle.setStartY(circleObstacle90.getCenterY() - 500);
            lineObstacle.setEndY(circleObstacle90.getCenterY() - 500);
        }
        if(starView2.getY() >= anchorPane.getHeight()){
            starView2.setY(lineObstacle.getStartY() - 2*starView2.getFitHeight());
        }

        if(plusHorizontal.getStartY() >= (anchorPane.getHeight() - 20)){
            plusHorizontal.setStartY(lineObstacle.getStartY() - 500);
            plusHorizontal.setEndY(lineObstacle.getStartY() - 500);
            plusVertical.setStartY(plusHorizontal.getStartY() - 100);
            plusVertical.setEndY(plusHorizontal.getStartY() + 100);
        }

        if(starView3.getY() >= anchorPane.getHeight()){
            starView3.setY(plusVertical.getStartY() - 2*starView3.getFitHeight());
        }

        if(colorChanger.getY() >= anchorPane.getHeight()){
            colorChanger.setY(starView3.getY() - 200);
        }

        if(rectangleObstacle.getY() >= anchorPane.getHeight()){
            rectangleObstacle.setY(colorChanger.getY() - 400);
        }

        if(starView4.getY() >= anchorPane.getHeight()){
            starView4.setY(rectangleObstacle.getY() + rectangleObstacle.getHeight()/2 - starView4.getFitHeight()/2);
        }


        circleObstacle90.setCenterY(circleObstacle90.getCenterY() + 3);
        lineObstacle.setStartY(lineObstacle.getStartY() + 3);
        lineObstacle.setEndY(lineObstacle.getEndY() + 3);
        plusHorizontal.setStartY(plusHorizontal.getStartY() + 3);
        plusHorizontal.setEndY(plusHorizontal.getEndY() + 3);
        plusVertical.setStartY(plusVertical.getStartY() + 3);
        plusVertical.setEndY(plusVertical.getEndY() + 3);
        rectangleObstacle.setY(rectangleObstacle.getY() + 3);
        starView.setY(starView.getY() + 3);
        starView2.setY(starView2.getY() + 3);
        starView3.setY(starView3.getY() + 3);
        starView4.setY(starView4.getY() + 3);
        colorChanger.setY(colorChanger.getY() + 3);
        handView.setY(handView.getY() + 3);

    }

//    Just a var for debugging collisions
    int sad = 0;

    private boolean checkCollision() {

//        Collision detection for color changer
        if(gameBall.getCenterY() <= (colorChanger.getY() + colorChanger.getFitHeight()) && gameBall.getCenterY() >= (colorChanger.getY() + colorChanger.getFitHeight()/2) && colorChanger.isVisible()){
            gameBall.setStroke(colors[Main.getGameController().getCounter()%4]);
            gameBall.setFill(colors[(Main.getGameController().getCounter()+1)%4]);
            colorChanger.setVisible(false);
        }

//        Collision detection for stars
        if(gameBall.getCenterY() <= (starView.getY() + starView.getFitHeight()) && gameBall.getCenterY() >= (starView.getY() + starView.getFitHeight()/2) && starView.isVisible()){
            Main.getGameController().setScore(Main.getGameController().getScore() + 1);
            scoreLabel.setText(Integer.toString(Main.getGameController().getScore()));
//            System.out.println(score);
            starView.setVisible(false);
            starView2.setVisible(true);
            colorChanger.setVisible(true);
        }

        if(gameBall.getCenterY() <= (starView2.getY() + starView2.getFitHeight()) && gameBall.getCenterY() >= (starView2.getY() + starView2.getFitHeight()/2) && starView2.isVisible()){
            Main.getGameController().setScore(Main.getGameController().getScore() + 1);
            scoreLabel.setText(Integer.toString(Main.getGameController().getScore()));
//            System.out.println(score);
            starView2.setVisible(false);
            starView3.setVisible(true);
        }

        if(gameBall.getCenterY() <= (starView3.getY() + starView3.getFitHeight()) && gameBall.getCenterY() >= (starView3.getY() + starView3.getFitHeight()/2) && starView3.isVisible()){
            Main.getGameController().setScore(Main.getGameController().getScore() + 1);
            scoreLabel.setText(Integer.toString(Main.getGameController().getScore()));
//            System.out.println(score);
            starView3.setVisible(false);
            starView4.setVisible(true);
        }

        if(gameBall.getCenterY() <= (starView4.getY() + starView4.getFitHeight()) && gameBall.getCenterY() >= (starView4.getY() + starView4.getFitHeight()/2) && starView4.isVisible()){
            Main.getGameController().setScore(Main.getGameController().getScore() + 1);
            scoreLabel.setText(Integer.toString(Main.getGameController().getScore()));
//            System.out.println(score);
            starView4.setVisible(false);
            starView.setVisible(true);
        }

//        If ball goes out of screen
//        System.out.println(gameBall.getCenterY() + " " + anchorPane.getHeight());
        if(gameBall.getCenterY() > anchorPane.getHeight()){
            return true;
        }

        double plusCenterX = rectangleObstacle.getX() + rectangleObstacle.getWidth()/2;
        double plusCenterY = rectangleObstacle.getY() + rectangleObstacle.getHeight()/2;

        double distance = (Math.sqrt(Math.pow(gameBall.getCenterX() - circleObstacle90.getCenterX(),2) + Math.pow(gameBall.getCenterY() - circleObstacle90.getCenterY(),2)));

        double distanceRect = (Math.sqrt(Math.pow(gameBall.getCenterX() - plusCenterX,2) + Math.pow(gameBall.getCenterY() - plusCenterY,2)));

        if( distance >= (gameBall.getRadius() + circleObstacle90.getRadius()-15) && distance <= (gameBall.getRadius() + circleObstacle90.getRadius()+10)){

            if(!gameBall.getFill().equals(circleObstacle90.getStroke())){
//                System.out.println("bump " + sad++);
                return true;
            }
        }

        if(gameBall.getCenterY() >= (plusHorizontal.getStartY() + plusHorizontal.getEndY())/2 && gameBall.getCenterY() <= ((plusHorizontal.getStartY() + plusHorizontal.getEndY())/2 + 50)){
            if(!gameBall.getFill().equals(plusHorizontal.getStroke()) || !gameBall.getFill().equals(plusVertical.getStroke())){
//                System.out.println("bump with plus " + sad++);
                return true;
            }
        }

        if(gameBall.getCenterY() >= lineObstacle.getStartY() && gameBall.getCenterY() <= lineObstacle.getStartY() + lineObstacle.getStrokeWidth()){
            if(!gameBall.getFill().equals(lineObstacle.getStroke())){
//                System.out.println("bump with line " + sad++);
                return true;
            }
        }

        if(distanceRect >= (rectangleObstacle.getHeight()/2 + gameBall.getRadius() - 20)&& distanceRect <= (rectangleObstacle.getHeight()/2 + gameBall.getRadius() + 20)){
            if(!gameBall.getFill().equals(rectangleObstacle.getStroke())){
//                System.out.println("bump with rectangle " + sad++);
                return true;
            }
        }
        return false;
    }

    @FXML
    public void resumeGame() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("resumeGame.fxml"));

        AnchorPane anchorPaneResume = loader.load();

        Map<String, Object> map = loader.getNamespace();

        JFXListView<String> jfxListView = new JFXListView<>();

        jfxListView = (JFXListView<String>) map.get("listview");

        File directory = new File("src\\Saved Files\\");
        File[] savedFiles = directory.listFiles();

        List<JFXButton> jfxButtonList = new ArrayList<>();

        if(savedFiles != null){

//            We need to first make as many buttons as files are so to listen to individual button seperately
            for(int i=0; i<savedFiles.length; i++){
                jfxListView.getItems().add(savedFiles[i].getName());
            }

            JFXListView<String> finalJfxListView = jfxListView;
            jfxListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                try {
                    readAndSave(finalJfxListView.getSelectionModel().getSelectedItem());
                    anchorPaneResume.setVisible(false);
                    Main.getGameController().startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
//                System.out.println(finalJfxListView.getSelectionModel().getSelectedItem());
            });

        }


        Scene scene = new Scene(anchorPaneResume,800,900);

        scene.getStylesheets().add("src/CSS/styles.css");

        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("ColorSwitch | ResumeWindow");
        Main.getMainStage().show();
    }

    private void readAndSave(String selectedItem) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("src\\Saved Files\\" + selectedItem);
        ObjectInputStream objectInputStream = new ObjectInputStream(file);

        Controller gameState = (Controller) objectInputStream.readObject();

        saveLoadedState(gameState);

        objectInputStream.close();
        file.close();

    }

    private void saveLoadedState(Controller gameState) {
        Main.getGameController().setGameBallX(gameState.getGameBallX());
        Main.getGameController().setGameBallY(gameState.getGameBallY()-100);
        Main.getGameController().setCircleObstacleX(gameState.getCircleObstacleX());
        Main.getGameController().setCircleObstacleY(gameState.getCircleObstacleY());
        Main.getGameController().setStarY(gameState.getStarY());
        Main.getGameController().setLineObstacleStartY(gameState.getLineObstacleStartY());
        Main.getGameController().setLineObstacleEndY(gameState.getLineObstacleEndY());
        Main.getGameController().setStar2Y(gameState.getStar2Y());
        Main.getGameController().setPlusHorizontalStartY(gameState.getPlusHorizontalStartY());
        Main.getGameController().setPlusHorizontalEndY(gameState.getPlusHorizontalEndY());
        Main.getGameController().setPlusVerticalStartY(gameState.getPlusVerticalStartY());
        Main.getGameController().setPlusVerticalEndY(gameState.getPlusVerticalEndY());
        Main.getGameController().setStar3Y(gameState.getStar3Y());
        Main.getGameController().setColorChangerY(gameState.getColorChangerY());
        Main.getGameController().setRectObstacleY(gameState.getRectObstacleY());
        Main.getGameController().setStar4Y(gameState.getStar4Y());
        Main.getGameController().setScore(gameState.getScore());
    }

    @FXML
    public void exitGame(){
        Main.getMainStage().close();
    }


    // Rendering MainMenu buttons
    public void renderMainWindow() throws IOException {

//        Setting the randVal-> Color of the gameBall in the beginning
        Main.getGameController().randVal = new Random().nextInt(4);

        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("GameWindow.fxml"));

        Map<String, Object> fxmlNamespace = loader.getNamespace();

        AnchorPane anchorPane = loader.load();

        startGame = (Polygon) fxmlNamespace.get("startGame");
        resumeGame = (Polygon) fxmlNamespace.get("resumeGame");
        exitGame = (Circle) fxmlNamespace.get("exitGame");
        strip1 = (Rectangle) fxmlNamespace.get("strip1");
        strip2 = (Rectangle) fxmlNamespace.get("strip2");

        Group mainWindowButtons = new Group();

//        exitButtonSprites.getChildren().addAll(exitGame,strip1,strip2);

        mainWindowButtons.getChildren().addAll(startGame, resumeGame, exitGame,strip1, strip2);

//        Rotation of startGame button
        Rotate rotate = new Rotate();

        rotate.pivotXProperty().bind(new SimpleDoubleProperty(startGame.getScaleX()/2 + 12));

        rotate.pivotYProperty().bind(new SimpleDoubleProperty(startGame.getScaleY()/2));

        startGame.getTransforms().add(rotate);

        Timeline timeline = new Timeline();

        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(3), new KeyValue(rotate.angleProperty(), 360));
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

//        Rotation of resumeGame Button
        Rotate rotate2 = new Rotate();

        rotate2.pivotXProperty().bind(new SimpleDoubleProperty(resumeGame.getScaleX()/2 + 45));
        rotate2.pivotYProperty().bind(new SimpleDoubleProperty(resumeGame.getScaleY()/2+20));

        resumeGame.getTransforms().add(rotate2);

        Timeline timeline2 = new Timeline();

        timeline2.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(3), new KeyValue(rotate2.angleProperty(), -360));
        timeline2.getKeyFrames().addAll(keyFrame2);
        timeline2.play();

//        Rotation of exitGame button
        Rotate rotate3 = new Rotate();
        Rotate rotate4 = new Rotate();

        rotate3.pivotXProperty().bind(new SimpleDoubleProperty(strip1.getScaleX()/2+strip1.getScaleY()/2+12));
        rotate3.pivotYProperty().bind(new SimpleDoubleProperty(strip1.getScaleX()/2+strip1.getScaleY()/2+115));
        rotate4.pivotXProperty().bind(new SimpleDoubleProperty(strip2.getScaleX()/2+strip2.getScaleY()/2+12));
        rotate4.pivotYProperty().bind(new SimpleDoubleProperty(strip2.getScaleX()/2+strip2.getScaleY()/2+115));

        strip1.getTransforms().add(rotate3);
        strip2.getTransforms().add(rotate4);

        Timeline timeline3 = new Timeline();

        timeline3.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(3), new KeyValue(rotate3.angleProperty(), -360));
        KeyFrame keyFrame4 = new KeyFrame(Duration.seconds(3), new KeyValue(rotate4.angleProperty(), -360));

        timeline3.getKeyFrames().addAll(keyFrame3, keyFrame4);
        timeline3.play();

        anchorPane.getChildren().addAll(mainWindowButtons);

        Scene scene = new Scene(anchorPane,800,900);

        scene.getStylesheets().add("src/CSS/styles.css");

        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("ColorSwitch | MainWindow");
        Main.getMainStage().show();

    }


    @FXML
    public void onMouseEntered(){
        startGame.setCursor(Cursor.HAND);
        resumeGame.setCursor(Cursor.HAND);
        strip1.setCursor(Cursor.HAND);
        strip2.setCursor(Cursor.HAND);
        exitGame.setCursor(Cursor.HAND);
    }

    //         GameWindow methods

    @FXML
    public void resumeGamePlay() throws IOException{

        Group group = new Group();

        Parent root = FXMLLoader.load(getClass().getResource("resumeGame.fxml"));

        group.getChildren().addAll(root, resumeButton, exitButton);

        Scene scene = new Scene(group, 800,900);

        scene.getStylesheets().add("src/CSS/styles.css");

        resumeGame.getScene().getWindow().hide();
        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("Color Switch | ResumeGame");
        Main.getMainStage().show();
    }


//    GamePlay window methods

    public void renderGamePlayWindow() {
        System.out.println(Main.getGameController().randVal);
        switch (Main.getGameController().randVal) {
            case 0:
                Image blue = new Image(new File("src/Images/Ball/blue.png").toURI().toString());
                gameBall.setFill(Color.rgb(54, 225, 243));
                break;
            case 1:
                Image pink = new Image(new File("src/Images/Ball/pink.jpg").toURI().toString());
                gameBall.setFill(Color.rgb(255, 0, 130));
                break;
            case 2:
                Image violet = new Image(new File("src/Images/Ball/violet.jpg").toURI().toString());
                gameBall.setFill(Color.rgb(140, 21, 245));
                break;
            case 3:
                Image yellow = new Image(new File("src/Images/Ball/yellow.jpg").toURI().toString());
                gameBall.setFill(Color.rgb(245, 225, 50));
                break;
        }

        gameBall.setSmooth(true);
        gameBall.setScaleX(1.5);
        gameBall.setScaleY(1.5);
        gameBall.setCenterX(Main.getGameController().getGameBallX());
//        System.out.println(gameBall.getCenterX());
        gameBall.setCenterY(Main.getGameController().getGameBallY());
//        System.out.println(gameBall.getCenterY());
        gameBall.setRadius(10);

//        Pause button
        pauseButton.setText("Pause");
        pauseButton.setMaxWidth(400);
        pauseButton.setMaxHeight(30);
        pauseButton.setStyle("-fx-font-weight: bold; -fx-background-color: #8a2be2;-fx-font-size: 42px;\n" +
                "    -fx-font-family: 'Blissful Thinking', serif;\n" +
                "    src: url('http://fonts.cdnfonts.com/css/blissful-thinking');");
        pauseButton.setLayoutX(800 - 180);
        pauseButton.setLayoutY(pauseButton.getMaxHeight()-20);
        pauseButton.setVisible(true);
        pauseButton.setFocusTraversable(false);

//        Score Label
        scoreLabel.setText(Integer.toString(Main.getGameController().getScore()));
        scoreLabel.setLayoutX(0);
        scoreLabel.setLayoutY(0);
        scoreLabel.setMaxWidth(400);
        scoreLabel.setMaxHeight(30);
        scoreLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #8a2be2;-fx-font-size: 42px;\n" +
                "    -fx-font-family: 'Blissful Thinking', serif;\n" +
                "    src: url('http://fonts.cdnfonts.com/css/blissful-thinking');");
        scoreLabel.setVisible(true);
        scoreLabel.setFocusTraversable(false);

        renderObstacle1();
    }

    public void renderObstacle1(){

        colorChanger.setImage(colorChange);
        colorChanger.setFitWidth(50);
        colorChanger.setFitHeight(50);
        colorChanger.setPreserveRatio(true);
        colorChanger.setVisible(true);

        starView.setImage(star);
        starView.setFitWidth(80);
        starView.setFitHeight(80);
        starView.setPreserveRatio(true);
        starView.setVisible(true);

        starView2.setImage(star);
        starView2.setFitWidth(80);
        starView2.setFitHeight(80);
        starView2.setPreserveRatio(true);
        starView2.setVisible(true);

        starView3.setImage(star);
        starView3.setFitWidth(80);
        starView3.setFitHeight(80);
        starView3.setPreserveRatio(true);
        starView3.setVisible(true);

        starView4.setImage(star);
        starView4.setFitWidth(80);
        starView4.setFitHeight(80);
        starView4.setPreserveRatio(true);
        starView4.setVisible(true);

        if(!images.getChildren().contains(starView)){
            images.getChildren().add(starView);
        }
        if(!images.getChildren().contains(starView2)){
            images.getChildren().add(starView2);
        }
        if(!images.getChildren().contains(starView3)){
            images.getChildren().add(starView3);
        }
        if(!images.getChildren().contains(starView4)){
            images.getChildren().add(starView4);
        }
        if(!images.getChildren().contains(colorChanger)){
            images.getChildren().add(colorChanger);
        }

        colors[0] = Color.rgb(54,225,243);
        colors[1] = Color.rgb(255, 0, 130);
        colors[2] = Color.rgb(140, 21, 245);
        colors[3] = Color.rgb(245, 225, 50);

//        Obstacle 1 -> Circle with radius 90
        circleObstacle90.setCenterX(Main.getGameController().getCircleObstacleX());
        circleObstacle90.setCenterY(Main.getGameController().getCircleObstacleY());
        starView.setX(circleObstacle90.getCenterX() - starView.getFitWidth()/2);
        starView.setY(Main.getGameController().getStarY());
        circleObstacle90.setStroke(Color.rgb(54,225,243));
        circleObstacle90.setStrokeWidth(10);
        circleObstacle90.setRadius(90);
        circleObstacle90.setFill(Color.TRANSPARENT);



//      Obstacle 2 -> Line with some width
        lineObstacle.setStartX(0);
        lineObstacle.setStartY(Main.getGameController().getLineObstacleStartY());
        lineObstacle.setEndX(800);
        lineObstacle.setEndY(Main.getGameController().getLineObstacleEndY());
        starView2.setX(400 - starView.getFitWidth()/2);
        starView2.setY(Main.getGameController().getStar2Y());
        lineObstacle.setStrokeWidth(15);
        lineObstacle.setStroke(Color.rgb(54,225,243));

//        Obstacle 3 -> Plus rotating
        plusHorizontal.setStartX(300);
        plusHorizontal.setStartY(Main.getGameController().getPlusHorizontalStartY());
        plusHorizontal.setEndX(500);
        plusHorizontal.setEndY(Main.getGameController().getPlusHorizontalEndY());
        plusHorizontal.setStrokeWidth(15);
        plusHorizontal.setStroke(Color.rgb(54,225,243));

        plusVertical.setStartX(400);
        plusVertical.setStartY(Main.getGameController().getPlusVerticalStartY());
        plusVertical.setEndX(400);
        plusVertical.setEndY(Main.getGameController().getPlusVerticalEndY());
        plusVertical.setStrokeWidth(15);
        plusVertical.setStroke(Color.rgb(54,225,243));

        starView3.setX(plusVertical.getStartX() - starView3.getFitWidth()/2);
        starView3.setY(Main.getGameController().getStar3Y());

//        After 3 obstacles color changer appears
        colorChanger.setX(starView3.getX());
        colorChanger.setY(Main.getGameController().getColorChangerY());

//        Obstacle 4 -> Rectangle rotating
        rectangleObstacle.setX(275);
        rectangleObstacle.setY(Main.getGameController().getRectObstacleY());
        rectangleObstacle.setWidth(250);
        rectangleObstacle.setHeight(250);
        starView4.setX(rectangleObstacle.getX() + rectangleObstacle.getWidth()/2 - starView4.getFitWidth()/2);
        starView4.setY(Main.getGameController().getStar4Y());
        rectangleObstacle.setStrokeWidth(15);
        rectangleObstacle.setStroke(colors[0]);
        rectangleObstacle.setFill(Color.TRANSPARENT);

        Rotate rotate = new Rotate(0,(plusHorizontal.getStartX() + plusHorizontal.getEndX())/2,(plusVertical.getStartY() + plusVertical.getEndY())/2);
        if(!plusHorizontal.getTransforms().contains(rotate) && !plusVertical.getTransforms().contains(rotate)){
            plusHorizontal.getTransforms().add(rotate);
            plusVertical.getTransforms().add(rotate);
        }

        Rotate rotate1 = new Rotate(0,rectangleObstacle.getX() + rectangleObstacle.getWidth()/2, rectangleObstacle.getY() + rectangleObstacle.getHeight()/2);

        if(!rectangleObstacle.getTransforms().contains(rotate1)){
            rectangleObstacle.getTransforms().add(rotate1);
        }

        timelineRotation = new Timeline();
        timelineRotation.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
            circleObstacle90.setStroke(colors[Main.getGameController().getCounter()%4]);
            lineObstacle.setStroke(colors[(Main.getGameController().getCounter()+1)%4]);
            plusHorizontal.setStroke(colors[Main.getGameController().getCounter()%4]);
            plusVertical.setStroke(colors[Main.getGameController().getCounter()%4]);
            rectangleObstacle.setStroke(colors[(Main.getGameController().getCounter()+1)%4]);

            if(!anchorPane.getChildren().contains(circleObstacle90)){
                anchorPane.getChildren().add(circleObstacle90);
            }

            if(!anchorPane.getChildren().contains(lineObstacle)){
                anchorPane.getChildren().add(lineObstacle);
            }

            if(!anchorPane.getChildren().contains(plusHorizontal)){
                anchorPane.getChildren().add(plusHorizontal);
            }

            if(!anchorPane.getChildren().contains(plusVertical)){
                anchorPane.getChildren().add(plusVertical);
            }

            if(!anchorPane.getChildren().contains(rectangleObstacle)){
                anchorPane.getChildren().add(rectangleObstacle);
            }

            Main.getGameController().setCounter(Main.getGameController().getCounter() + 1);
        }, new KeyValue(plusHorizontal.rotateProperty(),360), new KeyValue(plusVertical.rotateProperty(), 360), new KeyValue(rectangleObstacle.rotateProperty(), 360));
        timelineRotation.getKeyFrames().add(keyFrame);
        timelineRotation.play();

        handView.setImage(hand);
        handView.setX(360);
        handView.setY(750);
        handView.setFitWidth(100);
        handView.setFitHeight(100);
        handView.setPreserveRatio(true);
        handView.setVisible(true);

        if(!images.getChildren().contains(handView)){
            images.getChildren().add(handView);
        }

        Ball.setGameBall(gameBall);
    }


    /*
    @FXML
    public void pause() throws IOException {

        System.out.println("In");
        Group group = new Group();

        Parent root = FXMLLoader.load(getClass().getResource("pauseWindow.fxml"));

        group.getChildren().addAll(root);

        Scene scene = new Scene(group,800,900);

        scene.getStylesheets().add("src/CSS/styles.css");

        gameBall.getScene().getWindow().hide();
        Main.getMainStage().setScene(scene);
        Main.getMainStage().setTitle("Color Switch | PauseWindow");
        Main.getMainStage().show();

    }
*/

    @FXML
    public void resumePlay() throws IOException {

        pauseLayout.setVisible(false);

        saveCurrentState();

        Main.getGameController().startGame();
    }

    @FXML
    public void saveGame() throws IOException, ClassNotFoundException {
        String fileName = "src\\Saved Files\\Save_" + fileCounter + ".txt";
        FileOutputStream file = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);

        saveCurrentState();
        objectOutputStream.writeObject(Main.getGameController());

        objectOutputStream.close();
        file.close();

//        FileInputStream fileInputStream = new FileInputStream(fileName);
//        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//
//        Controller reader = (Controller) objectInputStream.readObject();
//        System.out.println(reader.getGameBallY());

        fileCounter++;

    }

    private void saveCurrentState() {
        Main.getGameController().setGameBallX(gameBall.getCenterX());
        Main.getGameController().setGameBallY(gameBall.getCenterY());
        Main.getGameController().setCircleObstacleX(circleObstacle90.getCenterX());
        Main.getGameController().setCircleObstacleY(circleObstacle90.getCenterY());
        Main.getGameController().setStarY(starView.getY());
        Main.getGameController().setLineObstacleStartY(lineObstacle.getStartY());
        Main.getGameController().setLineObstacleEndY(lineObstacle.getEndY());
        Main.getGameController().setStar2Y(starView2.getY());
        Main.getGameController().setPlusHorizontalStartY(plusHorizontal.getStartY());
        Main.getGameController().setPlusHorizontalEndY(plusHorizontal.getEndY());
        Main.getGameController().setPlusVerticalStartY(plusVertical.getStartY());
        Main.getGameController().setPlusVerticalEndY(plusVertical.getEndY());
        Main.getGameController().setStar3Y(starView3.getY());
        Main.getGameController().setColorChangerY(colorChanger.getY());
        Main.getGameController().setRectObstacleY(rectangleObstacle.getY());
        Main.getGameController().setStar4Y(starView4.getY());
        Main.getGameController().setScore(Integer.parseInt(scoreLabel.getText()));
    }

    @FXML
    public void goBack() throws IOException {
        Main.getGameController().renderMainWindow();
    }

    @FXML
    public void restartGame() throws IOException{
        pauseLayout.setVisible(false);
        Main.initializeGame();
        Main.getGameController().startGame();
    }

    @FXML
    public void exitGamePlay() throws IOException{
        pauseLayout.setVisible(false);
        Main.runGame();
    }

    public static Circle getGameBall() {
        return gameBall;
    }

    public static void setGameBall(Circle gameBall) {
        Controller.gameBall = gameBall;
    }

    public static AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        Controller.anchorPane = anchorPane;
    }

    public static ImageView getHandView() {
        return handView;
    }

    public static void setHandView(ImageView handView) {
        Controller.handView = handView;
    }

    public double getGameBallX() {
        return gameBallX;
    }

    public void setGameBallX(double gameBallX) {
        this.gameBallX = gameBallX;
    }

    public double getGameBallY() {
        return gameBallY;
    }

    public void setGameBallY(double gameBallY) {
        this.gameBallY = gameBallY;
    }

    public double getCircleObstacleX() {
        return circleObstacleX;
    }

    public void setCircleObstacleX(double circleObstacleX) {
        this.circleObstacleX = circleObstacleX;
    }

    public double getCircleObstacleY() {
        return circleObstacleY;
    }

    public void setCircleObstacleY(double circleObstacleY) {
        this.circleObstacleY = circleObstacleY;
    }

    public double getLineObstacleStartY() {
        return lineObstacleStartY;
    }

    public void setLineObstacleStartY(double lineObstacleStartY) {
        this.lineObstacleStartY = lineObstacleStartY;
    }

    public double getLineObstacleEndY() {
        return lineObstacleEndY;
    }

    public void setLineObstacleEndY(double lineObstacleEndY) {
        this.lineObstacleEndY = lineObstacleEndY;
    }

    public double getPlusHorizontalStartY() {
        return plusHorizontalStartY;
    }

    public void setPlusHorizontalStartY(double plusHorizontalStartY) {
        this.plusHorizontalStartY = plusHorizontalStartY;
    }

    public double getPlusHorizontalEndY() {
        return plusHorizontalEndY;
    }

    public void setPlusHorizontalEndY(double plusHorizontalEndY) {
        this.plusHorizontalEndY = plusHorizontalEndY;
    }

    public double getPlusVerticalStartY() {
        return plusVerticalStartY;
    }

    public void setPlusVerticalStartY(double plusVerticalStartY) {
        this.plusVerticalStartY = plusVerticalStartY;
    }

    public double getPlusVerticalEndY() {
        return plusVerticalEndY;
    }

    public void setPlusVerticalEndY(double plusVerticalEndY) {
        this.plusVerticalEndY = plusVerticalEndY;
    }

    public double getRectObstacleY() {
        return rectObstacleY;
    }

    public void setRectObstacleY(double rectObstacleY) {
        this.rectObstacleY = rectObstacleY;
    }

    public double getStarY() {
        return starY;
    }

    public void setStarY(double starY) {
        this.starY = starY;
    }

    public double getColorChangerY() {
        return colorChangerY;
    }

    public void setColorChangerY(double colorChangerY) {
        this.colorChangerY = colorChangerY;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getStar2Y() {
        return star2Y;
    }

    public void setStar2Y(double star2Y) {
        this.star2Y = star2Y;
    }

    public double getStar3Y() {
        return star3Y;
    }

    public void setStar3Y(double star3Y) {
        this.star3Y = star3Y;
    }

    public double getStar4Y() {
        return star4Y;
    }

    public void setStar4Y(double star4Y) {
        this.star4Y = star4Y;
    }
}
