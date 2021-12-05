package jonaJmartBO.jmart_android;



import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import jonaJmartBO.jmart_android.request.RegisterStoreRequest;

public class AboutMeActivity extends AppCompatActivity {
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserBalance;
    private Button btnTopUp;
    private Button btnRegisterStore;
    private EditText inputTopUpAmount;
    private CardView cv_storeExists;
    private CardView cv_registerStore;
    private EditText inputStoreName;
    private EditText inputStoreAddress;
    private EditText inputStorePhoneNumber;
    private Button btnRegisterStoreCancel;
    private Button btnRegisterStoreConfirm;
    private TextView tv_storeNameF;
    private TextView tv_storeAddressF;
    private TextView tv_storePhoneNumberF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        RequestQueue queue = Volley.newRequestQueue(this);

        btnTopUp = findViewById(R.id.buttonTopUp);
        btnRegisterStore = findViewById(R.id.buttonRegisterStore);
        cv_registerStore = findViewById(R.id.cv_registerStore);
        cv_storeExists = findViewById(R.id.cv_storeExists);
        inputStoreName = findViewById(R.id.et_storeName);
        inputStoreAddress = findViewById(R.id.et_storeAddress);
        inputStorePhoneNumber = findViewById(R.id.et_storePhoneNumber);
        btnRegisterStoreCancel = findViewById(R.id.btnRegisterStoreCancel);
        btnRegisterStoreConfirm = findViewById(R.id.btnRegisterStoreConfirm);
        textUserName = findViewById(R.id.textUserName);
        textUserEmail = findViewById(R.id.textUserEmail);
        textUserBalance = findViewById(R.id.textUserBalance);
        inputTopUpAmount = findViewById(R.id.inputTopUpAmount);
        textUserName.setText(LoginActivity.getLoggedAccount().name);
        textUserEmail.setText(LoginActivity.getLoggedAccount().email);
        textUserBalance.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));
        if(LoginActivity.getLoggedAccount().store != null){
            btnRegisterStore.setVisibility(View.GONE);
            cv_storeExists.setVisibility(View.VISIBLE);
            tv_storeNameF = findViewById(R.id.tv_storeNameF);
            tv_storeAddressF = findViewById(R.id.tv_storeAddressF);
            tv_storePhoneNumberF = findViewById(R.id.tv_storePhoneNumberF);
            tv_storeNameF.setText(LoginActivity.getLoggedAccount().store.name);
            tv_storeAddressF.setText(LoginActivity.getLoggedAccount().store.address);
            tv_storePhoneNumberF.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }

        btnRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterStore.setVisibility(View.INVISIBLE);
                cv_registerStore.setVisibility(View.VISIBLE);
            }
        });
        btnRegisterStoreCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnRegisterStore.setVisibility(View.VISIBLE);
                cv_registerStore.setVisibility(View.INVISIBLE);
            }
        });
        btnRegisterStoreConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputStoreName.getText().toString();
                String address = inputStoreAddress.getText().toString();
                String phoneNumber = inputStorePhoneNumber.getText().toString();
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(LoginActivity.getLoggedAccount().id, name, address ,phoneNumber, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.insertLoggedAccountStore(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Register Store successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerStoreRequest);
            }
        });
    }
}