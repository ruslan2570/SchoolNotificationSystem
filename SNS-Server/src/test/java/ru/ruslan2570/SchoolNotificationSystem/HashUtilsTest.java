package ru.ruslan2570.SchoolNotificationSystem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilsTest {

    @Test
    void getHash() {
        assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
                HashUtils.getHash("test"));
    }

    @Test
    @DisplayName("isValid Positive")
    void isValid() {
        String password = "test";
        String hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

        assertEquals(true, HashUtils.isValid(password, hash));
    }

    @Test
    @DisplayName("isValid Negative")
    void isValidNegative() {
        String password = "42";
        String hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

        assertEquals(false, HashUtils.isValid(password, hash));
    }

    @Test
    @DisplayName("mixed test")
    void getAndVerifyHash(){
        String password = "test";
        assertEquals(true, HashUtils.isValid(password,HashUtils.getHash(password)));
    }
}