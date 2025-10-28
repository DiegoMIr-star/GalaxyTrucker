package View;

import Connections.ClientInterface;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main of the GUI: it allows to start the first scene of the GUI
 */
public class GUIStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GUI gui = new GUI(stage);
        ClientInterface clientInterface = new ClientInterface(gui);
        gui.setInterface(clientInterface);
        gui.startGame();
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(_ -> gui.askConnection());
        delay.play();
    }
}
