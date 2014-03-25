package utility;

import java.sql.Timestamp;
import java.util.Calendar;

public class UrizaHelpers
{
	public static Timestamp getTime()
	{		
		Calendar calendar = Calendar.getInstance();		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static String classCleanup(String classes)
	{
		classes.replace("ui-draggable", "");
		classes.replace("ui-sortable", "");
		
		return classes;
	}
}
