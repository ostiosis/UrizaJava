package controllers;

import static play.data.Form.form;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import models.MediaObject;
import models.Page;
import models.User;
import play.Logger;
import play.Play;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utility.MediaObjectType;
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
    public static Result uploadAjax(String fileName) throws IOException
    {    	
    	BufferedImage img = null;
    	
    	String extension = FilenameUtils.getExtension(fileName);
    	String name = FilenameUtils.removeExtension(fileName);
    	
    	File file = request().body().asRaw().asFile();
    	
    	InputStream is = null;
    	
		is = new BufferedInputStream(new FileInputStream(file));
		
		String mimeType = URLConnection.guessContentTypeFromStream(is);
		
		if (mimeType.contains("image"))
		{
			MediaObject.create(name, extension, file);
		}
		//file.renameTo(upload);
		file.delete();
    	//Logger.info("file: " + new MimetypesFileTypeMap().getContentType(upload).toString());

		return ok("File uploaded @");    	
		
    }
    
    public void processUpload(String fileName, String fileType, File file)
    {
    	switch (fileType)
    	{
	    	case "image/jpeg":
	    	case "image/gif":
	    	case "image/png":
	    		break;
    	}
    }
    /**/

}
