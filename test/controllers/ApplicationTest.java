package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.header;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.session;
import static play.test.Helpers.status;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import play.mvc.Result;
import play.test.WithApplication;

/**
 * Application controller test
 * @author Philip Lipman
 *
 */
public class ApplicationTest extends WithApplication
{
	/**
	 * test setup
	 */
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase()));
	}
	
	/**
	 * user authentication test
	 */
	@Test
	public void authenticated()
	{
		Result result = callAction(
			controllers.routes.ref.Application.index(),
			fakeRequest().withSession("email", "bob@example.com")
		);
		
		assertEquals(200, status(result));	
	}
	
	/**
	 * user fails authentication test
	 */
	@Test
	public void authenticateFailure()
	{
		Result result = callAction(
			controllers.routes.ref.Application.authenticate(),
			fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
				"email", "bob@example.com",
				"password", "badpassword"
			))			
		);
		
		assertEquals(400, status(result));
		assertNull(session(result).get("email"));
	}
	
	/**
	 * 
	 */
	@Test
	public void notAuthenticated()
	{
		Result result = callAction(
				controllers.routes.ref.Application.index(),
				fakeRequest()
		);
		
		assertEquals(303, status(result));
		assertEquals("/login", header("Location", result));
	}

}
