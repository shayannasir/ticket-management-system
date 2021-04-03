package tech.shayannasir.tms.constants;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final Integer ESCAPE_BEARER = 7;

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String EMAIL_CONTENT_TYPE = "text/plain";

    public static final String REPORT_ACCEPT_HEADER = "text/csv";

    public static final String USERNAME_PATTERN = "^[a-zA-Z]*$";

    public static final String NAME_AND_DESIGNATION_PATTERN = "[a-zA-Z\\s.]+";

    public static final String PAN_FORMAT = "([a-zA-Z]{3}[abcfghljtABCFGHLJT]{1}[a-zA-Z]{1}[0-9]{4}[a-zA-Z]{1})";

    public static final String PIN_CODE_FORMAT = "^[1-9][0-9]{5}$";

    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,15})";

    public static final String MOBILE_NUMBER_PATTERN = "(0|^\\+91|^\\+91-)?[6-9]{1}[0-9]{9}";

    public static final String URL_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)";

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_*+/=?`~-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";

    public static final int MIN_LENGTH_TEXTAREA = 500;

    public static final int MAX_LENGTH_TEXTAREA = 5000;

    public static final String PASSWORD_INVALID_MESSAGE = "Password should contain at least one uppercase letter, one numeric digit, and one special character. Minimum length should be 8 and maximum 15.";

}
