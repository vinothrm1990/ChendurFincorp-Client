package com.app.chendurfincorp.client.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements InternetConnectivityListener {

    MaterialEditText etName, etPass;
    AVLoadingIndicatorView progress;
    InternetAvailabilityChecker internetAvailabilityChecker;
    FloatingActionButton fabLogin;
    LinearLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);

        Constants.pref = getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();

        progress = findViewById(R.id.progress);
        etName = findViewById(R.id.log_et_name);
        etPass = findViewById(R.id.log_et_pass);
        fabLogin = findViewById(R.id.fab_login);
        loginLayout = findViewById(R.id.login_layout);

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                boolean emptyfeilds = false;

                if (name.length()==0){
                    emptyfeilds = true;
                    etName.setError("Username Required");
                }if (pass.length()==0){
                    emptyfeilds = true;
                    etPass.setError("Password Required");
                }if (emptyfeilds == false) {
                    new login(LoginActivity.this, name, pass).execute();
                    loginLayout.setBackgroundColor(Color.parseColor("#50000000"));
                }
            }
        });
    }

    private class login extends AsyncTask<String, Integer, String> {

        Context context;
        String name, pass;
        String url = Constants.BASE_URL + Constants.LOGIN;

        public login(Context context, String name, String pass) {
            this.context = context;
            this.name = name;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(Constants.name, name)
                    .add(Constants.password, pass)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);

            progress.hide();
            JSONObject jonj = null;
            try {
                if (jsonData != null) {
                    jonj = new JSONObject(jsonData);
                    if (jonj.getString("status").equalsIgnoreCase(
                            "success")) {

                        String data = jonj.getString("message");
                        JSONArray array = new JSONArray(data);
                        JSONObject jcat = array.getJSONObject(0);

                        Constants.editor.putBoolean("isLogged", true);
                        Constants.editor.putString("id", jcat.getString("customer_id"));
                        Constants.editor.putString("name", jcat.getString("name"));
                        Constants.editor.putString("username", jcat.getString("username"));
                        Constants.editor.putString("pass", jcat.getString("own_password"));
                        Constants.editor.putString("address", jcat.getString("address"));
                        Constants.editor.commit();
                        Constants.editor.apply();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        Toasty.error(context, jonj.getString("message"), Toast.LENGTH_LONG, true).show();
                    }
                }else {
                    Toasty.warning(context, "No Data Response", Toast.LENGTH_LONG, true).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        internetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            Toasty.warning(LoginActivity.this, "Check your Network Connection", Toast.LENGTH_LONG, true).show();
        }
    }
}
