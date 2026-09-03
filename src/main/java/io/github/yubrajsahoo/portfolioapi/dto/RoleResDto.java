package io.github.yubrajsahoo.portfolioapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Response Data Transfer Object for representing a role.
 *
 * @author Yubraj Sahoo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResDto {

    /**
     * The name of the role.
     */
    private String name;

    /**
     * The list of privileges associated with this role.
     */
    private List<PrivilegeResDto> privileges;
}
