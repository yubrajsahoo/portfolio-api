package io.github.yubrajsahoo.portfolioapi.unit.utils;

import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.getFileExtension;
import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.removeFileExtension;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileUtils Unit Test")
public class FileUtilsTest {

    @Test
    @DisplayName("Test case for json file extension")
    void testGetFileExtension_Json() {
        String fileName = "a.json";
        assertEquals("json", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Test case for without file extension")
    void testGetFileExtension_WithoutExtension() {
        String fileName = "logo";
        assertEquals("", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Test case for empty file name")
    void testGetFileExtension_Empty() {
        String fileName = "";
        assertEquals("", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Test case for null file name")
    void testGetFileExtension_Null() {
        assertEquals("", getFileExtension(null));
    }

    @Test
    @DisplayName("Test case to remove extension from file name")
    void testRemoveFileExtension_Valid() {
        String fileName = "logo.png";
        assertEquals("logo", removeFileExtension(fileName));
    }

    @Test
    @DisplayName("Test case to remove extension from file name with empty file name")
    void testRemoveFileExtension_Empty() {
        String empty = "";
        assertEquals("", removeFileExtension(empty));
    }

    @Test
    @DisplayName("Test case to remove extension from file name with null file name")
    void testRemoveExtension_Null() {
        assertNull(removeFileExtension(null));
    }

    @Test
    @DisplayName("Test case for remove file extension without extension")
    void testRemoveExtension_WithoutExtension() {
        String logo = "logo";
        assertEquals("logo", removeFileExtension(logo));
    }

    @Test
    @DisplayName("Test case for sanitize file name")
    void testSanitizeFileName() {
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("  path/to/file.txt  "));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path\\to\\file.txt"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path//to/file.txt"));
        assertEquals("./path/to/file.txt", FileUtils.sanitizeFileName("./path/to/file.txt"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("/path/to/file.txt/"));
        assertEquals("avatar.png", FileUtils.sanitizeFileName("portfolio/avatar.png"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path/to/../../file.txt"));
        assertEquals("path/file.txt", FileUtils.sanitizeFileName("path/../file.txt"));

        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName(null));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName(""));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName("  "));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName("../"));
    }
}