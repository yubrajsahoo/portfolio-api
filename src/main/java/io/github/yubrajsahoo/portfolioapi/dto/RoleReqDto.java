package io.github.yubrajsahoo.portfolioapi.dto;

import lombok.Data;

import java.util.List;

/**
 * Request Data Transfer Object for creating a role.
 *
 * @author Yubraj Sahoo
 */
@Data
public class RoleReqDto {

    /**
     * The name of the role.
     */
    private String name;

    /**
     * The list of privilege names associated with this role.
     */
    private List<String> privileges;
}
