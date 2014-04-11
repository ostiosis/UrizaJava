package controllers;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

/**
 * Custom controller test
 * @author Philip Lipman
 *
 */
public class CustomTest extends WithApplication
{
	/**
	 * test setup
	 */
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((Collection<?>) Yaml.load("test-data.yml"));
	}
	
	/**
	 * check if custom page loads
	 */
	@Test
	public void custom()
	{
		Result result = 
				callAction(controllers.routes.ref.Custom.custom("test"));	
		
		assertEquals(200, status(result));
	}
	
	/**
	 * check if dynamic form processes information
	 */
	@Test
	public void dynamicForm()
	{
		Result result = callAction(
			controllers.routes.ref.Custom.dynamicForm(),
			fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
				"email", "bob@example.com",
				"password", "secret"
			))
		);
		
		assertTrue(session(result).containsKey("email"));
		assertTrue(session(result).containsValue("bob@example.com"));
		assertTrue(session(result).containsKey("password"));
		assertTrue(session(result).containsValue("secret"));
		
		assertEquals("bob@example.com", session(result).get("email"));
		assertEquals("secret", session(result).get("password"));
	}
}
