package jonaJmartBO.jmart_android.model;

/**
 * @author Jona
 * @version 18/12/21
 */
public class Shipment {
    public String address;
    public int cost;
    public byte plan;
    public String receipt;

    public Shipment(String address, int cost, byte plan, String receipt){
        this.address = address;
        this.cost = cost;
        this.plan = plan;
        this.receipt = receipt;
    }
}
