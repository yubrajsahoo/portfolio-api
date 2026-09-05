package io.github.yubrajsahoo.portfolioapi.dto;

import lombok.Data;

/**
 * Request Data Transfer Object for creating a privilege.
 *
 * @author Yubraj Sahoo
 */
@Data
public class PrivilegeReqDto {

    /**
     * The name of the privilege.
     */
    private String name;
}
