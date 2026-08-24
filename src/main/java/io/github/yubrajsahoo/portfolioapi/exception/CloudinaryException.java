package io.github.yubrajsahoo.portfolioapi.exception;

/**
 * Exception thrown when an error occurs while interacting with Cloudinary.
 *
 * @author Yubraj Sahoo
 * @since 0.0.1-SNAPSHOT
 */
public class CloudinaryException extends RuntimeException {
    /**
     * Creates a Cloudinary exception with the specified message.
     *
     * @param message exception message
     */
    public CloudinaryException(String message) {
        super(message);
    }

    /**
     * Creates a Cloudinary exception with the specified message and cause.
     *
     * @param message exception message
     * @param cause   underlying exception
     */
    public CloudinaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
