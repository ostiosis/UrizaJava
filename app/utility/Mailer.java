package utility;

import org.apache.commons.mail.*;

import com.typesafe.config.ConfigFactory;

/**
 * mailer
 * @author Philip Lipman
 *
 */
public class Mailer
{
	private String _host = ConfigFactory.load().getString("smtp.host");
	private Integer _port = ConfigFactory.load().getInt("smtp.port");
	private Boolean _setSSL = ConfigFactory.load().getBoolean("smtp.ssl");
	
	private String _authUsername = ConfigFactory.load().getString("smtp.user");
	private String _authPassword = ConfigFactory.load().getString("smtp.password");
	
	private Email _email = new SimpleEmail();
	
	public Mailer()
	{
		_email.setHostName(_host);
		_email.setSmtpPort(_port);
		_email.setAuthenticator(
				new DefaultAuthenticator(_authUsername, _authPassword));
		_email.setSSLOnConnect(_setSSL);
	}
	
	public void sendMail(String from, 
			String to, 
			String subject, 
			String body) throws EmailException
	{
		try
		{
			_email.setFrom(from);
			_email.addTo(to);
			_email.setSubject(subject);
			_email.setMsg(body);
			
			_email.send();
		}
		catch (EmailException e)
		{
			System.out.println(e);
		}

	}
}
