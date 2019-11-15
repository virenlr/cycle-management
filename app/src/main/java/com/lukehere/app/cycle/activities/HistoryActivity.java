package com.lukehere.app.cycle.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.adapters.HistoryAdapter;
import com.lukehere.app.cycle.pojo.History;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mHistoryRecyclerView;
    private SwipeRefreshLayout pullToRefresh;

    private CollectionReference historyCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        historyCollection = firestoreDB.collection("history");

        mHistoryRecyclerView = findViewById(R.id.history_recycler_view);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pullToRefresh = findViewById(R.id.pull_to_refresh);
        pullToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        loadHistory();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHistory();
            }
        });
    }

    private void loadHistory() {
        Query baseQuery = historyCollection.orderBy("returnDate", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();
        FirestorePagingOptions<History> options = new FirestorePagingOptions.Builder<History>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, History.class)
                .build();

        HistoryAdapter mHistoryAdapter = new HistoryAdapter(this, pullToRefresh, options, new HistoryAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(LinearLayout clickedHistoryItemLayout) {
                if (clickedHistoryItemLayout.isShown()) {
                    clickedHistoryItemLayout.setVisibility(View.GONE);
                } else {
                    clickedHistoryItemLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);
    }
}
