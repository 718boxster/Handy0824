package com.handytrip.Structures;

public class NotificationData {
    String date;
    String text;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NotificationData(String date, String text) {
        this.date = date;
        this.text = text;
    }
}
