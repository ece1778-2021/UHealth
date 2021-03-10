package com.example.uhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class InitRegPage extends AppCompatActivity {
    private static final String TAG = InitRegPage.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;
    private CachedThreadPool threadPool;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Handler handler;

    private Toolbar toolbar;

    private EditText mName, mPhone, mEContact, mDietary, mAllergy, mDrug, mSmokeinputET, mAlcoholinputET;
    private CheckBox mSmokeCurrent, mAlcoholCurrent;
    private DatePickerDialog.OnDateSetListener mBirthdayListener, mSmokeStartListener, mSmokeEndListener, mAlcoholStartListener, mAlcoholEndListener;
    private TextView mBirth, mSmokeStart, mSmokeEnd, mAlcoholStart, mAlcoholEnd, mSmokeDes, mAlcoholDes;
    private Spinner spGender;
    private RadioGroup rgAllergy, rgDrug;
    private RadioButton rbAllergy, rbSurgery, rbTransfusion, rbSmoke, rbAlcohol, rbDrug;

    private RadioGroup rgHeart, rgCancer, rgHereditary;



    private enum dialogOption {
        SURGERY,
        TRANSFUSION
    };

    private RadioGroup rgSurgery, rgTransfusion, rgSmoke, rgAlcohol;
    private Button btAddSurgery, btAddTransfusion;
    private RecyclerView rvSurgery, rvTransfusion;
    private RecyclerView.LayoutManager layoutManagerdialog1, layoutManagerdialog1tf;
    private RelativeLayout rhiddenSurgery, rhiddenTransfusion;
    private rvadapter_initreg_dialog1 mRVAdapterdialog1, mRVAdapterdialog1tf;

    private RelativeLayout layoutSmoke, layoutAlcohol;

    private Button msubmit, mSkip, mAddSurgery, mAddTransfusion;

    private String mBirthHolder, mGenderHolder, mSmokeStartHolder, mSmokeEndHolder, mAlcoholStartHolder, mAlcoholEndHolder;
    private int mSmokeinputHolder, mAlcoholinputHolder;
    private ArrayList<String> mSurgeryDates, mSurgeryNames, mTransfusionDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_reg_page);
        toolbar = findViewById(R.id.initreg_toolbar);
        setSupportActionBar(toolbar);

        mFireBaseInfo = new FireBaseInfo();
        threadPool = CachedThreadPool.getInstance();
        progressDialog = new ProgressDialog(this);

        mName = findViewById(R.id.initreg_ET_name);
        mPhone = findViewById(R.id.initreg_ET_phone);
        mEContact = findViewById(R.id.initreg_ET_EContact);
        mDietary = findViewById(R.id.initreg_ET_dietary);
        mAllergy = findViewById(R.id.initreg_ET_allergy);
        mBirth = findViewById(R.id.initreg_TV_showbirth);
        mBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InitRegPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mBirthdayListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mBirthdayListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                mBirthHolder = year +"/" + month + "/"+dayOfMonth;
                mBirth.setText(mBirthHolder);
            }
        };
        spGender = findViewById(R.id.initreg_SP_gender);
        ArrayAdapter<CharSequence> sp_gender_adapter = ArrayAdapter.createFromResource(this, R.array.gender_option, android.R.layout.simple_spinner_item);
        sp_gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(sp_gender_adapter);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = parent.getItemAtPosition(position).toString();
                mGenderHolder = gender;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rgAllergy = findViewById(R.id.initreg_RG_allergy);

        rgHeart = findViewById(R.id.initreg_RG_heart);
        rgCancer = findViewById(R.id.initreg_RG_cancer);
        rgHereditary = findViewById(R.id.initreg_RG_hereditary);

        rhiddenSurgery = findViewById(R.id.initreg_surgeryhidden);
        rhiddenTransfusion = findViewById(R.id.initreg_transfusionhidden);

        layoutSmoke = findViewById(R.id.smoke_subview);
        mSmokeEnd = layoutSmoke.findViewById(R.id.subview1_enddate);
        mSmokeStart = layoutSmoke.findViewById(R.id.subview1_startdate);

        mDrug = findViewById(R.id.initreg_ET_drug);
        rgDrug = findViewById(R.id.initreg_RG_drug);

        layoutAlcohol = findViewById(R.id.alcohol_subview);
        mAlcoholEnd = layoutAlcohol.findViewById(R.id.subview1_enddate);
        mAlcoholStart = layoutAlcohol.findViewById(R.id.subview1_startdate);

        msubmit = findViewById(R.id.initreg_submit);
        mSkip = findViewById(R.id.initreg_toolbar_BT_skip);
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitRegPage.this, ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });
        if (savedInstanceState!=null){
            mBirthHolder = savedInstanceState.getString("BIRTHDAY", "");
            mGenderHolder = savedInstanceState.getString("GENDER", "Male");

            mAllergy.setVisibility(savedInstanceState.getInt("ALLERGY", View.GONE));
            mDrug.setVisibility(savedInstanceState.getInt("DRUG", View.GONE));

            rhiddenSurgery.setVisibility(savedInstanceState.getInt("SURGERY", View.GONE));
            rhiddenTransfusion.setVisibility(savedInstanceState.getInt("TRANSFUSION", View.GONE));

            mSurgeryNames = savedInstanceState.getStringArrayList("SURGERYNAMES");
            mSurgeryDates = savedInstanceState.getStringArrayList("SURGERYDATES");
            mTransfusionDates = savedInstanceState.getStringArrayList("TRANSFUSIONDATES");

            layoutSmoke.setVisibility(savedInstanceState.getInt("SMOKE", View.GONE));
            mSmokeEnd.setVisibility(savedInstanceState.getInt("SMOKECURRENT", View.GONE));
            mSmokeStartHolder = savedInstanceState.getString("SMOKESTART", "");
            mSmokeEndHolder = savedInstanceState.getString("SMOKEEND", "");
            mSmokeinputHolder = savedInstanceState.getInt("SMOKEINPUT", 0);

            layoutAlcohol.setVisibility(savedInstanceState.getInt("ALCOHOL", View.GONE));
            mAlcoholEnd.setVisibility(savedInstanceState.getInt("ALCOHOLCURRENT", View.GONE));
            mAlcoholStartHolder = savedInstanceState.getString("ALCOHOLSTART", "");
            mAlcoholEndHolder = savedInstanceState.getString("ALCOHOLEND", "");
            mAlcoholinputHolder = savedInstanceState.getInt("ALCOHOLINPUT", 0);


            if (!mBirthHolder.equals("")){
                mBirth.setText(mBirthHolder);
            }
            if (!mGenderHolder.equals("")){
                int spinnerpos = sp_gender_adapter.getPosition(mGenderHolder);
                spGender.setSelection(spinnerpos);
            }
            if (!mSmokeStartHolder.equals("")){
                mSmokeStart.setText(mSmokeStartHolder);
            }
            if (!mSmokeEndHolder.equals("")){
                mSmokeEnd.setText(mSmokeEndHolder);
            }
            if (!mAlcoholStartHolder.equals("")){
                mAlcoholStart.setText(mAlcoholStartHolder);
            }
            if (!mAlcoholEndHolder.equals("")){
                mAlcoholEnd.setText(mAlcoholEndHolder);
            }


        }else{
//            first time
            mBirthHolder = "";
            mGenderHolder = "";

            mSmokeStartHolder = "";
            mSmokeEndHolder = "";
            mSmokeinputHolder = 0;

            mAlcoholStartHolder = "";
            mAlcoholEndHolder = "";
            mAlcoholinputHolder = 0;

            mSurgeryNames = new ArrayList<>();
            mSurgeryDates = new ArrayList<>();
            mTransfusionDates = new ArrayList<>();
        }

        initViewSurgery();
        initViewTransfusion();
        initViewSmoke();
        initViewAlcohol();
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("ALLERGY", mAllergy.getVisibility());
        outState.putInt("DRUG", mDrug.getVisibility());
        outState.putInt("SURGERY", rhiddenSurgery.getVisibility());
        outState.putInt("TRANSFUSION", rhiddenTransfusion.getVisibility());

        outState.putInt("SMOKE", layoutSmoke.getVisibility());
        outState.putInt("SMOKECURRENT", mSmokeEnd.getVisibility());
        outState.putString("SMOKESTART", mSmokeStartHolder);
        outState.putString("SMOKEEND", mSmokeEndHolder);
        outState.putInt("SMOKEINPUT", mSmokeinputHolder);

        outState.putInt("ALCOHOL", layoutAlcohol.getVisibility());
        outState.putInt("ALCOHOLCURRENT", mAlcoholEnd.getVisibility());
        outState.putString("ALCOHOLSTART", mAlcoholStartHolder);
        outState.putString("ALCOHOLEND", mAlcoholEndHolder);
        outState.putInt("ALCOHOLINPUT", mAlcoholinputHolder);

        outState.putStringArrayList("SURGERYNAMES", mSurgeryNames);
        outState.putStringArrayList("SURGERYDATES", mSurgeryDates);
        outState.putStringArrayList("TRANSFUSIONDATES", mTransfusionDates);

        if (!mBirthHolder.equals("")){
            outState.putString("BIRTHDAY", mBirthHolder);
        }
        if (!mGenderHolder.equals("")){
            outState.putString("GENDER", mGenderHolder);
        }

        super.onSaveInstanceState(outState);

    }

    public void selectAllergy(View view) {
        int radioid = rgAllergy.getCheckedRadioButtonId();
        rbAllergy = findViewById(radioid);
        if (rbAllergy.getText().toString().equals("Yes")){
            mAllergy.setVisibility(View.VISIBLE);
        }else{
            mAllergy.setVisibility(View.GONE);
            mAllergy.setText("");
        }
    }

    public void selectDrug(View view) {
        int radioid = rgDrug.getCheckedRadioButtonId();
        rbDrug = findViewById(radioid);
        if (rbDrug.getText().toString().equals("Yes")){
            mDrug.setVisibility(View.VISIBLE);
        }else{
            mDrug.setVisibility(View.GONE);
            mDrug.setText("");
        }
    }

    public void selectSmoke(View view) {
        int smokeid = rgSmoke.getCheckedRadioButtonId();
        rbSmoke = findViewById(smokeid);
        if (rbSmoke.getText().toString().equals("Yes")){
            layoutSmoke.setVisibility(View.VISIBLE);
        }else{
            layoutSmoke.setVisibility(View.GONE);
            mSmokeCurrent.setChecked(true);
            mSmokeinputHolder = 0;
            mSmokeStartHolder = "";
            mSmokeEndHolder = "";

            mSmokeStart.setText("Start");
            mSmokeEnd.setText("End");
            mSmokeinputET.setText("");
        }
    }

    public void selectAlcohol(View view){
        int radioid = rgAlcohol.getCheckedRadioButtonId();
        rbAlcohol = findViewById(radioid);
        if (rbAlcohol.getText().toString().equals("Yes")){
            layoutAlcohol.setVisibility(View.VISIBLE);
        }else{
            layoutAlcohol.setVisibility(View.GONE);
            mAlcoholCurrent.setChecked(true);
            mAlcoholinputHolder = 0;
            mAlcoholStartHolder = "";
            mAlcoholEndHolder = "";

            mAlcoholStart.setText("Start");
            mAlcoholEnd.setText("End");
            mAlcoholinputET.setText("");
        }

    }


    private void initViewAlcohol() {
        rgAlcohol = findViewById(R.id.initreg_RG_alcohol);
        mAlcoholCurrent = layoutAlcohol.findViewById(R.id.subview1_check);
        mAlcoholDes = layoutAlcohol.findViewById(R.id.subview1_des);
        mAlcoholDes.setText("Number of drinks per week");
        mAlcoholinputET = layoutAlcohol.findViewById(R.id.subview1_input);
        mAlcoholCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlcoholCurrent.isChecked()){
                    mAlcoholEnd.setVisibility(View.GONE);
                    mAlcoholEnd.setText("End");
                    mAlcoholEndHolder = "";
                }else{
                    mAlcoholEnd.setVisibility(View.VISIBLE);
                }
            }
        });

        mAlcoholStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InitRegPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mAlcoholStartListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mAlcoholStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                mAlcoholStartHolder = year +"/" + month + "/"+dayOfMonth;
                mAlcoholStart.setText(mAlcoholStartHolder);
            }
        };

        mAlcoholEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InitRegPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mAlcoholEndListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mAlcoholEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                mAlcoholEndHolder = year +"/" + month + "/"+dayOfMonth;
                mAlcoholEnd.setText(mAlcoholEndHolder);
            }
        };
    }


    private void initViewSmoke() {
        rgSmoke = findViewById(R.id.initreg_RG_smoke);
        mSmokeCurrent = layoutSmoke.findViewById(R.id.subview1_check);
        mSmokeDes = layoutSmoke.findViewById(R.id.subview1_des);
        mSmokeDes.setText("Number of cigarretes per day");
        mSmokeinputET = layoutSmoke.findViewById(R.id.subview1_input);
        mSmokeCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSmokeCurrent.isChecked()){
                    mSmokeEnd.setVisibility(View.GONE);
                    mSmokeEnd.setText("End");
                    mSmokeEndHolder = "";
                }else{
                    mSmokeEnd.setVisibility(View.VISIBLE);
                }
            }
        });

        mSmokeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InitRegPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mSmokeStartListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mSmokeStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                mSmokeStartHolder = year +"/" + month + "/"+dayOfMonth;
                mSmokeStart.setText(mSmokeStartHolder);
            }
        };

        mSmokeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InitRegPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        mSmokeEndListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mSmokeEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                mSmokeEndHolder = year +"/" + month + "/"+dayOfMonth;
                mSmokeEnd.setText(mSmokeEndHolder);
            }
        };
    }

    private void initViewTransfusion() {
        rgTransfusion = findViewById(R.id.initreg_RG_transfusion);
        rvTransfusion = findViewById(R.id.initreg_RV_transfusion);
        btAddTransfusion = findViewById(R.id.initreg_BT_addtransfusion);
        layoutManagerdialog1tf = new GridLayoutManager(this, 1);

        mRVAdapterdialog1tf = new rvadapter_initreg_dialog1(new removeRV() {
            @Override
            public void rfSurgery(int position) {
                mSurgeryNames.remove(position);
                mSurgeryDates.remove(position);
            }

            @Override
            public void rfTransfusion(int position) {
                mTransfusionDates.remove(position);
            }
        }, mTransfusionDates);

        rvTransfusion.setLayoutManager(layoutManagerdialog1tf);
        rvTransfusion.setAdapter(mRVAdapterdialog1tf);
        rvTransfusion.setHasFixedSize(true);

        btAddTransfusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builddialog(dialogOption.TRANSFUSION);
            }
        });
    }

    private void initViewSurgery() {
        rgSurgery = findViewById(R.id.initreg_RG_surgery);
        rvSurgery = findViewById(R.id.initreg_RV_surgery);
        btAddSurgery = findViewById(R.id.initreg_BT_addsurgery);
        layoutManagerdialog1 = new GridLayoutManager(this, 1);

        mRVAdapterdialog1 = new rvadapter_initreg_dialog1(new removeRV() {
            @Override
            public void rfSurgery(int position) {
                mSurgeryNames.remove(position);
                mSurgeryDates.remove(position);
            }

            @Override
            public void rfTransfusion(int position) {
                mTransfusionDates.remove(position);
            }
        }, mSurgeryNames, mSurgeryDates);
        rvSurgery.setLayoutManager(layoutManagerdialog1);
        rvSurgery.setAdapter(mRVAdapterdialog1);
        rvSurgery.setHasFixedSize(true);

        btAddSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builddialog(dialogOption.SURGERY);
            }
        });
    }

    private void builddialog(dialogOption val) {
        dialog = new Dialog(this);
        ImageButton mCancel, mAdd;
        EditText mName, mDate;
        dialog.setContentView(R.layout.dialog_initreg_type1);
        mName = dialog.findViewById(R.id.initreg_dialog1_name);
        mDate = dialog.findViewById(R.id.initreg_dialog1_date);
        mCancel = dialog.findViewById(R.id.initreg_dialog1_cancel);
        mAdd = dialog.findViewById(R.id.initreg_dialog1_add);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        switch (val){
            case SURGERY:
                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = mName.getText().toString();
                        String date = mDate.getText().toString();
                        if (name.equals("")){
                            mName.setError("Cant be empty");
                            return;
                        }
                        if (date.equals("")){
                            mDate.setError("Cant be empty");
                            return;
                        }
                        mSurgeryNames.add(name);
                        mSurgeryDates.add(date);
                        mRVAdapterdialog1.notifyItemInserted(mSurgeryNames.size()-1);
                        dialog.dismiss();
                    }
                });
                break;
            case TRANSFUSION:
                mName.setVisibility(View.GONE);
                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = mDate.getText().toString();
                        if (date.equals("")){
                            mDate.setError("Cant be empty");
                        }
                        mTransfusionDates.add(date);
                        mRVAdapterdialog1tf.notifyItemInserted(mTransfusionDates.size()-1);
                        dialog.dismiss();
                    }
                });
                break;
            default:
                Log.e(TAG, "Build Dialog Option Incorrect");
        }
        dialog.show();

    }

    public void selectTransfusion(View view) {
        int tfid = rgTransfusion.getCheckedRadioButtonId();
        rbTransfusion = findViewById(tfid);
        if (rbTransfusion.getText().toString().equals("Yes")){
            rhiddenTransfusion.setVisibility(View.VISIBLE);
        }else{
            mTransfusionDates.clear();
            mRVAdapterdialog1tf.notifyDataSetChanged();
            rhiddenTransfusion.setVisibility(View.GONE);
        }
    }

    public void selectSurgery(View view) {
        int surgeryid = rgSurgery.getCheckedRadioButtonId();
        rbSurgery = findViewById(surgeryid);
        if (rbSurgery.getText().toString().equals("Yes")){
            rhiddenSurgery.setVisibility(View.VISIBLE);
        }else{
            mSurgeryNames.clear();
            mSurgeryDates.clear();
            mRVAdapterdialog1.notifyDataSetChanged();
            rhiddenSurgery.setVisibility(View.GONE);
        }
    }

    public interface removeRV{
        void rfSurgery(int position);
        void rfTransfusion(int position);
    }
}