package com.fstrout.emsassist;

import android.util.Log;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fstrout on 3/25/18.
 *
 */

public class Questions extends RealmObject {

    @PrimaryKey
    private int id;
    private String question;
    private boolean inputField;
    private RealmList<Answers> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean inputFieldDisplayed() {
        return inputField;
    }

    public void displayInputField(boolean inputField) {
        this.inputField = inputField;
    }

    public RealmList<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(RealmList<Answers> answers) {
        this.answers = answers;
    }

    private static Questions createQuestion(int id, String questionText, boolean displayInput,
                                     String ans1Text, int ans1Next, String ans2Text, int ans2Next,
                                     String ans3Text, int ans3Next, String ans4Text, int ans4Next) {
        Questions question = new Questions();
        RealmList<Answers> answerList = new RealmList<>();
        Answers answer1 = new Answers();
        Answers answer2 = new Answers();
        Answers answer3 = new Answers();
        Answers answer4 = new Answers();

        question.setId(id);
        question.setQuestion(questionText);
        question.displayInputField(displayInput);

        // Answers
        if (!ans1Text.isEmpty()) {
            answer1.setId(id + 1);
            answer1.setParentID(id);
            answer1.setAnswerText(ans1Text);
            answer1.setNextQuestionID(ans1Next);
            answerList.add(answer1);
        }

        if (!ans2Text.isEmpty()) {
            answer2.setId(id + 2);
            answer2.setParentID(id);
            answer2.setAnswerText(ans2Text);
            answer2.setNextQuestionID(ans2Next);
            answerList.add(answer2);
        }

        if (!ans3Text.isEmpty()) {
            answer3.setId(id + 3);
            answer3.setParentID(id);
            answer3.setAnswerText(ans3Text);
            answer3.setNextQuestionID(ans3Next);
            answerList.add(answer3);
        }

        if (!ans4Text.isEmpty()) {
            answer4.setId(id + 4);
            answer4.setParentID(id);
            answer4.setAnswerText(ans4Text);
            answer4.setNextQuestionID(ans4Next);
            answerList.add(answer4);
        }

        question.setAnswers(answerList);
        return question;

    }

    static RealmList<Questions> createDefaults() {
        RealmList<Questions> questionList = new RealmList<>();

        // First Question
        questionList.add(createQuestion(1,
                "Is the subject Male or Female?",
                false,
                "Male", 50,
                "Female", 50,
                "", 0,
                "", 0));

        // First Question
        questionList.add(createQuestion(50,
                "What is the subject's current state of alertness?",
                false,
                "Unconscious", 500,
                "Groggy, Barely Awake", 500,
                "Zombie like", 500,
                "Fully Alert", 100));

        // Basic Aid
        questionList.add(createQuestion(100,
                "Obtain Consent\nCall 911 If Needed\nGet AED/First Aid Kit\nUse PPE",
                false,
                "Start Interview\nGet Vital Signs", 200,
                "", 0,
                "", 0,
                "", 0));

        // Signs & Symptoms
        questionList.add(createQuestion(200,
                "Signs & Symptoms",
                true,
                "Next", 210,
                "", 0,
                "", 0,
                "", 0));

        // Allergies
        questionList.add(createQuestion(210,
                "Allergies",
                true,
                "Next", 220,
                "", 0,
                "", 0,
                "", 0));

        // Medication
        questionList.add(createQuestion(220,
                "Medication",
                true,
                "Next", 230,
                "", 0,
                "Scan Barcode", 100000,
                "Add Medication", 0));

        // Past Illnesses
        questionList.add(createQuestion(230,
                "Past Illnesses",
                true,
                "Next", 240,
                "", 0,
                "", 0,
                "", 0));

        // Last Oral Intake
        questionList.add(createQuestion(240,
                "Last Oral Intake",
                true,
                "Next", 250,
                "", 0,
                "", 0,
                "", 0));

        // Events leading up to incident
        questionList.add(createQuestion(250,
                "Events Leading Up To This Moment",
                true,
                "Next", 5000,
                "", 0,
                "", 0,
                "", 0));

        // Did the subject become responsive
        questionList.add(createQuestion(500,
                "Attempt to get the subjects attention. Shout, use their name if known. Tap subject's shoulders or the bottom of the foot if the subject is an infant.\nDid the subject respond?",
                false,
                "Yes", 100,
                "No", 600,
                "", 0,
                "", 0));

        // Still unresponsive - GET HELP - is CPR needed?
        questionList.add(createQuestion(600,
                "EMS Notified!\nIs the subject breathing?",
                false,
                "Yes", 1000,
                "No", 700,
                "", 0,
                "", 0));

        // CPR Required
        questionList.add(createQuestion(700,
                "Get subject face up on a firm surface.\nBegin CPR.\n\nContinue CPR Until:\n-The subject responds\n-EMS takes over\n-You're too exhausted to continue",
                false,
                "Subject Responded!", 1000,
                "Hand Off to EMS", 5000,
                "", 0,
                "", 0));

        // Subject is breathing
        questionList.add(createQuestion(1000,
                "Head-to-Toe Check",
                false,
                "Next", 1010,
                "", 0,
                "", 0,
                "", 0));

        // Hand Off
        questionList.add(createQuestion(5000,
                "Hand Off to EMS",
                false,
                "", 0,
                "", 0,
                "", 0,
                "", 0));

        // Create Defaults
        return questionList;
    }
}
