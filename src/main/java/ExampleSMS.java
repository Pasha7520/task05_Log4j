import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.Serializable;

public class ExampleSMS {
    private static Logger logger1 = LogManager.getLogger(ExampleSMS.class);

    public static void main(String[] args) {

           send("sssss");

    }
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACf62eecd100cf78fb7807cfa8b3c05994";
    public static final String AUTH_TOKEN = "b7795fd92670f4b47998596555fc5e82";
    public static void send(String str) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message
                .creator(new PhoneNumber("+380932922388"), /*my phone number*/
                        new PhoneNumber("+12139857960"), str) .create(); /*attached to me number*/
    }
    @PluginFactory
    public static SmsAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            logger1.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new SmsAppender(name, filter, layout, true);
    }

    // note: class name need not match the @Plugin name.
    @Plugin(name="SMS", category="Core", elementType="appender", printObject=true)
    public static final class SmsAppender extends AbstractAppender {
        protected SmsAppender(String name, Filter filter,
                              Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
            super(name, filter, layout, ignoreExceptions);
        }

        //here I send SMS-message
        public void append(LogEvent event) {
            try {
                ExampleSMS.send(new String(getLayout().toByteArray(event)));
            } catch (Exception ex) {
            }
        }
    }

}
