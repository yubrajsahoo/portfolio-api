package io.github.yubrajsahoo.portfolioapi.unit.utils;

import io.github.yubrajsahoo.portfolioapi.utils.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.getFileExtension;
import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.removeFileExtension;
import static io.github.yubrajsahoo.portfolioapi.utils.FileUtils.assertFileName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FileUtils}.
 */
@DisplayName("Unit: File Utilities")
class FileUtilsTest {

    @Test
    @DisplayName("Should Extract JSON Extension from Filename")
    void testGetFileExtension_Json() {
        String fileName = "a.json";
        assertEquals("json", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Should Handle Dot At End")
    void testGetFileExtension_DotAtEnd() {
        assertEquals("", getFileExtension("a."));
    }

    @Test
    @DisplayName("Should Return Empty String When Filename Has No Extension")
    void testGetFileExtension_WithoutExtension() {
        String fileName = "logo";
        assertEquals("", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Should Return Empty String When Filename is Empty")
    void testGetFileExtension_Empty() {
        String fileName = "";
        assertEquals("", getFileExtension(fileName));
    }

    @Test
    @DisplayName("Should Return Empty String When Filename is Null")
    void testGetFileExtension_Null() {
        assertEquals("", getFileExtension(null));
    }

    @Test
    @DisplayName("Should Remove Extension from Valid Filename")
    void testRemoveFileExtension_Valid() {
        String fileName = "logo.png";
        assertEquals("logo", removeFileExtension(fileName));
    }

    @Test
    @DisplayName("Should Handle Dot At Start")
    void testRemoveFileExtension_DotAtStart() {
        assertEquals(".hidden", removeFileExtension(".hidden"));
    }

    @Test
    @DisplayName("Should Handle Empty Filename Gracefully")
    void testRemoveFileExtension_Empty() {
        String empty = "";
        assertEquals("", removeFileExtension(empty));
    }

    @Test
    @DisplayName("Should Return Null When Input is Null")
    void testRemoveExtension_Null() {
        assertNull(removeFileExtension(null));
    }

    @Test
    @DisplayName("Should Return Original Filename if No Extension Present")
    void testRemoveExtension_WithoutExtension() {
        String logo = "logo";
        assertEquals("logo", removeFileExtension(logo));
    }

    @Test
    @DisplayName("Should Sanitize File Names by Removing Unsafe Patterns")
    void testSanitizeFileName() {
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("  path/to/file.txt  "));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path\\to\\file.txt"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path//to/file.txt"));
        assertEquals("./path/to/file.txt", FileUtils.sanitizeFileName("./path/to/file.txt"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("/path/to/file.txt/"));
        assertEquals("portfolio/avatar.png", FileUtils.sanitizeFileName("portfolio/avatar.png"));
        assertEquals("path/to/file.txt", FileUtils.sanitizeFileName("path/to/../../file.txt"));
        assertEquals("path/file.txt", FileUtils.sanitizeFileName("path/../file.txt"));

        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName(null));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName(""));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName("  "));
        assertThrows(IllegalArgumentException.class, () -> FileUtils.sanitizeFileName("../"));
    }

    @Test
    @DisplayName("Should Assert File Name Successfully")
    void testAssertFileName_Valid() {
        assertDoesNotThrow(() -> assertFileName("test.txt"));
        assertDoesNotThrow(() -> assertFileName(".hidden"));
    }

    @Test
    @DisplayName("Should Throw Exception When Extension is Missing")
    void testAssertFileName_MissingExtension() {
        assertThrows(IllegalArgumentException.class, () -> assertFileName("test"));
        assertThrows(IllegalArgumentException.class, () -> assertFileName("test."));
    }

    @Test
    @DisplayName("Should Throw Exception When File Name is Missing")
    void testAssertFileName_MissingName() {
        assertThrows(IllegalArgumentException.class, () -> assertFileName(" .ext"));
    }
}
