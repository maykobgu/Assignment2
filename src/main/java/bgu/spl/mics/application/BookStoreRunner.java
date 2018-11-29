package bgu.spl.mics.application;

import java.io.FileNotFoundException;
import java.io.FileReader;

import bgu.spl.mics.application.passiveObjects.Customer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

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
        Customer[] Customers = new Customer[customers.size()];
        for (JsonElement element : customers) {
            Object id = element.getAsJsonObject().get("id");
            Object name = element.getAsJsonObject().get("name");
            Object address = element.getAsJsonObject().get("address");
            Object distance = element.getAsJsonObject().get("distance");
            JsonElement creditCard = element.getAsJsonObject().get("creditCard");
            Object creditCardnumber = creditCard.getAsJsonObject().get("number");
            Object creditCardamount = creditCard.getAsJsonObject().get("amount");
            Customer customer = new Customer((int) id, (String) name, (String) address, (int) distance, (int) creditCardamount, (int) creditCardamount);
            Customers[index] = customer;
        }
        //initialize inventory, first thing
        //load the book info in the inventory
        //get numbers of customers from the json and crete webapi for each one of them
        //according to the json, create the micro services needed
        //create timeservice (singelton)
    }

    private static JsonElement getNumOfInstances(JsonElement services, String field) {
        return services.getAsJsonObject().get(field);
    }

    private static JsonReader getReader(String fileName) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return reader;
    }
}
