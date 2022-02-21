package com.example.safemedicare;

import java.util.Date;

public class Note {
    String NoteTitle, NoteInfo;
    Date NoteDate;

    public Note() {
    }

    public Note(String noteTitle, String noteInfo, Date noteDate) {
        NoteTitle = noteTitle;
        NoteInfo = noteInfo;
        NoteDate = noteDate;
    }

    public String getNoteTitle() {
        return NoteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        NoteTitle = noteTitle;
    }

    public String getNoteInfo() {
        return NoteInfo;
    }

    public void setNoteInfo(String noteInfo) {
        NoteInfo = noteInfo;
    }

    public Date getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(Date noteDate) {
        NoteDate = noteDate;
    }
}
