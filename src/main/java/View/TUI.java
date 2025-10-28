package View;

import Connections.ClientInterface;
import Connections.Messages.LogResponseMessage;
import Controller.State.GameState;
import Controller.State.ShipConstructionState;
import View.ColorManagement.ConsoleColor;
import View.tuiGraphics.*;
import model.Cards.*;
import model.Deck;
import model.DifferentShipComponents.*;
import model.Projectiles.Orientation;
import model.Projectiles.Projectile;
import model.ShipDashboard;
import model.Stocks;
import model.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static View.ColorManagement.ConsoleColor.*;

/**
 * Text user interface, used for the interaction with the players
 */
public class TUI implements UI{

	/**
	 * Client manager associated to the player
	 */
	private ClientInterface clientInterface;

	/**
	 * Scanner used to collect the inputs
	 */
	private final Scanner scanner;

	/**
	 * Current component selected
	 */
	ShipComponent selectedComponent;

	/**
	 * Attribute used to store the builder,
	 * necessary to create the string representation of the ship dashboard
	 */
	private ShipStringBuilder shipString = null;

	/**
	 * Attribute used to store the number of characters
	 * necessary to represent the width of a ship component
	 */
	private int ASCIIcharsPerShipComponentWidth = 36;

	/**
	 * Attribute used to store the number of characters
	 * necessary to represent the width of a component (not a ship component)
	 */
	private int ASCIIcharsPerComponentWidth = 64;

	/**
	 * Client console type: windows, apple or intelliJ
	 */
	ConsoleType consoleType;

	/**
	 * Class constructor, used to initialize the scanner
	 */
	public TUI() {
		scanner = new Scanner(System.in);
	}

	/**
	 * Setter of the client interface
	 * @param clientInterface current client interface
	 */
	@Override
	public void setInterface(ClientInterface clientInterface){
		this.clientInterface=clientInterface;
	}

	/**
	 * Method used to interact with the client in order to ask for the console type wished
	 */
	public void askConsole(){
		String input;
		int answer;
		while(true){
			System.out.println("What console is this text being printed on?");
			System.out.println("Insert '1' for the intelliJ integrated console,");
			System.out.println("Insert '2' for Windows Terminal,");
			System.out.println("Insert '3' for the macOS console,");

			synchronized (System.in){
				input = scanner.nextLine();

				try{
					answer = Integer.parseInt(input);
					switch(answer){
						case 1:
							System.out.println("You selected the intelliJ integrated console.");
							consoleType = ConsoleType.IntelliJ_console;
							ASCIIcharsPerShipComponentWidth = 36;
							ASCIIcharsPerComponentWidth = 64;
							return;
						case 2:
							System.out.println("You selected the Widows Terminal.");
							consoleType = ConsoleType.Windows_terminal;
							ASCIIcharsPerShipComponentWidth = 90;
							ASCIIcharsPerComponentWidth = 100;
							return;
						case 3:
							System.out.println("You selected the macOS console.");
							consoleType = ConsoleType.MacOS_terminal;
							ASCIIcharsPerShipComponentWidth = 90;
							ASCIIcharsPerComponentWidth = 100;
							return;
						default:
							System.out.println("Please select one of the options.");
							break;
					}
				} catch(NumberFormatException e){
					System.out.println("Please insert a valid input.");
				}
			}
		}

	}

	/**
	 * Method used to print the logo of the game
	 * @throws IOException input output exception thrown
	 */
	public void printTitle() throws IOException {
		StringImage title = new StringImage(consoleType, 256);

		System.out.println("here");
		String thisstr = title.computeAndGetStringImage("/components/title.ppm");
		System.out.println(thisstr);
		System.out.println("there");
	}

	/**
	 * Method used to print the first interaction with the player, calling the printing
	 * of the logo
	 * @see #printTitle()
	 * @throws IOException input output exception thrown
	 */
	public void gameStart() throws IOException {
		printTitle();
       /*System.out.println("\n" +TEXT_PURPLE+
				" _______  _______  _        _______                     _________ _______           _______  _        _______  _______ \n" +
				"(  ____ \\(  ___  )( \\      (  ___  )|\\     /||\\     /|  \\__   __/(  ____ )|\\     /|(  ____ \\| \\    /\\(  ____ \\(  ____ )\n" +
				"| (    \\/| (   ) || (      | (   ) |( \\   / )( \\   / )     ) (   | (    )|| )   ( || (    \\/|  \\  / /| (    \\/| (    )|\n" +
				"| |      | (___) || |      | (___) | \\ (_) /  \\ (_) /      | |   | (____)|| |   | || |      |  (_/ / | (__    | (____)|\n" +
				"| | ____ |  ___  || |      |  ___  |  ) _ (    \\   /       | |   |     __)| |   | || |      |   _ (  |  __)   |     __)\n" +
				"| | \\_  )| (   ) || |      | (   ) | / ( ) \\    ) (        | |   | (\\ (   | |   | || |      |  ( \\ \\ | (      | (\\ (   \n" +
				"| (___) || )   ( || (____/\\| )   ( |( /   \\ )   | |        | |   | ) \\ \\__| (___) || (____/\\|  /  \\ \\| (____/\\| ) \\ \\__\n" +
				"(_______)|/     \\|(_______/|/     \\||/     \\|   \\_/        )_(   |/   \\__/(_______)(_______/|_/    \\/(_______/|/   \\__/\n" +
				"                                                                                                                       \n" + RESET);*/
		System.out.println("Let's start with the game!");
		askConnection();
	}

	/**
	 * Method used to interact with the client, asking the kind of connection socket or RMI.
	 * After the decision it's asked for the IP and the port of the server in order to begin
	 * the connection
	 * @throws IOException input output exception thrown
	 */
	public void askConnection() throws IOException {
		String IP = "";
		String in;
		int input;
		String IP_regex="^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$";
		int port = 0;
		boolean valid=false;
		boolean socketConnection=true;
		while(!valid){
			System.out.println("Just choose your connection preferences!");
			System.out.println("Choose one of your options:");
			System.out.println("1. Socket.");
			System.out.println("2. RMI.");
			synchronized (System.in){
				in=scanner.nextLine();
			}
			try{
				input=Integer.parseInt(in);
			} catch (NumberFormatException e) {
				System.out.println("Problem with number format");
				input=-1;
			}
			switch(input){
				case 1: {
					valid=true;
					System.out.println("So socket is your choice!");
					break;
				}
				case 2: {
					socketConnection=false;
					valid=true;
					System.out.println("So RMI is your choice!");
					break;
				}
				default: {
					System.out.println("You're trying to select an invalid option.");
					break;
				}
			}
		}
		System.out.println("It's time to insert IP and port of the server.");
		valid=false;
		while(!valid){
			System.out.println("Please begin with IP:");
			synchronized (System.in){IP=scanner.nextLine();}
			System.out.println("Please insert now the port:");
			synchronized (System.in){
				try{
					port=scanner.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Invalid port number");
				}
				scanner.nextLine();
			}
			if((IP.matches(IP_regex)) && port>=1024 && port<65536){
				valid=true;
			}
			else {
				System.out.println("Invalid IP, try again.");
			}
		}
		System.out.println("You have selected real IP and port!");
		System.out.println("Just try to connect...");
		clientInterface.connect(IP,port,socketConnection);
	}

	/**
	 * Method used to print a generic message
	 * @param message current message to print
	 */
	@Override
	public void sendGenericMessage(String message) {
		System.out.println(TEXT_BLUE + message + RESET);
	}

	/**
	 * Method used to ask player nickname and check if it's well written
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void askingNickname() throws IOException {
		System.out.println("Choose your nickname:");
		String nickname = null;
		boolean valid = false;
		while(!valid) {
			synchronized (System.in) {
				nickname = scanner.nextLine();
			}
			//check if the nickname is not null
			if (nickname == null) {
				System.out.println("Nickname cannot be null");
			}
			//check if the nickname is empty
			else if (nickname.isEmpty()) {
				System.out.println("Nickname cannot be empty");
			}
			//check if the nickname contains only spaces
			else if (nickname.trim().isEmpty()) {
				System.out.println("Nickname cannot contain just spaces");
			}
			else {
				valid = true;
			}
		}
		clientInterface.setNickname(nickname);
	}

	/**
	 * Method used to wait for a random character, giving to the client the time to read
	 */
	void letPlayerRead(){
		System.out.println("Insert anything to continue...");
		synchronized (System.in) { scanner.nextLine(); }
	}

	/**
	 * Method called only for the first player, used to ask the number of the players
	 * for the current game
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void numberOfPlayers() throws IOException {
		String in;
		int playersNumber=0;
		System.out.println("You're the first player of your game!");
		System.out.println("A game can be played from a maximum of 4 players.");
		do{
			System.out.println("You have the right to choose the number of players:");
			synchronized (System.in){
				in=scanner.nextLine();
			}
			try{
				playersNumber=Integer.parseInt(in);
			} catch (NumberFormatException e){
				System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}
		}
		//remember to set the lower bound at 2, after debug
		while(playersNumber<2 || playersNumber>4);
		clientInterface.setPlayersNumber(playersNumber);
	}

	/**
	 * Method used to signal that you are waiting for the other players
	 */
	@Override
	public void waitingPartners() {
		System.out.println("You are in the waiting room...");
		System.out.println("Just wait until your game partners arrive...");
	}

	/**
	 * Method used to signal that you are waiting for the other players
	 */
	@Override
	public void waitingForOthersTurns() {
		System.out.println(TEXT_CYAN + "Please wait while the other players finish with their turn..." + RESET);
	}

	/**
	 * Method called after reaching the number of players in order to begin the game
	 * @param players list of game players, used to signal the game partners
	 */
	@Override
	public void begin(ArrayList<String> players) {
		System.out.println("The game can begin!");
		System.out.println("There are "+players.size()+" players in this game!");
		System.out.println("Here are their names:");
        for (String player : players) {
            System.out.println(player);
        }
	}

