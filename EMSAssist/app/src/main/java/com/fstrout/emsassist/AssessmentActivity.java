package com.fstrout.emsassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import io.realm.Realm;

public class AssessmentActivity extends AppCompatActivity {

    Context context;
    public static final int BARCODE_REQUEST_CODE = 100;
    boolean connect;
    String notifyEMS = "Notify EMS";
    TextView questionText;
    Button button1, button2, button3, button4;
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

        if (connect) Toast.makeText(context, notifyEMS, Toast.LENGTH_LONG).show();

        questionText = findViewById(R.id.question_text);
        button1 = findViewById(R.id.selection_one);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button1.getTag();
                displayNextQuestion(tagValue, v);
            }
        });
        button2 = findViewById(R.id.selection_two);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button2.getTag();
                displayNextQuestion(tagValue, v);
            }
        });
        button3 = findViewById(R.id.selection_three);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button3.getTag();
                displayNextQuestion(tagValue, v);
            }
        });
        button4 = findViewById(R.id.selection_four);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tagValue = (int) button4.getTag();
                displayNextQuestion(tagValue, v);
            }
        });

        displayNextQuestion(1, null);
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

                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    button4.setVisibility(View.GONE);
                    break;
                case 2:
                    button1.setText(question.getAnswers().get(0).getAnswerText());
                    button1.setTag(Integer.valueOf(question.getAnswers().get(0).getNextQuestionID()));

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

                // TODO: Do something with the scanned barcode.
                // Lookup medication and add it to the medication list.
                int scanFormat = barcode.format;
                String barcodeData = barcode.displayValue;
                Toast.makeText(AssessmentActivity.this, barcodeData, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
