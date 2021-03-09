package com.example.uhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class InitRegPage extends AppCompatActivity {
    private static final String TAG = InitRegPage.class.getSimpleName();
    private FireBaseInfo mFireBaseInfo;
    private CachedThreadPool threadPool;
    private ProgressDialog progressDialog;
    private Handler handler;

    private Toolbar toolbar;

    private EditText mName, mPhone, mEContact, mDietary, mAllergy;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mBirth;
    private Spinner spGender;
    private RadioGroup rgAllergy;
    private RadioButton rbAllergy;

    private RadioGroup rgHeart, rgCancer, rgHereditary;


//    todo implement dynamic recyclerview w/ add and minus
    private RadioGroup rgSurgery;
    private Button btAddSurgery;
    private RecyclerView rcSurgery;
    private RecyclerView.LayoutManager layoutManager;



    private Button msubmit, mSkip;

    private String mBirthHolder, mGenderHolder;

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
                        mDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
            if (!mBirthHolder.equals("")){
                mBirth.setText(mBirthHolder);
            }
            if (!mGenderHolder.equals("")){
                int spinnerpos = sp_gender_adapter.getPosition(mGenderHolder);
                spGender.setSelection(spinnerpos);
            }
        }else{
            mBirthHolder = "";
            mGenderHolder = "";
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("ALLERGY", mAllergy.getVisibility());
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

    private boolean checkfields(){
        return true;
    }
}