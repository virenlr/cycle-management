package com.lukehere.app.cycle.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.adapters.CyclesAdapter;
import com.lukehere.app.cycle.pojo.Cycle;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CycleActivity extends AppCompatActivity {

    public static int mBackCount = 0;

    private FirebaseFirestore firestoreDB;

    private RecyclerView mCyclesRecyclerView;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressBar mCycleAddingProgressBar;

    private CollectionReference cycleCollection;
    private static final int OPEN_EDITOR_ACTIVITY = 7;

    @Override
    public void onBackPressed() {
        mBackCount++;

        if (mBackCount == 1) {
            Toast.makeText(this, getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show();
        } else if (mBackCount == 2) {
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        firestoreDB = FirebaseFirestore.getInstance();
        cycleCollection = firestoreDB.collection("cycles");

        mCycleAddingProgressBar = findViewById(R.id.cycle_adding_progress_bar);
        mCyclesRecyclerView = findViewById(R.id.cycles_recycler_view);
        mCyclesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pullToRefresh = findViewById(R.id.pull_to_refresh);
        pullToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        loadCycleInformation();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCycleInformation();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_cycle:
                showConfirmation();
                return true;
            case R.id.menu_statistics:
                Intent statisticsIntent = new Intent(CycleActivity.this, StatisticsActivity.class);
                startActivity(statisticsIntent);
                return true;
            case R.id.menu_history:
                Intent historyIntent = new Intent(CycleActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
                return true;
            case R.id.menu_sign_out:
                Intent signOutIntent = new Intent(CycleActivity.this, SignOutActivity.class);
                startActivity(signOutIntent);
                return true;
            case R.id.menu_about:
                Intent aboutIntent = new Intent(CycleActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCycleInformation() {
        Query baseQuery = cycleCollection.orderBy("decommissionedStatus", Query.Direction.ASCENDING)
                .orderBy("cycleNumber", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();
        FirestorePagingOptions<Cycle> options = new FirestorePagingOptions.Builder<Cycle>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, Cycle.class)
                .build();

        CyclesAdapter mCyclesAdapter = new CyclesAdapter(this, pullToRefresh, options, new CyclesAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Cycle cycle) {
                Intent i = new Intent(CycleActivity.this, CycleEditorActivity.class);
                i.putExtra("mode", 1);
                i.putExtra("cycle", cycle);

                startActivityForResult(i, OPEN_EDITOR_ACTIVITY);
            }
        });
        mCyclesRecyclerView.setAdapter(mCyclesAdapter);
    }

    private void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.add_cycle_warning));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                incrementCounterAndAddCycle();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void incrementCounterAndAddCycle() {
        if (isConnected(this)) {
            mCycleAddingProgressBar.setVisibility(View.VISIBLE);
            final Map<String, Object> count = new HashMap<>();
            final DocumentReference countReference = firestoreDB.collection("counter").document("count");
            countReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    count.put("count", ((Long) task.getResult().get("count")).intValue());
                    int newCount = (Integer) count.get("count");
                    newCount++;
                    addCycle(newCount);

                    count.put("count", newCount);
                    countReference.set(count);
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void addCycle(int cycleNumber) {
        DocumentReference cycleReference = cycleCollection.document();

        final Cycle cycle = new Cycle(cycleReference.getId(), cycleNumber, 0, "", 0, 0, 0, 0, 0, 0, 0);
        cycleReference.set(cycle).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mCycleAddingProgressBar.setVisibility(View.GONE);

                Intent i = new Intent(CycleActivity.this, CycleEditorActivity.class);
                i.putExtra("mode", 0);
                i.putExtra("cycle", cycle);

                startActivityForResult(i, OPEN_EDITOR_ACTIVITY);
            }
        });
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_EDITOR_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                loadCycleInformation();
            }
        }
    }
}