package Controller;

import View.ColorManagement.ConsoleColor;

import java.io.*;
import java.util.ConcurrentModificationException;

/**
 * Class use to implement the persistence functionality
 */
public class Persistence {
    /**
     * SINGLE instance of controller
     */
    private static Persistence instance;

    private Persistence(){}

    /**
     * getter of the controller instance
     *
     * @return the controller instance
     */
    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    /**
     * Method used to create a directory where is stored the game for persistence
     * @throws IOException input output exception thrown
     */
    public void createDirIfNotExists() throws IOException {
        File dir = new File("GalaxyTrucker/GameSaved");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                System.out.println("Error creating directory for persistence...");
                return;
            }
            else
                System.out.println("Directory created successfully");
        }
        File out = new File(dir, "store.txt");
        if (!out.exists()) {
            boolean created = out.createNewFile();
            if(!created){
                System.out.println("Error creating file for persistence...");
            }
            else
                System.out.println("File created successfully");
        }
    }

    /**
     * Method used to save a specific controller into a txt file
     * @param controller controller with the game logic
     */
    synchronized public void save(Controller controller) {
        try {
            File dir = new File("GalaxyTrucker/GameSaved");
            if (!dir.exists()) {
                createDirIfNotExists();
            }

            File file = new File(dir, "store.txt");
            while(true){
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(controller);
                    System.out.println("Wrote to file");
                    break;
                } catch (ConcurrentModificationException e) {
                    System.out.println(ConsoleColor.BACKGROUND_WHITE + ConsoleColor.TEXT_RED +
                            "Concurrent modification exception: Error writing to file, trying again..." + ConsoleColor.RESET);
                }
            }

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            printProblem(e);
        }
    }

    /**
     * Method used to load a controller with the game logic from a txt file
     * @return game controller
     */
    public Controller load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("GalaxyTrucker/GameSaved/store.txt"))) {
            Controller controller = (Controller) ois.readObject();
            if (controller == null) {
                System.out.println("No corresponding controller found");
                return null;
            }
            else
                return controller;
        } catch (FileNotFoundException e) {
            System.out.println("Error loading from file");
            printProblem(e);
            return null;
        }
        catch (IOException e) {
            System.out.println("IOException loading from file");
            printProblem(e);
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException loading from file");
            printProblem(e);
            return null;
        }
    }

    /**
     * Method used to verify if the directory with the file exists
     * @return true if the directory with the file exists
     */
    public boolean directoryWithFileExists() {
        File dir = new File("GalaxyTrucker/GameSaved");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if(files!=null){
                for (File file : files) {
                    if (file.getName().equals("store.txt"))
                        return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Method used to delete a txt file with the controller, when the game is finished
     */
    public void deleteSavedFile() {
        File file = new File("GalaxyTrucker/GameSaved/store.txt");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Store file deleted...");
            } else {
                System.out.println("Problem during the deletion of the file...");
            }
        } else {
            System.out.println("The store file doesn't exists...");
        }
    }

    /**
     * Method used for printing exceptions
     * @param e current exception
     */
    private void printProblem(Exception e){e.printStackTrace();}
}
