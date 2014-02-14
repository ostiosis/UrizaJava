package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import models.MediaObject;
import models.MediaObjectThumbnail;
import models.Page;
import play.Logger;
import play.Play;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utility.MediaHelpers;
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
		
		//return ok(editor.render(newPage));
    	return ok(development.render(newPage, "Dev Menu"));        

	}
    
	/**/
    public static Result openMenu()
    {
    	return ok(open.render(Page.pages()));
    }
    
    /**
    public static Result open(Long pageId)
    {
    	Page getPage = Page.find.byId(pageId);
    	
    	return ok(views.html.custom.page.render(getPage));
    }
    
    /**/
    public static Result uploadAjax(String fileName) throws IOException
    {    	   	
    	Logger.info("uploadAjax");
    	
    	String extension = FilenameUtils.getExtension(fileName);
    	String name = FilenameUtils.removeExtension(fileName);
    	
    	File file = null;

		file = request().body().asRaw().asFile();
		Logger.info("TEST2");
		   	
    	InputStream is = null;
    	
		is = new BufferedInputStream(new FileInputStream(file));
		
		//String mimeType = URLConnection.guessContentTypeFromStream(is);
		String mimeType = MediaHelpers.guessContentTypeFromStream(is);
		
		Logger.info(mimeType);
		
		if (mimeType != null && mimeType.contains("image"))
		{
			MediaObject.create(name, extension, file);
		}
		//file.renameTo(upload);
		file.delete();
    	//Logger.info("file: " + new MimetypesFileTypeMap().getContentType(upload).toString());

		return ok("File uploaded");    	
		
    }
    /**/
    
    public static Result showImageThumbnails(String label) throws SQLException
    {
    	List<MediaObject> thumbnails = MediaObject.thumbnailList(label);
    	
    	return ok(mediathumbnail.render(thumbnails));    	
    }
    
    public static Result getImage(Long imageId, String label)
    {
    	MediaObject findImage = null;
    	
    	if (label == "")
    	{
	    	findImage = MediaObject.find.byId(imageId);
    	}
    	else
    	{
	    	findImage = MediaObject.find.byId(
				MediaObjectThumbnail
				.find
				.where()
				.eq("parent_id", imageId)
				.eq("label", label).findUnique().childId
			);
    	}
    	
    	return ok(views.html.development.image.render(findImage));
    }
    
    /**/
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
