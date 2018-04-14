package com.fstrout.emsassist;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


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

    String getQuestion() {
        return question;
    }

    private void setQuestion(String question) {
        this.question = question;
    }

    public boolean inputFieldDisplayed() {
        return inputField;
    }

    private void displayInputField(boolean inputField) {
        this.inputField = inputField;
    }

    RealmList<Answers> getAnswers() {
        return answers;
    }

    private void setAnswers(RealmList<Answers> answers) {
        this.answers = answers;
    }

    private static Questions createQuestion(int id, String questionText, boolean displayInput,
                                            String ans1Text, int ans1Next, int ans1ODP,
                                            String ans2Text, int ans2Next, int ans2ODP,
                                            String ans3Text, int ans3Next, int ans3ODP,
                                            String ans4Text, int ans4Next, int ans4ODP) {
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
            answer1.setOverdosePenalty(ans1ODP);
            answerList.add(answer1);
        }

        if (!ans2Text.isEmpty()) {
            answer2.setId(id + 2);
            answer2.setParentID(id);
            answer2.setAnswerText(ans2Text);
            answer2.setNextQuestionID(ans2Next);
            answer2.setOverdosePenalty(ans2ODP);
            answerList.add(answer2);
        }

        if (!ans3Text.isEmpty()) {
            answer3.setId(id + 3);
            answer3.setParentID(id);
            answer3.setAnswerText(ans3Text);
            answer3.setNextQuestionID(ans3Next);
            answer3.setOverdosePenalty(ans3ODP);
            answerList.add(answer3);
        }

        if (!ans4Text.isEmpty()) {
            answer4.setId(id + 4);
            answer4.setParentID(id);
            answer4.setAnswerText(ans4Text);
            answer4.setNextQuestionID(ans4Next);
            answer4.setOverdosePenalty(ans4ODP);
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
                "Male", 50, 0,
                "Female", 50, 0,
                "", 0, 0,
                "", 0, 0));

        // First Question
        questionList.add(createQuestion(50,
                "What is the subject's current state of alertness?",
                false,
                "Unconscious", 500, 1,
                "Groggy, Barely Awake", 500, 1,
                "Zombie like", 500, 2,
                "Fully Alert", 100, 0));

        // Basic Aid
        questionList.add(createQuestion(100,
                "Obtain Consent\nCall 911 If Needed\nGet AED/First Aid Kit\nUse PPE",
                false,
                "Start Interview\nGet Vital Signs", 200, 0,
                "", 0, 0,
                "", 0, 0,
                "", 0, 0));

        // Signs & Symptoms
        questionList.add(createQuestion(200,
                "Signs & Symptoms",
                true,
                "Next", 210,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        // Allergies
        questionList.add(createQuestion(210,
                "Allergies",
                true,
                "Next", 220,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        // Medication
        questionList.add(createQuestion(220,
                "Medication",
                true,
                "Next", 230,0,
                "", 0,0,
                "Scan Barcode", 100000,0,
                "Add Medication", 0,0));

        // Past Illnesses
        questionList.add(createQuestion(230,
                "Past Illnesses",
                true,
                "Next", 240,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        // Last Oral Intake
        questionList.add(createQuestion(240,
                "Last Oral Intake",
                true,
                "Next", 250,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        // Events leading up to incident
        questionList.add(createQuestion(250,
                "Events Leading Up To This Moment",
                true,
                "Next", 5000,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        // Did the subject become responsive
        questionList.add(createQuestion(500,
                "Attempt to get the subjects attention. Shout, use their name if known. Tap subject's shoulders or the bottom of the foot if the subject is an infant.\nDid the subject respond?",
                false,
                "Yes", 100,0,
                "No", 600,1,
                "", 0,0,
                "", 0,0));

        // Still unresponsive - GET HELP - is CPR needed?
        questionList.add(createQuestion(600,
                "EMS Notified!\nIs the subject breathing?",
                false,
                "Yes", 1000,0,
                "No", 700,0,
                "", 0,0,
                "", 0,0));

        // CPR Required
        questionList.add(createQuestion(700,
                "Get subject face up on a firm surface.\nBegin CPR.\n\nContinue CPR Until:\n-The subject responds\n-EMS takes over\n-You're too exhausted to continue",
                false,
                "Subject Responded!", 1000,0,
                "Hand Off to EMS", 6000,0,
                "", 0,0,
                "", 0,0));

        // Head to Toe Check
        questionList.add(createQuestion(1000,
                "Head-to-Toe Check",
                false,
                "Next", 1010,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1010,
                "Head-to-Toe Check\nHead & Neck Exam\nLesions?",
                true,
                "Next", 1015,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1015,
                "Head-to-Toe Check\nHead & Neck Exam\nLips Color?",
                false,
                "Purple", 1020,2,
                "Blue", 1020, 2,
                "Normal", 1020, 0,
                "",0,0));

        questionList.add(createQuestion(1020,
                "Head-to-Toe Check\nHead & Neck Exam\nPupils?",
                false,
                "Constricted", 1030,3,
                "Dilated", 1030,0,
                "Un-Equal", 1030,0,
                "Normal", 1030,0));

        questionList.add(createQuestion(1030,
                "Head-to-Toe Check\nHead & Neck Exam\nEar Discharge?",
                false,
                "Fluid", 1040,0,
                "Blood", 1040,0,
                "Fluid & Blood", 1040,0,
                "None", 1040,0));

        questionList.add(createQuestion(1040,
                "Head-to-Toe Check\nHead & Neck Exam\nNose Discharge?",
                false,
                "Fluid", 1050,0,
                "Blood", 1050,0,
                "Fluid & Blood", 1050,0,
                "None", 1050,0));

        questionList.add(createQuestion(1050,
                "Head-to-Toe Check\nHead & Neck Exam\nOral Trauma - Bitten Lips, Cheek or Tongue?",
                false,
                "Yes", 1060,0,
                "No", 1060,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1060,
                "Head-to-Toe Check\nHead & Neck Exam\nFacial Symmetry?",
                false,
                "Yes", 1070,0,
                "No", 1070,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1070,
                "Head-to-Toe Check\nChest Exam\nLesions?",
                true,
                "Next", 1080,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1080,
                "Head-to-Toe Check\nChest Exam\nChest Wall Symmetry?",
                false,
                "Yes", 1090,0,
                "No", 1090,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1090,
                "Head-to-Toe Check\nChest Exam\nChest Wall Movement?",
                false,
                "Weird", 1100,0,
                "Normal", 1100,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1100,
                "Head-to-Toe Check\nRespiratory Exam\nBreathing Quality?",
                false,
                "Deep", 1110,0,
                "Shallow", 1110,0,
                "Labored", 1110,1,
                "Normal", 1110,0));

        questionList.add(createQuestion(1110,
                "Head-to-Toe Check\nRespiratory Exam\nBreath Sounds?",
                false,
                "Wheezing", 1120,0,
                "Gurgling", 1120,3,
                "Normal", 1120,0,
                "", 0,0));

        questionList.add(createQuestion(1120,
                "Head-to-Toe Check\nAbdominal Exam\nLesions?",
                true,
                "Next", 1130,0,
                "", 1130,0,
                "", 1130,0,
                "", 1130,0));

        questionList.add(createQuestion(1130,
                "Head-to-Toe Check\nAbdominal Exam\nTemperature?",
                false,
                "Warm", 1140,0,
                "Cool", 1140,0,
                "Normal", 1140,0,
                "", 0,0));

        questionList.add(createQuestion(1140,
                "Head-to-Toe Check\nPelvic Exam\nLesions?",
                true,
                "Next", 1150,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1150,
                "Head-to-Toe Check\nPelvic Exam\nUrinary Incontinence?",
                false,
                "Yes", 1160,0,
                "No", 1160,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1160,
                "Head-to-Toe Check\nPelvic Exam\nFecal Incontinence?",
                false,
                "Yes", 1170,0,
                "No", 1170,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(1170,
                "Head-to-Toe Check\nExtremity Exam\nLesions?",
                true,
                "Next", 1180,0,
                "", 1180,0,
                "", 1180,0,
                "", 1180,0));

        questionList.add(createQuestion(1180,
                "Head-to-Toe Check\nExtremity Exam\nFingertips/Fingernails Color?",
                false,
                "Black", 1190,0,
                "Purple", 1190,0,
                "Blue", 1190,1,
                "Normal", 1190,0));

        questionList.add(createQuestion(1190,
                "Head-to-Toe Check\nExtremity Exam\nCapillary Refill?",
                true,
                "Good", 4000,0,
                "Bad", 4000,0,
                "", 0,0,
                "", 0,0));

        // Hand Off
        questionList.add(createQuestion(6000,
                "Hand Off to EMS",
                false,
                "", 0,0,
                "", 0,0,
                "", 0,0,
                "", 0,0));

        questionList.add(createQuestion(20000,
                "Panic Button Pressed, Please Choose Reason",
                false,
                "Other", 0,0,
                "Stroke ", 0,0,
                "Opioid Overdose", 0,0,
                "Heart Attack", 0,0));
        // Create Defaults
        return questionList;
    }
}
