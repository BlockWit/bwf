package com.blockwit.bwf.models.service;

import com.blockwit.bwf.models.entity.Role;
import com.blockwit.bwf.models.repository.RoleRepository;
import com.blockwit.bwf.models.service.exceptions.DefaultAdminRoleNotExistsServiceException;
import com.blockwit.bwf.models.service.exceptions.DefaultUserRoleNotExistsServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class RoleService {

    public final static String ROLE_ADMIN = "ADMIN";

    public final static String ROLE_USER = "USER";

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getDefaultUserRole() throws DefaultUserRoleNotExistsServiceException {
        Optional<Role> roleOpt = roleRepository.findByName(ROLE_ADMIN);
        return roleOpt.orElseThrow(() -> new DefaultUserRoleNotExistsServiceException());
    }

    public Role getDefaultAdminRole() throws DefaultAdminRoleNotExistsServiceException {
        Optional<Role> roleOpt = roleRepository.findByName(ROLE_ADMIN);
        return roleOpt.orElseThrow(() -> new DefaultAdminRoleNotExistsServiceException());
    }


}
