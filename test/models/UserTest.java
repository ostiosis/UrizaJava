package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.test.WithApplication;
import utility.PasswordHash;

public class UserTest extends WithApplication
{
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createAndRetrieveUser() throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		new User("Bob", "bob@gmail.com", "secret").save();
		
		User bob = User.find.where()
				.eq("email", "bob@gmail.com")
				.findUnique();
		
		assertNotNull(bob);
		assertEquals("Bob", bob.username);
	}
	
	@Test
	public void resetPassword() throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		new User("Bob", "bob@gmail.com", "secret").save();
				
		try
		{
			User.resetPassword("bob@gmail.com", "new-secret");
		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		User bob = User.find.where()
			.eq("email", "bob@gmail.com")
			.findUnique();
		
		Logger.info(bob.passwordHash);
		
		assertTrue(PasswordHash.validatePassword("new-secret", bob.passwordHash));
		
	}

	

}
