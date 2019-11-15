package com.lukehere.app.cycle.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.pojo.Cycle;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {

    CollectionReference cycleReference;
    private ArrayList<Cycle> mCycleArrayList;

    private ProgressBar mStatisticsProgressBar;

    private TextView totalNumberOfCyclesTextView, numberOfAvailableCyclesTextView, numberOfCyclesInUseTextView, numberOfBrokenCyclesTextView, numberOfDecommissionedCyclesTextView;
    private TextView numberOfCyclesInFourthBlockTextView, numberOfCyclesInDevadanBlockTextView;
    private TextView tyresTextView, chainTextView, cableTextView, brakeTextView, lubricationTextView, miscellaneousTextView;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistics, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_refresh:
                refreshStatistics();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        cycleReference = firestoreDB.collection("cycles");

        mStatisticsProgressBar = findViewById(R.id.statistics_progress_bar);

        totalNumberOfCyclesTextView = findViewById(R.id.statistics_total_number_of_cycles);
        numberOfAvailableCyclesTextView = findViewById(R.id.statistics_number_of_available_cycles);
        numberOfCyclesInUseTextView = findViewById(R.id.statistics_number_of_cycles_in_use);
        numberOfBrokenCyclesTextView = findViewById(R.id.statistics_number_of_broken_cycles);
        numberOfDecommissionedCyclesTextView = findViewById(R.id.statistics_number_of_decommissioned_cycles);
        numberOfCyclesInFourthBlockTextView = findViewById(R.id.statistics_number_of_cycles_in_fourth_block);
        numberOfCyclesInDevadanBlockTextView = findViewById(R.id.statistics_number_of_cycles_in_devadan_block);
        tyresTextView = findViewById(R.id.statistics_tyres);
        chainTextView = findViewById(R.id.statistics_chain);
        cableTextView = findViewById(R.id.statistics_cable);
        brakeTextView = findViewById(R.id.statistics_brake);
        lubricationTextView = findViewById(R.id.statistics_lubrication);
        miscellaneousTextView = findViewById(R.id.statistics_miscellaneous);

        refreshStatistics();
    }

    private void refreshStatistics() {
        mStatisticsProgressBar.setVisibility(View.VISIBLE);
        cycleReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mCycleArrayList = new ArrayList<>();

                    for (DocumentSnapshot doc : task.getResult()) {
                        Cycle cycle = doc.toObject(Cycle.class);
                        mCycleArrayList.add(cycle);
                    }

                    int totalNumberOfCycles = mCycleArrayList.size();
                    int numberOfAvailableCycles = 0;
                    int numberOfBusyCycles = 0;
                    int numberOfDamagedCycles = 0;
                    int numberOfDecommissionedCycles = 0;

                    for (int i = 0; i < totalNumberOfCycles; i++) {
                        Cycle c = mCycleArrayList.get(i);

                        if (c.getDecommissionedStatus() == 1) {
                            numberOfDecommissionedCycles++;
                        } else if (c.getTyreCondition() == 1 || c.getChainCondition() == 1 || c.getCableCondition() == 1 || c.getBrakeCondition() == 1 || c.getLubricationCondition() == 1 || c.getMiscellaneousCondition() == 1) {
                            numberOfDamagedCycles++;
                        } else if (c.getStatus() == 1) {
                            numberOfBusyCycles++;
                        } else {
                            numberOfAvailableCycles++;
                        }
                    }

                    int numberOfCyclesInFourthBlock = 0;
                    int numberOfCyclesInDevadanBlock = 0;

                    for (int i = 0; i < totalNumberOfCycles; i++) {
                        Cycle c = mCycleArrayList.get(i);

                        if (c.getLocation().equals(getResources().getStringArray(R.array.location_list)[0]) && c.getStatus() == 0 && c.getDecommissionedStatus() == 0 && c.getTyreCondition() == 0 && c.getChainCondition() == 0 && c.getCableCondition() == 0 && c.getBrakeCondition() == 0 && c.getLubricationCondition() == 0 && c.getMiscellaneousCondition() == 0) {
                            numberOfCyclesInFourthBlock++;
                        } else if (c.getLocation().equals(getResources().getStringArray(R.array.location_list)[1]) && c.getStatus() == 0 && c.getDecommissionedStatus() == 0 && c.getTyreCondition() == 0 && c.getChainCondition() == 0 && c.getCableCondition() == 0 && c.getBrakeCondition() == 0 && c.getLubricationCondition() == 0 && c.getMiscellaneousCondition() == 0) {
                            numberOfCyclesInDevadanBlock++;
                        }
                    }

                    int numberOfDamagedTyres = 0;
                    int numberOfDamagedChains = 0;
                    int numberOfDamagedCables = 0;
                    int numberOfDamagedBrakes = 0;
                    int numberOfUnlubricatedCycles = 0;
                    int numberOfMiscellaneousDamages = 0;

                    for (int i = 0; i < totalNumberOfCycles; i++) {
                        Cycle c = mCycleArrayList.get(i);

                        if (c.getTyreCondition() == 1) {
                            numberOfDamagedTyres++;
                        }

                        if (c.getChainCondition() == 1) {
                            numberOfDamagedChains++;
                        }

                        if (c.getCableCondition() == 1) {
                            numberOfDamagedCables++;
                        }

                        if (c.getBrakeCondition() == 1) {
                            numberOfDamagedBrakes++;
                        }

                        if (c.getLubricationCondition() == 1) {
                            numberOfUnlubricatedCycles++;
                        }

                        if (c.getMiscellaneousCondition() == 1) {
                            numberOfMiscellaneousDamages++;
                        }
                    }

                    totalNumberOfCyclesTextView.setText(String.valueOf(totalNumberOfCycles));
                    numberOfAvailableCyclesTextView.setText(String.valueOf(numberOfAvailableCycles));
                    numberOfCyclesInUseTextView.setText(String.valueOf(numberOfBusyCycles));
                    numberOfBrokenCyclesTextView.setText(String.valueOf(numberOfDamagedCycles));
                    numberOfDecommissionedCyclesTextView.setText(String.valueOf(numberOfDecommissionedCycles));

                    numberOfCyclesInFourthBlockTextView.setText(String.valueOf(numberOfCyclesInFourthBlock));
                    numberOfCyclesInDevadanBlockTextView.setText(String.valueOf(numberOfCyclesInDevadanBlock));

                    tyresTextView.setText(String.valueOf(numberOfDamagedTyres));
                    chainTextView.setText(String.valueOf(numberOfDamagedChains));
                    cableTextView.setText(String.valueOf(numberOfDamagedCables));
                    brakeTextView.setText(String.valueOf(numberOfDamagedBrakes));
                    lubricationTextView.setText(String.valueOf(numberOfUnlubricatedCycles));
                    miscellaneousTextView.setText(String.valueOf(numberOfMiscellaneousDamages));

                    mStatisticsProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}