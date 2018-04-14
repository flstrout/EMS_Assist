package com.fstrout.emsassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import io.realm.Realm;


public class AssessmentActivity extends AppCompatActivity {

    Context context;
    public static HashMap<String, Drug> drugList = new HashMap<>();
    ArrayList<String> questionAnswer = new ArrayList<>();

    public static final int BARCODE_REQUEST_CODE = 100;
    boolean connect;
    int questionId = 1;
    int drugCount = 0;
    String notifyEMS = "Notify EMS";
    TextView questionText;
    Button button1, button2, button3, button4;
    EditText inputText;
    private String TAG = MainActivity.class.getSimpleName();
    StringBuilder drugName = new StringBuilder();
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    private static String url = "https://hhs-opioid-codeathon.data.socrata.com/resource/9qqv-5nvv.json";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Assess Subject");
        setSupportActionBar(toolbar);
        context = this;
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        inputText = findViewById(R.id.input_field);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.start:
                        displayNextQuestion(1, null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.alert_level:
                        displayNextQuestion(50, null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.breathing:
                        displayNextQuestion(600, null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.interview:
                        displayNextQuestion(100, null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.head_to_toe:
                        displayNextQuestion(1000, null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Get the connect boolean passed through the Intent.
        Intent intent = getIntent();
        connect = intent.getBooleanExtra(MainActivity.EXTRA_CONNECT, false);


        if (connect){
            Toast.makeText(context, notifyEMS, Toast.LENGTH_LONG).show();
            questionId = 20000;
        }
        questionText = findViewById(R.id.question_text);

        button1 = findViewById(R.id.selection_one);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button1.getTag();
                if(connect){
                   broadcastEMSAndReturnToMain();
                }
                else
                    displayNextQuestion(tagValue, v);
            }
        });
        button2 = findViewById(R.id.selection_two);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button2.getTag();
                if(connect){
                    broadcastEMSAndReturnToMain();
                }
                else displayNextQuestion(tagValue, v);
            }
        });
        button3 = findViewById(R.id.selection_three);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button3.getTag();
                if(connect){
                    broadcastEMSAndReturnToMain();
                }
                else displayNextQuestion(tagValue, v);
            }
        });
        button4 = findViewById(R.id.selection_four);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button4.getTag();
                if (connect) {
                    broadcastEMSAndReturnToMain();
                }
                else displayNextQuestion(tagValue, v);
            }
        });

        new AssessmentActivity.GetDrugData().execute();
        displayNextQuestion(questionId, null);
    }

    private void displayNextQuestion(int questionID, View view) {
        Realm rlm = Realm.getDefaultInstance();
        if (questionID == 100000) {
            scanBarcode(view);
        } else {
            Questions question = rlm.where(Questions.class).equalTo("id", questionID).findFirst();
            questionText.setText(question.getQuestion());
            // Toast.makeText(AssessmentActivity.this, "Number of answers: " + question.getAnswers().size(), Toast.LENGTH_SHORT).show();

            switch (question.getAnswers().size()) {
                case 1:
                    button1.setText(question.getAnswers().get(0).getAnswerText());
                    button1.setTag(Integer.valueOf(question.getAnswers().get(0).getNextQuestionID()));
                    questionAnswer.add(question.getQuestion());
                    questionAnswer.add(question.getAnswers().get(0).getAnswerText());
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    button4.setVisibility(View.GONE);
                    break;
                case 2:
                    button1.setText(question.getAnswers().get(0).getAnswerText());
                    button1.setTag(Integer.valueOf(question.getAnswers().get(0).getNextQuestionID()));
                    questionAnswer.add(question.getQuestion());
                    questionAnswer.add(question.getAnswers().get(0).getAnswerText());
                    button2.setText(question.getAnswers().get(1).getAnswerText());
                    button2.setTag(Integer.valueOf(question.getAnswers().get(1).getNextQuestionID()));

                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.GONE);
                    button4.setVisibility(View.GONE);
                    break;

                case 3:
                    button1.setText(question.getAnswers().get(0).getAnswerText());
                    button1.setTag(Integer.valueOf(question.getAnswers().get(0).getNextQuestionID()));
                    questionAnswer.add(question.getQuestion());
                    questionAnswer.add(question.getAnswers().get(0).getAnswerText());
                    button2.setText(question.getAnswers().get(1).getAnswerText());
                    button2.setTag(Integer.valueOf(question.getAnswers().get(1).getNextQuestionID()));

                    button3.setText(question.getAnswers().get(2).getAnswerText());
                    button3.setTag(Integer.valueOf(question.getAnswers().get(2).getNextQuestionID()));

                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.GONE);
                    break;

                default:
                    button1.setText(question.getAnswers().get(0).getAnswerText());
                    button1.setTag(Integer.valueOf(question.getAnswers().get(0).getNextQuestionID()));
                    questionAnswer.add(question.getQuestion());
                    questionAnswer.add(question.getAnswers().get(0).getAnswerText());
                    button2.setText(question.getAnswers().get(1).getAnswerText());
                    button2.setTag(Integer.valueOf(question.getAnswers().get(1).getNextQuestionID()));

                    button3.setText(question.getAnswers().get(2).getAnswerText());
                    button3.setTag(Integer.valueOf(question.getAnswers().get(2).getNextQuestionID()));

                    button4.setText(question.getAnswers().get(3).getAnswerText());
                    button4.setTag(Integer.valueOf(question.getAnswers().get(3).getNextQuestionID()));

                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button4.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;

        switch (item.getItemId()) {

            case R.id.medical_history:
                Toast.makeText(context, "Launch Medical History", Toast.LENGTH_LONG).show();
                return true;
            case R.id.chief_complaint:
                Toast.makeText(context, "Launch Chief Complaint Alert", Toast.LENGTH_LONG).show();
                return true;
            case R.id.get_help:
                Toast.makeText(context, notifyEMS, Toast.LENGTH_LONG).show();
                return true;
            case R.id.view_map:
                Toast.makeText(context, "View Map", Toast.LENGTH_LONG).show();
                return true;
            case R.id.take_video:
                Toast.makeText(context, "Launch Camera", Toast.LENGTH_LONG).show();
                return true;
            case R.id.take_picture:
                Toast.makeText(context, "Launch Camera", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Barcode Stuff
    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent, BARCODE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");

                // Lookup medication and add it to the medication list.
                int scanFormat = barcode.format;
                String barcodeData = barcode.displayValue;
                Drug drug = drugList.get(barcodeData);
                drugName.append(drug.getPROPRIETARYNAME() + ", ");
                Toast.makeText(AssessmentActivity.this, barcodeData, Toast.LENGTH_LONG).show();
                Toast.makeText(AssessmentActivity.this, drug.getPROPRIETARYNAME(), Toast.LENGTH_LONG).show();
                inputText.setVisibility(View.VISIBLE);
                inputText.setText(drugName);
                questionAnswer.add("Medicine list: " + drugName);
                drugCount ++;
                if(drugCount >= 3 ){
                Snackbar.make(findViewById(android.R.id.content), "Warning! Possible opioid overdose!", Snackbar.LENGTH_LONG)
                        .setAction("DISMISS", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // save operations
                            }
                        })
                        .setActionTextColor(Color.RED)
                        .setDuration(20000).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void broadcastEMSAndReturnToMain(){
        new AlertDialog.Builder(AssessmentActivity.this).setTitle("EMS Message").setMessage("EMS broadcasted, help is on the way").setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
//                Intent goBackToMain = new Intent(context, MainActivity.class);
//                startActivity(goBackToMain);
            }
        }).show();
    }

    private class GetDrugData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AssessmentActivity.this);
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
