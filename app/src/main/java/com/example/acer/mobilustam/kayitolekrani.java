package com.example.acer.mobilustam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class kayitolekrani extends AppCompatActivity {

    TextView kayitolustur;
    private EditText ad_reg, soyad_reg, email_reg, telno_reg, sifre_reg;
    private Button ButtonRegister;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitolekrani);

        //User zaten login ise direk anasayfaya geçiþ için ;


        kayitolustur = (TextView) findViewById(R.id.kolustur);
        ad_reg = (EditText) findViewById(R.id.adgiris);
        soyad_reg = (EditText) findViewById(R.id.soyadgiris);
        email_reg = (EditText) findViewById(R.id.emailgiris);
        telno_reg = (EditText) findViewById(R.id.telnogiris);
        sifre_reg = (EditText) findViewById(R.id.sifregiris);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Kayýt olusturuluyor..");

        findViewById(R.id.buttonregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });
        Typeface tfc1 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Light.otf");
        Typeface tfc2 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Bold.otf");
        Typeface tfc3 = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Book.otf");
        kayitolustur.setTypeface(tfc1);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }
        ad_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    ad_reg.setHint("");
                else
                    ad_reg.setHint("Ad");
                ad_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (ad_reg.getText().length() < 3) {
                            ad_reg.setError("Lutfen gecerli bir isim giriniz..");
                        }
                    }
                });

            }
        });
        soyad_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    soyad_reg.setHint("");
                else
                    soyad_reg.setHint("Soyad");

            }
        });
        email_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    email_reg.setHint("");
                else
                    email_reg.setHint("E-Mail Adresi");

            }
        });
        telno_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    telno_reg.setHint("");
                else
                    telno_reg.setHint("5xx xxx xxxx");

            }
        });
        sifre_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    sifre_reg.setHint("");
                else
                    sifre_reg.setHint("******");

            }
        });

    }

    private void registerUser() {
        final String ad = ad_reg.getText().toString().trim();
        final String soyad = soyad_reg.getText().toString().trim();
        final String email = email_reg.getText().toString().trim();
        final String telno = telno_reg.getText().toString().trim();
        final String sifre = sifre_reg.getText().toString().trim();


        //first we will do the validations

        if (TextUtils.isEmpty(ad)) {
            ad_reg.setError("Please enter ad");
            ad_reg.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(soyad)) {
            soyad_reg.setError("Please enter your soyad");
            soyad_reg.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_reg.setError("Enter a valid email");
            email_reg.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(telno)) {
            telno_reg.setError("Enter a telno");
            telno_reg.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sifre)) {
            sifre_reg.setError("Enter a password");
            sifre_reg.requestFocus();
            return;
        }

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("ad", ad);
                params.put("soyad", soyad);
                params.put("email", email);
                params.put("telno", telno);
                params.put("sifre", sifre);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
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
                        startActivity(new Intent(getApplicationContext(), SmsOnayEkrani.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }
}

