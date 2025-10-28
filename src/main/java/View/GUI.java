package View;

import Connections.ClientInterface;
import Connections.Messages.LogResponseMessage;
import Controller.State.CardDrawing;
import Controller.State.GameState;
import View.Controllers.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cards.Card;
import model.Cards.Planets;
import model.Deck;
import model.DifferentShipComponents.ShipComponent;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.Stocks;
import Controller.State.ShipConstructionState;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Graphic user interface, used for the interaction with the players.
 */
public class GUI implements UI{

    /**
     * Current client manager
     */
    private ClientInterface clientInterface;

    /**
     * Primary stage, it's set in order to show a featured stage
     */
    private final Stage primaryStage;

    /**
     * Controller used during the ship construction
     */
    private DrawingController drawingController;
    private GameState gameState = null;
    private final GameTableWithCardsController gameTableWithCardsController = null;

    private boolean cardDrawingStarted = false;
    private ArrayList<ShipDashboard> players = new ArrayList<>();

    /**
     * Class constructor, it sets the primary stage
     * @param primaryStage current primary stage
     */
    public GUI(Stage primaryStage) { this.primaryStage = primaryStage; }

    /**
     * Lock object used to synchronize the threads
     */
    private final Object lock = new Object();

    /**
     * True if the player is resumed
     */
    private boolean resumed = false;

