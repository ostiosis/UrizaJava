package controllers;

import java.sql.SQLException;

import org.apache.commons.mail.EmailException;

import models.Page;
import models.PasswordReset;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import utility.Mailer;
import views.html.custom.*;
import views.html.*;

public class Custom extends Controller
{	
	public static Result custom(String pageName)
	{
		Page page = Page.getPage(pageName);
		
		if (page == null)
		{
			return ok(index.render("Page " + pageName + " not found."));
		}
		
		return ok(custom.render(page));
	}
}
