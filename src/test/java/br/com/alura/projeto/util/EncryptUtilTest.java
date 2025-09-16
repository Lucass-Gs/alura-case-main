package br.com.alura.projeto.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EncryptUtilTest {

    @Test
    @DisplayName("should encrypt password to MD5 hash")
    void shouldEncryptPasswordToMD5Hash() {
        String password = "mudar123";
        String expectedHash = "5f4dcc3b5aa765d61d8327deb882cf99"; // MD5 of "password"
        
        String actualHash = EncryptUtil.toMD5(password);
        
        assertThat(actualHash).isNotEqualTo(password);
        assertThat(actualHash).hasSize(32); // MD5 hash is always 32 characters
        assertThat(actualHash).matches("^[a-f0-9]{32}$"); // Only lowercase hex characters
    }

    @Test
    @DisplayName("should produce same hash for same input")
    void shouldProduceSameHashForSameInput() {
        String password = "test123";
        
        String hash1 = EncryptUtil.toMD5(password);
        String hash2 = EncryptUtil.toMD5(password);
        
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("should produce different hashes for different inputs")
    void shouldProduceDifferentHashesForDifferentInputs() {
        String password1 = "password1";
        String password2 = "password2";
        
        String hash1 = EncryptUtil.toMD5(password1);
        String hash2 = EncryptUtil.toMD5(password2);
        
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "123", "password", "very-long-password-with-special-chars!@#$%^&*()"})
    @DisplayName("should handle various input types")
    void shouldHandleVariousInputTypes(String input) {
        String hash = EncryptUtil.toMD5(input);
        
        assertThat(hash).isNotNull();
        assertThat(hash).hasSize(32);
        assertThat(hash).matches("^[a-f0-9]{32}$");
    }

    @Test
    @DisplayName("should handle null input gracefully")
    void shouldHandleNullInputGracefully() {
        assertThatThrownBy(() -> EncryptUtil.toMD5(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        String passwordWithSpecialChars = "p@ssw0rd!@#$%^&*()";
        
        String hash = EncryptUtil.toMD5(passwordWithSpecialChars);
        
        assertThat(hash).isNotNull();
        assertThat(hash).hasSize(32);
        assertThat(hash).matches("^[a-f0-9]{32}$");
    }

    @Test
    @DisplayName("should handle unicode characters")
    void shouldHandleUnicodeCharacters() {
        String passwordWithUnicode = "senha123çãé";
        
        String hash = EncryptUtil.toMD5(passwordWithUnicode);
        
        assertThat(hash).isNotNull();
        assertThat(hash).hasSize(32);
        assertThat(hash).matches("^[a-f0-9]{32}$");
    }
}