    /**
     * Method used to show the logo in the primary scene
     */
    public void startGame(){
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Start.fxml"));
                Scene scene = new Scene(loader.load());
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the connection scene: the scene is controlled
     * with the related connection controller
     * @see ConnectionController
     */
    public void askConnection(){
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Connection.fxml"));
                Parent root = loader.load();
                ConnectionController controller = loader.getController();
                controller.setClientInterface(clientInterface);
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Old method used to print and visualize the current ship
     * @deprecated
     * @param ship current ship
     */
    public void printShip(ShipComponent[][] ship){
        Platform.runLater(() -> {
            try {
                if(ship==null) return;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/PrintShip.fxml"));
                Parent root = loader.load();
                PrintShipController controller = loader.getController();
                controller.setShip(ship);
                Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm());
                BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        backgroundSize
                );
                ((GridPane) root).setBackground(new Background(backgroundImage));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Your Ship");
                stage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Old method used to visualize the ship with the components which need to be removed
     * @deprecated
     * @param ship current ship
     * @param wrongComponents matrix of boolean, true indicates a component which need to be
     *                        removed
     */
    private void printShipToBeFixed(ShipComponent[][] ship, boolean[][] wrongComponents){
        Platform.runLater(() -> {
            try {
                if(ship==null) return;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/PrintToBeFixed.fxml"));
                Parent root = loader.load();
                PrintToBeFixedShipController controller = loader.getController();
                controller.setShipWithErrors(ship,wrongComponents);
                Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/cardboard-1b.jpg")).toExternalForm());
                BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
                BackgroundImage backgroundImage = new BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        backgroundSize
                );
                ((GridPane) root).setBackground(new Background(backgroundImage));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ship to be fixed");
                stage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Setter of the current client interface
     * @param clientInterface current client interface
     */
    @Override
    public void setInterface(ClientInterface clientInterface){this.clientInterface = clientInterface;}

    /**
     * Method used to set the primary stage with the show login scene: the scene is controlled
     * with the related log response controller
     * @see LogResponseController
     */
    @Override
    public void showLoginResponse(LogResponseMessage message) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/LogResponse.fxml"));
                Parent root = loader.load();
                LogResponseController controller = loader.getController();
                controller.setGUI(this);
                controller.showMessageFrom(message);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login response");
                stage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(_ -> stage.close());
                delay.play();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the claim reward scene: the scene is controlled
     * with the related claim reward controller
     * @see ClaimRewardController
     */
    @Override
    public void claimReward(Card card) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/ClaimReward.fxml"));
                Parent root = loader.load();
                ClaimRewardController controller = loader.getController();
                controller.setClientInterface(this.clientInterface);
                controller.handleCard(card);
                primaryStage.setTitle("Card Choice");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the draw component scene: the scene is controlled
     * with the related drawing controller
     * @see DrawingController
     */
    @Override
    public void drawShipComponentOrSmallDeck() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/drawShipComponentOrSmallDeck.fxml"));
                Parent root = loader.load();
                drawingController = loader.getController();
                drawingController.setResumed(resumed);
                drawingController.setClientInterface(this.clientInterface);
                drawingController.postInitialize();
                primaryStage.setTitle("Building Ship");
                primaryStage.setScene(new Scene(root, 1000, 700));
                primaryStage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to handle an incoming projectile, with a GUI visual representation
     * @param ship is the current ship
     * @param projectile is the projectile to handle
     * @param trajectory is the trajectory of the projectile
     * @see ManageProjectileController
     */
    @Override
    public void handleProjectile(Projectile projectile, int trajectory, ShipDashboard ship) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loaderHandleProjectile = new FXMLLoader(getClass().getResource("/FXML_files/HandleProjectile.fxml"));
                Parent root = loaderHandleProjectile.load();
                ManageProjectileController controller = loaderHandleProjectile.getController();

                controller.handleProjectile(projectile, trajectory, clientInterface);

                Stage stage = new Stage();
                stage.setTitle("handle projectile");
                stage.setScene(new Scene(root, 1000, 700));
                stage.show();
            } catch (Exception e) {
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the hire crew scene: the scene is controlled
     * with the related hire crew controller
     * @see HireCrewController
     */
    @Override
    public void connectLifeSupportsHireCrewInitializeAttributesAndSendShip(ShipComponent[][] ship) {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                }catch(Exception e){
                    printError(e);
                }
            }

            Platform.runLater(() -> {
                    try {
                        FXMLLoader loaderHireCrew = new FXMLLoader(getClass().getResource("/FXML_files/HireCrew.fxml"));
                        Parent rootHireCrew = loaderHireCrew.load();
                        HireCrewController hireCrewController = loaderHireCrew.getController();

                        hireCrewController.setClientInterface(this.clientInterface);
                        hireCrewController.setGridAndBackground();
                        hireCrewController.printShip();
                        //hireCrewController.startHiring();
                        primaryStage.setTitle("Hire Crew");
                        primaryStage.setScene(new Scene(rootHireCrew, 1000, 700));
                        primaryStage.show();
                    } catch (Exception e) {
                        printError(e);
                    }
            });
        }).start();
    }

    /**
     * Old method used to visualize the fixed ship
     * @deprecated
     */
    public void showCheckedShip() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GenericMessage.fxml"));
                Parent root = loader.load();
                GenericMessageController controller = loader.getController();
                controller.showMessage("Your ship is ready to take-off! Just wait for the others to be ready too!");
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the fix ship scene: the scene is controlled
     * with the related fix controller
     * @see FixController
     */
    @Override
    public void fixShip(ShipDashboard ship) {
        //serialExecutor.submit(()-> {
            Platform.runLater(() -> {
                synchronized (lock) {
                    try {
                        FXMLLoader loaderFixShip = new FXMLLoader(getClass().getResource("/FXML_files/FixShip.fxml"));
                        Parent rootFixShip = loaderFixShip.load();
                        FixController fixController = loaderFixShip.getController();

                        fixController.setClientInterface(this.clientInterface);
                        fixController.setShip(ship);
                        fixController.setCheck(ship.checkShip());
                        fixController.setGridAndBackground();
                        fixController.setShipWithErrors();
                        primaryStage.hide();
                        Stage stage = new Stage();
                        stage.setTitle("Fix Ship");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene(rootFixShip, 1000, 700));
                        stage.setOnCloseRequest(event -> {
                            if (ship.shipNeedsFixing(ship.checkShip())) {
                                showAlert("You must fix the ship before proceeding.");
                                event.consume();
                            }
                        });
                        stage.setOnHidden(_ -> {
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                        });
                        stage.showAndWait();

                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                        printError(e);
                    }
                }
            });
        //});
    }

    /**
     * Method used to show alerts
     * @param message current alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method used to pass the component which needs to be handled inside the right controller
     * for ship construction
     * @param component current component
     */
    @Override
    public void handleComponent(ShipComponent component) {Platform.runLater(() -> drawingController.handleComponent(component));}

    /**
     * Method used to set a stage with the small deck scene: the scene is controlled
     * with the related small deck controller
     * @see SmallDeckController
     */
    @Override
    public void handleSmallDeck(Deck deck) {
        if (deck == null) {
            return;
        }
        Platform.runLater(()-> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/SmallDeck.fxml"));
                Parent root = loader.load();
                SmallDeckController controller = loader.getController();
                controller.setDeck(deck);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Small Deck");
                stage.showAndWait();
                clientInterface.returnSmallDeck();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the ask double motor scene: the scene is controlled
     * with the related ask double motor controller
     * @see AskDoubleMotorController
     */
    @Override
    public void askDoubleMotor() {
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/AskDoubleMotor.fxml"));

                Parent root = loader.load();
                AskDoubleMotorController controller = loader.getController();

                controller.initializeMeth(clientInterface);
                controller.setGridAndBackground();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Activate Double Motor");
                stage.show();
            } catch (Exception e) {
                System.out.println("Error askDoubleMotorGUI: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the ask docking scene: the scene is controlled
     * with the related ask docking controller
     * @see AskDockingController
     */
    @Override
    public void askDocking(Card curCard) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/AskDocking.fxml"));
                Parent root = loader.load();
                AskDockingController controller = loader.getController();

                controller.setClientInterface(clientInterface);
                controller.startCard(curCard);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ask Docking");
                stage.show();

            } catch (Exception e) {
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the ask double cannon scene: the scene is controlled
     * with the related ask double cannon controller
     * @see AskDoubleCannonController
     */
    @Override
    public void askDoubleCannon(ShipComponent[][] ship) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/AskDoubleCannon.fxml"));
                Parent root =loader.load();
                AskDoubleCannonController controller = loader.getController();

                controller.setGridAndBackgroundAndInitializeScene(clientInterface);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Activation Double Cannons");
                stage.show();
            } catch (Exception e) {
                System.out.println("Error ASK DOUBLE CANNON GUI: " + e);
                printError(e);
            }
        });

    }

    /**
     * Method used to set a stage with the ask activate double cannon when attacked scene:
     * the scene is controlled with the related ask activate double cannon when attacked controller
     * @see AskToActivateDoubleCannonController
     */
    @Override
    public boolean askToActivateDoubleCannonWhenAttacked(Projectile projectile, int trajectory, ShipDashboard ship){
        final boolean[] res = new boolean[1];

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/ActivateDoubleCannonWhenAttacked.fxml"));
                Parent root = loader.load();
                AskToActivateDoubleCannonController controller = loader.getController();

                controller.init(clientInterface, projectile, res);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Activate Double Cannon");
                stage.show();
            } catch (Exception e) {
                printError(e);
                res[0] = false;
            }
        });

