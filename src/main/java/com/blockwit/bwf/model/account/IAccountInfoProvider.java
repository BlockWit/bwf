package com.blockwit.bwf.model.account;

import java.util.Set;

public interface IAccountInfoProvider {

	Long getId();

	AccountType getAccountType();

	String getUsername();

	String getEmail();

	Set<String> getAllPermissions();

	Set<String> getPermissions();

	Set<String> getRoles();

}

