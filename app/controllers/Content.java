package controllers;

import models.Blog;
import models.Template;
import play.mvc.*;

/**
 * CMS System
 * @author Philip Lipman
 *
 */
public class Content extends Controller
{
	/**
	 * load templates
	 * @param name
	 * @return
	 */
	public static Result template(String name)
	{
    	Template getTemplate = Template.getTemplate(name);
    	return ok(views.html.content.template.render(getTemplate));   		
	}
	
	/**
	 * load blogs
	 * @param title
	 * @return
	 */
	public static Result blog(String title)
	{
    	Blog getBlog = Blog.getBlog(title);
    	return ok(views.html.content.blog.render(getBlog));   		
	}
}
