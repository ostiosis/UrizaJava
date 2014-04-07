package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;
import play.Logger;

public class BlogTest
{
	@Before
	public void setUp()
	{
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createBlog()
	{
		List<String> tags = new ArrayList<String>(Arrays.asList("Buenos Aires", "Cordoba", "La Plata"));
		Blog blog = Blog.create("This is a new blog", "This is a new blog post", null, tags);
		
		for(String t: blog.tags)
		{
			Logger.info("tag: " + t);
		}
		assertNotNull(blog);
		assertEquals("This is a new blog", blog.title);
		assertEquals("This is a new blog post", blog.post);
		
		assertEquals("Buenos Aires", blog.tags.get(0));
		assertEquals("Cordoba", blog.tags.get(1));
		assertEquals("La Plata", blog.tags.get(2));
		
	}
	
}
