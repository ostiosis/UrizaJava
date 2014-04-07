package controllers;

import models.Blog;
import models.Page;
import models.Template;
import play.mvc.*;

public class Content extends Controller
{
	public static Result template(String name)
	{
    	Template getTemplate = Template.getTemplate(name);
    	return ok(views.html.content.template.render(getTemplate));   		
	}
	public static Result blog(String title)
	{
    	Blog getBlog = Blog.getBlog(title);
    	return ok(views.html.content.blog.render(getBlog));   		
	}
}
