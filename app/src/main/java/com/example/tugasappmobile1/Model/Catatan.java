package com.example.tugasappmobile1.Model;

public class Catatan {
    public String id;
    public String note;

    // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    public Catatan() {
    }

    public Catatan(String id, String note) {
        this.id = id;
        this.note = note;
    }
}
