package com.fstrout.emsassist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserInfo extends AppCompatActivity {
    EditText ageInfo, heightInfo1, heightInfo2, weightInfo, contactNumber, medicationInfo;
    RadioButton femaleRadio, maleRadio;
    public static final int BARCODE_REQUEST_CODE = 100;
    boolean userInfoSaved =false, userInfoSkipped = false;
    Button saveUserInfo, skipUserInfo, scanBarcode;
    Spinner userCategorySpinner;
    ArrayAdapter<String> userCategoryAdapter;
    String userCategory, userGender = "Unknown";
    static final String MALE = "male", FEMALE = "female";
    SharedPreferences userPref ;
    SharedPreferences.Editor userEditor;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    public static HashMap<String, Drug> drugList = new HashMap<>();
    StringBuilder drugName = new StringBuilder();
    // URL to get contacts JSON
    private static String url = "https://hhs-opioid-codeathon.data.socrata.com/resource/9qqv-5nvv.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userPref = getSharedPreferences("UserPref", MODE_PRIVATE);
        userEditor =  userPref.edit();
        ageInfo = findViewById(R.id.age);
        weightInfo = findViewById(R.id.weight);
        heightInfo1 = findViewById(R.id.heightFeet);
        heightInfo2 = findViewById(R.id.heightInches);
        femaleRadio = findViewById(R.id.radioFemale);
        maleRadio = findViewById(R.id.radioMale);
        saveUserInfo = findViewById(R.id.createButton);
        skipUserInfo = findViewById(R.id.skipButton);
        medicationInfo = findViewById(R.id.medication);
        scanBarcode = findViewById(R.id.scanBarcode);
        userCategorySpinner = findViewById(R.id.spinner);
        contactNumber = findViewById(R.id.contactNumber);
        userCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        userCategorySpinner.setAdapter(userCategoryAdapter);
        userCategoryAdapter.add("Regular User");
        userCategoryAdapter.add("First Responder");
        userCategoryAdapter.add("EMT");
        userCategoryAdapter.add("Paramedic");
        userCategoryAdapter.add("Nurse");
        userCategoryAdapter.add("Doctor");
        new UserInfo.GetDrugData().execute();

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
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivityForResult(intent, BARCODE_REQUEST_CODE);
            }
        });
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
        userEditor.putString("age", ageInfo.getText().toString());
        userEditor.putString("weight", weightInfo.getText().toString());
        userEditor.putString("height1", heightInfo1.getText().toString());
        userEditor.putString("height2", heightInfo2.getText().toString());
        userEditor.putString("userCategory", userCategory );
        userEditor.putString("contactNumber", contactNumber.getText().toString() );
        userEditor.putString("medication", medicationInfo.getText().toString() );
        userEditor.putBoolean("userInfoSaved", true);
        userEditor.commit();
        Toast.makeText(this, "New profile created", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");

                // Lookup medication and add it to the medication list.
                String barcodeData = barcode.displayValue;
                Drug drug = drugList.get(barcodeData);
                drugName.append(drug.getPROPRIETARYNAME() + ", ");
                medicationInfo.setText(drugName);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetDrugData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UserInfo.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    jsonStr = jsonStr.replaceAll("\n", "");
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    for(int i=0; i < jsonarray.length(); i++) {
                        Drug drug = new Drug();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String PRODUCTID = getJsonString(jsonobject,"productid" );
                        String PRODUCTNDC = getJsonString(jsonobject,"productndc");
                        String PRODUCTTYPENAME = getJsonString(jsonobject,"producttypename");
                        String PROPRIETARYNAME = getJsonString(jsonobject,"proprietaryname");
                        String PROPRIETARYNAMESUFFIX = getJsonString(jsonobject,"proprietarynames");
                        String NONPROPRIETARYNAME = getJsonString(jsonobject,"nonproprietaryname");
                        String DOSAGEFORMNAME = getJsonString(jsonobject,"dosageformname");

                        drug.setPRODUCTID(PRODUCTID);
                        drug.setPRODUCTNDC(PRODUCTNDC);
                        drug.setPRODUCTTYPENAME(PRODUCTTYPENAME);
                        drug.setPROPRIETARYNAME(PROPRIETARYNAME);
                        drug.setPROPRIETARYNAMESUFFIX(PROPRIETARYNAMESUFFIX);
                        drug.setNONPROPRIETARYNAME(NONPROPRIETARYNAME);
                        drug.setDOSAGEFORMNAME(DOSAGEFORMNAME);
                        drugList.put(PRODUCTNDC, drug);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        public String getJsonString(JSONObject jso, String field) {
            if(jso.isNull(field))
                return null;
            else
                try {
                    return jso.getString(field);
                }
                catch(Exception ex) {
                    return null;
                }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}
