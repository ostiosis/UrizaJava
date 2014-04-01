package controllers;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.List;

import models.Page;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

public class CustomTest extends WithApplication
{
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}
	
	@Test
	public void customTest()
	{
		Result result = callAction(controllers.routes.ref.Custom.custom("test"));	
		assertEquals(200, status(result));
	}
	
	@Test
	public void dynamicFormTest()
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
