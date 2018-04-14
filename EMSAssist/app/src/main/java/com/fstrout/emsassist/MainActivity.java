package com.fstrout.emsassist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Context context;
    private static final int CAMERA_PERMISSION_REQUEST = 5000;
    private static final String TAG = "MainActivity";
    Realm realm;
    public static final String EXTRA_CONNECT = "com.fstrout.emsassist.CONNECT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Realm.init(context);
        realm = Realm.getDefaultInstance();

        RealmResults<Questions> questions = realm.where(Questions.class).findAll();

        createDefaults();
        listQuestions();
        ImageButton startAssessment = findViewById(R.id.no_ems_button);
        startAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAssessment(false);
            }
        });

        ImageButton connectWithEMS = findViewById(R.id.ems_button);
        connectWithEMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAssessment(true);
            }
        });

        ImageButton call911 = findViewById(R.id.call_button);
        call911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place911Call();
            }
        });

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, CAMERA_PERMISSION_REQUEST);
        }
    }

    private void createDefaults() {
        RealmResults<Questions> questions = realm.where(Questions.class).findAll();

        if (questions.size() == 0) {
            Log.d("CreateDefaults", "Create and Save Default Questions to Realm.");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmList<Questions> defaultQuestions = Questions.createDefaults();

                    Log.e("defaultQuestion", "Size: " + defaultQuestions.size());
                    Log.d(TAG, "Saving " + defaultQuestions.size() + "to the Realm database.");
                    realm.copyToRealm(defaultQuestions);
                }
            });

        } else {
            Log.d("CreateDefaults", "Aborted! Database already has records.");
        }
    }

    private void listQuestions() {
        RealmResults<Questions> results = realm.where(Questions.class).findAll();
        for (Questions question : results) {
            Log.w("Question", question.getQuestion());
        }
    }

    private void launchAssessment(boolean connect) {
        Intent intent = new Intent(context, AssessmentActivity.class);
        intent.putExtra(EXTRA_CONNECT, connect);
        startActivity(intent);
    }

    private void place911Call() {
        Log.d("Call911", "Button Pressed");
        Intent intent = new Intent(Intent.ACTION_DIAL); // ACTION_CALL requires permission.
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);
    }
}
