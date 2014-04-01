package controllers;

import java.util.Map;

import controllers.Application.Login;
import models.Page;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
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

		return ok(views.html.custom.page.render(page));
	}

    public static Result formtest() 
	{
        return ok(views.html.formtest.render(Form.form(DynamicForm.class)));
    }
	
	public static Result dynamicForm()
	{
		DynamicForm requestData = Form.form().bindFromRequest();

		session().clear();
		for (Map.Entry<String, String> entry: requestData.data().entrySet())
		{
			Logger.info(entry.getKey() + ": " + entry.getValue());
    		session(entry.getKey(), entry.getValue());
		}
		
		return ok("ok");
	}
}
