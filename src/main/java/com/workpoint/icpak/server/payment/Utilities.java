package com.workpoint.icpak.server.payment;

/**
 * Created by achachiez on 02/03/15.
 */
public class Utilities {
    //google related static Strings: Created by Kenga.
    public static String CLIENT_ID = "717736851971-to50m7t8dqd96nb1f8spss316hbadhk6.apps.googleusercontent.com";
    public static String CLIENT_SECRET = "2mPBZIXpzRtRHNPN76Xe1kSk";
    public static String DEVELOPER_EMAIL_ADDRESS ="717736851971-to50m7t8dqd96nb1f8spss316hbadhk6@developer.gserviceaccount.com";

    public static String ERP_URI = "http://41.139.138.165/members/memberdata.php";
    public static String ACCESS_TOKEN_REQUEST_URL = "https://accounts.google.com/o/oauth2/token";
    public static String charset = "UTF-8";

    //json variables from acces token

    public static String ACCESS_TOKEN = "access_token";
    public static String ACCESS_TYPE = "token_type";
    public static String EXPIRY= "expires_in";

    public static final String GROUPS_URL = "https://www.google.com/m8/feeds/groups/default/full";
    public static String MY_PRODUCT_NAME = "Harambesa";

    public static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
    public static final int MAX_NB_CONTACTS = 100000;
    public static final String LIPISHA_FEED_FILE = "lipishaResponse.json";


    public static String LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/authorize_card_transaction";
    public static String LIPISHA_COMPLETE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/complete_card_transaction";
    public static String LIPISHA_REVERSE_CARD_AUTHORIZATION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/reverse_card_authorization";
    public static String LIPISHA_SEND_MONEY_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/send_money";
    public static String LIPISHA_TRANSACTIONS_URL = "https://lipisha.com/payments/accounts/index.php/v2/api/get_transactions";
    public static String LIPISHA_CONFIRM_TRANSACTION_URL = "https://lipisha.com/payments/accounts/index.php/v2/api/acknowledge_transaction";

    public static String LIPISHA_API_VERSION = "1.3.0";
    public static String LIPISHA_API_TYPE = "Callback";

    //public static String HARAMBESA_ACCOUNT_NUMBER = "01494";
    //public static String HARAMBESA_ACCOUNT_NUMBER_PAYOUT = "01558";

    public static String NOTIFICATION_URL = "../";

    public static double WITHDRAWAL_RATE = 8;//This is a percentage earnings for Harambesa for the amount withdrawn
    public static double MPESA_CHARGES = 100;

}
