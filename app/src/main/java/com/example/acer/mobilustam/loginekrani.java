package com.example.acer.mobilustam;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginekrani extends AppCompatActivity {
    TextView kullanicigirisi;
    private EditText emailgir, sifregir;
    private Button loginbuton;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    TextView kayitsiz;
    TextView kayitol;


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }
          progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Giris yapılıyor...");
        setContentView(R.layout.activity_loginekrani);
        kullanicigirisi = (TextView) findViewById(R.id.textView2);
        Typeface tfc1 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Light.otf");
        Typeface tfc2 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Bold.otf");
        Typeface tfc3 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Book.otf");
        kullanicigirisi.setTypeface(tfc1);
        emailgir = (EditText) findViewById(R.id.email);
        sifregir = (EditText) findViewById(R.id.sifre);
        kayitsiz = (TextView) findViewById(R.id.soru);
        kayitol = (TextView) findViewById(R.id.kayitol);


        kayitsiz.setTypeface(tfc1);
        kayitol.setTypeface(tfc2);
        emailgir.setTypeface(tfc3);
        sifregir.setTypeface(tfc3);





        emailgir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    emailgir.setHint("");
                else
                    emailgir.setHint("E-Mail Adresi");


            }
        });
        sifregir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    sifregir.setHint("");
                else
                    sifregir.setHint("Şifre");

            }
        });
        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tiklandi = new Intent(getApplicationContext(), kayitolekrani.class);
                startActivity(tiklandi);

            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });


    }

    private void userLogin() {
        //first getting the values
        final String email = emailgir.getText().toString();
        final String sifre = sifregir.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            emailgir.setError("Please enter your username");
            emailgir.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sifre)) {
            sifregir.setError("Please enter your password");
            sifregir.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("ad"),
                                userJson.getString("soyad"),
                                userJson.getString("email"),
                                userJson.getString("telno")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginSonrasiekran.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("sifre", sifre);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    }








