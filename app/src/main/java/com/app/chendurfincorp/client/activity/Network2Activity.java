package com.app.chendurfincorp.client.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;
import com.app.chendurfincorp.client.holder.ViewHolder;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;
import java.util.HashMap;
import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;
import es.dmoral.toasty.Toasty;
import static com.app.chendurfincorp.client.fragment.HomeFragment.tree2A;
import static com.app.chendurfincorp.client.fragment.HomeFragment.tree2B;
import static com.app.chendurfincorp.client.fragment.HomeFragment.tree2C;

public class Network2Activity extends AppCompatActivity implements InternetConnectivityListener {

    GraphView graphView;
    InternetAvailabilityChecker internetAvailabilityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network2);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("NETWORK 2");
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/cousine_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);

        Constants.pref = getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        String source = Constants.pref.getString("id", "");
        graphView = findViewById(R.id.graph_view);

        if (tree2A.size() > 0 || tree2B.size() > 0 || tree2C.size() > 0) {

            int j = 0, k = 0, l = 0;

            Graph graph = new Graph();
            Node src = new Node(source);
            Node nodeA[] = new Node[tree2A.size()];
            Node nodeB[] = new Node[tree2B.size()];
            Node nodeC[] = new Node[tree2C.size()];
            HashMap<String, String> map;

            if (tree2A.size() > 0) {
                nodeA[0] = new Node(tree2A.get(0).get("A"));
                graph.addEdge(src, nodeA[0]);
                for (int i = 1; i < tree2A.size(); i++) {
                    map = tree2A.get(i);
                    nodeA[i] = new Node(map.get("A"));
                    graph.addEdge(nodeA[j], nodeA[i]);
                    j++;
                }
            }
            if (tree2B.size() > 0) {
                nodeB[0] = new Node(tree2B.get(0).get("B"));
                graph.addEdge(src, nodeB[0]);
                for (int i = 1; i < tree2B.size(); i++) {
                    map = tree2B.get(i);
                    nodeB[i] = new Node(map.get("B"));
                    graph.addEdge(nodeB[k], nodeB[i]);
                    k++;
                }
            }
            if (tree2C.size() > 0) {
                nodeC[0] = new Node(tree2C.get(0).get("C"));
                graph.addEdge(src, nodeC[0]);
                for (int i = 1; i < tree2C.size(); i++) {
                    map = tree2C.get(i);
                    nodeC[i] = new Node(map.get("C"));
                    graph.addEdge(nodeC[l], nodeC[i]);
                    l++;
                }
            }

            BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(Network2Activity.this, R.layout.graph_layout, graph) {
                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(View view) {
                    return new ViewHolder(view);
                }

                @Override
                public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                    viewHolder.mTextView.setText(data.toString());
                }
            };
            graphView.setAdapter(adapter);

            BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                    .setSiblingSeparation(100)
                    .setLevelSeparation(300)
                    .setSubtreeSeparation(300)
                    .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                    .build();
            adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));

        }else graphView.setVisibility(View.GONE);
    }

        @Override
        public void onDestroy () {
            super.onDestroy();
            internetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
        }

        @Override
        public void onInternetConnectivityChanged ( boolean isConnected){
            if (!isConnected) {
                Toasty.warning(Network2Activity.this, "Check your Network Connection", Toast.LENGTH_LONG, true).show();
            }
        }
}
