package com.blockwit.bwf.models.service;

import com.blockwit.bwf.models.entity.Permission;

import java.util.Set;

public interface DefaultPermissionsProvider {

    Set<Permission> getPermissions();

}
