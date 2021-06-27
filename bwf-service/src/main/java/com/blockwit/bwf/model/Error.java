/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

  public static final int EC_NOTIFICATION_TYPE_NOT_FOUND = 1;

  public static final int EC_CAN_NOT_UPDATE_TASK = 2;

  public static final int EC_WRONG_TASK_STATUS = 3;

  public static final int EC_TASK_NOT_FOUND = 4;

  public static final int EC_LOGIN_EXISTS = 5;

  public static final int EC_EMAIL_EXISTS = 6;

  public static final int EC_CAN_NOT_CREATE_ACCOUNT = 7;

  public static final int EC_CAN_NOT_UPDATE_ACCOUNT = 8;

  public static final int EC_CAN_NOT_UPDATE_ACCOUNT_PASSWORD = 9;

  public static final int EC_ACCOUNT_NOT_FOUND = 10;

  public static final int EC_CAN_NOT_GET_OPTIONS = 11;

  public static final String EM_LOGIN_EXISTS = "Login already exists ";

  public static final String EM_EMAIL_EXISTS = "Email already exists ";

  public static final String EM_CAN_NOT_CREATE_ACCOUNT = "Can't create account ";

  public static final String EM_CAN_NOT_UPDATE_ACCOUNT = "Can not update account ";

  public static final String EM_CAN_NOT_UPDATE_ACCOUNT_PASSWORD = "Can not update account password ";

  public static final String EM_ACCOUNT_NOT_FOUND = "Account not found ";

  public static final String EM_CAN_NOT_GET_OPTIONS = "Can't get options  ";

  public static final String EM_NOTIFICATION_TYPE_NOT_FOUND = "Notification type not found ";

  private int code;

  private String descr;

}
