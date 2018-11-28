package bgu.spl.mics.application;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) throws FileNotFoundException {
    JsonParser parser = new JsonParser();
    JsonReader reader = new JsonReader(new FileReader("/Users/meshiy/Downloads/input.json"));
    System.out.println(parser.parse(reader).getAsJsonObject().get("initialInventory").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject().get("bookTitle"));
        }
}
