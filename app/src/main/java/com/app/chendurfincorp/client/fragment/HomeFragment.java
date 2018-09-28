package com.app.chendurfincorp.client.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.adapter.HomeTabAdapter;
import com.app.chendurfincorp.client.helper.Constants;
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
    public static ArrayList<HashMap<String, String>> tree1A = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree1B = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree1C = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree2A = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree2B = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree2C = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree3A = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree3B = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> tree3C = new ArrayList<>();

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
        final HomeTabAdapter adapter = new HomeTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
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
        protected void onPreExecute() {
            super.onPreExecute();
            tree1A.clear();
            tree1B.clear();
            tree1C.clear();
            tree2A.clear();
            tree2B.clear();
            tree2C.clear();
            tree3A.clear();
            tree3B.clear();
            tree3C.clear();

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
                            JSONObject jcat = array.getJSONObject(i);

                            map = new HashMap<>();

                            String position = jcat.getString("po");
                            String source = jcat.getString("source");
                            String type = jcat.getString("tree_type");

                            if (type.equalsIgnoreCase("1")){
                                if (position.startsWith("a")){
                                    map.put("A", source);
                                    tree1A.add(map);
                                }else if (position.startsWith("b")){
                                    map.put("B", source);
                                    tree1B.add(map);
                                }else if (position.startsWith("c")){
                                    map.put("C", source);
                                    tree1C.add(map);
                                }
                            }else if (type.equalsIgnoreCase("2")){
                                if (position.startsWith("a")){
                                    map.put("A", source);
                                    tree2A.add(map);
                                }else if (position.startsWith("b")){
                                    map.put("B", source);
                                    tree2B.add(map);
                                }else if (position.startsWith("c")){
                                    map.put("C", source);
                                    tree2C.add(map);
                                }
                            }else if (type.equalsIgnoreCase("3")){
                                if (position.startsWith("a")){
                                    map.put("A", source);
                                    tree3A.add(map);
                                }else if (position.startsWith("b")){
                                    map.put("B", source);
                                    tree3B.add(map);
                                }else if (position.startsWith("c")){
                                    map.put("C", source);
                                    tree3C.add(map);
                                }
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

}
