package ru.practicum.shareit.item.comments.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.comments.CommentRequestDto;
import ru.practicum.shareit.item.comments.CommentResponseDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@ActiveProfiles("test")
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentResponseDto> json;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void parseToJsonCommentResponseDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 6, 27, 10, 30);
        CommentResponseDto dto = new CommentResponseDto(1L, "Test comment", "John Doe", created);

        JsonContent<CommentResponseDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Test comment");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-06-27T10:30:00");
    }

    @Test
    void parseToJsonCommentRequestDto() {
        CommentRequestDto dto = new CommentRequestDto("Valid comment");

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
