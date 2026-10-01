/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.metrics;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MetricsValues {
    /**
     * commons outcome
     */
    public static final String ALL = "ALL";

    public static final String UPLOAD = "UPLOAD";

    public static final String DELETE = "DELETE";

    /*
    Cloudinary related
     */
    public static final String GET_URL = "GET_URL";

    public static final String GET_ALL_URL = "GET_ALL_URL";

    public static final String CLOUDINARY = "CLOUDINARY";

    public static final String META_DATA_RESOURCE_TYPE_NAME = "#metaData.resourceType.name()";

    public static final String META_DATA_ACCESS_TYPE_NAME = "#metaData.accessType.name()";

    public static final String ACCESS_TYPE = "#accessType";

    public static final String CLOUDINARY_OUTCOME = "#error != null ? #error.metricsType.name() : 'SUCCESS'";

    /*
    General
     */
    public static final String CLOUD_FILE_CONTROLLER = "CLOUD_FILE_CONTROLLER";

    public static final String USER_CONTROLLER = "USER_CONTROLLER";

    public static final String CLOUD_FILE_SERVICE = "CLOUD_FILE_SERVICE";

    public static final String USER_SERVICE = "USER_SERVICE";

    public static final String GENERAL_OUTCOME = "#error != null ? 'ERROR' : 'SUCCESS'";

    public static final String REGISTER = "REGISTER";
    public static final String GET_ALL_USERS = "GET_ALL_USERS";
    public static final String INSERT_ROLE = "INSERT_ROLE";
    public static final String GET_ALL_ROLES = "GET_ALL_ROLES";
    public static final String INSERT_PRIVILEGE = "INSERT_PRIVILEGE";
    public static final String GET_ALL_PRIVILEGES = "GET_ALL_PRIVILEGES";
    public static final String ASSIGN_ROLE_TO_USER = "ASSIGN_ROLE_TO_USER";
    public static final String ASSIGN_PRIVILEGE_TO_ROLE = "ASSIGN_PRIVILEGE_TO_ROLE";
    public static final String EXCEPTION_NAME = "#a0.getClass().getSimpleName()";

    public static final String CUSTOM_USER_DETAILS_SERVICE = "CUSTOM_USER_DETAILS_SERVICE";
    public static final String LOAD_USER_BY_USERNAME = "LOAD_USER_BY_USERNAME";

    public static final String NO_OF_USER_LOGIN = "NO_OF_USER_LOGIN";
    public static final String NONE = "NONE";
    public static final String GLOBAL_EXCEPTION_HANDLER = "GlobalExceptionHandler";
    public static final String CLOUDINARY_CLIENT = "CloudinaryClient";
    public static final String CLOUD_FILE_SERVICE_IMPL = "CloudFileServiceImpl";
    public static final String METHOD_UPLOAD = "upload";
    public static final String METHOD_GET_URL = "getUrl";
}