package io.github.yubrajsahoo.portfolioapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Response Data Transfer Object for user data.
 *
 * @author Yubraj Sahoo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResDto {

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The roles assigned to the user.
     */
    private List<RoleResDto> roles;
}
