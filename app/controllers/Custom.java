package controllers;

import java.util.Map;

import models.Page;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

/**
 * render custom pages
 * @author Philip Lipman
 *
 */
public class Custom extends Controller
{	
	/**
	 * load page
	 * @param pageName
	 * @return
	 */
	public static Result custom(String pageName)
	{
		Page page = Page.getPage(pageName);
		
		if (page == null)
		{
			return ok(index.render("Page " + pageName + " not found."));
		}

		return ok(views.html.custom.page.render(page));
	}

	/**
	 * generic form renderer
	 * @return
	 */
    public static Result formGeneric() 
	{
        return ok(views.html.formgeneric.render(Form.form(DynamicForm.class)));
    }
	
    /**
     * generic form processor
     * @return
     */
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
