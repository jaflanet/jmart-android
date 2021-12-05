package jonaJmartBO.jmart_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import jonaJmartBO.jmart_android.request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText RegisterName = findViewById(R.id.RegisterName);
        EditText RegisterEmail = findViewById(R.id.RegisterEmail);
        EditText RegisterPassword = findViewById(R.id.RegisterPassword);
        Button registerButton = findViewById(R.id.Registerbutton);

        registerButton.setOnClickListener(v -> {
            Response.Listener<String> listener = response -> {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj != null){
                        Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Register unsuccessful, jsonObj null", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException err){
                    Toast.makeText(RegisterActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorListener = response -> {
                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            };
            String name = RegisterName.getText().toString();
            String email = RegisterEmail.getText().toString();
            String password = RegisterPassword.getText().toString();
            RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(registerRequest);

        });
    }

}