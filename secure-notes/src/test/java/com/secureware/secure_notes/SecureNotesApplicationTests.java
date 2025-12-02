package com.secureware.secure_notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureware.secure_notes.dto.NoteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecureNotesApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${app.security.token}")
	private String validToken;

	@Test
	void testCreateNote_success() throws Exception {
		NoteRequest request = new NoteRequest("Test Title", "Test Content");

		mockMvc.perform(post("/notes")
						.header("Authorization", "Bearer " + validToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.title").value("Test Title"))
				.andExpect(jsonPath("$.content").value("Test Content"))
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());
	}

	@Test
	void testUnauthorizedRequest() throws Exception {
		NoteRequest request = new NoteRequest("Test Title", "Test Content");

		mockMvc.perform(post("/notes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").value("Unauthorized"));
	}

	@Test
	void testValidationFailure() throws Exception {
		NoteRequest request = new NoteRequest("", ""); // Empty title and content

		mockMvc.perform(post("/notes")
						.header("Authorization", "Bearer " + validToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testHealthEndpoint_noAuthRequired() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void testGetAllNotes_success() throws Exception {
		mockMvc.perform(get("/notes")
						.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}
}