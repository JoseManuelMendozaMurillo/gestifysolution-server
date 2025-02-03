package com.ventuit.adminstrativeapp.utils;

public class RegexUtils {
    public static final String RFC_PATTERN = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$";
    public static final String LATITUDE_PATTERN = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$";
    public static final String LONGITUDE_PATTERN = "^[-+]?((1[0-7]\\d|[1-9]?\\d)(\\.\\d+)?|180(\\.0+)?)$";
    public static final String NUMBERS_ONLY_PATTERN = "^\\d+$";
    public static final String CAPITAL_LETTERS_ONLY_PATTERN = "^[A-Z]+$";
    public static final String POSTAL_CODE_PATTERN = "^\\d{5}$";
    public static final String PHONE_NUMBER_PATTERN = "^\\+\\d{2} \\d{10}$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_.@-]+$";
    public static final String NAME_PATTERN = "^[a-zA-ZñÑ ]+$";

}