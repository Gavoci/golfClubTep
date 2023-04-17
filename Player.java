import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

import java.util.concurrent.Semaphore;

class Player {

    private int playerId;
    private int numBalls = 0;
    private Label playerLabel = new Label();
    private DistributorePalline dispenser;

    public Player(int playerId, DistributorePalline dispenser) {
        this.playerId = playerId;
        this.dispenser = dispenser;
        updatePlayerLabel();
    }

    public VBox getPlayerNode() {
        VBox playerBox = new VBox();
        playerBox.getChildren().addAll(playerLabel);
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep((long) (Math.random() * 5000) + 2000);
                    int numBallsToRequest = (int) (Math.random() * 3) + 1;
                    if (dispenser.dispenseBalls(numBallsToRequest)) {
                        numBalls += numBallsToRequest;
                        updatePlayerLabel();
                        Thread.sleep((long) (Math.random() * 5000) + 2000);
                        dispenser.returnBalls(numBalls);
                        numBalls = 0;
                        updatePlayerLabel();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return playerBox;
    }

    private void updatePlayerLabel() {
        Platform.runLater(() -> playerLabel.setText("Player " + playerId + ":  palline: " + numBalls));
    }
}