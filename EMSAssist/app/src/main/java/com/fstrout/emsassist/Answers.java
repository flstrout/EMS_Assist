package com.fstrout.emsassist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fstrout on 3/25/18.
 *
 */

public class Answers extends RealmObject {

    @PrimaryKey
    private int id;
    private int parentID;
    private String answerText;
    private int nextQuestionID;
    private int overdosePenalty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getNextQuestionID() {
        return nextQuestionID;
    }

    public void setNextQuestionID(int nextQuestionID) {
        this.nextQuestionID = nextQuestionID;
    }

    public int getOverdosePenalty() {
        return overdosePenalty;
    }

    public void setOverdosePenalty(int overdosePenalty) {
        this.overdosePenalty = overdosePenalty;
    }
}
