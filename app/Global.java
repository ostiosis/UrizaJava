import play.*;
import play.libs.*;

import com.avaje.ebean.Ebean;

import models.*;

import java.io.File;
import java.util.*;

public class Global extends GlobalSettings
{
	@Override
	public void onStart(Application app)
	{
		// Check if the database is empty
		new File(Play.application().path().getAbsolutePath() + "\\public\\uploads\\").mkdirs();
				
		if (User.find.findRowCount() == 0)
		{
			Ebean.save((List<?>) Yaml.load("test-data.yml"));
		}
	}
}
