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
		classes = classes.replace("ui-draggable", "");
		classes = classes.replace("ui-sortable", "");
		classes = classes.replace("dragging", "");
		
		return classes;
	}
}
