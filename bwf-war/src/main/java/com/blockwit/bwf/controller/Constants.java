package com.blockwit.bwf.controller;

public class Constants {

	public final static String REGEXP_LOGIN = "^(\\w|\\.){3,50}$";

	public final static String REGEXP_CONFIRM_CODE = "^[a-f0-9]{99}$";

	public final static String REGEXP_PASSWORD = "^(\\w|\\{|\\}|!|#|\\$|%|~|\\^|&|\\*|\\(|\\)|\\\\|\"|'|\\+|-|=|_|\\{|\\}|\\[|\\]|\\/|\\?|>|<|,|\\,|`|\\|:|;){8,50}$";

}
