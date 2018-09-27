package com.app.chendurfincorp.client.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.activity.HomeActivity;
import com.app.chendurfincorp.client.activity.LoginActivity;
import com.app.chendurfincorp.client.adapter.TabAdapter;
import com.app.chendurfincorp.client.helper.Constants;
import com.app.chendurfincorp.client.tab.InvestmentFragment;
import com.app.chendurfincorp.client.tab.TodayFragment;
import com.app.chendurfincorp.client.tab.TotalFragment;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements InternetConnectivityListener {

    InternetAvailabilityChecker internetAvailabilityChecker;
    TabLayout tabLayout;
    String cusId;
    public static ArrayList<HashMap<String, String>> listA = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> listB = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> listC = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();

        cusId = Constants.pref.getString("id", "");
        new viewNetwork(getActivity(), cusId).execute();

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Revenue"));
        tabLayout.addTab(tabLayout.newTab().setText("Investment"));
        tabLayout.addTab(tabLayout.newTab().setText("Credits"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private class viewNetwork extends AsyncTask<String, Integer, String> {

        Context context;
        String id;
        HashMap<String, String> map;
        String url = Constants.BASE_URL + Constants.VIEW_NETWORK;

        public viewNetwork(Context context, String id) {
            this.context = context;
            this.id = id;
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

            JSONObject jonj = null;
            try {
                if (jsonData != null) {
                    jonj = new JSONObject(jsonData);
                    if (jonj.getString("status").equalsIgnoreCase(
                            "success")) {

                        String data = jonj.getString("message");
                        JSONArray array = new JSONArray(data);
                        for(int i=0; i<array.length(); i++){
                            JSONObject jcat = array.getJSONObject(0);

                            map = new HashMap<>();

                            String position = jcat.getString("po");
                            String source = jcat.getString("source");

                            if (position.startsWith("a")){
                                map.put("A", source);
                                listA.add(map);
                            }else if (position.startsWith("b")){
                                map.put("B", source);
                                listB.add(map);
                            }else if (position.startsWith("c")){
                                map.put("C", source);
                                listC.add(map);
                            }
                        }

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
    public void onDestroy() {
        super.onDestroy();
        internetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            Toasty.warning(getActivity(), "Check your Network Connection", Toast.LENGTH_LONG, true).show();
        }
    }

}
