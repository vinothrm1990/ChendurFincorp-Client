package com.app.chendurfincorp.client.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.chendurfincorp.client.R;
import com.app.chendurfincorp.client.helper.Constants;
import com.app.chendurfincorp.client.holder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

import static android.content.Context.MODE_PRIVATE;
import static com.app.chendurfincorp.client.fragment.HomeFragment.listA;
import static com.app.chendurfincorp.client.fragment.HomeFragment.listB;
import static com.app.chendurfincorp.client.fragment.HomeFragment.listC;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {

    GraphView graphView;

    public NetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        Constants.pref = getActivity().getApplicationContext().getSharedPreferences("CF", MODE_PRIVATE);
        Constants.editor = Constants.pref.edit();
        String source = Constants.pref.getString("id", "");
        graphView = view.findViewById(R.id.graph_view);

        Graph graph = new Graph();
        Node src = new Node(source);
        Node nodeA[] = new Node[listA.size()];
        Node nodeB[] = new Node[listB.size()];
        Node nodeC[] = new Node[listC.size()];
        HashMap<String, String> map;
        nodeA[0]=new Node(listA.get(0).get("A"));
        graph.addEdge(src,nodeA[0]);
        nodeB[0]=new Node(listB.get(0).get("B"));
        graph.addEdge(src,nodeB[0]);
        nodeC[0]=new Node(listC.get(0).get("C"));
        graph.addEdge(src,nodeC[0]);
        int j=0, k=0, m=0;
        for (int i=1; i<listA.size(); i++){
            map = listA.get(i);
            nodeA[i] = new Node(map.get("A"));
            graph.addEdge(nodeA[j], nodeA[i]);
            j++;
        }
        for (int i=1; i<listB.size(); i++){
            map = listB.get(i);
            nodeB[i] = new Node(map.get("B"));
            graph.addEdge(nodeB[k], nodeB[i]);
            k++;
        }
        for (int i=1; i<listC.size(); i++){
            map = listC.get(i);
            nodeC[i] = new Node(map.get("C"));
            graph.addEdge(nodeC[m], nodeC[i]);
            m++;
        }

        BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(getActivity(), R.layout.graph_layout, graph) {
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

        return view;
    }

}
