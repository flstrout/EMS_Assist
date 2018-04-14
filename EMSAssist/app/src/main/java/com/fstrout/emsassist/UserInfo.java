package com.fstrout.emsassist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

public class UserInfo extends AppCompatActivity {
    EditText ageInfo, heightInfo1, heightInfo2, weightInfo;
    RadioButton femaleRadio, maleRadio;
    boolean userInfoSaved =false, userInfoSkipped = false;
    Button saveUserInfo, skipUserInfo;
    Spinner userCategorySpinner;
    ArrayAdapter<String> userCategoryAdapter;
    String userCategory, userGender = "Unknown";
    static final String MALE = "male", FEMALE = "female";
    SharedPreferences userPref ;
    SharedPreferences.Editor userEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userPref = getSharedPreferences("UserPref", MODE_PRIVATE);
        userEditor =  userPref.edit();
        ageInfo = findViewById(R.id.ageInfo);
        weightInfo = findViewById(R.id.weightInfo);
        heightInfo1 = findViewById(R.id.heightInfo1);
        heightInfo2 = findViewById(R.id.heightInfo2);
        femaleRadio = findViewById(R.id.femaleRadio);
        maleRadio = findViewById(R.id.maleRadio);
        saveUserInfo = findViewById(R.id.save_user_info);
        skipUserInfo = findViewById(R.id.skip_user_info);
        userCategorySpinner = findViewById(R.id.userCategory);
        userCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        userCategorySpinner.setAdapter(userCategoryAdapter);
        userCategoryAdapter.add("Regular User");
        userCategoryAdapter.add("First Responder");
        userCategoryAdapter.add("EMT");
        userCategoryAdapter.add("Paramedic");
        userCategoryAdapter.add("Nurse");
        userCategoryAdapter.add("Doctor");
        userCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userCategory = userCategoryAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(femaleRadio.isChecked()){
            userGender = FEMALE;
        }
        if(maleRadio.isChecked()){
            userGender = MALE;
        }
        saveUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
        skipUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipUserInfo();
            }
        });


    }
    private void saveUserInfo(){
        userEditor.putString("gender", userGender);
        userEditor.putInt("age", Integer.parseInt(ageInfo.getText().toString()));
        userEditor.putFloat("weight", Float.parseFloat(weightInfo.getText().toString()));
        userEditor.putFloat("height1", Float.parseFloat(heightInfo1.getText().toString()));
        userEditor.putFloat("height2", Float.parseFloat(heightInfo2.getText().toString()));
        userEditor.putString("userCategory", userCategory );
        userEditor.putBoolean("userInfoSaved", true);
        userEditor.commit();
        Toast.makeText(this, "User Information Saved", Toast.LENGTH_LONG).show();
        finish();
    }
    private void skipUserInfo(){
        new AlertDialog.Builder(UserInfo.this).setTitle("Message").setMessage("You sure you want to skip putting user information").setNegativeButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userEditor.putBoolean("userInfoSkipped", true);
                userEditor.commit();
                finish();
            }
        }).show();
    }


}
