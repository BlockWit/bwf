package com.blockwit.bwf.service;

import com.blockwit.bwf.entity.Permission;

import java.util.Set;

public interface DefaultPermissionsProvider {

    Set<Permission> getPermissions();

}
