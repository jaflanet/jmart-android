package jonaJmartBO.jmart_android.model;

/**
 * @author Jona
 * @version 18/12/21
 */

public class Product extends Serializable {
    public int accountId;
    public ProductCategory category;
    public boolean conditionUsed;
    public double discount;
    public String name;
    public double price;
    public byte shipmentPlans;
    public int weight;

    @Override
    public String toString(){
        return name;
    }
}
