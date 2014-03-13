package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import models.Component;
import models.MediaObject;
import models.MediaObjectThumbnail;
import models.Page;
import models.Template;
import play.Logger;
import play.mvc.*;
import utility.MediaHelpers;

public class Development extends Controller
{
	
    public static Result development(String name) 
	{
    	Page getPage = Page.getPage(name);
    	return ok(views.html.development.development.render(getPage, "Dev Menu"));        
    }
    
    /**/
    public static Result validatePage(String name)
	{    	    	
    	int check = Page.find.where().eq("name", name).findRowCount();
    	
		if (check > 0)
		{
			return ok("Error: page " + name + " already exists");  
		}
		return ok();
    	      

	}
    public static Result addPage(String name, String title, String description)
	{    	    	
    	Page newPage = Page.create(
			name, 
			title, 
			description
		);
		
		//return ok(editor.render(newPage));
    	return ok(views.html.development.development.render(newPage, "Dev Menu"));        

	}
    
	/**/
    public static Result openMenu()
    {
    	return ok(views.html.development.open.render(Page.pages()));
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
    	
    	return ok(views.html.development.mediathumbnail.render(thumbnails));    	
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

    public static Result updateTemplate(String name, String code, Long templateId, Long topPosition, Long leftPosition, Long pageId)
    {
    	name = name.trim();
    	code = code.trim();
    	
    	Logger.info("\nBegin Row");
    	Logger.info("name: " + name);
    	Logger.info("code: " + code);
    	Logger.info("topPosition: " + topPosition);
    	Logger.info("leftPosition: " + leftPosition);
    	
    	Logger.info("templateId: " + templateId);
    	Logger.info("pageId: " + pageId);
    	
    	Logger.info("\nEnd Row");
    	
    	Page getPage = Page.find.byId(pageId);
    	
    	Template getTemplate = null;
    	
    	if (templateId <= 0)
    	{
    		getTemplate = Template.create("", "", topPosition, leftPosition);
    		getPage.templates.add(getTemplate);
    		getPage.saveManyToManyAssociations("templates");
    	}    	
    	else
    	{
    		getTemplate = Template.find.byId(templateId);
    		getTemplate.update(topPosition, leftPosition);   
    		getTemplate.save();
    	}
    	
		/**/
    	return ok(views.html.utility.longresult.render(getTemplate.id));
    }
    
    public static Result updateComponent(String code, Long componentId, String componentType, Long width, Long height, Long templateId)
    {
    	code = code.trim();
    	
    	
    	Template getTemplate = null;
    	Component getComponent = null;
    	
    	Logger.info("\nBegin Row");
    	Logger.info("code: " + code.trim());
    	Logger.info("componentId: " + componentId);
    	Logger.info("componentType: " + componentType);
    	Logger.info("width: " + width);
    	Logger.info("height: " + height);
    	
    	Logger.info("templateId: " + templateId);
    	
    	Logger.info("\nEnd Row");

		getTemplate = Template.find.byId(templateId);
   	
    	/**/
		
		//getPage.save();
		
    	if (componentId <= 0)
    	{
    		getComponent = Component.create("", code, componentType, width, height);
    		getTemplate.components.add(getComponent);
        	getTemplate.saveManyToManyAssociations("components");
    	}
    	else
    	{
    		getComponent = Component.find.byId(componentId);
    		getComponent.update(code, width, height);
    		getComponent.save();
    	}
		/**/
    	//return ok(views.html.development.developmenttemplate.render(getTemplate));
    	return ok(views.html.utility.longresult.render(getComponent.id));
    }

}
