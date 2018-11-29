package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.services.APIService;

import java.util.List;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {
	private int id;
    private String name;
    private String address;
    private int distance;
    private List<OrderReceipt> receipts;
    private int creditCard;
    private int availableAmountInCreditCard;
    private APIService apiService;

	public Customer(int id, String name, String address, int distance, int creditCard, int availableAmountInCreditCard){
	    this.id = id;
	    this.name = name;
	    this.address = address;
	    this.distance = distance;
	    this.creditCard = creditCard;
	    this.availableAmountInCreditCard = availableAmountInCreditCard;
	    apiService = new APIService();
    }
	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		// TODO Implement this
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		// TODO Implement this
		return id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		// TODO Implement this
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		// TODO Implement this
		return distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		// TODO Implement this
		return receipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		// TODO Implement this
		return availableAmountInCreditCard;
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		// TODO Implement this
		return creditCard;
	}
	
}
