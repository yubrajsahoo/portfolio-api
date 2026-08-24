package io.github.yubrajsahoo.portfolioapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for reading and writing test data files.
 *
 * <p>This utility supports JSON serialization/deserialization and file
 * operations using paths relative to the project root.
 *
 * <p>Example:
 * <pre>
 * src/test/resources/images/avatar.json
 * </pre>
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@UtilityClass
public class DataBuilderUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Reads a file from the given path.
     *
     * <p>The path should be relative to the project root.
     *
     * @param filepath file path
     * @return input stream containing the file content
     * @throws IllegalArgumentException if the file does not exist
     */
    public InputStream readFile(String filepath) {
        Path path = Paths.get(filepath);

        if (!Files.exists(path)) {
            log.warn("File not found: {}", path.toAbsolutePath());
            throw new IllegalArgumentException("File not found: " + filepath);
        }

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("Error reading file: {}", path.toAbsolutePath(), e);
            throw new RuntimeException("Failed to read file: " + filepath, e);
        }
    }

    /**
     * Reads a JSON file and maps it to the specified Java object.
     *
     * <p>Example:
     * <pre>
     * Avatar avatar = FileUtils.readFromJson(
     *         "src/test/resources/images/avatar.json",
     *         Avatar.class
     * );
     * </pre>
     *
     * @param filepath file path
     * @param clazz    target Java class
     * @param <T>      target object type
     * @return mapped Java object
     */
    public <T> T readFromJson(String filepath, Class<T> clazz) {
        try (InputStream inputStream = readFile(filepath)) {
            return OBJECT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("Error mapping JSON file {} to class {}", filepath, clazz.getSimpleName(), e);
            throw new RuntimeException("Failed to read JSON file: " + filepath, e);
        }
    }

    /**
     * Serializes a Java object to JSON and writes it to the specified file.
     *
     * <p>Parent directories are created automatically if they do not exist.
     *
     * <p>Example:
     * <pre>
     * FileUtils.writeToJson(
     *         "src/test/resources/images/avatar.json",
     *         avatar
     * );
     * </pre>
     *
     * @param targetFilepath target file path
     * @param data           Java object to serialize
     */
    public void writeToJson(String targetFilepath, Object data) {
        Path path = Paths.get(targetFilepath);

        try {
            // Create parent directories if they do not exist.
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            // Serialize and write JSON.
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), data);

            log.info("Successfully saved JSON data to: {}", path.toAbsolutePath());

        } catch (IOException e) {
            log.error("Error writing JSON file: {}", path.toAbsolutePath(), e);
            throw new RuntimeException("Failed to write JSON file: " + targetFilepath, e);
        }
    }
}