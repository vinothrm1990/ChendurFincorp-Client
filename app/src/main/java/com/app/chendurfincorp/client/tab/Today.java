package com.app.chendurfincorp.client.tab;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.fragment.HomeFragment;
import com.app.chendurfincorp.client.helper.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class Today extends Fragment {

    AVLoadingIndicatorView progress;
    TextView tvSource, tvDate, tvAmount;

    public Today() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", 0);
        Constants.editor = Constants.pref.edit();

        progress = view.findViewById(R.id.progress);
        tvSource = view.findViewById(R.id.today_src_tv);
        tvDate = view.findViewById(R.id.today_date_tv);
        tvAmount = view.findViewById(R.id.today_amount_tv);

        String cusId = Constants.pref.getString("id", "");
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date dt = new Date();
        String date = sdf.format(dt);
        new todatRevenue(getActivity(), cusId, date).execute();
        return view;
    }

    private class todatRevenue extends AsyncTask<String, Integer, String> {

        Context context;
        String id, date;
        String url = Constants.BASE_URL + Constants.TODAY_REVENUE;

        public todatRevenue(Context context, String id, String date) {
            this.context = context;
            this.id = id;
            this.date = date;
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
                    .add("date", date)
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

                        String source = jcat.getString("source");
                        String date = jcat.getString("date");
                        String amount = jcat.getString("amount");

                        tvSource.setText(source);
                        tvDate.setText(date);
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
