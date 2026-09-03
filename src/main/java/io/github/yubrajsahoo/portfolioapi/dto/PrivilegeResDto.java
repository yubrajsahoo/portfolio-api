package io.github.yubrajsahoo.portfolioapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Response Data Transfer Object for representing a privilege.
 *
 * @author Yubraj Sahoo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrivilegeResDto {

    /**
     * The name of the privilege.
     */
    private String name;
}
