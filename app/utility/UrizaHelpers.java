package utility;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 
 * @author Philip Lipman
 *
 */
public class UrizaHelpers
{
	/**
	 * return timestamp
	 * @return
	 */
	public static Timestamp getTime()
	{		
		Calendar calendar = Calendar.getInstance();		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * classes that should not be stored in database
	 * @param classes
	 * @return
	 */
	public static String classCleanup(String classes)
	{
		classes = classes.replace("ui-draggable", "");
		classes = classes.replace("ui-sortable", "");
		classes = classes.replace("dragging", "");
		
		return classes;
	}
}
