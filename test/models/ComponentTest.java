package models;

import org.junit.Before;

import static play.test.Helpers.*;
import play.test.WithApplication;

public class ComponentTest extends WithApplication
{
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase()));
	}
	
	
}
