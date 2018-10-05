package com.app.chendurfincorp.client.tab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.adapter.ProfitAdapter;
import com.app.chendurfincorp.client.helper.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
public class Total extends Fragment {

    AVLoadingIndicatorView progress;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProfitAdapter profitAdapter;
    ArrayList<HashMap<String,String>> profitList;
    TextView tvSumTotal;
    int total;

    public Total() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total, container, false);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", 0);
        Constants.editor = Constants.pref.edit();

        progress = view.findViewById(R.id.progress);
        profitList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.total_rv);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        tvSumTotal = view.findViewById(R.id.total_sum_tv);

        String cusId = Constants.pref.getString("id", "");
        new totalProfit(getActivity(), cusId).execute();

        return view;
    }

    private class totalProfit extends AsyncTask<String, Integer, String> {


        Context context;
        String id;
        String url = Constants.BASE_URL + Constants.TOTAL_PROFIT;
        HashMap<String, String> map;

        public totalProfit(Context context, String id) {
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
                        for (int i=0; i<array.length(); i++) {
                            JSONObject jcat = array.getJSONObject(i);

                            map = new HashMap<>();

                            String date = jcat.getString("date");
                            String amount = jcat.getString("amount");

                            map.put("date", date);
                            map.put("amount", amount);
                            profitList.add(map);
                        }
                        profitAdapter = new ProfitAdapter(getActivity(), profitList);
                        recyclerView.setAdapter(profitAdapter);

                        tvSumTotal.setText("₹"+String.valueOf(grandTotal()));

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

    public int grandTotal(){

        total = 0;
        for(int i = 0 ; i < profitList.size(); i++) {
            total += Integer.parseInt(profitList.get(i).get("amount"));
        }
        return total;
    }
}
