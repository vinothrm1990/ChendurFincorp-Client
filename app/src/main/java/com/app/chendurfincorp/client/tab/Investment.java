package com.app.chendurfincorp.client.tab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Investment extends Fragment {

    AVLoadingIndicatorView progress;
    TextView tvSource, tvAmount, tvName, tvPhone;

    public Investment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_investment, container, false);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", 0);
        Constants.editor = Constants.pref.edit();

        progress = view.findViewById(R.id.progress);
        tvSource = view.findViewById(R.id.invst_src_tv);
        tvAmount = view.findViewById(R.id.invst_amount_tv);
        tvPhone = view.findViewById(R.id.invst_phone_tv);
        tvName = view.findViewById(R.id.invst_name_tv);

        String cusId = Constants.pref.getString("id", "");
        new investment(getActivity(), cusId).execute();

        return view;
    }

    private class investment extends AsyncTask<String, Integer, String> {

        Context context;
        String id;
        String url = Constants.BASE_URL + Constants.INVESTMENT;

        public investment(Context context, String id) {
            this.context = context;
            this.id = id;
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
                    .add("source", id)
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

                        String source = jcat.getString("customer_id");
                        String amount = jcat.getString("amount");
                        String name = jcat.getString("name");
                        String phone = jcat.getString("mobile");

                        tvSource.setText(source);
                        tvName.setText(name);
                        tvPhone.setText(phone);
                        tvAmount.setText("₹"+amount);

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
}
