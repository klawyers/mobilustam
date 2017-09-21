package com.example.acer.mobilustam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

public class SmsOnayEkrani extends AppCompatActivity {
TextView et_baslik,et_aciklama;
    EditText et_aktivasyon,et_id;
    ProgressDialog progressDialog;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_onay_ekrani);
        et_baslik = (TextView) findViewById(R.id.smsaktivasyonu);
        et_aciklama = (TextView) findViewById(R.id.aciklama);
        et_aktivasyon = (EditText) findViewById(R.id.aktivasyonet);


        findViewById(R.id.aktivasyonbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                onayOTP();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Lütfen Bekleyiniz ...");
        if(android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }
        Typeface tfca = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Light.otf");
        Typeface tfcb = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Bold.otf");
        Typeface tfcc = Typeface.createFromAsset(getAssets(), "fonts/Gravity-Book.otf");


        et_baslik.setTypeface(tfca);
        et_aciklama.setTypeface(tfcb);

        et_aktivasyon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et_aktivasyon.setHint("");
                else
                    et_aktivasyon.setHint("Aktivasyon Numaranýzý Giriniz");
            }
        });



    }
      User user = SharedPrefManager.getInstance(this).getUser();
      private void onayOTP(){
      final String id = String.valueOf(user.getId());
      final String otp = et_aktivasyon.getText().toString().trim();

          //first we will do the validations

          if (TextUtils.isEmpty(otp)) {
              et_aktivasyon.setError("Please enter ad");
              et_aktivasyon.requestFocus();
              return;
          }

//if everything is fine

          class OTPonayla extends AsyncTask<Void, Void, String> {

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
                          Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
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
                  params.put("id", id);
                  params.put("otp", otp);

                  //returing the response
                  return requestHandler.sendPostRequest(URLs.URL_SMS, params);
              }
          }

          OTPonayla ul = new OTPonayla();
          ul.execute();

}
}
