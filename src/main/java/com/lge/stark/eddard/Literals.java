package com.lge.stark.eddard;

public class Literals {

    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    public static final String DEFAULT_RIC_CODE = "KIC";
    public static final String DEFAULT_CNTRY_CODE = "KR";

    public static final String SUCCESS_STAT = "S";
    public static final String FAIL_STAT = "F";
    public static final String PENDING_STAT = "P";
    public static final String INITIAL_STAT = "I";
    public static final String RECEIVE_STAT = "R";
    public static final String TRANSFER_STAT = "T";
    public static final String WAIT_STAT = "W";

    public static final int MAX_BUFFER_SIZE = 1024 * 1024;

    public static final String DEV_LOUNGE = "dev_lounge";
    public static final String ADMIN = "admin";
    public static final String SELLER_LOUNGE = "seller_lounge";

    public static final String DEVICE_PLATFORM_ID_NETCAST = "NetCast";
    public static final String DEVICE_PLATFORM_ID_WEBOS = "WebOS";
    public static final String DEVICE_PLATFORM_ID_OLD = "OLD";

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    
    public static final String DATE_DEFAULT_FORMAT = "yy.MM.dd HH:mm:ss.SSS z";
    public static final String DATE_TIME_FORMAT = "HH:mm:ss.SSS";
    public static final String DATE_DEFAULT_TIMEZONE = "Asia/Seoul";
    
    public static final String PREFIX_NEW = "new_";
    public static final String PREFIX_LEGACY = "legacy_";
    
    public static final String FORWARD = "forward";
    public static final String BACKWARD = "backward";
    
    public static final String NEW_FORWARD = PREFIX_NEW + FORWARD;
    public static final String LEGACY_FORWARD = PREFIX_LEGACY + FORWARD;
    
    public static final String NEW_BACKWARD = PREFIX_NEW + BACKWARD;
    public static final String LEGACY_BACKWARD = PREFIX_LEGACY + BACKWARD;
}