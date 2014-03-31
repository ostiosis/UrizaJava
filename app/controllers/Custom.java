package controllers;

import models.Page;
import play.mvc.*;
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
		
		//return ok(custom.render(page));
		return ok(views.html.custom.page.render(page));
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
}
