package controllers;

import static play.data.Form.form;
import models.Page;
import play.Logger;
import play.mvc.*;
import views.html.custom.*;
import views.html.development.*;

public class Development extends Controller
{
    public static Result development() 
	{
        return ok(development.render("Dev Menu"));
    }
    /**
    public static Result add()
	{
		Page newPage = Page.create(
			form().bindFromRequest().get("name"), 
			form().bindFromRequest().get("title"), 
			form().bindFromRequest().get("description")
		);
		
		return ok(custom.render(newPage));
	}
    /**/
    public static Result add(String name, String title, String description)
	{
    	Logger.info("reached");
    	
		Page newPage = Page.create(
			name, 
			title, 
			description
		);
		
		return ok(custom.render(newPage));
	}
	/**/
}
