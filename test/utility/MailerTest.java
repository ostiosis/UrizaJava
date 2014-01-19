package utility;

import com.typesafe.config.ConfigFactory;
import com.typesafe.plugin.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.*;

import play.api.Play;

public class MailerTest
{
	@Test
	public void sendApacheEmailTest()
	{
		try
		{				
			String host = ConfigFactory.load().getString("smtp.host");
			Integer port = ConfigFactory.load().getInt("smtp.port");
			Boolean setSSL = ConfigFactory.load().getBoolean("smtp.ssl");
			
			String authUsername = ConfigFactory.load().getString("smtp.user");
			String authPassword = ConfigFactory.load().getString("smtp.password");
			
			Email email = new SimpleEmail();
			//email.setHostName("smtp.googlemail.com");
			email.setHostName(host);
			email.setSmtpPort(port);
			email.setAuthenticator(new DefaultAuthenticator(authUsername, authPassword));
			email.setSSLOnConnect(setSSL);
			
			email.setFrom("philip.lipman@gmail.com");
			email.setSubject("TestMail");
			email.setMsg("This is a test mail ... :-)");
			email.addTo("philip.lipman@gmail.com");
			email.send();
		
			assertTrue(true);
		}
		catch(EmailException e)
		{
			e.printStackTrace();
            fail();			
		}
	}
}