	/**
	 * Method used to signal if the connection is well established or if there
	 * are some problems
	 * @param message current log response message
	 */
	@Override
	public void showLoginResponse(LogResponseMessage message) {
		if(message.isConnected()){
			System.out.println("Connection established.");
		}
		else{
			System.out.println("Connection lost.");
			System.exit(0);
		}
		if(!message.getNicknameStatus()){
			System.out.println("The nickname you have selected can't be used, please pick a different one.");
			try {
				askingNickname();
			} catch (IOException e){
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * Method used to signal the card, according to the visitor pattern. The method
	 * is used also to ask if the player wants to claim the reward of the specific card
	 * @param card current card
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void claimReward(Card card) throws IOException {
		String in;
		System.out.println("You have won the battle against the enemy!");
		int unexpected = card.returner(new TUICardVisitor());
		if(unexpected==0) sendGenericMessage("Unexpected card...");
		do{
			System.out.println("Do you want to claim your reward?");
			System.out.println("Just say 'yes' or 'no':");
			synchronized (System.in){in=scanner.nextLine();}
			if(!in.equals("yes") && !in.equals("no")) System.out.println("Invalid option!");
		}
		while(!in.equals("yes")&&!in.equals("no"));
		clientInterface.claimReward(in.equals("yes"));
	}

	/**
	 * Method used to print and show to the client the wanted component
	 * @param component current component to show
	 */
	private void printComponent(ShipComponent component){
		/*
		System.out.println("\nN " + component.getNorthSide());
		System.out.println("E " + component.getEastSide());
		System.out.println("S " + component.getSouthSide());
		System.out.println("W " + component.getWestSide() + "\n");*/

		ShipStringBuilder temp = new ShipStringBuilder(1,1, ASCIIcharsPerComponentWidth, 0, 0, consoleType);
		ArrayList<ArrayList<ShipComponent>> comps = new ArrayList<>();
		ArrayList<ShipComponent> comp = new ArrayList<>();
		comp.add(component);
		comps.add(comp);
		try {
			System.out.println(temp.buildShipString(comps));
		} catch (IOException e) {
			throw new RuntimeException("There was an issue while trying to build the ship string");
		}
	}

	/**
	 * Method used to notify that the the revealed components have been updated
	 */
	public void notifyRevealedComponents(){
		System.out.println(TEXT_CYAN + "The revealed components have been updated." + RESET);
	}

	/**
	 * Method used to print and show the list of components
	 * @param components list of current components to be printed
	 */
	public void printComponents(ArrayList<ShipComponent> components) {
		/*for(ShipComponent comp : components){
			printComponent(comp);
		}*/
		if(components == null)
			throw new RuntimeException("The array of components was null");

		ShipStringBuilder temp = new ShipStringBuilder(components.size(),1, ASCIIcharsPerComponentWidth, 0, 0, consoleType);
		ArrayList<ArrayList<ShipComponent>> comps = new ArrayList<>();
		comps.add(components);
		try {
			System.out.println(temp.buildShipString(comps));
		} catch (IOException e) {
			System.out.println("There was an issue while trying to build the ship string");
		}
	}

	/**
	 * Method used to set up the string ship, if it's the first time
	 * it creates a new ship builder using the correct dimensions, lower bounds, characters width and console type
	 */
	public void setUpShipString(){
		if(shipString == null)
			shipString = new ShipStringBuilder(clientInterface.get_xUpperBound() - clientInterface.get_xLowerBound(),
					clientInterface.get_yUpperBound() - clientInterface.get_yLowerBound(), ASCIIcharsPerShipComponentWidth,
					clientInterface.get_xLowerBound(), clientInterface.get_yLowerBound(), consoleType);
	}

	/**
	 * Method used to print and show a ship, so that the client can see it
	 * @param ship ship to be printed
	 */
	private void printShip(ShipComponent[][] ship){
		/*
        for (ShipComponent[] shipComponents : ship) {
            printComponents((ArrayList<ShipComponent>) Arrays.asList(shipComponents));
        }*/
		setUpShipString();
		if(ship == null)
			return;
		try {
			System.out.println(shipString.buildShipString(ship));
		} catch (IOException e) {
			System.out.println("There was an issue while trying to build the ship string");
		}
	}

	/**
	 * Method used to print the ship during the fixing state, in order to show which
	 * components are not well inserted
	 * @param ship current ship to show
	 * @param wrongComponents matrix of boolean, used to signal wrong components
	 */
	private void printShipToBeFixed(ShipComponent[][] ship, boolean[][] wrongComponents){
		/*
        for (ShipComponent[] shipComponents : ship) {
            printComponents((ArrayList<ShipComponent>) Arrays.asList(shipComponents));
        }*/
		setUpShipString();
		try {
			System.out.println(shipString.buildShipToBeFixedString(ship, wrongComponents));
		} catch (IOException e) {
			System.out.println("There was an issue while trying to build the ship string");
		}
	}

	/**
	 * When it's chosen the crew for a cabin, the cabin is colored
	 * @param ship current ship
	 * @param x x cabin coordinate
	 * @param y y cabin coordinate
	 */
	private void printShipCabins(ShipComponent[][] ship, int x, int y){
		/*
        for (ShipComponent[] shipComponents : ship) {
            printComponents((ArrayList<ShipComponent>) Arrays.asList(shipComponents));
        }*/
		setUpShipString();
		try {
			System.out.println(shipString.buildShipWithColoredCabin(ship, x, y));
		} catch (IOException e) {
			System.out.println("There was an issue while trying to build the ship string");
		}
	}

	/**
	 * Method used to ask for the selection of a card pile
	 * @return true if a card pile is selected
	 */
	private boolean chooseCardPile(){
		int pileIndex;
		String input;
		while (true) {
			if (clientInterface.getSmallDecksIndexes() == null) {
				System.out.println("There isn't any available deck right now! Try later!");
				return true;
			}
			System.out.println("The currently available piles are:");
			for(int i = 0; i < clientInterface.getSmallDecksIndexes().size(); i++){
				int index = clientInterface.getSmallDecksIndexes().get(i);
				System.out.print(index+1 + "  ");
			}
			System.out.println("\nInsert the number of the pile you'd like to peek at, or 'c' to cancel the action.");
			synchronized (System.in) {input = scanner.nextLine();}
			if(input.equals("c")){
				System.out.println("You canceled the action.");
				return false;
			}
			try {
				pileIndex = Integer.parseInt(input);
				//if the input is a number
					if (pileIndex < 1 || pileIndex > 4) {
						System.out.println(TEXT_YELLOW + "Invalid pile index!" + RESET);
					}
					else if(pileIndex == 4){
						System.out.println(TEXT_YELLOW + "The forecast service has decided to keep that card pile undisclosed." + RESET);
					}
					else if(!clientInterface.getSmallDecksIndexes().contains(pileIndex - 1)) {
						System.out.println("That pile isn't available right now.");
					}
					else{
						System.out.println("You selected pile " + pileIndex + ". It's being retrieved.");
						clientInterface.requestSmallDeck(pileIndex);
						return true;
					}
			} catch (NumberFormatException e){
					System.out.println(TEXT_YELLOW + "Please choose a valid input. Enter 'c' to cancel the action." + RESET);
			} catch (IOException e) {
				System.out.println("There was a problem while requesting the card pile. Please try again.");
            }
		}
	}

//debug
/*	private void printShipComponentsForShortcut(ShipComponent[][] ship){
		for (ShipComponent[] components : ship) {
			for (ShipComponent component : components) {
				System.out.println(component.getID() + " ");
			}
			System.out.println();
		}

		for (ShipComponent[] shipComponents : ship) {
			for (ShipComponent shipComponent : shipComponents) {
				System.out.println(Orientation.getCounterClockwise90DegRotationFromOrientation(shipComponent.getOrientation()) + " ");
			}
			System.out.println();
		}
	}*/

	/**
	 * After the player has placed their ship, it's asked if they want to restart the timer
	 * @throws IOException input output exception thrown
	 */
	private void askRestartPendingTimerAfterPlacingShip() throws IOException {
		String input;
		if(clientInterface.isTimerLast()){
			System.out.println(TEXT_YELLOW + "You now have the possibility to restart the LAST timer for the players who aren't finished building their ships yet." + RESET);
			while(!clientInterface.lastTimerExpired() && clientInterface.getState() instanceof ShipConstructionState &&
			clientInterface.isTimerPaused()) {
				System.out.println(TEXT_YELLOW + "Insert 'y' to restart the LAST timer" + RESET);
				input = scanner.nextLine();
				if(input.equals("y")){
					clientInterface.startTimer();
					clientInterface.setCanRestartTimer(false);
					System.out.println("You restarted the LAST timer >:)");
					return;
				}
			}
		}
		else {
			System.out.println(TEXT_YELLOW + "Do you want to restart the timer for the players who aren't finished building their ships yet?" + RESET);
			while(!clientInterface.lastTimerExpired() && clientInterface.getState() instanceof ShipConstructionState &&
			clientInterface.isTimerPaused()) {
				System.out.println("Insert 'y' to restart the timer");
				input = scanner.nextLine();
				if(input.equals("y")){
					clientInterface.startTimer();
					clientInterface.setCanRestartTimer(false);
					System.out.println("You restarted the timer!");
					return;
				}
			}
		}
	}

	/**
	 * Method used during the construction in order to ask for the different interaction
	 * options: 'd' for drawing component, 'b' for picking a booked component, 'r' for
	 * returning a component, 'p' for peeking at card pile, 's' for looking to their own
	 * ship and 'f' for sending the finished ship
	 */
	@Override
	public void drawShipComponentOrSmallDeck() {
		setUpShipString();

		String input;
		int componentIndex;
		while(!clientInterface.lastTimerExpired()){
			System.out.println("Insert:\n" +
					"'d' if you'd like to draw a random component,\n" +
					"'b' if you'd like to pick one of your booked components,\n" +
					"'r' if you wish to pick a specific revealed component,");
			if(clientInterface.firstComponentPlaced())
				System.out.println("'p' if you want to peek at card pile,");
			System.out.println("'s' if you want to look at your ship.\n" +
					"'f' if you are finished building you're ship and want to place it on the board.");

			if(clientInterface.canRestartTimer())
				System.out.println(TEXT_GREEN + "'t' if you want to restart the timer." + RESET);
			synchronized (System.in){input = scanner.nextLine();}
			switch(input){
				case "i":
					System.out.println("Insert the id of the component you want");
					int specificCompId;
					synchronized (System.in) {specificCompId = scanner.nextInt();}
					System.out.println("It's being retrieved.");
					try {
						clientInterface.drawSpecificComponent(specificCompId);
						return;
					} catch (IOException e) {
						System.out.println("Something went wrong while retrieving the component! Try again.");
						break;
					}
				case "a":
					clientInterface.resetPremadeShip();
					clientInterface.setPremadeShipWrong(false);
					clientInterface.premakeShip();
					return;

				case "w":
					clientInterface.resetPremadeShip();
					clientInterface.setPremadeShipWrong(true);
					clientInterface.premakeShip();
					return;

				case "d":
					if(clientInterface.lastTimerExpired())
						return;
					System.out.println("You chose to pick a component at random. It's being retrieved.");
					try {
						clientInterface.drawComponent();
						return;
					} catch (IOException e) {
						System.out.println("Something went wrong while retrieving the component! Try again.");
						break;
					}
					//break;

				case "b":
					while(!clientInterface.lastTimerExpired()) {
						if(clientInterface.getBookedComponents().isEmpty()){
							System.out.println("You don't have any booked components yet.");
							break;
						}
						//else
						printComponents(clientInterface.getBookedComponents());
						System.out.println("Insert the index of the booked component you would like to place." +
								"Insert 'c' to cancel the action");
						synchronized (System.in){input = scanner.nextLine();}
						if (input.equals("c")) {
							System.out.println("You canceled the action.");
							break;
						}
						//else
						try{
							componentIndex = Integer.parseInt(input);
							try{
								selectedComponent = clientInterface.getBookedComponent(componentIndex);
								printShip(clientInterface.getShip());
								printComponent(selectedComponent);
								if(placeComponent(selectedComponent)){
									selectedComponent = null;
									clientInterface.removeBookedComponent(componentIndex);
									break;
								}
							} catch (NoBookedComponentException e) {
								System.out.println("There wasn't a booked component in the specified position.");
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Please choose a valid booked component");
							}

						} catch (NumberFormatException e) {
							System.out.println("Please insert a valid index.");
						}
					}
					break;

				case "r":
					while(!clientInterface.lastTimerExpired()){
						if(clientInterface.getSmallDecksIndexes() == null || clientInterface.getRevealedComponents().isEmpty()){
							System.out.println(TEXT_YELLOW + "There are no face-up components at the moment" + RESET);
							break;
						}
						else
							printComponents(clientInterface.getRevealedComponents());
						System.out.println("Insert the index of the revealed component you'd like to pick.\n" +
								"Insert 'c' to cancel the action.");
						synchronized (System.in){input=scanner.nextLine();}
						if(input.equals("c")){
							System.out.println("You canceled the operation.");
							break;
						}
						//else
						try {
							componentIndex = Integer.parseInt(input);
							if(clientInterface.isComponentAvailable(componentIndex)){
								clientInterface.pickRevealedComponent(componentIndex);
								return;
							}
							else {
								System.out.println("Please insert a valid index.");
							}
						} catch (NumberFormatException e) {
							System.out.println("Please insert a valid component ID.");
						} catch (IOException e) {
							System.out.println("Something went wrong while retrieving the component! Try again.");
						}
					}
					break;

				case "p":
					if(clientInterface.lastTimerExpired())
						return;
					if(clientInterface.firstComponentPlaced()){
						System.out.println("You chose to peek at a card pile.");
						if(chooseCardPile())
							return;
						//else, the player canceled the action, so continue the loop
					}
					else{
						System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
					}
					break;

				case "s":
					printShip(clientInterface.getShip());
					if(clientInterface.lastTimerExpired())
						return;
					break;

				case "f":
					if(clientInterface.lastTimerExpired())
						return;
					System.out.println("Your ship is being placed on the board.");
					try {
						if (!clientInterface.isShipPlaced()) {
							clientInterface.placeShip(selectedComponent);
						}
						if(clientInterface.isTimerPaused())
							askRestartPendingTimerAfterPlacingShip();
						//else, the player will be asked automatically by the incoming messages

						return;
					}catch(IOException e){
						throw new RuntimeException("There was a problem while sending the finished ship to the server.");
					}

				case "t":
					if(clientInterface.lastTimerExpired())
						return;
					if(clientInterface.canRestartTimer())
					{
						System.out.println("Sending request to start the timer...");
						try {
							clientInterface.startTimer();
							clientInterface.setCanRestartTimer(false);
						} catch (IOException e) {
							System.out.println("Something went wrong while starting the timer! Try again.");
						}
						break;
					}
					//else... vvv
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
					break;
			}
		}
	}

	/**
	 * Method used to ask if they want to activate a double motor loosing batteries
	 */
	@Override
	public void askDoubleMotor(){
		int doubleMotors = clientInterface.getShipDoubleMotors();
		int batteries = clientInterface.getShipBatteries();
		int answer;
		System.out.println("You have the chance to activate double motors by spending batteries. (+2 motor power per double motor)");
		System.out.println("Your current motor power is: " + clientInterface.getShipMotorPower());
		try{
			if(doubleMotors == 0) {
				System.out.println("Unfortunately you don't have any double motors to activate.");
				clientInterface.sendDynamicMotorPower(0);
				return;
			}
			if(batteries == 0){
				System.out.println("Unfortunately you don't have any batteries to use.");
				clientInterface.sendDynamicMotorPower(0);
				return;
			}
		} catch(IOException e){
			System.out.println("There was a problem while communicating the motor power.");
			e.printStackTrace();
		}
		//else...
		while(true){
			System.out.println("You have " + doubleMotors + " double motors and " + batteries + " batteries available.\n" +
					"How many would you like to activate?");

			try{
				synchronized (System.in){answer = Integer.parseInt(scanner.nextLine());}
				if(answer > doubleMotors)
					System.out.println("You don't have that many motors!");
				else if (answer > batteries)
					System.out.println("You don't have enough batteries to activate " + answer + " motors.");
				else {
					clientInterface.useShipBatteries(answer);
					clientInterface.sendUpdatedShip();
					clientInterface.sendDynamicMotorPower(answer * 2);
					if(answer != 0)
						System.out.println("You activated " + answer + " double motors! Hang tight!");
					else
						System.out.println("You didn't activate any double motors.");
					return;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please insert a valid answer.");
			} catch (IOException e) {
				System.out.println("There was a problem while communicating the motor power.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method used to interact with the client, asking for double cannon activation,
	 * loosing some batteries
	 * @param ship current ship
	 */
	@Override
	public void askDoubleCannon(ShipComponent[][] ship){
		int doubleCannons = clientInterface.getShipDoubleCannons();
		int batteries = clientInterface.getShipBatteries();
		int cannonX, cannonY;
		double totalFirePower;
		double staticFirePower = clientInterface.getShipFirePower();
		double dynamicFirePower = 0;
		String aOrAnother;
		boolean[][] activatedDoubleCannons = new boolean[ship.length][ship[0].length];
		int batteriesToUse = 0;

		System.out.println("You have the chance to activate double cannons by spending batteries to increase your fire power.");
		System.out.println("Your current fire power is: " + staticFirePower +
				", and you have " + clientInterface.getShipBatteries() + " batteries left.");
		totalFirePower = staticFirePower;

		if(batteries <= 0){
			System.out.println("Unfortunately you don't have any batteries to activate double cannons.");
			try {
				clientInterface.sendDynamicFirePower(0);
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if(doubleCannons <= 0){
			System.out.println("Unfortunately you don't have any double cannons to activate.");
			try {
				clientInterface.sendDynamicFirePower(0);
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		aOrAnother = "a";
		while(true){
			if(batteriesToUse < clientInterface.getShipBatteries()){
				if(askBooleanQuestion("Do you want to activate " + aOrAnother + " double cannon? (y/n)")){
					printShip(clientInterface.getShip());
					System.out.println("Insert the x coordinate of the double cannon.");
					cannonX = getValidCoordinateInput(clientInterface.get_xLowerBound(), clientInterface.get_xUpperBound(), false);

					System.out.println("Insert the y coordinate of the double cannon. Insert 'c' to insert again.");
					cannonY = getValidCoordinateInput(clientInterface.get_yLowerBound(), clientInterface.get_yUpperBound(), true);

					if(cannonY != -1){
						cannonX -= clientInterface.get_xLowerBound();
						cannonY -= clientInterface.get_yLowerBound();

						switch(ship[cannonY][cannonX]){
							case Cannon cannon:
								if(!cannon.isDouble()){
									System.out.println(TEXT_YELLOW + "The selected cannon is not double." + RESET);
								}
								else if(activatedDoubleCannons[cannonY][cannonX]){
									System.out.println(TEXT_YELLOW + "You already activated the selected cannon." + RESET);
								}
								else {
									dynamicFirePower += (cannon.getOrientation() == Orientation.NORTH ? 2 : 1);
									totalFirePower = staticFirePower + dynamicFirePower;
									System.out.println("Your total fire power has increased to a whopping " + totalFirePower + "!");
									aOrAnother = "another";
									activatedDoubleCannons[cannonY][cannonX] = true;
									batteriesToUse++;
								}
								break;
							default:
								System.out.println(TEXT_YELLOW + "The selected component is not a cannon." + RESET);
						}
					}
				}
				else{
					try {
						clientInterface.useShipBatteries(batteriesToUse);
						clientInterface.sendUpdatedShip();
						clientInterface.sendDynamicFirePower(dynamicFirePower);
						if(aOrAnother.equals("a")){
							System.out.println("You didn't activate any double cannons.");
						}
						else
							System.out.println("You decided that those cannons were enough to get the job done.");
						return;
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
			else {
				try {
					clientInterface.useShipBatteries(batteriesToUse);
					clientInterface.sendUpdatedShip();
					clientInterface.sendDynamicFirePower(dynamicFirePower);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				System.out.println("You don't have any more batteries to use, the ones you activated will have to be enough.");
			}
			/*
			System.out.println("You have " + doubleCannons + " double cannons and " + batteries + " batteries available.\n" +
					"How many would you like to activate?");

			try{
				synchronized (System.in){answer = Integer.parseInt(scanner.nextLine());}
				if(answer > doubleCannons)
					System.out.println("You don't have that many cannons!");
				else if (answer > batteries)
					System.out.println("You don't have enough batteries to activate " + answer + " cannons.");
				else {
					clientInterface.useShipBatteries(answer);
					clientInterface.sendDynamicFirePower(answer);
					return;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please insert a valid answer.");
			} catch (IOException e) {
				System.out.println("There was a problem while communicating the fire power.");
			}*/
		}
	}

	/**
	 * Method used to interact with the client, asking to remove some crew members. It's
	 * used both for humans and aliens
	 * @param x x cabin coordinate
	 * @param y y cabin coordinate
	 * @param amountLeft amount of people to remove from the crew
	 * @return updated amount of people to remove
	 */
	private int askRemoveCrewFromCabin(int x, int y, int amountLeft){
		String input;
		boolean loopDone;
		int humanNumber;
		Cabin cabin;
		ShipComponent comp = clientInterface.getShipComponent(x, y);
		if(comp instanceof Cabin){
			cabin = (Cabin)comp;
			int numHumans = cabin.getHumans();
			boolean purpleAlien = cabin.getPurpleAlienEquip();
			boolean brownAlien = cabin.getBrownAlienEquip();
			if(numHumans == 1)
				System.out.println("There is 1 human in the cabin");
			else if(numHumans == 2)
				System.out.println("There are 2 humans in the cabin");

			if(numHumans == 1 || numHumans == 2){
				if(amountLeft == 1){
					loopDone = false;
					while(!loopDone){
						System.out.println("Do you want to give up one human? (y/n)");
						synchronized (System.in){input = scanner.nextLine();}
						switch(input){
							case "y":
								System.out.println("You gave up one human.");
								clientInterface.removeCrewFromCabin(x, y, 1, false, false);
								amountLeft--;
								loopDone = true;
								break;
							case "n":
								System.out.println("You didn't give up a human.");
								loopDone = true;
								break;
							default:
								System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
								break;
						}
					}
				}
				else {
					do{
						System.out.println("How many do you want to give up?");
						synchronized (System.in){input = scanner.nextLine();}
						try{
							humanNumber = Integer.parseInt(input);
							if(humanNumber > numHumans)
								System.out.println("You don't have that many humans in the cabin.");
							else{
								clientInterface.removeCrewFromCabin(x, y, humanNumber, false, false);
								amountLeft -= humanNumber;
							}

						} catch(NumberFormatException e) {
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							humanNumber = numHumans + 1;
						}

					} while(humanNumber > numHumans);
				}
			}
			else if(purpleAlien && amountLeft > 0){
				loopDone = false;
				while(!loopDone){
					System.out.println("Do you want to give up the" + TEXT_PURPLE +  " purple" + RESET + " alien (-2 fire power)? (y/n)");
					synchronized (System.in){input = scanner.nextLine();}
					switch(input){
						case "y":
							System.out.println("You gave up the purple alien.");
							clientInterface.removeCrewFromCabin(x, y, 0, true, false);
							amountLeft--;
							loopDone = true;
							break;
						case "n":
							System.out.println("You didn't give up the purple alien.");
							loopDone = true;
							break;
						default:
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
					}
				}
			}
			else if(brownAlien && amountLeft > 0){
				loopDone = false;
				while(!loopDone){
					System.out.println("Do you want to give up the" + TEXT_YELLOW +  " brown" + RESET +" alien (-2 motor power)? (y/n)");
					synchronized (System.in){input = scanner.nextLine();}
					switch(input){
						case "y":
							System.out.println("You gave up the brown alien.");
							clientInterface.removeCrewFromCabin(x, y, 0, false, true);
							amountLeft--;
							loopDone = true;
							break;
						case "n":
							System.out.println("You didn't give up the brown alien.");
							loopDone = true;
							break;
						default:
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
					}
				}
			}
			else {
				System.out.println("There are no crew mates to remove from this cabin.");
				letPlayerRead();
			}
		}
		else
			System.out.println("The component at x = " + x + ", y = " + y + " isn't a cabin.");

		return amountLeft;
	}

	/**
	 * Method used to handle the give crew choice, calculating the x and the y coordinate
	 * of the cabin and invoking the previous method
	 * @see #askRemoveCrewFromCabin(int, int, int)
	 * @param amount amount of people to remove
	 */
	@Override
	public void askGiveUpCrew(int amount){
		int x,y;

		if(amount > clientInterface.getShipCrew())
		{
			System.out.println("You had to give up more crewmates than you had, so you lost all of them.");
			clientInterface.removeAllCrewmates();
			try {
				clientInterface.sendUpdatedShip();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		//else
		printShip(clientInterface.getShip());
		System.out.println("You have to give up " + amount + "  of your " + clientInterface.getShipCrew() + " crew mates. From which cabin do you want to start removing them?");
		while(amount > 0){
			System.out.println("Insert the x coordinate of the cabin.");
			x = getValidCoordinateInput(clientInterface.get_xLowerBound(), clientInterface.get_xUpperBound(), false);

			System.out.println("Insert the y coordinate of the cabin. Insert 'c' to insert again.");
			y = getValidCoordinateInput(clientInterface.get_yLowerBound(), clientInterface.get_yUpperBound(), true);

			if(y != -1){
				amount = askRemoveCrewFromCabin(x, y, amount);
			}
			if(amount > 0){
				printShip(clientInterface.getShip());
				System.out.println("You still have to give up " + amount + " crew mates. From which cabin do you want to remove them?");
			}
		}

		System.out.println("You gave up all the necessary crew mates.");
		try {
			clientInterface.sendUpdatedShip();
		} catch (IOException e) {
			System.out.println("Something went wrong while sending the updated ship.");
		}
	}

	/**
	 * Method used to check if in a specific place there's a cargo hold
	 * @param ship current ship
	 * @param x x component coordinate
	 * @param y y component coordinate
	 * @return true if in the specific place there's a cargo hold
	 */
	private boolean isComponentCargoHold(ShipComponent[][] ship, int x, int y){
		switch (ship[y- clientInterface.get_yLowerBound()][x- clientInterface.get_xLowerBound()]){
			case CargoHold _:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Invoker of the ask question
	 * @see #askBooleanQuestion(String)
	 * @param message current yes or no question
	 * @return true if the answer is yes
	 */
	private boolean askForCertainty(String message){
		return askBooleanQuestion(message);
	}

	/**
	 * It receives a certain yes or no question in order to print it and ask for
	 * the answer
	 * @param message current yes or no question
	 * @return true if the answer is yes
	 */
	private boolean askBooleanQuestion(String message){
		String answer;
		while(true){
			System.out.println(message);

			synchronized (System.in){answer = scanner.nextLine();}
			switch(answer){
				case "y":
					return true;
				case "n":
					return false;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
					break;
			}
		}
	}

	/**
	 * It's used to get a specific stock object with a specific kind of stock: it's used to
	 * discard some stocks
	 * @param containerIndex container index in order to get a stock object with a specific
	 *                       kind of stock
	 * @return object stock
	 */
	private Stocks getContainerFromIndex(int containerIndex){
        return switch (containerIndex) {
            case 0 -> new Stocks(1, 0, 0, 0);
            case 1 -> new Stocks(0, 1, 0, 0);
            case 2 -> new Stocks(0, 0, 1, 0);
            case 3 -> new Stocks(0, 0, 0, 1);
            default -> throw new RuntimeException("Unexpected container index.");
        };
	}

	/**
	 * Method used to interact with the client, asking if they want to discard or place a
	 * container: 'p' for placing, 'd' for discarding and 'c' to cancel the action
	 * @param ship current player ship, used for placing the containers
	 * @param containerIndex current container index
	 * @return true if the client has selected 'p' or 'd'
	 */
	private boolean placeOrDiscardContainer(ShipComponent[][] ship, int containerIndex){
		int x, y;
		String input;
		while(true){
			System.out.println("Insert 'd' if you want to discard the container, 'p' if you want to place it in another cargo hold, 'c' to cancel.");
			synchronized (System.in){input = scanner.nextLine();}
			switch(input){
				case "d":
					if(askForCertainty("Are you sure you want to discard the " +
							switch (containerIndex) {
								case 0 -> "blue";
								case 1 -> "green";
								case 2 -> "yellow";
								case 3 -> "red";
								default -> throw new RuntimeException("Unexpected container index.");
							} + " container? You will not be able to get it back. (y/n)")){
						System.out.println("You discarded the container.");
						try {
							clientInterface.sendStocksToDiscard(getContainerFromIndex(containerIndex));
						} catch (IOException e) {
							System.out.println("Something went wrong while sending the container to discard.");
						}
						return true;
					}
					else
						System.out.println("You didn't discard the container.");
					break;
				case "p":
					printShip(ship);
					System.out.println("Where would you like to place the container?");
					System.out.println("Insert the x coordinate of the cargo hold ");
					x = getValidCoordinateInput(clientInterface.get_xLowerBound(), clientInterface.get_xUpperBound(), false);
					System.out.println("Insert the y coordinate of the cargo hold ");
					y = getValidCoordinateInput(clientInterface.get_yLowerBound(), clientInterface.get_yUpperBound(), true);
					if(y != -1){
						if(isComponentCargoHold(ship, x, y)){
							CargoHold cargoHold = (CargoHold) ship[y- clientInterface.get_yLowerBound()][x-clientInterface.get_xLowerBound()];
							if(cargoHold.getStocks().numberOfStocks() >= cargoHold.getMaxCapacity()){
								System.out.println(TEXT_YELLOW + "This cargo hold is at max capacity, you can't place containers here." + RESET);
							}
							else {
								switch (containerIndex){
									case 0:
										System.out.println("You placed the container.");
										cargoHold.addStocks(1,0,0,0);
										return true;
									case 1:
										System.out.println("You placed the container.");
										cargoHold.addStocks(0,1,0,0);
										return true;
									case 2:
										System.out.println("You placed the container.");
										cargoHold.addStocks(0,0,1,0);
										return true;
									case 3:
										if(!cargoHold.canContainSpecial())
											System.out.println("This cargo hold is not suitable for a red container.");
										else{
											System.out.println("You placed the container.");
											cargoHold.addStocks(0,0,0,1);
											return true;
										}
								}
							}
						}
						else
							System.out.println("The component at x = " + x + ", y = " + y + " isn't a cargo hold.");
					}
					break;
				case "c":
					return false;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}
		}
	}

	/**
	 * Method used to interact with the client in order to move the stocks from a cargo
	 * hold, if the cargo is not empty: '0' for blue stocks, '1' for green stocks, '2' yellow
	 * stocks, '3' for red stocks and 'c' for cancel the action
	 * @param ship player ship
	 * @param x x cargo hold coordinate
	 * @param y y cargo hold coordinate
	 */
	private void moveStocksFromCargoHold(ShipComponent[][] ship, int x, int y){
		String input;
		x -= clientInterface.get_xLowerBound();
		y -= clientInterface.get_yLowerBound();
		CargoHold cargoHold = (CargoHold) ship[y][x];
		int containerIndex;

		while(true){
			if(cargoHold.getStocks().numberOfStocks() == 0){
				System.out.println("This cargo hold is empty, you can't take any containers from here.");
				return;
			}
			//else
			System.out.println("The selected cargo hold contains " + cargoHold.getStocks().toString());
			System.out.println("What type of container do you want to move?");
			if(cargoHold.getStocks().getBlueStocks() > 0) // balls here
				System.out.println("Insert " + BACKGROUND_BLUE + TEXT_BLACK + "'0'" + RESET + " to select a blue container.");
			if(cargoHold.getStocks().getGreenStocks() > 0)
				System.out.println("Insert " + BACKGROUND_GREEN + TEXT_BLACK + "'1'" + RESET + " to select a green container.");
			if(cargoHold.getStocks().getYellowStocks() > 0)
				System.out.println("Insert " + BACKGROUND_YELLOW + TEXT_BLACK + "'2'" + RESET + " to select a yellow container.");
			if(cargoHold.getStocks().getSpecialRedStocks() > 0)
				System.out.println("Insert " + BACKGROUND_RED + TEXT_WHITE + "'3'" + RESET + " to select a red container.");
			System.out.println("Insert 'c' once you are done moving containers from this cargo hold.");

			synchronized (System.in){input = scanner.nextLine();}
			if(input.equals("c")){
				System.out.println("You completed this action.");
				return;
			}
			//else
			try{
				containerIndex = Integer.parseInt(input);
				switch(containerIndex){
					case 0:
						if(cargoHold.getStocks().getBlueStocks() > 0){
							System.out.println("You selected a blue container.");
							if(placeOrDiscardContainer(ship, containerIndex))
								cargoHold.getStocks().remove(0,0,0,1);
							break;
						}
						else{
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
						}
					case 1:
						if(cargoHold.getStocks().getGreenStocks() > 0){
							System.out.println("You selected a green container.");
							if(placeOrDiscardContainer(ship, containerIndex))
								cargoHold.getStocks().remove(0,0,1,0);
							break;
						}
						else{
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
						}
					case 2:
						if(cargoHold.getStocks().getYellowStocks() > 0){
							System.out.println("You selected a yellow container.");
							if(placeOrDiscardContainer(ship, containerIndex))
								cargoHold.getStocks().remove(0,1,0,0);
							break;
						}
						else{
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
						}
					case 3:
						if(cargoHold.getStocks().getSpecialRedStocks() > 0){
							System.out.println("You selected a red container.");
							if(placeOrDiscardContainer(ship, containerIndex))
								cargoHold.getStocks().remove(1,0,0,0);
							break;
						}
						else{
							System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
							break;
						}
					default:
						System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);

				}
			} catch (NumberFormatException e){
				System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}
		}
	}

	/**
	 * Method used to select a specific cargo hold in order to take some containers: it calls
	 * the previous method in order to move the stocks
	 * @see #moveStocksFromCargoHold(ShipComponent[][], int, int)
	 * @param ship player ship
	 */
	private void moveStocks(ShipComponent[][] ship){
		printShip(ship);
		int x, y;
		while(true){
			System.out.println("From which cargo hold do you want to take a container? Insert 'c' once you are done moving your containers");
			System.out.println("Insert the x coordinate of the cargo hold.");
			x = getValidCoordinateInput(clientInterface.get_xLowerBound(), clientInterface.get_xUpperBound(), true);
			if(x == -1){
				System.out.println("You completed this action.");
				return;
			}
			System.out.println("Insert the y coordinate of the cargo hold.");
			y = getValidCoordinateInput(clientInterface.get_yLowerBound(), clientInterface.get_yUpperBound(), true);
			if(y != -1){
				if(isComponentCargoHold(ship, x, y))
						moveStocksFromCargoHold(ship, x, y);
				else
					System.out.println("The component at x = " + x + ", y = " + y + " isn't a cargo hold.");

			}
			else{
				System.out.println("You completed this action.");
				return;
			}
		}
	}

	/**
	 * Method used to check if a specific side has the shield protection
	 * @param side current side
	 * @return true if the side has got the shield protection
	 */
	private boolean isSideShield(Side side){
		return (side == Side.ShieldProtection || side == Side.ShieldAndSingleConnector ||
				side == Side.ShieldAndDoubleConnector || side == Side.ShieldAndUniversalConnector);
	}

	/**
	 * Method used to check the player ship and find all the shield inside according to the
	 * projectile direction: the ship is scrolled in order to find any shield
	 * @param ship current player ship
	 * @param projectile current projectile
	 * @return list of shields
	 */
	private ArrayList<Shield> getShieldsProtectingAgainst(ShipComponent[][] ship, Projectile projectile){
		ArrayList<Shield> shields = new ArrayList<>();
        for (ShipComponent[] shipComponents : ship) {
            for (ShipComponent shipComponent : shipComponents) {
                switch (shipComponent) {
                    case Shield shield:
                        switch (projectile.getDirection()) {
                            case Orientation.NORTH:
                                if (isSideShield(shield.getSouthSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.EAST:
                                if (isSideShield(shield.getWestSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.SOUTH:
                                if (isSideShield(shield.getNorthSide()))
                                    shields.add(shield);
                                break;
                            case Orientation.WEST:
                                if (isSideShield(shield.getEastSide()))
                                    shields.add(shield);
                                break;
                        }
                    default:
                        break;
                }
            }
        }
		return shields;
	}

	/**
	 * Method used to interact with the client, asking if they want to move some placed stocks 'm',
	 * to place the new containers 'p' or to return them 'c'. In 'p' case it's shown the number of
	 * each stock per container and you're asked to decide the specific colored stock
	 * @param stocks current stocks
	 * @param ship current player ship
	 */
	@Override
	public void askAddAndRearrangeStocks(Stocks stocks, ShipComponent[][] ship){
		String input;
		int containerIndex;
		System.out.println("You received " + stocks.toString() + ".");
		while(true){
			System.out.println("""
					Insert
					'm' if you want to move the containers you have already placed
					'p' if you want to place one of the new containers
					'c' if you want to give up the remaining new containers.""");
			synchronized (System.in){input = scanner.nextLine();}

			switch (input){
				case "m":
					System.out.println("You decided to move around the containers.");
					moveStocks(ship);
					break;
				case "p":
					System.out.println("What type of container do you want to place?");
					if(stocks.getBlueStocks() > 0) // balls here
						System.out.println("Insert " + BACKGROUND_BLUE + TEXT_BLACK + "'0'" + RESET + " to select a blue container.");
					if(stocks.getGreenStocks() > 0)
						System.out.println("Insert " + BACKGROUND_GREEN + TEXT_BLACK + "'1'" + RESET + " to select a green container.");
					if(stocks.getYellowStocks() > 0)
						System.out.println("Insert " + BACKGROUND_YELLOW + TEXT_BLACK + "'2'" + RESET + " to select a yellow container.");
					if(stocks.getSpecialRedStocks() > 0)
						System.out.println("Insert " + BACKGROUND_RED + TEXT_WHITE + "'3'" + RESET + " to select a red container.");
					System.out.println("Insert 'c' once you are done placing the obtained containers.");

					synchronized (System.in){input = scanner.nextLine();}
					if(input.equals("c")){
						System.out.println("You completed this action.");
						break;
					}
					//else
					try{
						containerIndex = Integer.parseInt(input);
						switch(containerIndex){
							case 0:
								if(stocks.getBlueStocks() > 0){
									System.out.println("You selected a blue container.");
									if(placeOrDiscardContainer(ship, containerIndex))
										stocks.remove(0,0,0,1);
									break;
								}
								else{
									System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
									break;
								}
							case 1:
								if(stocks.getGreenStocks() > 0){
									System.out.println("You selected a green container.");
									if(placeOrDiscardContainer(ship, containerIndex))
										stocks.remove(0,0,1,0);
									break;
								}
								else{
									System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
									break;
								}
							case 2:
								if(stocks.getYellowStocks() > 0){
									System.out.println("You selected a yellow container.");
									if(placeOrDiscardContainer(ship, containerIndex))
										stocks.remove(0,1,0,0);
									break;
								}
								else{
									System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
									break;
								}
							case 3:
								if(stocks.getSpecialRedStocks() > 0){
									System.out.println("You selected a red container.");
									if(placeOrDiscardContainer(ship, containerIndex))
										stocks.remove(1,0,0,0);
									break;
								}
								else{
									System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
									break;
								}
							default:
								System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);

						}
					} catch (NumberFormatException e){
						System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
					}
					break;
				case "c":
					if(askForCertainty("Are you sure you want to discard the containers? You will not be able to get them back. (y/n)")){
						System.out.println("You discarded the remaining containers.");
						try {
							clientInterface.sendStocksToDiscard(stocks);
						} catch (IOException e) {
							System.out.println("Something went wrong while sending the container to discard.");
						}
						try{
							clientInterface.sendUpdatedShip();
						} catch (IOException e){
							System.out.println("Something went wrong while sending the updated ship.");
						}
						return;
					}
					else
						System.out.println("You didn't discard the containers.");
					break;
			}

			if(stocks.numberOfStocks() > 0){
				System.out.println("You still have " + stocks + ", what do you want to do?");
			}
			else {
				System.out.println("You took care of all the new containers.");
				try{
					clientInterface.sendUpdatedShip();
				} catch (IOException e){
					System.out.println("Something went wrong while sending the updated ship.");
				}
				return;
			}

		}
	}

	/**
	 * In case of a broken ship, the player can see the different parts and it's asked to
	 * choose a piece as the new player ship
	 * @param shipPieces current list of different ship pieces
	 * @return index of the decided piece
	 */
	private int askShipPiece(ArrayList<ShipComponent[][]> shipPieces){
		if(shipPieces.size() == 1){
			return 0;
		}
		else {
			letPlayerRead();
		}
		int curPieceIndex = 0;
		String input;
		while(true){
			curPieceIndex %= shipPieces.size();
			printShip(shipPieces.get(curPieceIndex));
			System.out.println("Your ship has been broken off into " + shipPieces.size() + " pieces." +
					"Which ship piece do you want to continue your journey with? The rest will be added to your garbage pile.");
			System.out.println("To see the next ship piece, insert 'n'. To see the previous ship Piece, insert 'p'.\n" +
					"To select a ship piece, insert 's'.");
			synchronized (System.in){input = scanner.nextLine();}
			switch(input){
				case "n":
					curPieceIndex++;
					break;
				case "p":
					curPieceIndex--;
					break;
				case "s":
					if(askForCertainty("Are you sure you want to pick this piece to continue your journey? The rest will be discarded. (y/n)"))
						return curPieceIndex;
					break;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}
		}
	}

	/**
	 * A broken component is replaced with an unavailable slot and it's put in the garbage
	 * @param x x component coordinate
	 * @param y y component coordinate
	 * @param ship current player ship
	 * @throws IllegalPositionException thrown when the coordinate of a ship is not respected
	 */
	public void breakComponent (int x, int y, ShipDashboard ship) throws IllegalPositionException {
		ArrayList<ShipComponent[][]> brokenShipPieces = new ArrayList<>();

		if(y<0||y>=ship.getShip().length||x<0||x>=ship.getShip()[y].length)
			throw new IllegalPositionException("The provided coordinates are outside of the index range");

		if(ship.isUnavailableSlot(x,y))
			return;

		ship.getShip()[y][x] = new UnavailableSlot();
		clientInterface.addGarbage(1);

		brokenShipPieces = ship.getBrokenPieces();

		ShipComponent[][] keptPiece;
		if(!brokenShipPieces.isEmpty())
			keptPiece = brokenShipPieces.get(askShipPiece(brokenShipPieces));
		else
			keptPiece = ship.getShip();

		ship.updateShip(keptPiece);

		//add the lost pieces to the garbage heap
		for(ShipComponent[][] shipPiece : brokenShipPieces){
			int curPieceComponents = 0;
			//if the piece considered isn't the one picked by the player
			if(shipPiece != ship.getShip()) {
				for(int cy = 0; cy < shipPiece.length; cy++) {
					for(int cx = 0; cx < shipPiece[y].length; cx++){
						if(!ship.isUnavailableSlot(cx,cy)){
							curPieceComponents++;
						}
					}
				}
				clientInterface.addGarbage(curPieceComponents);
			}
		}

	}

	/**
	 * When a projectile tries to hit you, the player can try to activate a shield in case of
	 * sufficient batteries
	 * @param ship current player ship
	 * @param projectile current projectile
	 * @param trajectory current projectile trajectory
	 * @return true if the batteries are sufficient and the player has decided to activate the shield
	 */
	private boolean askShield(ShipComponent[][] ship,Projectile projectile, int trajectory){
		String input;
		ArrayList<Shield> eligibleShields;
		while(true){
			System.out.println("Your ship is about to be damaged by a " + projectile + "!");
			if(clientInterface.getShipBatteries() == 0){
				System.out.println("Unfortunately you don't have batteries to activate any shields.");
				return false;
			}
			//else
			eligibleShields = getShieldsProtectingAgainst(ship, projectile);
			if(eligibleShields.isEmpty()){
				System.out.println("Unfortunately your ship doesn't have any shields protecting that side.");
				return false;
			}
			//else
			boolean ansIsYes = askBooleanQuestion("Do you want to use one of your " + clientInterface.getShipBatteries() + " batteries to activate a shield to protect your ship? (y/n)");
			if(ansIsYes){
				if(askForCertainty("Are you sure? You will use a battery to do so.")){
					System.out.println("You activated a shield and deflected the " + projectile + ".");
					return true;
				}
			} else{
				if (askForCertainty("Are you sure you want to let the " + projectile + " hit the ship?")){
					return false;
				}
			}
		/*	System.out.println("Do you want to use one of your " + clientInterface.getShipBatteries() + " batteries to activate a shield to protect your ship? (y/n)");
			synchronized (System.in){input = scanner.nextLine();}
			switch(input){
				case "y":
					if(askForCertainty("Are you sure? You will use a battery to do so.")){
						System.out.println("You activated a shield and deflected the " + projectile + ".");
						return true;
					}
					break;

				case "n":
					if (askForCertainty("Are you sure you want to let the " + projectile + " hit the ship?")){
						return false;
					}
					break;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}*/
		}

	}

	/**
	 * In case of a big asteroid the player has the opportunity to activate a double cannon
	 * in case of sufficient batteries: the double cannon can destroy the asteroid
	 * @param projectile current projectile
	 * @param trajectory projectile trajectory
	 * @param ship current player ship
	 * @return true if the player has activated the double cannon
	 */
	public boolean askToActivateDoubleCannonWhenAttacked(Projectile projectile, int trajectory, ShipDashboard ship){
		String input;
		boolean ansIsYes = askBooleanQuestion("The " + projectile + " is directed right toward your ship, but it's not too late.\n" +
				"You have a double cannon ready to blow the asteroid to smithereens, but it will have to use one of your " +
				clientInterface.getShipBatteries() + " batteries." +
				"Do you want to take the opportunity? (y/n)");

		while(true){
			if(ansIsYes){
				if(askForCertainty("Are you sure? You will use a battery to do so.")){
					System.out.println("You activated the double cannon and destroyed the " + projectile + ".");
					return true;
				}
			}
			else {
				if(askForCertainty("Are you sure? Your ship will be damaged if you don't.")){
					System.out.println("You decided to do nothing and watch the big asteroid crash into your ship.");
					return false;
				}
			}
			ansIsYes = askBooleanQuestion("Do you want to activate the double cannon and destroy the big meteor? (y/n)");
		/*	synchronized (System.in){input = scanner.nextLine();}
			switch(input){
				case "y":
					if(askForCertainty("Are you sure? You will use a battery to do so.")){
						System.out.println("You activated the double cannon and destroyed the " + projectile + ".");
						return true;
					}
					break;
				case "n":
					if(askForCertainty("Are you sure? Your ship will be damaged if you don't.")){
						System.out.println("You decided to do nothing and watch the big asteroid crash into your ship.");
						return false;
					}
					break;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}

			System.out.println("Do you want to activate the double cannon and destroy the big meteor? (y/n)");*/
		}
	}

	/**
	 * In case of an abandoned ship card or an abandoned station the player can decide to lose
	 * some members of the crew gaining credits or to lose some days for stocks
	 * @param curCard current card
	 * @throws IOException input output exception thrown
	 */
	public void askDocking(Card curCard) throws IOException {
		boolean ansIsYes;
		switch (curCard) {
			case AbandonedShip abandonedShipCard:
				System.out.println("While admiring the emptiness of space from your cabin's glass walls, you spot what appears to be an abandoned spaceship drifting slowly.\n" +
						abandonedShipCard.crewLoss + " of the " + clientInterface.getShipCrew() + " members of your crew offer to pay you " + abandonedShipCard.creditsGained + " credits " +
						"to let them go explore the cosmos with this newfound ship.");
				if (clientInterface.getShipCrew() < abandonedShipCard.crewLoss) {
					System.out.println("Unfortunately you didn't have a big enough crew to raid the station");
					clientInterface.sendCardActivationRequest(false);
					return;
				}
				while (true) {
					ansIsYes = askBooleanQuestion("Will you accept their offer, knowing that it will make you lose " + abandonedShipCard.daysLoss + " days? (y/n)");
					if (ansIsYes) {
						if (askForCertainty("Are you sure? (y/n)")) {
							System.out.println("You decided to let some of your crew members follow their dreams. But who?");
							clientInterface.sendCardActivationRequest(true);
							return;
						}
					} else {
						System.out.println("You decided that your crew members are too important to let them go.");
						clientInterface.sendCardActivationRequest(false);
						return;
					}
				}

			case AbandonedStation abandonedStationCard:
				System.out.println("While emptying the waste chute, your eye catches a weirdly shaped moon in the distance. Once you get closer, you realize that it is actually a space station.\n" +
						"Since it seems abandoned, you consider docking it to look for any residual goods left behind.\n" +
						"Doing so would make you lose " + abandonedStationCard.daysLoss + " days, but you'd gain " + abandonedStationCard.stocks + ".");
				if (clientInterface.getShipCrew() < abandonedStationCard.requiredCrew) {
					System.out.println("Unfortunately you didn't have a big enough crew to raid the station");
					clientInterface.sendCardActivationRequest(false);
					return;
				}
				while (true) {
					System.out.println("Will you dock the station, knowing that you would lose " + abandonedStationCard.daysLoss +
							" days, but gain " + abandonedStationCard.stocks + "? (y/n)\n" +
							"Insert 'v' to view your current stocks");
					String input = scanner.nextLine();
					switch (input) {
						case "v":
							viewContainers(clientInterface.getShip());
							break;
						case "y":
							if (askForCertainty("Are you sure? (y/n)")) {
								System.out.println("You decided to lose " + abandonedStationCard.daysLoss + " days by docking the station");
								clientInterface.sendCardActivationRequest(true);
								return;
							}
							break;
						case "n":
							System.out.println("You concurred that the goods held by the station weren't worth losing days over.");
							clientInterface.sendCardActivationRequest(false);
							return;
						default:
							System.out.println("Please insert a valid input.");
					}
				}

			default:
				throw new RuntimeException("The current card doesn't include docking: " + curCard);
		}
	}

	/**
	 * In case of a small meteor it's checked if the meteor missed the ship: if not
	 * it's checked if the player ship is well-built. When a ship has an exposed component,
	 * it's checked if there's a shield to activate
	 * @see #askShield(ShipComponent[][], Projectile, int)
	 * @param smallMeteor current small meteor
	 * @param trajectory meteor trajectory
	 * @param ship current player ship
	 */
	private void handleSmallMeteor(Projectile smallMeteor, int trajectory, ShipDashboard ship) {
		// {x,y}
		int[] hitCoordinates = new int[2];
		Orientation meteorDirection = smallMeteor.getDirection();
		ship.getHitComponent(smallMeteor, trajectory, hitCoordinates);
		//if no component was hit
		if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
			System.out.println("The meteor missed the ship, " + TEXT_YELLOW + "phew!" + RESET);
			return;
		}
		//else
		ShipComponent hitComponent = ship.getShip()[hitCoordinates[1]][hitCoordinates[0]];
		boolean shiedActivated;

//if small meteor hits component with exposed connectors
		if((meteorDirection == Orientation.SOUTH && hitComponent.getNorthSide() != Side.BlankSide) ||
				(meteorDirection == Orientation.WEST && hitComponent.getEastSide() != Side.BlankSide) ||
				(meteorDirection == Orientation.NORTH && hitComponent.getSouthSide() != Side.BlankSide) ||
				(meteorDirection == Orientation.EAST && hitComponent.getWestSide() != Side.BlankSide)) {
			shiedActivated = askShield(ship.getShip(), smallMeteor, trajectory);

			//if the player decided to not activate the shield
			if(!shiedActivated) {
				System.out.println("The small meteor hit and damaged your ship.");
				breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
            }
			else {
				clientInterface.useShipBatteries(1);
            }
		}
		else{
			System.out.println("The small meteor bounced on a smooth side and left nothing but a minuscule scratch");
        }
	}

	/**
	 * Method used to handle a big meteor, it sees if the meteor reaches a component, in this case
	 * it's checked the presence of cannons or double cannons in the line
	 * @param bigMeteor current big meteor
	 * @param trajectory meteor trajectory
	 * @param ship current player ship
	 */
	private void handleBigMeteor(Projectile bigMeteor, int trajectory, ShipDashboard ship) {
		// {x,y}
		int[] hitCoordinates = new int[2];
		hitCoordinates = ship.getHitComponent(bigMeteor, trajectory, hitCoordinates);
		//if no component was hit
		if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
			System.out.println("The big meteor missed the ship, " + TEXT_YELLOW + "phew!" + RESET);
			return;
		}
		//else


		ArrayList<Cannon> eligibleCannons = ship.getEligibleCannons(bigMeteor, trajectory);
		for(Cannon cannon : eligibleCannons) {
			if(!cannon.isDouble()) {
				System.out.println("Your ship has a regular cannon pointing at the big meteor, so it was able to destroy it before it caused any damage.");
				return;
			}
		}

		//if this line is reached, then the list is either empty or only contains double cannons
		if(eligibleCannons.isEmpty())
		{
			System.out.println("Your ship doesn't have any cannons able to shoot the big meteor. " + TEXT_RED + "Brace for impact." + RESET);
			breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
			return;
		}
		//else. The list must contain only double cannons
		if(clientInterface.getShipBatteries() == 0){
			System.out.println("You have a double cannon pointing at the big meteor, but unfortunately you don't have any batteries to activate it with.\n" +
					TEXT_RED + "Brace for impact." + RESET);
			breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
			return;
		}
		//else
		if(askToActivateDoubleCannonWhenAttacked(bigMeteor, trajectory, ship)){
			clientInterface.useShipBatteries(1);
		}
		else {
			breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
		}
    }

	/**
	 * Method used to handle a light shot, it's checked if the shot is missed or if there's
	 * a shield which protects in the shot direction
	 * @param lightShot current light shot
	 * @param trajectory shot trajectory
	 * @param ship current player ship
	 */
	private void handleLightShot(Projectile lightShot, int trajectory, ShipDashboard ship) {
		// {x,y}
		int[] hitCoordinates = new int[2];
		hitCoordinates = ship.getHitComponent(lightShot, trajectory, hitCoordinates);
		//if no component was hit
		if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
			System.out.println("The shot missed the ship, " + TEXT_YELLOW + "phew!" + RESET);
			return ;
		}
		//else

		boolean shieldActivated = askShield(ship.getShip(), lightShot, trajectory);
		if(!shieldActivated) {
			breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
        }
		else {
			clientInterface.useShipBatteries(1);
        }
	}

	/**
	 * Method used to handle a heavy shot: it checks if it's missed, in case the shot reaches the
	 * player ship, it's handled the break component
	 * @see #breakComponent(int, int, ShipDashboard)
	 * @param heavyShot current heavy shot
	 * @param trajectory current shot trajectory
	 * @param ship current player ship
	 */
	private void handleHeavyShot(Projectile heavyShot, int trajectory, ShipDashboard ship) {
		// {x,y}
		int[] hitCoordinates = new int[2];
		hitCoordinates = ship.getHitComponent(heavyShot, trajectory, hitCoordinates);
		//if no component was hit
		if(hitCoordinates[0] == -1 || hitCoordinates[1] == -1) {
			System.out.println("The heavy shot missed the ship, " + TEXT_YELLOW + "phew!" + RESET);
			return;
		}
		//else
		System.out.println("You watch helplessly as the heavy shot hits your precious ship.");
		breakComponent(hitCoordinates[0], hitCoordinates[1], ship);
	}

	/**
	 * Method used to dispatch the different projectiles and to call the right methods to
	 * handle them
	 * @param projectile current projectile
	 * @param trajectory current trajectory projectile
	 * @param ship current player ship
	 */
	@Override
	public void handleProjectile(Projectile projectile, int trajectory, ShipDashboard ship){
		printShip(ship.getShip());
		System.out.println("A " + projectile + " is incoming towards your ship at coordinate " +
				((projectile.getDirection() == Orientation.NORTH || projectile.getDirection() == Orientation.SOUTH) ? "X" : "Y")
				+ " = " + trajectory + "!");

		trajectory = ship.convertTrajectoryToShipSpace(trajectory, projectile);

		switch(projectile.getType()) {
			case SMALL_ASTEROID -> handleSmallMeteor(projectile, trajectory, ship);
			case BIG_ASTEROID -> handleBigMeteor(projectile, trajectory, ship);
			case LIGHT_SHOT -> handleLightShot(projectile, trajectory, ship);
			case HEAVY_SHOT -> handleHeavyShot(projectile, trajectory, ship);
		}
		letPlayerRead();

		try {
			clientInterface.sendUpdatedShip();
		} catch (IOException e) {
			System.out.println("There was a problem while sending the updated ship.");
		}
	}



	/**
	 * Method used to interact with the player and hire the crew, it's used to fill the cabins
	 * with aliens and humans
	 * @param ship current player ship
	 */
	@Override
	public void connectLifeSupportsHireCrewInitializeAttributesAndSendShip(ShipComponent[][] ship){
		clientInterface.ship.connectLifeSupports();

		System.out.println("Now it's time to hire the crew for your ship.");
		boolean shipAlreadyHasPurpleAlien = false;
		boolean shipAlreadyHasBrownAlien = false;
		String input;
		boolean done;
		for(int y = 0; y < ship.length; y++){
			for(int x = 0; x < ship[y].length; x++){
				switch(ship[y][x]){
					case Cabin cabin:
						printShipCabins(ship, x, y);

						if(cabin.getIsCentral()){
							cabin.setHumans(true, true);
							System.out.println("2 humans have been hired to live in the central cabin.");
							letPlayerRead();
						}
						else{
							if((!cabin.hasPurpleLifeSupport() || shipAlreadyHasPurpleAlien) && (!cabin.hasBrownLifeSupport() || shipAlreadyHasBrownAlien)){
								cabin.setHumans(true, true);
								System.out.println("2 humans have been hired to live in the cabin with coordinates " +
										"x = " + (x + clientInterface.get_xLowerBound()) + ", y = " + (y + clientInterface.get_yLowerBound()));
								letPlayerRead();
							}
							else {
								done = false;
								while(!done){
									System.out.println("Who do you want to hire to live in the cabin at " +
											"x = " + (x + clientInterface.get_xLowerBound()) + ", y = " + (y + clientInterface.get_yLowerBound()) + "?");
									System.out.println("Insert 'h' to hire 2 humans");
									if(cabin.hasPurpleLifeSupport() && !shipAlreadyHasPurpleAlien)
										System.out.println("Insert 'p' to hire 1 purple alien (+2 fire power)");
									if(cabin.hasBrownLifeSupport() && !shipAlreadyHasBrownAlien)
										System.out.println("Insert 'b' to hire 1 brown alien (+2 motor power)");

									synchronized (System.in) {
										input = scanner.nextLine();
										switch (input){
											case "h":
												cabin.setHumans(true, true);
												cabin.setPurpleAlien(false);
												cabin.setBrownAlien(false);
												System.out.println("2 humans have been hired to live in the cabin with coordinates " +
														"x = " + (x + clientInterface.get_xLowerBound()) + ", y = " + (y + clientInterface.get_yLowerBound()));
												done = true;
												break;
											case "p":
												if(cabin.hasPurpleLifeSupport() && !shipAlreadyHasPurpleAlien)
												{
													shipAlreadyHasPurpleAlien = true;
													cabin.setHumans(false, false);
													cabin.setPurpleAlien(true);
													cabin.setBrownAlien(false);
													System.out.println("1 purple alien has been hired to live in the cabin with coordinates " +
															"x = " + (x + clientInterface.get_xLowerBound()) + ", y = " + (y + clientInterface.get_yLowerBound()));
													done = true;
												}
												else
													System.out.println("Please insert a valid input.");
												break;
											case "b":
												if(cabin.hasBrownLifeSupport() && !shipAlreadyHasBrownAlien){
													shipAlreadyHasBrownAlien = true;
													cabin.setHumans(false, false);
													cabin.setPurpleAlien(false);
													cabin.setBrownAlien(true);
													System.out.println("1 brown alien has been hired to live in the cabin with coordinates " +
															"x = " + (x + clientInterface.get_xLowerBound()) + ", y = " + (y + clientInterface.get_yLowerBound()));
													done = true;
												}
												else
													System.out.println("Please insert a valid input.");
												break;
											default:
												System.out.println("Please insert a valid input.");
												break;
										}
									}
								}
							}
						}

					default:
						break;
				}
			}
		}
		System.out.println("Every cabin has been successfully filled with crew members. Now the ship can be placed on the board.");


		clientInterface.ship.initializeShipAttributesFromComponents();
		try {
			clientInterface.sendShip();
		} catch (IOException e) {
			throw new RuntimeException("Something went wrong while sending the finished ship to the server.");
		}
		letPlayerRead();
	}

	/**
	 * Method used to print the ship which needs to be fixed, it shows the wrong components
	 * as red components, in order to visualize better the condition. The player can select x
	 * and y coordinates in order to signal the component to erase
	 * @param ship current player ship
	 */
	@Override
	public void fixShip(ShipDashboard ship){
		System.out.println("Time to check if the ship is well built!");
		boolean[][] wrongComponents = ship.checkShip();

		while(ship.shipNeedsFixing(wrongComponents)) {
			int x, y;
			printShipToBeFixed(ship.getShip(), wrongComponents);
			System.out.println("There are components placed in illegal ways, you must remove components until the entire ship is properly built");

			do {
				System.out.println("Insert the X coordinate of the component you want to remove.");

				x = getValidCoordinateInput(ship.get_xLowerBound(), ship.get_xUpperBound(), false);

				System.out.println("Insert the Y coordinate of the slot where you'd like to place the component. ('c' to re-insert the X)");
				y = getValidCoordinateInput(ship.get_yLowerBound(), ship.get_yUpperBound(), true);
				//user cancelled the action
			} while (y == -1);

			if (ship.isUnavailableSlot(x - clientInterface.get_xLowerBound(), y - clientInterface.get_yLowerBound())) {
				System.out.println("There was no component to remove in the slot with the provided coordinates.");
			}
			else {
				ship.removeComponent(x - clientInterface.get_xLowerBound(), y - clientInterface.get_yLowerBound());
				//update
				wrongComponents = ship.checkShip();
			}
		}

		printShipToBeFixed(ship.getShip(), wrongComponents);
		System.out.println("The ship is completely well built!");
	}

	/**
	 * Method used to signal the timer has been restarted in case if it's not the last one.
	 * For the last timer, it's signaled that the timer is the last one
	 * @param isLast true if the timer is the last one
	 */
	@Override
	public void notifyTimerRestarted(boolean isLast){
		if(!isLast)
			System.out.println(TEXT_YELLOW + "The timer has been restarted!" + RESET);
		else
			System.out.println(TEXT_RED + "The LAST timer has started! Time to hurry!" + RESET);
	}

	/**
	 * Method used to signal that the timer has expired
	 */
	@Override
	public void notifyTimerExpired(){System.out.println(TEXT_YELLOW + "The timer has expired!" + RESET);}

	/**
	 * Method used to signal the timer has expired, for a timer which is not the last one
	 * the player can restart it selecting "y". If the timer can not be restarted, the player
	 * can select anything to continue
	 * @param isLast true if the timer is the last one
	 */
	@Override
	public void handleTimerExpired(boolean isLast){
		String input;
		do {
			if(!isLast){
				if(clientInterface.canRestartTimer()){
					System.out.println(TEXT_YELLOW + "The timer has expired! Do you want to restart it?\n" +
							"Insert 'y' if so, anything else otherwise. " + (!clientInterface.isShipPlaced() ?
							"If you choose to not restart it now you'll be able to do so when you choose a component." : "") + RESET);
					synchronized (System.in){input = scanner.nextLine();}
					if (input.equals("y")) {
						System.out.println("Sending request to start the timer...");
						try {
							clientInterface.startTimer();
							clientInterface.setCanRestartTimer(false);
						} catch (IOException e) {
							System.out.println("Something went wrong while starting the timer! Try again.");
						}
					}
				}
			}
			else if(!clientInterface.isShipPlaced()){
				System.out.println(TEXT_YELLOW + "The last timer has expired. Your ship is being placed on the board.\n" + RESET);
				System.out.println("Press anything to continue");
				try {
					clientInterface.setCanRestartTimer(false);
					clientInterface.placeShip(selectedComponent);
				} catch (IOException e) {
					throw new RuntimeException("There was a problem while sending the finished ship to the server.");
				}
			}
			else{
				System.out.println(TEXT_YELLOW + "The last timer has expired. The game can begin!.\n" + RESET);
				System.out.println("Press anything to continue");
			}
		}while(clientInterface.isShipPlaced() && clientInterface.isTimerPaused() && !clientInterface.lastTimerExpired());
	}

	/**
	 * Method used to interact with the client, asking x and y coordinates to place the
	 * current component on the ship
	 * @param lowerBound lower bound used to remain in the range of possible coordinates
	 * @param upperBound upper bound used to remain in the range of possible coordinates
	 * @param canBeCanceled true if the operation can be canceled
	 * @return current coordinate, it's "-1" if the coordinate is not valid
	 */
	private int getValidCoordinateInput(int lowerBound, int upperBound, boolean canBeCanceled){
		String input;
		int coordinate;
		while(!(clientInterface.lastTimerExpired() && clientInterface.getState() instanceof ShipConstructionState)) {
			if(canBeCanceled)
				System.out.println("Insert 'c' to cancel the action.");
			synchronized (System.in) {input = scanner.nextLine();}
			if (input.equals("c") && canBeCanceled) {
				System.out.println("You canceled the operation.");
				return -1;
			}
			//else
			try {
				coordinate = Integer.parseInt(input);
				if(coordinate >= upperBound || coordinate < lowerBound){
					System.out.println("The coordinate was outside of the ship building area. Insert a coordinate between "+ lowerBound + " and " + (upperBound - 1) + ".");
				}
				else
				{
					return coordinate;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please insert a valid coordinate.");
			}
		}
		return -1;
	}

	/**
	 * Method used to interact with the player in order to ask for a possible rotation:
	 * "r" for a clockwise rotation, "l" for a counterclockwise rotation, "d" to confirm the rotation
	 * and "c" to cancel the selection
	 * @param component current ship component
	 * @return true if the player has done, false if the action is canceled
	 */
	private boolean handleComponentRotation(ShipComponent component){
		String input;
		while(!clientInterface.lastTimerExpired()){
			System.out.println("Insert 'r' or 'l' to rotate the component clockwise and counterclockwise.\n" +
					"Once you're done, insert 'd' ('c' to cancel the selection, the rotation will be retained.)");
			synchronized (System.in) {input = scanner.nextLine();}
			switch(input){
				case "c":
					System.out.println("You canceled the action.");

					return false;
				case "r":
					component.clockwiseRotation();
					printComponent(component);
					break;
				case "l":
					component.counterClockwiseRotation();
					printComponent(component);
					break;
				case "d":
					return true;
				default:
					System.out.println("Please insert a valid command.");
					handleComponentRotation(component);
					break;
			}
		}
		return false;
	}

	/**
	 * Method used to ask the player the x and y coordinates of the current component
	 * to place, it calls the method to get the valid coordinates
	 * @see #getValidCoordinateInput(int, int, boolean)
	 * @param component current component
	 * @return true if there's a valid coordinate
	 */
	private boolean placeComponent(ShipComponent component){
		int x, y;
        while(!clientInterface.lastTimerExpired()){
			System.out.println("Where would you like to place the component? (You can cancel At any time by inserting 'c').\n" +
					"Insert the X coordinate of the slot where you'd like to place the component.");
			x = getValidCoordinateInput(clientInterface.get_xLowerBound(), clientInterface.get_xUpperBound(), true);
			//user cancelled the action
			if(x == -1)
				return false;
			//else
			System.out.println("Insert the Y coordinate of the slot where you'd like to place the component. ('c' to cancel)");
			y = getValidCoordinateInput(clientInterface.get_yLowerBound(), clientInterface.get_yUpperBound(), true);
			//user cancelled the action
			if(y == -1)
				return false;

			if(!handleComponentRotation(component))
				return false;
			//else, meaning if the player HASN'T canceled the action

			try {
				clientInterface.addComponent(component, x - clientInterface.get_xLowerBound(), y - clientInterface.get_yLowerBound());
				printShip(clientInterface.getShip());
				System.out.println("Component successfully placed.");
				clientInterface.setFirstComponentPlaced(true);
				return true;
			} catch (SlotTakenException e) {
				System.out.println(TEXT_YELLOW + "The slot selected is already occupied by a component. Please pick a different slot." + RESET);
			} catch (IllegalPositionException e) {
				System.out.println(TEXT_YELLOW + "The slot selected cannot be part of the ship. Please pick a different one." + RESET);
			} catch (FloatingComponentException e) {
				System.out.println(TEXT_YELLOW + "You must place the new component next to an already placed one in order to attach it." + RESET);
			}
		}

		return false;
	}

	/**
	 * Method used to show the small deck which is selected from the client
	 * @param deck current small deck
	 */
	@Override
	public void handleSmallDeck(Deck deck) {
		if (deck == null) {
			System.out.println(TEXT_BLUE + "This pile has been just taken! Try later!" + RESET);
			drawShipComponentOrSmallDeck();
		}
		else {
			System.out.println("You have received the card pile, here's what this game has in store:");
			for(int i = 0; i < deck.getSize(); i++) {
				System.out.println(TEXT_CYAN + deck.getCards().get(i).toString() + RESET);
			}
			System.out.println("Insert anything once you are ready to return the card pile.");
			synchronized (System.in){scanner.nextLine();}
			try{
				clientInterface.returnSmallDeck();
				drawShipComponentOrSmallDeck();
			} catch (IOException e){
				System.out.println("There was a problem while returning the card pile.");
			}
		}
	}

	/**
	 * Method used to interact with the client and handle a current component: the client can
	 * select "p" to pick the component, "b" to book the component and "r" to return it
	 * @param component current component to be handled
	 */
	@Override
	public void handleComponent(ShipComponent component) {
		System.out.println("Component ID: " + component.getID());
		System.out.println("Component ID: " + component.getType());
		System.out.println("Component sides: " + component.getSides());
		printShip(clientInterface.getShip());
		selectedComponent = component;
		String input;
		boolean done = false;
		System.out.println("You received this component: ");
		printComponent(component);
		while(!done){
			System.out.println("Insert: \n" +
					"'p' if you want to place the component (keep in mind you can't remove it once placed)\n" +
					"'b' if you'd like to book it (if you do, you won't be able to return it)\n" +
					"'r' if you wish to return it.");
			synchronized (System.in){input = scanner.nextLine();}
			switch(input) {
				case "p":
					done = placeComponent(component);
					selectedComponent = null;
					break;
				case "b":
					try {
						clientInterface.bookComponent(component);
						System.out.println("The component has been booked!\n");
						selectedComponent = null;
						done = true;
					} catch (BookingSlotsFullException e) {
						System.out.println("You already have 2 booked components. You'll have to place one in order to be able book more.");
						System.out.println("Choose what to do with this component: ");
						printComponent(component);
					}
					break;
				case "r":
					try {
						clientInterface.returnComponent(component);
					//	System.out.println("The component has been turned face up!\n");
						selectedComponent = null;
						done = true;
					} catch (IOException e) {
						System.out.println("There was a problem while returning the component.");
						System.out.println("Choose what to do with this component: ");
						printComponent(component);
					}
					break;
				default:
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
			}
		}
		drawShipComponentOrSmallDeck();
	}

	/**
	 * Method used to show that a card is drawn: it calls the drawing card method
	 * on the client interface
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void drawCard() throws IOException {
		System.out.println("It's your time to draw a card!");
		letPlayerRead();
		clientInterface.drawCard();
	}

	/**
	 * Method used to signal that the player is landing on a planets
	 */
	@Override
	public void planetsState() {System.out.println("In a while you will be able to land on some planets...");}

	/**
	 * Method used to scroll the ship components and print the stocks and the capacity
	 * of a cargo hold
	 * @param ship current player ship
	 */
	private void viewContainers(ShipComponent[][] ship){
		for(int y = 0; y < ship.length; y++) {
			for(int x = 0; x < ship[y].length; x++){
				if(ship[y][x] instanceof CargoHold cargoHold) {
					System.out.println("The cargo hold at coordinates X = " + x + ", Y = " + y +
							" with maximum capacity of " + cargoHold.getMaxCapacity() + " ");
					if(cargoHold.getStocks().numberOfStocks() == 0)
						System.out.println("is empty.");
					else
						System.out.println("contains: " + cargoHold.getStocks());
				}
			}
		}
	}

	/**
	 * Method used to land on a planet, showing the different planets and the different options,
	 * which the player can choose
	 * @param freePlanetsIndex list of planets indexes
	 * @param curCard current Planets card
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void landOnPlanet(ArrayList<Integer> freePlanetsIndex, Planets curCard) throws IOException {
		String input;
		int in;
		if(freePlanetsIndex.size() == 0){
			System.out.println(TEXT_CYAN + "You reached a system with a few planets, but unfortunately the players before you hav already landed on all of them\n" +
					"You therefore decide to continue onward with the journey to gain days on them." + RESET);
			clientInterface.landOnPlanet(-1);
			return;
		}
		System.out.println("Now you have the opportunity to land on a planet!");
		System.out.println("You can land on these different planets:");
		for(int i=0;i<freePlanetsIndex.size();i++){
			System.out.println("  -  Planet number "+ (freePlanetsIndex.get(i) + 1) +" with "+
					curCard.planetStocks.get(freePlanetsIndex.get(i)) + ".");
		}
		System.out.println("Landing on a planet costs "+curCard.daysLost+" days.");
		do{
			System.out.println("Just decide if you want to land on a planet!");
			System.out.println("You can select the number of the planet or '-1' in order to avoid landing.\n" +
					"Insert 'v' to view the containers you currently have.");
			synchronized(System.in){
				input=scanner.nextLine();
			}
			if(input.equals("v")){
				in = -2;
				viewContainers(clientInterface.getShip());
			}
			else{
				try{
					in=Integer.parseInt(input);
				} catch (NumberFormatException e){
					in=-2;
					System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
				}
				if(in > 0 && in < curCard.planetStocks.size() && !freePlanetsIndex.contains(in - 1))
					System.out.println("Another player has already landed on that planet.");
			}
		}
		while(!freePlanetsIndex.contains(in - 1) && in!=-1);
		if(in==-1) System.out.println("You have decided to not land on any of the planets.");
		else System.out.println("You have decided to land on planet " + in +"!");
		clientInterface.landOnPlanet(in <= -1 ? -1 : in - 1);
	}

	/**
	 * Method used to print the players and show the different positions during the game:
	 * also the turns are notified
	 * @param players current players
	 */
	@Override
	public void showPositionsAndTurns(ArrayList<ShipDashboard> players) {
		System.out.println(TEXT_CYAN + "These are the current positions and laps of all the players! Watch them carefully :)\n" +
								"Positions go from 1 to 24" + RESET);
		System.out.println(TEXT_CYAN + "\nPositions:" + RESET);
		for (ShipDashboard player : players) {
			int position = player.getPosition();
			if (position < 0) {
				position = 24 + position + 1;
			}
			else position = player.getPosition() % 24 + 1;
			System.out.println(player.getNickname() + ": " + position);
		}
		System.out.println(TEXT_CYAN + "\nLaps:" + RESET);
		for (ShipDashboard player : players) {
			int turns;
			if (player.getPosition() < 0) {
				int position1 = -player.getPosition();
				turns = -(position1 / 24) - 1;
			}
			else turns = player.getPosition()/24;
			System.out.println(player.getNickname() + ": " + turns);
		}
	}

	/**
	 * Method used to notify the turn
	 */
	@Override
	public void notifyTurn() {System.out.println("It's your turn!");}

	/**
	 * Method used to show the amount of earned credits
	 * @param credits current amount of credits
	 */
	@Override
	public void notifyCredits(int credits) {System.out.println("You earned " + credits + " credits!");}

	/**
	 * Method used to show the current game state
	 * @param newGameState current game state
	 */
	@Override
	public void notifyNewGameState(GameState newGameState) {System.out.println("The current state of the game is: "+newGameState+" !");}

	/**
	 * Method used to show the current card
	 * @param card current card
	 */
	@Override
	public void notifyNewCardDrawn(Card card) {System.out.println("The current card is:\n" + TEXT_PURPLE + card + RESET);}

	/**
	 * Method used in the end of the game in order to print the winners and say if the
	 * player has won or not. The method asks if the player want to begin another game or
	 * if it's the game over
	 * @param winners list of current winners
	 * @param nickname player nickname
	 * @throws IOException input output exception thrown
	 */
	@Override
	public void winners(ArrayList<ShipDashboard> winners, String nickname) throws IOException {
		String input;
		int in;
		boolean found=false;
		//Maybe here can be printed also the ship dashboards
		if(winners.size()>1){
			System.out.println("This game has multiple winners!");
			System.out.println("The winners are:");
			for(int i=0;i<winners.size();i++){
				System.out.println((i+1)+". "+winners.get(i).getNickname());
				if(winners.get(i).getNickname().equals(nickname)) found=true;
			}
			if(found) System.out.println("Congratulation, you are one of the winners!");
			else System.out.println("You have lost...");
		}
		else{
			System.out.println("The winner is "+winners.getFirst().getNickname());
			if(winners.getFirst().getNickname().equals(nickname)) System.out.println("Congratulation, you have won!");
			else System.out.println("You have lost...");
		}
		do{
			System.out.println("Do you want to join another game?");
		    System.out.println("Just select '1' to disconnect or '2' to start a new game:");
			synchronized (System.in){
				input=scanner.nextLine();
			}
			try{
				in=Integer.parseInt(input);
			} catch (NumberFormatException e){
				System.out.println(TEXT_YELLOW + "Please insert a valid input." + RESET);
				in=-1;
			}
		}
		while(in!=1 && in!=2);

		clientInterface.sendEndGame(in);
		if(in==1) {
			System.out.println("Game over...");
		}
		else {
			System.out.println("Connecting to a new game...");

			askingNickname();
		}
	}

	/**
	 * Method used to welcome back a player who has lost the connection with
	 * the server: it's used to resume the lost players according to resilience
	 * and persistence
	 */
	@Override
	public void resume() {
		System.out.println("Welcome back! You have returned into the game!");
	}
}
