package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileReader;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        String path = "/Users/meshiy/Downloads/input.json";
        JsonElement initialInventory = parser.parse(getReader(path)).getAsJsonObject().get("initialInventory").getAsJsonArray();
        JsonElement initialResources = parser.parse(getReader(path)).getAsJsonObject().get("initialResources").getAsJsonArray().get(0);
        JsonArray vehicles = initialResources.getAsJsonObject().get("vehicles").getAsJsonArray();
        JsonElement services = parser.parse(getReader(path)).getAsJsonObject().get("services").getAsJsonObject();
        JsonElement time = services.getAsJsonObject().get("time").getAsJsonObject();
        JsonElement numOfSelling = getNumOfInstances(services, "selling");
        JsonElement numOfinventoryService = getNumOfInstances(services, "inventoryService");
        JsonElement numOflogistics = getNumOfInstances(services, "logistics");
        JsonElement numOfresourcesService = getNumOfInstances(services, "resourcesService");
        JsonArray customers = services.getAsJsonObject().get("customers").getAsJsonArray();

        BookInventoryInfo[] inventory = new BookInventoryInfo[((JsonArray) initialInventory).size()];
        Inventory inv = new Inventory();
        for (int i =0; i < ((JsonArray) initialInventory).size(); i++ ) {
            String bookTitle = ((JsonArray) initialInventory).get(i).getAsJsonObject().get("bookTitle").getAsString();
            int amountInInventory = ((JsonArray) initialInventory).get(i).getAsJsonObject().get("amount").getAsInt();
            int price = ((JsonArray) initialInventory).get(i).getAsJsonObject().get("price").getAsInt();
            inventory[i] = new BookInventoryInfo(bookTitle, amountInInventory, price);
        }
        inv.load(inventory);
        DeliveryVehicle[] vehiclesList = new DeliveryVehicle[vehicles.size()];
        for (int i =0; i < vehicles.size(); i++ ){
            int license = vehicles.get(i).getAsJsonObject().get("license").getAsInt();
            int speed = vehicles.get(i).getAsJsonObject().get("speed").getAsInt();
            vehiclesList[i] = new DeliveryVehicle (license,  speed);
        }
        ResourcesHolder rh = new ResourcesHolder(vehiclesList);
        System.out.println(((JsonObject) time).get("speed").getAsString());
        TimeService tickTime = new TimeService(((JsonObject) time).get("speed").getAsInt(), ((JsonObject) time).get("duration").getAsInt());

        //get numbers of customers from the json and crete webapi for each one of them
        //according to the json, create the micro services needed
    }

    private static JsonElement getNumOfInstances(JsonElement services, String field) {
        return services.getAsJsonObject().get(field);
    }

    private static JsonReader getReader(String fileName) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return reader;
    }
}
