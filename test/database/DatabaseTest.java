package database;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import org.junit.*;

import play.db.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Methods for testing the database.
 * @author Philip Lipman
 *
 */
public class DatabaseTest
{
	/**
	 * Checks for a valid database connection
	 * 
	 * @throws SLQException if connection doesn't work
	 */
	@Test
	public void checkDatabaseConnection() 
	{
	  running(fakeApplication(), new Runnable() 
	  {
	    public void run() 
	    {
	    	/**/
	        try (Connection conn = DB.getConnection())
	        {        	
	            assertTrue(true);
	        } 
	        catch (SQLException e1) 
	        {
	            e1.printStackTrace();
	            fail();
	        }
	        /**/
	    }
	  });
	}
}
