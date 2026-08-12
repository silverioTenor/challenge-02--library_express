package org.libraryexpress.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.libraryexpress.domain.book.exception.InvalidIsbnException;
import org.libraryexpress.domain.book.valueobject.Isbn;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
@DisplayName("ISBN Value Object - Unit Test")
class IsbnTest {

    @Test
    @DisplayName("Should create the instance successfully when the ISBN format is valid")
    void shouldCreateInstance_whenIsbnIsValid() {
        // Arrange & Act
        String validIsbnStr = "978-43-12345-67-8";
        Isbn isbn = new Isbn(validIsbnStr);

        // Assert
        assertEquals(validIsbnStr, isbn.value());
    }

    @Test
    @DisplayName("Should throw InvalidIsbnException when the ISBN is null")
    void shouldThrowInvalidIsbnException_whenIsbnIsNull() {
        // Act & Assert
        assertThrows(InvalidIsbnException.class, () -> new Isbn(null));
    }

    @Test
    @DisplayName("Should throw InvalidIsbnException when the ISBN is empty or blank")
    void shouldThrowInvalidIsbnException_whenIsbnIsEmptyOrBlank() {
        // Act & Assert
        assertThrows(InvalidIsbnException.class, () -> new Isbn(""));
        assertThrows(InvalidIsbnException.class, () -> new Isbn("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123-45-67890-12",       // Missing the last block
            "978-4-12345-67-8",       // Second block has only 1 digit (needs 2)
            "978-43-1234-67-8",       // Third block has only 4 digits (needs 5)
            "978-43-12345-678-9",     // Fourth block has 3 digits (needs 2)
            "abc-de-fghij-kl-m",      // Letters instead of numbers
            "9784312345678"           // Valid digits but missing hyphens
    })
    @DisplayName("Should throw InvalidIsbnException for various malformed ISBN formats")
    void shouldThrowInvalidIsbnException_whenIsbnFormatIsInvalid(String invalidIsbn) {
        // Act & Assert
        assertThrows(InvalidIsbnException.class, () -> new Isbn(invalidIsbn), "The provided ISBN is invalid.");
    }

    @Test
    @DisplayName("Should generate a valid and compliant ISBN string automatically")
    void shouldGenerateValidIsbn_whenGenerateIsCalled() {
        // Act
        Isbn generatedIsbn = Isbn.generate();

        // Assert
        assertNotNull(generatedIsbn);
        assertNotNull(generatedIsbn.value());

        // Aqui garantimos que o valor gerado aleatoriamente passa na nossa própria validação sem estourar erro
        assertDoesNotThrow(() -> new Isbn(generatedIsbn.value()),
                "The generated ISBN string must be compliant with the ISBN pattern");
    }

    @Test
    @DisplayName("Should ensure structural equality between two ISBN records with the same value")
    void shouldBeEqual_whenIsbnsHaveSameValue() {
        // Arrange
        String commonIsbn = "111-22-33333-44-5";
        Isbn isbn1 = new Isbn(commonIsbn);
        Isbn isbn2 = new Isbn(commonIsbn);
        Isbn isbn3 = new Isbn("999-88-77777-66-5");

        // Assert
        assertEquals(isbn1, isbn2);
        assertEquals(isbn1.hashCode(), isbn2.hashCode());
        assertNotEquals(isbn1, isbn3);
    }
}
