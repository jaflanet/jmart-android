package jonaJmartBO.jmart_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import jonaJmartBO.jmart_android.model.Payment;
import jonaJmartBO.jmart_android.model.Product;

//Product RecycleView List
public class MyRecyclerViewTransactionsAdapter extends RecyclerView.Adapter<MyRecyclerViewTransactionsAdapter.ViewHolder> {
    private static final Gson gson = new Gson();
    private List<Payment> mData;
    private List<Product> transactionProducts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;




    MyRecyclerViewTransactionsAdapter(Context context, List<Payment> data, List<Product> transactionProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.transactionProducts = transactionProducts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_invoice_default, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Payment paymentName = mData.get(position);
        Product productName = transactionProducts.get(position);
        holder.id = paymentName.id;
        if(productName.toString().length() >= 38){
            holder.invoiceNameTransaction.setTextSize(12.0f);
            holder.invoiceNameTransaction.setMaxEms(10);
        }
        holder.invoiceNameTransaction.setText(productName.toString());
        holder.invoiceStatusTransaction.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.invoiceAmountTransaction.setText("x"+String.valueOf(paymentName.productCount));
        holder.invoiceTotalPriceTransaction.setText(String.valueOf(Math.round(productName.price * paymentName.productCount* 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.invoiceShipmentPlanTransaction.setText(("INSTANT"));
                break;
            case 1:
                holder.invoiceShipmentPlanTransaction.setText(("SAME_DAY"));
                break;
            case 2:
                holder.invoiceShipmentPlanTransaction.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.invoiceShipmentPlanTransaction.setText(("KARGO"));
                break;
            default:
                holder.invoiceShipmentPlanTransaction.setText(("REGULER"));
                break;
        }
        holder.productIdTransaction.setText("Product ID: "+ paymentName.productId);
        holder.invoiceStore.setText("Seller ID: " + productName.accountId);
        holder.invoiceShipmentAddressTransaction.setText(paymentName.shipment.address);
        if (paymentName.history.get(paymentName.history.size() - 1).status.toString() != "WAITING_CONFIRMATION") {
            holder.rv_btnCancelTransactionT.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void refresh(List<Payment> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshProducts(List<Product> transactionProducts) {
        this.transactionProducts = transactionProducts;
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView invoiceNameTransaction;
        TextView invoiceStatusTransaction;
        TextView invoiceAmountTransaction;
        TextView invoiceTotalPriceTransaction;
        TextView invoiceShipmentPlanTransaction;
        TextView productIdTransaction;
        TextView invoiceStore;
        TextView invoiceShipmentAddressTransaction;
        ImageView productInvoiceTransaction_img;
        Button rv_btnCancelTransactionT;
        int id;

        ViewHolder(View itemView) {
            super(itemView);
            invoiceNameTransaction = itemView.findViewById(R.id.invoiceNameT);
            invoiceStatusTransaction = itemView.findViewById(R.id.invoiceStatusT);
            invoiceAmountTransaction = itemView.findViewById(R.id.invoiceAmountT);
            invoiceTotalPriceTransaction = itemView.findViewById(R.id.invoiceTotalPriceT);
            invoiceShipmentPlanTransaction = itemView.findViewById(R.id.invoiceShipmentPlanT);
            productIdTransaction = itemView.findViewById(R.id.productIdT);
            invoiceStore = itemView.findViewById(R.id.invoiceStore);
            invoiceShipmentAddressTransaction = itemView.findViewById(R.id.invoiceAddressTransaction);
            rv_btnCancelTransactionT = itemView.findViewById(R.id.btnCancelTransactionT);
            rv_btnCancelTransactionT.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {


                    StringRequest cancelRequest = new StringRequest(Request.Method.POST, "http://192.168.0.8:8080/payment/"+id+"/cancel", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(mInflater.getContext(), "Cancel successful", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mInflater.getContext(), "Cancel failed", Toast.LENGTH_LONG).show();
                            }
                        }

                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mInflater.getContext(), "Cancel failed", Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(mInflater.getContext());
                    queue.add(cancelRequest);
                }
            });
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }
    int getClickedItemId(int id){ return mData.get(id).id;}

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }
}

