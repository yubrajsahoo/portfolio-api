package io.github.yubrajsahoo.portfolioapi.unit.utils;

import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.getFileExtension;
import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.removeFileExtension;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FileUtils}.
 */
@DisplayName("Unit: File Utilities")
public class FileUtilsTest {

    /**
     * Tests extracting the JSON file extension.
     */
    @Test
    @DisplayName("Should Extract JSON Extension from Filename")
    void testGetFileExtension_Json() {
        String fileName = "a.json";
        assertEquals("json", getFileExtension(fileName));
    }

    /**
     * Tests extracting the file extension when there is no extension.
     */
    @Test
    @DisplayName("Should Return Empty String When Filename Has No Extension")
    void testGetFileExtension_WithoutExtension() {
        String fileName = "logo";
        assertEquals("", getFileExtension(fileName));
    }

    /**
     * Tests extracting the file extension with an empty string.
     */
    @Test
    @DisplayName("Should Return Empty String When Filename is Empty")
    void testGetFileExtension_Empty() {
        String fileName = "";
        assertEquals("", getFileExtension(fileName));
    }

    /**
     * Tests extracting the file extension with a null value.
     */
    @Test
    @DisplayName("Should Return Empty String When Filename is Null")
    void testGetFileExtension_Null() {
        assertEquals("", getFileExtension(null));
    }

    /**
     * Tests removing a valid file extension.
     */
    @Test
    @DisplayName("Should Remove Extension from Valid Filename")
    void testRemoveFileExtension_Valid() {
        String fileName = "logo.png";
        assertEquals("logo", removeFileExtension(fileName));
    }

    /**
     * Tests removing the file extension from an empty string.
     */
    @Test
    @DisplayName("Should Handle Empty Filename Gracefully")
    void testRemoveFileExtension_Empty() {
        String empty = "";
        assertEquals("", removeFileExtension(empty));
    }

    /**
     * Tests removing the file extension from a null value.
     */
    @Test
    @DisplayName("Should Return Null When Input is Null")
    void testRemoveExtension_Null() {
        assertNull(removeFileExtension(null));
    }

    /**
     * Tests removing the file extension when there is no extension.
     */
    @Test
    @DisplayName("Should Return Original Filename if No Extension Present")
    void testRemoveExtension_WithoutExtension() {
        String logo = "logo";
        assertEquals("logo", removeFileExtension(logo));
    }

    /**
     * Tests sanitizing various file names.
     */
    @Test
    @DisplayName("Should Sanitize File Names by Removing Unsafe Patterns")
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
