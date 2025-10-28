import Connections.ClientInterface;
import View.GUIStart;
import View.TUI;
import javafx.application.Application;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main of the client, useful to start the game and choose the interface: TUI or GUI
 */
public class MainPlayer3 {
    public static void main( String[] args ) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in thread: " + thread.getName());
            throwable.printStackTrace();

            // Optional: terminate the whole program
            System.exit(1);
        });

        String input;
        int in;
        do{
            Scanner scanner=new Scanner(System.in);
            System.out.println("Before the start choose the user interface.");
            System.out.println("These are your options:");
            System.out.println("1. TUI (Text User Interface).");
            System.out.println("2. GUI (Graphical User Interface).");
            input=scanner.nextLine();
            try{
                in=Integer.parseInt(input);
            } catch (NumberFormatException e){
                in=-1;
                System.out.println("Please insert a valid input.");
            }
        }
        while(in!=1 && in!=2);
        switch(in){
            case 1: {
                System.out.println("Congratulations! You have chosen the TUI.");
                TUI clientTUI=new TUI();
                ClientInterface clientInterface= new ClientInterface(clientTUI);
                clientTUI.setInterface(clientInterface);
                clientTUI.askConsole();
                clientTUI.gameStart();
                break;
            }
            case 2: {
                System.out.println("Congratulations! You have chosen the GUI.");
                Application.launch(GUIStart.class);
            }
        }

    }
}
