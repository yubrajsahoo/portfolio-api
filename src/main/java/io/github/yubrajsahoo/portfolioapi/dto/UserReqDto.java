package io.github.yubrajsahoo.portfolioapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * Request Data Transfer Object for user registration.
 *
 * @author Yubraj Sahoo
 */
@Data
public class UserReqDto {

    /**
     * The email address of the user.
     */
    @Email(message = "Invalid Email Id")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    /**
     * The plain-text password for the new user.
     */
    @NotBlank(message = "Password cannot be blank")
    private String password;

    /**
     * The roles to assign to the user upon registration.
     */
    private List<String> roles;
}
