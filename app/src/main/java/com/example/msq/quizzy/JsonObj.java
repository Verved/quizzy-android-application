package com.example.msq.quizzy;

import android.os.Parcel;
import android.os.Parcelable;

public class JsonObj implements Parcelable {
    String question;
    String QID;
    String options[];
    String answer;
    String qNo;

    public JsonObj(String question, String QID, String[] options, String answer, String qNo) {
        this.question = question;
        this.QID = QID;
        this.options = options;
        this.answer = answer;
        this.qNo = qNo;
    }

    public JsonObj() {
        this.question = "not found!";
        this.QID = "not found!";
        this.options = new String[]{"not found!"};
        this.answer = "not found!";
        this.qNo = "not found!";
    }

    protected JsonObj(Parcel in) {
        question = in.readString();
        QID = in.readString();
        options = in.createStringArray();
        answer = in.readString();
        qNo = in.readString();
    }

    public static final Creator<JsonObj> CREATOR = new Creator<JsonObj>() {
        @Override
        public JsonObj createFromParcel(Parcel in) {
            return new JsonObj(in);
        }

        @Override
        public JsonObj[] newArray(int size) {
            return new JsonObj[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQID() {
        return QID;
    }

    public void setQID(String QID) {
        this.QID = QID;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getqNo() {
        return qNo;
    }

    public void setqNo(String qNo) {
        this.qNo = qNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(QID);
        parcel.writeStringArray(options);
        parcel.writeString(answer);
        parcel.writeString(qNo);
    }
}
