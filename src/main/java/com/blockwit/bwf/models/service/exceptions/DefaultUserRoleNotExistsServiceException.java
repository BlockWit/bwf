package com.blockwit.bwf.models.service.exceptions;

import com.blockwit.bwf.models.service.RoleService;

public class DefaultUserRoleNotExistsServiceException extends RoleServiceException {

    public DefaultUserRoleNotExistsServiceException() {
        super("Default user role " + RoleService.ROLE_USER + " not exists!");
    }

}
