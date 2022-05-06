package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText txtName, txtEmail, txtPassword, txtRePassword;
    private TextView txtStatus;
    private Button btnRegister;
    private String URL = "https://192.168.1.9:80/login/register.php";
    private  String name, email, password, reenterPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        txtStatus = findViewById(R.id.txtStatus);
        btnRegister = findViewById(R.id.btnRegister);
        name = email = password =reenterPassword = "";

    }

    public void save(View view) {
        name = txtName.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        reenterPassword = txtRePassword.getText().toString().trim();
        if(!password.equals(reenterPassword)) {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }
        else if(!name.equals("") && !email.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        txtStatus.setText("Successfully registered.");
                        btnRegister.setClickable(false);
                    } else if (response.equals("failure")) {
                        txtStatus.setText("Something went wrong!");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("name", name);
                    data.put("email", email);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            requestQueue.add(stringRequest);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
