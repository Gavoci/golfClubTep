import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.util.concurrent.Semaphore;

public class DistributorePalline extends Application {

    private static final int NUM_PLAYERS = 5;
    private static final int NUM_BALLS = 20;

    private Semaphore dispenserLock = new Semaphore(1);
    private Semaphore ballsAvailable = new Semaphore(NUM_BALLS);

    private Label ballsAvailableLabel = new Label("Palline disponibili: " + NUM_BALLS);

    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        VBox players = new VBox();
        for (int i = 01; i <= NUM_PLAYERS; i++) {
            Player player = new Player(i, this);
            players.getChildren().add(player.getPlayerNode());
        }
        
        Image image = new Image("sfondo.jpg");
        
        /*BackgroundImage backgroundImage = new BackgroundImage(
            image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT
        );*/
        
        
        root.setCenter(players);
        //root.setBackground(new Background(backgroundImage));

        root.setBottom(ballsAvailableLabel);

        Scene scene = new Scene(root, 1000, 750);

        primaryStage.setTitle("Dispenser di palline");
        primaryStage.setScene(scene);
        
        int x=220;
        //creazione buco 
        for(int i=0;i<NUM_PLAYERS;i++){
            Circle circle = new Circle(x, 150, 20, Color.BLACK);
            root.getChildren().add(circle);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2);
            x+=140;
        }
       
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public boolean dispenseBalls(int numBalls) {
        boolean result = false;
        try {
            dispenserLock.acquire();
            if (ballsAvailable.availablePermits() >= numBalls) {
                ballsAvailable.acquire(numBalls);
                result = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dispenserLock.release();
        }
        updateBallsAvailableLabel();
        return result;
    }

    public void returnBalls(int numBalls) {
        ballsAvailable.release(numBalls);
        updateBallsAvailableLabel();
    }

    private void updateBallsAvailableLabel() {
        Platform.runLater(() -> ballsAvailableLabel.setText("Palline disponibili: " + ballsAvailable.availablePermits()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}


