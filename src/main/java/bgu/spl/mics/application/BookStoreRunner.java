package bgu.spl.mics.application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.LogisticsService;
import bgu.spl.mics.application.services.ResourceService;
import bgu.spl.mics.application.services.APIService;
import bgu.spl.mics.application.services.InventoryService;
import bgu.spl.mics.application.services.SellingService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import javafx.util.Pair;

import static java.lang.Thread.sleep;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner implements Serializable {
    public static void main(String[] args) throws IOException {
        LinkedList<Thread> threads = new LinkedList<>();
        int index = 0;
        JsonParser parser = new JsonParser();
        String path = "/Users/meshiy/Downloads/input.json";
//        String path = args[0];
        JsonArray initialInventory = parser.parse(getReader(path)).getAsJsonObject().get("initialInventory").getAsJsonArray();
        JsonElement initialResources = parser.parse(getReader(path)).getAsJsonObject().get("initialResources").getAsJsonArray().get(0);
        JsonArray vehicles = initialResources.getAsJsonObject().get("vehicles").getAsJsonArray();
        JsonElement services = parser.parse(getReader(path)).getAsJsonObject().get("services").getAsJsonObject();
        JsonObject time = services.getAsJsonObject().get("time").getAsJsonObject();
        int timeSpeed = time.get("speed").getAsInt();
        int duration = time.get("duration").getAsInt();

        int numOfSelling = getNumOfInstances(services, "selling");
        int numOfinventoryService = getNumOfInstances(services, "inventoryService");
        int numOflogistics = getNumOfInstances(services, "logistics");
        int numOfresourcesService = getNumOfInstances(services, "resourcesService");
        JsonArray customers = services.getAsJsonObject().get("customers").getAsJsonArray();
        BookInventoryInfo[] inventory = new BookInventoryInfo[initialInventory.size()];
        for (int i = 0; i < initialInventory.size(); i++) {
            String bookTitle = initialInventory.get(i).getAsJsonObject().get("bookTitle").getAsString();
            int amountInInventory = initialInventory.get(i).getAsJsonObject().get("amount").getAsInt();
            int price = initialInventory.get(i).getAsJsonObject().get("price").getAsInt();
            inventory[i] = new BookInventoryInfo(bookTitle, amountInInventory, price);
        }
        DeliveryVehicle[] vehiclesList = new DeliveryVehicle[vehicles.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            int license = vehicles.get(i).getAsJsonObject().get("license").getAsInt();
            int speed = vehicles.get(i).getAsJsonObject().get("speed").getAsInt();
            vehiclesList[i] = new DeliveryVehicle(license, speed);
        }

        //customers output:
        HashMap customersHashMap = new HashMap();
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
            customersHashMap.put(id, customer);
            Customers[index] = customer;
            index++;
        }

        // logistics Threads
        for (int i = 0; i < numOflogistics; i++) {
            Thread t = new Thread(new LogisticsService("LogisticsService " + i));
            t.start();
            threads.add(t);
        }
        // resources Threads
        for (int i = 0; i < numOfresourcesService; i++) {
            Thread t = new Thread(new ResourceService(vehiclesList, "ResourceService "+ i));
            t.start();
            threads.add(t);
        }
        // inventory Threads
        for (int i = 0; i < numOfinventoryService; i++) {
            Thread t = new Thread(new InventoryService(inventory, "InventoryService "+ i));
            t.start();
            threads.add(t);

        }
        // selling Threads
        for (int i = 0; i < numOfSelling; i++) {
            Thread t = new Thread(new SellingService("SellingService "+ i));
            t.start();
            threads.add(t);
        }

        // API Threads
        for (int i = 0; i < Customers.length; i++) {
            Thread t = new Thread(new APIService(Customers[i], "APIService "+i));
            t.start();
            threads.add(t);
        }

        // TimeService Thread
        TimeService t = new TimeService(duration, timeSpeed, "TimeService");
        Thread timeServiceThread = new Thread(t);
        timeServiceThread.start();
        threads.add(timeServiceThread);


        int counter = 1;
        for (Thread thread : threads) {
            try {
                System.out.println(thread.getState() + " "+ thread.getName());
                if(thread.getState().equals(Thread.State.WAITING) | thread.getState().equals(Thread.State.BLOCKED)){
                    System.out.println(thread.getName()+ " Stack trace:");
                    System.out.println(thread.getStackTrace());
                }
                thread.join();
                System.out.println(" thread "+counter);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        //customers
//        FileOutputStream file = new FileOutputStream(args[1]);
//        ObjectOutputStream stream = new ObjectOutputStream(file);
//        stream.writeObject(customersHashMap);
//
//        //inventory
//        Inventory.getInstance().printInventoryToFile(args[2]);
//
//        //receipts
//        MoneyRegister.getInstance().printOrderReceipts(args[3]);
//
//        // moneyRegister
//        FileOutputStream file3 = new FileOutputStream(args[4]);
//        ObjectOutputStream stream3 = new ObjectOutputStream(file3);
//        stream3.writeObject(MoneyRegister.getInstance());
//        stream3.close();
//        file3.close();
//        stream.close();
//        file.close();
    }

    private static int getNumOfInstances(JsonElement services, String field) {
        return services.getAsJsonObject().get(field).getAsInt();
    }

    private static JsonReader getReader(String fileName) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return reader;
    }
}