        return res[0];
    }

    /**
     * Method used to set a stage with the ask give up crew scene: the scene is controlled
     * with the related ask give up crew controller
     * @see AskGiveUpCrewController
     */
    @Override
    public void askGiveUpCrew(int amount) {
     Platform.runLater(() -> {
         try{
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/AskGiveUpCrew.fxml"));
             Parent root =loader.load();
             AskGiveUpCrewController controller = loader.getController();

             controller.setGridAndBackground(clientInterface, amount);

             Stage stage = new Stage();
             stage.setScene(new Scene(root));
             stage.setTitle("Give up crew");
             stage.show();
         }catch(Exception e){
             System.out.println("Error in askGiveUpCrew GUI: " + e);
             printError(e);
         }
     });
    }

    /**
     * Method used to set a stage with the ask add and rearrange stocks scene: the scene is controlled
     * with the related ask add and rearrange stocks controller
     * @see AddAndRearrangeStocksController
     */
    @Override
    public void askAddAndRearrangeStocks(Stocks stocks, ShipComponent[][] ship) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/AddAndRearrangeStocks.fxml"));
                Parent root = loader.load();
                AddAndRearrangeStocksController controller = loader.getController();

                controller.setGridAndBackground(clientInterface, stocks, ship);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Add and Rearrange Stocks");
                stage.show();
            } catch(Exception e) {
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the notify timer restarted scene: the scene is
     * controlled with the related notify timer restarted controller
     * @see NotifyTimerRestartedController
     */
    @Override
    public void notifyTimerRestarted(boolean isLast) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/NotifyTimerRestarted.fxml"));
                Parent root = loader.load();
                NotifyTimerRestartedController controller = loader.getController();
                controller.setText(isLast);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Timer Restarted");
                stage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(_ -> stage.close());
                delay.play();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the notify timer expired scene: the scene is controlled
     * with the related notify timer expired controller
     * @see NotifyTimerExpiredController
     */
    @Override
    public void notifyTimerExpired(){
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/NotifyTimerExpired.fxml"));
                Parent root = loader.load();
                NotifyTimerExpiredController controller = loader.getController();
                controller.setInterface(clientInterface);
                controller.setText(false);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Timer Expired");
                stage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.play();
                delay.setOnFinished(_ -> stage.close());
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to handle the timer expired phase
     * @param isLast is a boolean that indicates if the current timer is the last one
     */
    @Override
    public void handleTimerExpired(boolean isLast) {}

    /**
     * Method used to set a stage with generic message scene, used to show the earned credits:
     * the scene is controlled with the generic message controller
     * @see GenericMessageController
     */
    @Override
    public void notifyCredits(int credits) {
        Platform.runLater(() -> {
            try{
                FXMLLoader loaderGenericMessage = new FXMLLoader(getClass().getResource("/FXML_files/GenericMessage.fxml"));
                GenericMessageController genericMessageController = loaderGenericMessage.getController();

                Parent rootGenericMessage = loaderGenericMessage.load();
                genericMessageController.showMessage("You have just earned" + credits + " credits!");
                Stage stage = new Stage();
                stage.setTitle("Notify Credits");
                stage.setScene(new Scene(rootGenericMessage));
                stage.showAndWait();
            } catch(Exception e) {
                printError(e);
            }
        });
    }

    /**
     * Method used to handle the land on planet phase, for GUI visualization
     * @param curCard is the current Planets card
     * @param freePlanetsIndex is an array of integers that represents the free planets indexes
     * @see PlanetsLandingController
     */
    @Override
    public void landOnPlanet(ArrayList<Integer> freePlanetsIndex, Planets curCard) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/PlanetsLandingChoice.fxml"));
                Parent root = loader.load();
                PlanetsLandingController controller = loader.getController();

                controller.askIfPlayerWantsToLand(clientInterface, freePlanetsIndex, curCard);

                Stage stage = new Stage();
                stage.setTitle("Planets Card");
                stage.setScene(new Scene(root));
                stage.show();

            }catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });

    }

    /**
     * Method used to set a stage with a generic scene in order to notify the landing
     * on a planet: the scene is controlled with the related generic message controller
     * @see GenericMessageController
     */
    @Override
    public void planetsState() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/PlanetsLandingChoice.fxml"));
                Parent root = loader.load();
                PlanetsLandingController controller = loader.getController();

                controller.setText("In a while you will be able to land on some planets...(Maybe)");

                Stage stage = new Stage();
                stage.setTitle("Planets Card");
                stage.setScene(new Scene(root));
                stage.show();

            }catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the game table with cards scene: the scene is controlled
     * with the related game table with cards controller
     * @see GameTableWithCardsController
     */
    @Override
    public void drawCard() {
        Platform.runLater(() -> {
            //this.gameState = new CardDrawing();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GameTableWithCards.fxml"));
                Parent root = loader.load();
                GameTableWithCardsController controller = loader.getController();

                controller.setClientInterface(this.clientInterface);
                controller.onDrawCard();
                controller.updatePositionsAndTurns(this.players);
                //Stage stage = new Stage();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Card Drawing");
                primaryStage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the state scene in order to notify the resuming:
     * the scene is controlled with the related state controller
     * @see StateController
     */
    @Override
    public void resume() {
        Platform.runLater(()->{
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/State.fxml"));
                Parent root = loader.load();
                StateController controller = loader.getController();
                controller.setText("Welcome back! You have returned into the game!");
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }
            catch(Exception e){
                printError(e);
            }
        }
        );
        this.setResumed();
        if(clientInterface.getState() instanceof ShipConstructionState) {
            this.drawShipComponentOrSmallDeck();
        }
    }

    /**
     * Setter of resumed
     */
    private void setResumed(){this.resumed = true;}

    /**
     * Method used to handle the positioning and turns visualization in the GUI
     * @param players is the arraylist of ships that represent the players
     */
    @Override
    public void showPositionsAndTurns(ArrayList<ShipDashboard> players) {
        this.players = players;
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GameTableWithCards.fxml"));
                Parent root = loader.load();
                GameTableWithCardsController controller = loader.getController();

                controller.setClientInterface(clientInterface);
                controller.onDrawCard();
                controller.updatePositionsAndTurns(players);

                //Stage stage = new Stage();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Card Drawing");
                primaryStage.show();

            } catch(Exception e){
                printError(e);
            }
        });
    }

    /**
     * Method used to handle the waiting for others turn phase
     */
    @Override
    public void waitingForOthersTurns() {

    }

    /**
     * Method used to set a stage with the generic message scene: the scene is controlled
     * with the related generic message controller
     * @see GenericMessageController
     */
    @Override
    public void sendGenericMessage(String message) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GenericMessage.fxml"));
                Parent root = loader.load();
                GenericMessageController controller = loader.getController();
                controller.showMessage(message);
                Stage stage = new Stage();
                stage.setTitle("Notification");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the asking nickname scene: the scene is
     * controlled with the related nickname controller
     * @see NicknameController
     */
    @Override
    public void askingNickname() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Nickname.fxml"));
                Parent root = loader.load();
                NicknameController controller = loader.getController();

                controller.setClientInterface(clientInterface);

                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Nickname");
                primaryStage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the asking number of players scene:
     * the scene is controlled with the related first player controller
     * @see FirstPlayerController
     */
    @Override
    public void numberOfPlayers() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/FirstPlayer.fxml"));
                Parent root = loader.load();
                FirstPlayerController controller = loader.getController();
                controller.setClientInterface(clientInterface);
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the waiting partners scene
     */
    @Override
    public void waitingPartners() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/WaitingPartners.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Waiting Room");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the beginning scene: the scene is controlled
     * with the related beginning controller
     * @see BeginController
     */
    @Override
    public void begin(ArrayList<String> players) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Begin.fxml"));
                Parent root = loader.load();
                BeginController controller = loader.getController();
                controller.setupPlayers(players);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Begin");
                stage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set a stage with the notify turn scene
     */
    @Override
    public void notifyTurn() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Turn.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Turn Notification");
                stage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the state scene: the scene is controlled
     * with the related state controller. In case of card drawing is set the game table
     * with cards scene as primary stage with related controller.
     * @see GameTableWithCardsController
     * @see StateController
     */
    @Override
    public void notifyNewGameState(GameState newGameState) {
        Platform.runLater(() -> {
            try {
                if (newGameState instanceof CardDrawing) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GameTableWithCards.fxml"));
                    Parent root = loader.load();

                    cardDrawingStarted = true;

                    primaryStage.setTitle("Card Drawing");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
                this.gameState = newGameState;
                if (cardDrawingStarted) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GameTableWithCards.fxml"));
                    Parent root = loader.load();
                    GameTableWithCardsController controller = loader.getController();

                    controller.setClientInterface(clientInterface);
                    controller.onDrawCard();
                    controller.updatePositionsAndTurns(this.players);
                    controller.showCard(clientInterface.getCurCard());

                    primaryStage.setTitle("Card Drawing");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/State.fxml"));
                Parent root = loader.load();
                StateController controller = loader.getController();
                newGameState.accept(new GUI_StateVisitor(controller));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the game table with cards scene: the scene is controlled
     * with the related game table with cards controller. It's used to notify a new card drawn
     * @see GameTableWithCardsController
     */
    @Override
    public void notifyNewCardDrawn(Card card) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/GameTableWithCards.fxml"));
                Parent root = loader.load();
                GameTableWithCardsController controller = loader.getController();

                controller.setClientInterface(clientInterface);
                controller.onDrawCard();
                controller.updatePositionsAndTurns(this.players);
                controller.showCard(card);

                //Stage stage = new Stage();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Card Drawing");
                primaryStage.show();

                clientInterface.setCard(card);
            } catch(Exception e){
                printError(e);
            }
        });
    }

    /**
     * Method used to set the primary stage with the winners scene: the scene is controlled
     * with the related winners controller
     * @see WinnersController
     */
    @Override
    public void winners(ArrayList<ShipDashboard> winners, String nickname) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_files/Winners.fxml"));
                Parent root = loader.load();
                WinnersController controller = loader.getController();

                controller.setClientInterface(clientInterface, this);
                controller.setEndGameData(winners, nickname);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Game Over");
                stage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                printError(e);
            }
        });
    }

    /**
     * Method used to print the exceptions
     * @param e current exception
     */
    private void printError(Exception e){e.printStackTrace();}

}
