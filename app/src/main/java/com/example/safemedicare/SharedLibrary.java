package com.example.safemedicare;

import java.util.ArrayList;

public class SharedLibrary {

    ArrayList<Note> NoteList = new ArrayList<Note>();

    public void AddNote(Note note) {
        NoteList.add(note);

    }

    public void DeleteNote(Note note) {
        NoteList.remove(note);
    }

    public Note DisplayNoteDetails() {
        if (NoteList.size() != 0)
            for (int i = 0; i < NoteList.size(); i++) {
                return NoteList.get(i);
            }
        return null;
    }

    public void ShowAllNote() {
        if (NoteList.size() != 0)
            for (int i = 0; i < NoteList.size(); i++) {
                System.out.println(NoteList.get(i));
            }
    }

    public ArrayList<Note> getNoteList() {
        return NoteList;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        NoteList = noteList;
    }
}
