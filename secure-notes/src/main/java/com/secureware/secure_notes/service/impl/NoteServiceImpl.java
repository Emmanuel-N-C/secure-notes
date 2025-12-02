package com.secureware.secure_notes.service.impl;

import com.secureware.secure_notes.dto.NoteRequest;
import com.secureware.secure_notes.dto.NoteResponse;
import com.secureware.secure_notes.entity.Note;
import com.secureware.secure_notes.repository.NoteRepository;
import com.secureware.secure_notes.service.EncryptionService;
import com.secureware.secure_notes.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final EncryptionService encryptionService;

    // Explicit constructor instead of @RequiredArgsConstructor
    public NoteServiceImpl(NoteRepository noteRepository, EncryptionService encryptionService) {
        this.noteRepository = noteRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional
    public NoteResponse createNote(NoteRequest request) {
        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(encryptionService.encrypt(request.getContent()));

        Note savedNote = noteRepository.save(note);
        return mapToResponse(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        return mapToResponse(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long id, NoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        note.setTitle(request.getTitle());
        note.setContent(encryptionService.encrypt(request.getContent()));

        Note updatedNote = noteRepository.save(note);
        return mapToResponse(updatedNote);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new RuntimeException("Note not found with id: " + id);
        }
        noteRepository.deleteById(id);
    }

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(encryptionService.decrypt(note.getContent()));
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }
}