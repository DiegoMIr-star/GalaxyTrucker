package Loader;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class used to load the cards and the ship components from the different json files
 * @see Controller.Controller
 */
public class Loader {

    /**
     * Method used to map the different cards from json files into an objectsCards object
     * @return ObjectsCard which contains the different cards loaded from json file
     * @throws IOException input output exception thrown
     */
    public static ObjectsCard loadCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = Loader.class.getResourceAsStream("/cards.json");
        if(in==null) throw new FileNotFoundException("File cards not found");
        return mapper.readValue(in, ObjectsCard.class);
    }

    /**
     * Method used to map the different ship components from json files into an objectsComponent object
     * @return ObjectsComponent which contains the different ship components loaded from json file
     * @throws IOException input output exception thrown
     */
    public static ObjectsComponent loadComponents() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        InputStream in = Loader.class.getResourceAsStream("/components.json");
        return mapper.readValue(in,ObjectsComponent.class);
    }
}
