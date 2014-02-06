package controllers;

import static play.data.Form.form;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import models.Page;
import models.User;
import play.Logger;
import play.Play;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.custom.*;
import views.html.development.*;

public class Development extends Controller
{
	
    public static Result development(String name) 
	{
    	Page getPage = Page.getPage(name);
    	return ok(development.render(getPage, "Dev Menu"));        
    }
    
    /**/
    public static Result add(String name, String title, String description)
	{    	
		Page newPage = Page.create(
			name, 
			title, 
			description
		);
		
		Logger.info("name: " + name);
		Logger.info("title: " + title);
		Logger.info("description: " + description);
		Logger.info("id: " + newPage.id);
		
		return ok(editor.render(newPage));
	}
    
	/**/
    public static Result openMenu()
    {
    	return ok(open.render(Page.pages()));
    }
    
    /**/
    public static Result open(Long pageId)
    {
    	Page getPage = Page.find.byId(pageId);
    	
    	return ok(editor.render(getPage));
    }
    
    public static class Upload
    {
    	public String picture;
    	public String pageName;
    	
    	public String validate()
    	{   		
    		return null;
    	}
    	
    }
    
    /**/
    public static Result upload()
    {
    	//Logger.info(Play.application().path().getAbsolutePath() + "\\public\\uploads\\");
    	
    	String uploadDir = (Play.application().path().getAbsolutePath() + "\\public\\uploads\\");
    	
    	File upload = new File(uploadDir);
    	
    	MultipartFormData body = request().body().asMultipartFormData();
  		FilePart picture = body.getFile("picture");
  		
  		if (picture != null) 
  		{
    		String fileName = picture.getFilename();
    		String contentType = picture.getContentType(); 
    		File file = picture.getFile();
    		//file = picture.getFile();
    		    		
    		file.renameTo(upload);
    		file.delete();
			return ok("File uploaded @" + upload.getAbsolutePath());    		
  		} 
  		else 
  		{
    		flash("error", "Missing file");
    		return redirect(routes.Development.development("error"));    
  		}    	
    }
    /**/
    
    /**/
    public static Result uploadAjax(String filename)
    {    	
    	/**/
    	String uploadDir = (Play.application().path().getAbsolutePath() + "\\public\\uploads\\" + filename);
    	
    	File file = request().body().asRaw().asFile();
    	
    	Logger.info("test: " + file.getAbsoluteFile());
    	Logger.info("file: " + file.getName());
    	Logger.info("file: " + new MimetypesFileTypeMap().getContentType(file).toString());
    	
    	File upload = new File(uploadDir);
    	    	
		file.renameTo(upload);
		file.delete();
		
		return ok("File uploaded @" + upload.getAbsolutePath());    	
		/**/

    }
    /**/

}
