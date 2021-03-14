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

    public static final String JOB_TIME = "0 0 1 * * ?"; // Daily at 1:00 AM

    public static final String PRIVATE_LIMITED_COMPANY = "Private Limited Company";

    public static final String LIMITED_LIABILITY_PARTNERSHIP = "Limited Liability Partnership";

    public static final String REGISTERED_PARTNERSHIP = "Registered Partnership";

    public static final String THRESHOLD_DATE_80_IAC = "01-04-2016";

    public static final String ONE_PERSON_COMPANY_KEY = "OPC";

    public static final String PRIVATE_LIMITED_COMPANY_KEY = "PTC";

    public static final String FOREIGN_SUBSIDIARY_COMPANY_KEY = "FTC";

    public static final String PUBLIC_COMPANY_KEY = "PLC";

    public static final String NOT_FOR_PROFIT_COMPANY_KEY = "NPL";

    public static final List<String> PRIVATE_LIMITED_COMPANY_OPTIONS = Arrays.asList(PRIVATE_LIMITED_COMPANY_KEY, NOT_FOR_PROFIT_COMPANY_KEY, ONE_PERSON_COMPANY_KEY);

    public static final String LLP_PATTERN_REGEX = "[A-Za-z]{3}-[0-9]\\d*";

    public static final Integer RESET_TOKEN_VALIDITY_IN_MINUTES = 30;


    public static final Integer STARTUP_REGISTRATION_INCOMPLETE_THRESHOLD = 3;

    //making thresold count 3 for testing only
    public static final Integer EXEMPTION_80_IAC_INCOMPLETE_THRESHOLD = 3;
    //public static final Integer EXEMPTION_80_IAC_INCOMPLETE_THRESHOLD = 20;

    public static final Integer SECTION_FIFTY_SIX_INCOMPLETE_THRESHOLD = 3;

    public static final String ALL_RESOURCE = "ALL";

    public static final String OTHERS_IF_ANY_OTHER = "Others- If any other";

    public static final String LOGO_ZIP_LINK = "uploads/";

    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_CLARIFICATION = "uploads/clarification/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_80_IAC = "uploads/section_80_iac_document/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_SEC_56 = "uploads/section_56_document/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER = "uploads/startup_application_document/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_RECOGNIZATION = "uploads/startup_application_certificate/recognization/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_ELIGIBILITY = "uploads/startup_application_certificate/eligibility/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_OTHER_DOCUMENTS = "uploads/other/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_NOTIFICATION_DOCUMENTS = "uploads/notifications/";
    public static final String PUBLIC_RECOGNITION_DOCUMENT_FOLDER_FOR_STARTUP_LOGO = "uploads/logo_document/";

    public static final String DOWNLOAD_DOC_PREFIX = "/s3/download/document/";

    public static final List<String> UNION_TERRITORIES = Arrays.asList("Andaman and Nicobar Islands", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Chandigarh", "Lakshadweep", "Puducherry");

    public static final String RECOGNITION_PREFIX = "DIPP";

    public static final String BULK_REJECTION = "Rejected in batch with multiple applications";

    public static final String REJECTION_ON_CANCELLATION = "Rejected due to cancellation of recognition";

    public static final String REJECTION_ON_EXPIRATION = "Rejected due to expiration of recognition";

    public static final int MIN_LENGTH_TEXTAREA = 500;

    public static final int MAX_LENGTH_TEXTAREA = 5000;

    public static final String TAG_MIGRATION_URL = "https://www.startupindia.gov.in/services/tags/children.json?tag=";

    public static final String LOGO_FILE_NAME = "Startup_India_Logo.zip";
}
