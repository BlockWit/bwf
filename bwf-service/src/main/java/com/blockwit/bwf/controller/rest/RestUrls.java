package com.blockwit.bwf.controller.rest;

public class RestUrls {

	public static final String REST_URL_API_V_1 = "/api/v1";

	public static final String REST_URL_API_V_1_PATTERN = REST_URL_API_V_1 + "/**";

	public static final String REST_URL_REL_AUTH = "/auth";

	public static final String REST_URL_REL_PROFILE = "/profile";

	public static final String REST_URL_REL_ACCOUNTS = "/accounts";

	public static final String REST_URL_REL_OPTIONS = "/options";

	public static final String REST_URL_REL_ADMIN = "/admin";

	public static final String REST_URL_REL_ROLES = "/roles";

	public static final String REST_URL_REL_POSTS = "/posts";

	public static final String REST_URL_REL_MEDIA = "/media";

	public static final String REST_URL_REL_PERMISSIONS = "/permissions";

	public static final String REST_URL_REL_NOTIFICATIONS = "/notifications";

	public static final String REST_URL_REL_PARTS = "/parts";

	public static final String REST_URL_REL_EXECSTATES = "/execstates";

	public static final String REST_URL_REL_NOTIFYTYPES = "/notifytypes";

	public static final String REST_URL_REL_EXECUTORS = "/executors";

	public static final String REST_URL_REL_ASSIGNS = "/assigns";


	public static final String REST_URL_REL_MENU = "/menu";

	public static final String REST_URL_ADMIN = REST_URL_API_V_1 + REST_URL_REL_ADMIN;


	public static final String REST_URL_API_V_1_PROFILE = REST_URL_API_V_1 + REST_URL_REL_PROFILE;

	public static final String REST_URL_API_V_1_AUTH = REST_URL_API_V_1 + REST_URL_REL_AUTH;


	//	public static final String REST_URL_REL_NOTIFICATIONS = "/notifications";
//
//	public static final String REST_URL_REL_PARTS = "/parts";
//
//	public static final String REST_URL_REL_EXECSTATES = "/execstates";
//
//	public static final String REST_URL_REL_NOTIFYTYPES = "/notifytypes";
//
//	public static final String REST_URL_REL_EXECUTORS = "/executors";
	public static final String REST_URL_REL_NOTIFICATIONS_PARTS = REST_URL_REL_NOTIFICATIONS + REST_URL_REL_PARTS;


	public static final String REST_URL_API_V_1_REL_ADMIN_EXEC_STATES = REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_EXECSTATES;

	public static final String REST_URL_API_V_1_REL_ADMIN_NOTIFY_ASSIGNS = REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_ASSIGNS;

	public static final String REST_URL_API_V_1_REL_ADMIN_NOTIFY_TYPES = REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_NOTIFYTYPES;

	public static final String REST_URL_API_V_1_REL_ADMIN_EXECUTORS = REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_EXECUTORS;


	public static final String REST_URL_API_V_1_ADMIN_NOTIFICATIONS = REST_URL_ADMIN + REST_URL_REL_NOTIFICATIONS;

	public static final String REST_URL_API_V_1_ADMIN_EXEC_STATES = REST_URL_ADMIN + REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_EXECSTATES;

	public static final String REST_URL_API_V_1_ADMIN_NOTIFY_TYPES = REST_URL_ADMIN + REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_NOTIFYTYPES;

	public static final String REST_URL_API_V_1_ADMIN_EXECUTORS = REST_URL_ADMIN + REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_EXECUTORS;

	public static final String REST_URL_API_V_1_ADMIN_NOTIFY_ASSIGNS = REST_URL_ADMIN + REST_URL_REL_NOTIFICATIONS_PARTS + REST_URL_REL_ASSIGNS;




	public static final String REST_URL_API_V_1_ADMIN_MEDIA = REST_URL_ADMIN + REST_URL_REL_MEDIA;

	public static final String REST_URL_API_V_1_ADMIN_POSTS = REST_URL_ADMIN + REST_URL_REL_POSTS;

	public static final String REST_URL_API_V_1_ADMIN_ACCOUNTS = REST_URL_ADMIN + REST_URL_REL_ACCOUNTS;

	public static final String REST_URL_API_V_1_ADMIN_OPTIONS = REST_URL_ADMIN + REST_URL_REL_OPTIONS;

	public static final String REST_URL_API_V_1_ADMIN_ROLES = REST_URL_ADMIN + REST_URL_REL_ROLES;

	public static final String REST_URL_API_V_1_ADMIN_PERMISSIONS = REST_URL_ADMIN + REST_URL_REL_PERMISSIONS;

	public static final String REST_URL_API_V_1_ADMIN_MENU = REST_URL_ADMIN + REST_URL_REL_MENU;

}