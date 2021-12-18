package jonaJmartBO.jmart_android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jonaJmartBO.jmart_android.model.Product;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private static final Gson gson = new Gson();
    public static final String EXTRA_PRODUCTID = "jonaJmartBO.jmart_android.EXTRA_PRODUCTID";
    MyRecyclerViewAdapter adapter;
    private TabLayout mainTabLayout;
    private CardView cv_product;
    private Button btnPrev;
    private Button btnNext;
    private Button btnGo;
    private EditText et_page;
    private int page;
    private CardView cv_filter;
    private EditText inputProductName;
    private EditText inputLowestPrice;
    private EditText inputHighestPrice;
    private CheckBox checkNew;
    private CheckBox checkUsed;
    private Button btnApply;
    private Button btnClear;
    private Spinner spinner_filterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);

        mainTabLayout = findViewById(R.id.mainTabLayout);
        cv_product = findViewById(R.id.cv_product);
        cv_filter = findViewById(R.id.cv_filter);
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        List<Product> productNames = new ArrayList<>();
        page = 0;
        fetchProduct(productNames, page, queue, false);
        RecyclerView recyclerView = findViewById(R.id.listProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, productNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //Add divider to each row
        btnPrev = findViewById(R.id.buttonPrevious);
        btnNext = findViewById(R.id.buttonNext);
        btnGo   = findViewById(R.id.buttonGo);
        et_page = findViewById(R.id.inputPage);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page > 0){
                    page--;
                    fetchProduct(productNames, page, queue, true);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                fetchProduct(productNames, page, queue, true);

            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = Integer.parseInt(et_page.getText().toString());
                fetchProduct(productNames, page, queue, true);
            }
        });
        //Filter CardView
        inputProductName = findViewById(R.id.productNamefield);
        inputLowestPrice = findViewById(R.id.lowestPriceField);
        inputHighestPrice = findViewById(R.id.highestPriceField);
        spinner_filterCategory = findViewById(R.id.filterCategory);
        checkNew = findViewById(R.id.checkNew);
        checkUsed = findViewById(R.id.checkUsed);
        String checkCondition;
        if(checkNew.isChecked()){
            checkCondition = checkNew.getText().toString();
        }
        if(checkUsed.isChecked()){
            checkCondition = checkUsed.getText().toString();
        }
        btnApply = findViewById(R.id.buttonApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Filter Product", Toast.LENGTH_LONG).show();
                String productName = inputProductName.getText().toString();
                String lowestPrice= inputLowestPrice.getText().toString();
                String highestPrice = inputHighestPrice.getText().toString();
                String category = spinner_filterCategory.getSelectedItem().toString();
                StringRequest filterRequest = new StringRequest(Request.Method.GET, "http://192.168.0.8:8080/product/getFiltered?pageSize=10&search="+productName+"&minPrice="+lowestPrice+"&maxPrice="+highestPrice+"&category="+category, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonReader reader = new JsonReader(new StringReader(response));
                        try {
                            productNames.clear();
                            reader.beginArray();
                            while(reader.hasNext()){
                                productNames.add(gson.fromJson(reader, Product.class));
                            }
                            adapter.refresh(productNames);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Filter product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }

                        cv_product.setVisibility(View.VISIBLE);
                        cv_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Filtering Succesful", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Filtering error, check again.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(filterRequest);
            }
        });
        btnClear = findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Testing Clear", Toast.LENGTH_SHORT).show();
                inputProductName.setText("");
                inputLowestPrice.setText("");
                inputHighestPrice.setText("");
                checkNew.setChecked(false);
                checkUsed.setChecked(false);
                spinner_filterCategory.setSelection(0);
            }
        });
    }
    //Fetch Products Request Method
    public void fetchProduct(List<Product> productNames, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://192.168.0.8:8080/product/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    productNames.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        productNames.add(gson.fromJson(reader, Product.class));
                    }
                    if(refreshAdapter){
                        adapter.refresh(productNames);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchProductsRequest);
    }
    //RecycleView Item ClickListener
    @Override
    public void onItemClick(View view, int position) {
        int clickedItemId = adapter.getClickedItemId(position);
        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCTID, clickedItemId);
        startActivity(intent);
    }
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(LoginActivity.getLoggedAccount().store == null){
            menu.getItem(1).setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
//                startActivity(new Intent(this, RegisterActivity.class));
                return true;
            case R.id.menu_add:
                startActivity(new Intent(this, CreateProductActivity.class));
                return true;
            case R.id.menu_aboutme:
                startActivity(new Intent(this, AboutMeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}