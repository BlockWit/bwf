/*
 * Copyright (c) 2017-present BlockWit, LLC. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 *
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.User;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class AccessContextHelper {

//  public static <T> ResponseEntity<T> responseEntityAccess(Supplier<T> admin,
//                                                        Function<Account, Optional<T>> authorizedCheckAccess,
//                                                        Supplier<ResponseEntity<T>> otherCasesCheckAccess) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if (!(authentication instanceof AnonymousAuthenticationToken)) {
//      Object principal = authentication.getPrincipal();
//      if (principal instanceof User) {
//        Collection<GrantedAuthority> authorities = ((User) principal).getAuthorities();
//        for (GrantedAuthority grantedAuthority : authorities) {
//          if (grantedAuthority.getAuthority().equals(PermissionService.PERMISSION_ADMIN)) {
//            return admin.get();
//          } else if (grantedAuthority.getAuthority().equals(PermissionService.PERMISSION_USER)) {
//            Optional<T> r = authorizedCheckAccess.apply(((User) principal).getAccount());
//            if(r.isPresent())
//              return r.get();
//          }
//        }
//      }
//    }
//    return otherCasesCheckAccess.get();
//  }

  public static <T> T access(Supplier<T> admin,
                             Function<Account, Optional<T>> authorizedCheckAccess,
                             Supplier<T> otherCasesCheckAccess) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      Object pricncipal = authentication.getPrincipal();
      if (pricncipal instanceof User) {
        Collection<GrantedAuthority> authorities = ((User) pricncipal).getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
          if (grantedAuthority.getAuthority().equals(PermissionService.PERMISSION_ADMIN)) {
            return admin.get();
          } else if (grantedAuthority.getAuthority().equals(PermissionService.PERMISSION_USER)) {
            Optional<T> r = authorizedCheckAccess.apply(((User) pricncipal).getAccount());
            if(r.isPresent())
              return r.get();
          }
        }
      }
    }
    return otherCasesCheckAccess.get();
  }

//  public static <T> T access(Supplier<T> ,Supplier<T> anonCheckAccess, Function<T, Boolean> notAdminCheckAccess) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if (authentication instanceof AnonymousAuthenticationToken) {
//      return target.get();
//    } else {
//      authentication.getAuthorities();
//      LDAPAccount ldapAccount = LDAPAccount.fromString(currentUserName);
//      String username = ldapAccount.getUsername();
//      Set<String> patterns = esbAccessProvider.getEsbAccessTopicsConfig().getPreparedPatterns().get(username);
//      return f.apply(patterns);
//    }
//    return e.get();
//  }

//  public static <T> T forAccount(Function<Set<String>, T> f,
//                                 Supplier<T> e) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication().getName();
//    if (!(authentication instanceof AnonymousAuthenticationToken)) {
//      String currentUserName = authentication.getName();
//      LDAPAccount ldapAccount = LDAPAccount.fromString(currentUserName);
//      String username = ldapAccount.getUsername();
//      Set<String> patterns = esbAccessProvider.getEsbAccessTopicsConfig().getPreparedPatterns().get(username);
//      return f.apply(patterns);
//    }
//    return e.get();
//  }

}
