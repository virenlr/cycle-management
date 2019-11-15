package com.lukehere.app.cycle.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.pojo.Cycle;
import com.lukehere.app.cycle.pojo.History;
import com.lukehere.app.cycle.pojo.Student;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class CycleEditorActivity extends AppCompatActivity {
    private Cycle mCycle;
    private Student mStudent;
    private ProgressBar mStatusProgressBar;

    private SwitchCompat mAvailabilitySwitch;
    private Spinner mLocationSpinner;
    private SwitchCompat mTyresSwitch;
    private SwitchCompat mChainSwitch;
    private SwitchCompat mCableSwitch;
    private SwitchCompat mBrakeSwitch;
    private SwitchCompat mLubricationSwitch;
    private SwitchCompat mMiscellaneousSwitch;
    private TextView mCycleCreationDate;
    private SwitchCompat mCycleDecommissionSwitch;
    private TextView mCycleDecommissionedOnHeader;
    private TextView mCycleDecommissionDate;

    private LinearLayout mStudentInformationLinearLayout;
    private EditText mRegistrationNumberEditText, mFullNameEditText, mPhoneNumberEditText;

    private boolean cycleOriginallyAssigned = false;

    private FirebaseAuth mAuth;
    private DocumentReference cycleReference;
    private DocumentReference studentReference;
    private CollectionReference historyReference;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setInformation();
                break;
            case R.id.nav_done:
                setInformation();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_editor);

        Intent i = getIntent();
        mCycle = i.getParcelableExtra("cycle");
        mStudent = new Student();

        getSupportActionBar().setTitle("Cycle " + String.valueOf(mCycle.getCycleNumber()));

        mStatusProgressBar = findViewById(R.id.status_progress_bar);

        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cycleReference = mDb.collection("cycles").document(mCycle.getCycleId());
        studentReference = mDb.collection("cycles").document(mCycle.getCycleId()).collection("student").document("student");
        historyReference = mDb.collection("history");

        mAvailabilitySwitch = findViewById(R.id.editor_availability_switch);
        mLocationSpinner = findViewById(R.id.editor_location_spinner);
        mTyresSwitch = findViewById(R.id.editor_tyres_switch);
        mChainSwitch = findViewById(R.id.editor_chain_switch);
        mCableSwitch = findViewById(R.id.editor_cable_switch);
        mBrakeSwitch = findViewById(R.id.editor_brake_switch);
        mLubricationSwitch = findViewById(R.id.editor_lubrication_switch);
        mMiscellaneousSwitch = findViewById(R.id.editor_miscellaneous_switch);
        mCycleCreationDate = findViewById(R.id.cycle_creation_date);
        mCycleDecommissionSwitch = findViewById(R.id.editor_decomission_switch);
        mCycleDecommissionedOnHeader = findViewById(R.id.decommissioned_on_header);
        mCycleDecommissionDate = findViewById(R.id.cycle_decommission_date);

        mStudentInformationLinearLayout = findViewById(R.id.student_information_layout);
        mRegistrationNumberEditText = findViewById(R.id.editor_student_registration_number);
        mFullNameEditText = findViewById(R.id.editor_student_name);
        mPhoneNumberEditText = findViewById(R.id.editor_student_phone);

        mAvailabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCycle.setStatus(1);
                    getAvailabilityInformation();
                } else {
                    mCycle.setStatus(0);
                    getAvailabilityInformation();
                }
            }
        });

        getInformation();
    }

    private void getInformation() {
        if (mCycle != null) {

            cycleOriginallyAssigned = mCycle.getStatus() == 1;

            getAvailabilityInformation();

            if (mCycle.getTyreCondition() == 0) {
                mTyresSwitch.setChecked(false);
            } else {
                mTyresSwitch.setChecked(true);
            }

            if (mCycle.getChainCondition() == 0) {
                mChainSwitch.setChecked(false);
            } else {
                mChainSwitch.setChecked(true);
            }

            if (mCycle.getCableCondition() == 0) {
                mCableSwitch.setChecked(false);
            } else {
                mCableSwitch.setChecked(true);
            }

            if (mCycle.getBrakeCondition() == 0) {
                mBrakeSwitch.setChecked(false);
            } else {
                mBrakeSwitch.setChecked(true);
            }

            if (mCycle.getLubricationCondition() == 0) {
                mLubricationSwitch.setChecked(false);
            } else {
                mLubricationSwitch.setChecked(true);
            }

            if (mCycle.getMiscellaneousCondition() == 0) {
                mMiscellaneousSwitch.setChecked(false);
            } else {
                mMiscellaneousSwitch.setChecked(true);
            }

            mCycleCreationDate.setText(mCycle.getCreationDate());

            if (mCycle.getDecommissionedStatus() == 0) {
                mCycleDecommissionedOnHeader.setVisibility(View.GONE);
                mCycleDecommissionDate.setVisibility(View.GONE);
                mCycleDecommissionSwitch.setChecked(false);
            } else {
                mAvailabilitySwitch.setEnabled(false);
                mLocationSpinner.setVisibility(View.GONE);

                mTyresSwitch.setEnabled(false);
                mChainSwitch.setEnabled(false);
                mCableSwitch.setEnabled(false);
                mBrakeSwitch.setEnabled(false);
                mMiscellaneousSwitch.setEnabled(false);
                mLubricationSwitch.setEnabled(false);

                mCycleDecommissionedOnHeader.setVisibility(View.VISIBLE);
                mCycleDecommissionDate.setVisibility(View.VISIBLE);
                mCycleDecommissionDate.setText(mCycle.getDecommissionedDate());
                mCycleDecommissionSwitch.setChecked(true);
            }
        }
    }

    private void getAvailabilityInformation() {
        if (mCycle.getStatus() == 1) {
            mAvailabilitySwitch.setChecked(true);
            mLocationSpinner.setVisibility(View.GONE);
            mStudentInformationLinearLayout.setVisibility(View.VISIBLE);

            mStatusProgressBar.setVisibility(View.VISIBLE);
            studentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        mStudent = documentSnapshot.toObject(Student.class);

                        if (mStudent != null) {
                            mRegistrationNumberEditText.setText(mStudent.getRegistrationNumber());
                        }
                        if (mStudent != null) {
                            mFullNameEditText.setText(mStudent.getFullName());
                        }
                        if (mStudent != null) {
                            mPhoneNumberEditText.setText(mStudent.getPhoneNumber());
                        }

                    }
                    mStatusProgressBar.setVisibility(View.GONE);
                }
            });

        } else {
            mAvailabilitySwitch.setChecked(false);
            mLocationSpinner.setVisibility(View.VISIBLE);
            mStudentInformationLinearLayout.setVisibility(View.GONE);

            String[] locations = getResources().getStringArray(R.array.location_list);

            for (int i = 0; i < locations.length; i++) {
                if (locations[i].equals(mCycle.getLocation())) {
                    mLocationSpinner.setSelection(i);
                }
            }
        }
    }

    private void setInformation() {

        if (mCycle.getStatus() == 0 && cycleOriginallyAssigned) {
            History history = new History();
            history.setCycleNumber(mCycle.getCycleNumber());
            history.setBorrowDate(mStudent.getBorrowTime());
            history.setReturnDate(String.valueOf(new Date().getTime()));
            history.setSecurityEmailAddress(mAuth.getCurrentUser().getEmail());
            history.setRegistrationNumber(mStudent.getRegistrationNumber());
            history.setFullName(mStudent.getFullName());
            history.setPhoneNumber(mStudent.getPhoneNumber());
            history.setBorrowedFrom(mCycle.getLocation());
            String location = mLocationSpinner.getSelectedItem().toString();
            history.setReturnedTo(location);
            historyReference.add(history);

            mCycle.setLocation(location);
            mStudent = new Student();
        } else if (mCycle.getStatus() == 0 && !cycleOriginallyAssigned) {
            String location = mLocationSpinner.getSelectedItem().toString();
            mCycle.setLocation(location);
            mStudent = new Student();
        } else if (mCycle.getStatus() == 1) {
            String registrationNumber = mRegistrationNumberEditText.getText().toString().trim();
            String fullName = mFullNameEditText.getText().toString().trim();
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();

            mStudent.setRegistrationNumber(registrationNumber);
            mStudent.setFullName(fullName);
            mStudent.setPhoneNumber(phoneNumber);

            if (!cycleOriginallyAssigned) {
                mStudent.setBorrowTime(String.valueOf(new Date().getTime()));
            }
        }

        if (mTyresSwitch.isChecked()) {
            mCycle.setTyreCondition(1);
        } else {
            mCycle.setTyreCondition(0);
        }

        if (mChainSwitch.isChecked()) {
            mCycle.setChainCondition(1);
        } else {
            mCycle.setChainCondition(0);
        }

        if (mCableSwitch.isChecked()) {
            mCycle.setCableCondition(1);
        } else {
            mCycle.setCableCondition(0);
        }

        if (mBrakeSwitch.isChecked()) {
            mCycle.setBrakeCondition(1);
        } else {
            mCycle.setBrakeCondition(0);
        }

        if (mLubricationSwitch.isChecked()) {
            mCycle.setLubricationCondition(1);
        } else {
            mCycle.setLubricationCondition(0);
        }

        if (mMiscellaneousSwitch.isChecked()) {
            mCycle.setMiscellaneousCondition(1);
        } else {
            mCycle.setMiscellaneousCondition(0);
        }

        if (mCycleDecommissionSwitch.isChecked()) {
            mCycle.setStatus(0);
            mStudent = new Student();

            mCycle.setTyreCondition(0);
            mCycle.setChainCondition(0);
            mCycle.setCableCondition(0);
            mCycle.setBrakeCondition(0);
            mCycle.setMiscellaneousCondition(0);
            mCycle.setLubricationCondition(0);

            mCycle.setDecommissionedStatus(1);
            long decommissionTime = new Date().getTime();
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            mCycle.setDecommissionedDate(dateFormat.format(decommissionTime));

        } else {
            mCycle.setDecommissionedStatus(0);
            mCycle.setDecommissionedDate("");
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

        cycleReference.set(mCycle);
        studentReference.set(mStudent);
    }

    @Override
    public void onBackPressed() {
        setInformation();
    }
}