import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.twiml.voice.Play;
import com.twilio.twiml.voice.Say.Language;
import com.twilio.rest.api.v2010.account.Call;
//import com.twilio.type.PhoneNumber;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import java.sql.*;
import java.util.Properties;
import static spark.Spark.*;
import io.github.cdimascio.dotenv.Dotenv;

public class VoiceApp {

    // DB Credentials
    // Modify them in .env file if you wish
    private final static Dotenv dotenv = Dotenv.load();
    private final static String DB_URL = dotenv.get("DB_URL");
    private final static String DB_USER = dotenv.get("DB_USER");
    private final static String DB_PASSWORD = dotenv.get("DB_PASSWORD");




   /* private final static String DB_URL= "jdbc:oracle:thin:@oratdb.mobiltel.bg:1521/gctest";
    private final static String DB_USER = "ivr_ident";
    private final static String DB_PASSWORD = "ivr_ident";*/



    public static String intToNatNormalization(String msisdn) {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(msisdn, "BG");
         //   System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            msisdn = phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL).replaceAll("\\s+","");
         //   System.out.println(msisdn);

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }



        return msisdn;
    }


    public static String getSegment(Connection connection, String msisdn) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically.
        String segment = "Unknown";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement
                    .executeQuery("select segment from IVR_CUST_DET2 where msisdn = '" + msisdn + "'")) {
                while (resultSet.next())
                    segment = resultSet.getString(1);
            }
        }
        return segment;
    }


    public static Connection connectToDB() throws SQLException {



        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");


        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);


        OracleConnection connection = (OracleConnection) ods.getConnection();

        return connection;
    }




    public static void main(String[] args) {


        get("/hello", (req, res) -> "Hello Web");

        post("/", (request, response) -> {


            // Get the Phone number of the caller
            String internationalPhoneFormat = request.queryParams("Caller");
//            String fromCity = request.queryParams("FromCity");  // not valid in Bulgaria
            String nationalPhoneFormat = intToNatNormalization(internationalPhoneFormat);
            String segment = getSegment(connectToDB(), nationalPhoneFormat);





            // Play the Announcements that we have
            VoiceResponse twiml = new VoiceResponse.Builder()
                    .say(new Say.Builder(String.format("Your International number is, %s! Your, national number is, %s and your, segment, is, %s",
                            internationalPhoneFormat, nationalPhoneFormat, segment)).voice(Say.Voice.ALICE).build())
                    .play(new Play.Builder("https://demo.twilio.com/docs/classic.mp3").build())
                    .build();

            return twiml.toXml();


         });


        /*

        String swissNumberStr = "+359882203797";
        intToNatNormalization(swissNumberStr);

        String norNumber = "";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "BG");
            System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            norNumber = phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            System.out.println(norNumber.replaceAll("\\s+",""));

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }*/

    }
}
