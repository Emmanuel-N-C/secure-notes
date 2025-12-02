package com.secureware.secure_notes.service;

import com.secureware.secure_notes.dto.NoteRequest;
import com.secureware.secure_notes.dto.NoteResponse;

import java.util.List;

public interface NoteService {
    NoteResponse createNote(NoteRequest request);
    NoteResponse getNoteById(Long id);
    List<NoteResponse> getAllNotes();
    NoteResponse updateNote(Long id, NoteRequest request);
    void deleteNote(Long id);
}