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
import views.html.development.*;

public class Development extends Controller
{
    public static Result development() 
	{
        return ok(development.render("Dev Menu"));
    }
}
