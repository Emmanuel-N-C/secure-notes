package com.secureware.secure_notes.service;

public interface EncryptionService {
    String encrypt(String plaintext);
    String decrypt(String ciphertext);
}