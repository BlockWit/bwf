package com.blockwit.bwf.models.service.exceptions;

import com.blockwit.bwf.models.service.RoleService;

public class DefaultAdminRoleNotExistsServiceException extends RoleServiceException {

    public DefaultAdminRoleNotExistsServiceException() {
        super("Default admin role " + RoleService.ROLE_ADMIN + " not exists!");
    }

}
