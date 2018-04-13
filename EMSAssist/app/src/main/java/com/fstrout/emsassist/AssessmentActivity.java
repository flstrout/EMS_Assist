package com.fstrout.emsassist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    int questionId= 0;
    String notifyEMS = "Notify EMS";
    TextView questionText;
    Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Assess Subject");
        setSupportActionBar(toolbar);
        context = this;

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
        switch (item.getItemId()) {
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
    private void broadcastEMSAndReturnToMain(){
        new AlertDialog.Builder(AssessmentActivity.this).setTitle("EMS Message").setMessage("EMS broadcasted, help is on the way").setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent goBackToMain = new Intent(context, MainActivity.class);
                startActivity(goBackToMain);
            }
        }).show();
    }
}
