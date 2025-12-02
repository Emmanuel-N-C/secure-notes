package com.secureware.secure_notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private Long id;
    private String title;
    private String content; // Decrypted content
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}