package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link OrderBookEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link bgu.spl.mics.application.passiveObjects.ResourcesHolder}, {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {
    private MoneyRegister moneyRegister = MoneyRegister.getInstance();
    private int currentTick;

    public SellingService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {
        subscribeEvent(OrderBookEvent.class, this::processEvent);
        subscribeBroadcast(TickBroadcast.class, this::act);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);
    }

    private void processEvent(OrderBookEvent e) {
        System.out.println(this.getName()+" got an orderBookevent");
        CheckAvailability check = new CheckAvailability(e.getCustomer(), e.getBookTitle());
        Future price = sendEvent(check); //should be price
        System.out.println("the price is:  "+price.get());
        OrderReceipt receipt = null;
        if ((int) price.get() != -1) {
            receipt = MoneyRegister.createReceipt(e.getCustomer().getName(), e.getCustomer().getId(),
                    e.getBookTitle(), (int) price.get(), currentTick, e.getOrderedTick(), currentTick); //make receipt
            moneyRegister.chargeCreditCard(e.getCustomer(), (int) price.get()); //charge the customer for this book
            moneyRegister.file(receipt);
        }
        if (receipt == null) {
            System.out.println(" no receipt so no charge ");
            this.complete((Event) e, -1);
        } else
            this.complete((Event) e, receipt);
    }


    private void act(TickBroadcast e) {
        currentTick = e.getCurrentTick();
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }
}
