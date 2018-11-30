package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Customer;
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
import com.sun.tools.javac.util.Pair;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) throws FileNotFoundException {
        int index = 0;
        JsonParser parser = new JsonParser();
        String path = "/Users/maykogan/Desktop/input.json";
        JsonArray initialInventory = parser.parse(getReader(path)).getAsJsonObject().get("initialInventory").getAsJsonArray();
        JsonElement initialResources = parser.parse(getReader(path)).getAsJsonObject().get("initialResources").getAsJsonArray().get(0);
        JsonArray vehicles = initialResources.getAsJsonObject().get("vehicles").getAsJsonArray();
        JsonElement services = parser.parse(getReader(path)).getAsJsonObject().get("services").getAsJsonObject();
        JsonObject time = services.getAsJsonObject().get("time").getAsJsonObject();
        JsonElement numOfSelling = getNumOfInstances(services, "selling");
        JsonElement numOfinventoryService = getNumOfInstances(services, "inventoryService");
        JsonElement numOflogistics = getNumOfInstances(services, "logistics");
        JsonElement numOfresourcesService = getNumOfInstances(services, "resourcesService");
        JsonArray customers = services.getAsJsonObject().get("customers").getAsJsonArray();
        BookInventoryInfo[] inventory = new BookInventoryInfo[initialInventory.size()];
        Inventory inv = new Inventory();
        for (int i = 0; i < initialInventory.size(); i++) {
            String bookTitle = initialInventory.get(i).getAsJsonObject().get("bookTitle").getAsString();
            int amountInInventory = initialInventory.get(i).getAsJsonObject().get("amount").getAsInt();
            int price = initialInventory.get(i).getAsJsonObject().get("price").getAsInt();
            inventory[i] = new BookInventoryInfo(bookTitle, amountInInventory, price);
        }
        inv.load(inventory);
        DeliveryVehicle[] vehiclesList = new DeliveryVehicle[vehicles.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            int license = vehicles.get(i).getAsJsonObject().get("license").getAsInt();
            int speed = vehicles.get(i).getAsJsonObject().get("speed").getAsInt();
            vehiclesList[i] = new DeliveryVehicle(license, speed);
        }
//        ResourcesHolder rh = new ResourcesHolder(vehiclesList);
        TimeService tickTime = new TimeService(time.get("speed").getAsInt(), time.get("duration").getAsInt());
        Customer[] Customers = new Customer[customers.size()];
        for (JsonElement element : customers) {
            int id = element.getAsJsonObject().get("id").getAsInt();
            String name = element.getAsJsonObject().get("name").getAsString();
            String address = element.getAsJsonObject().get("address").getAsString();
            int distance = element.getAsJsonObject().get("distance").getAsInt();
            JsonElement creditCard = element.getAsJsonObject().get("creditCard");
            int creditCardNumber = creditCard.getAsJsonObject().get("number").getAsInt();
            int creditCardAmount = creditCard.getAsJsonObject().get("amount").getAsInt();
            List<Pair<String, Integer>> orderSchedule = new ArrayList<>();
            JsonArray orderScheduleFromJson = element.getAsJsonObject().get("orderSchedule").getAsJsonArray();
            for (JsonElement os : orderScheduleFromJson) {
                Pair<String, Integer> pair = new Pair<>(os.getAsJsonObject().get("bookTitle").getAsString(), os.getAsJsonObject().get("tick").getAsInt());
                orderSchedule.add(pair);
            }
            Customer customer = new Customer(id, name, address, distance, creditCardNumber, creditCardAmount, orderSchedule);
            Customers[index] = customer;
            index++;
        }

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
