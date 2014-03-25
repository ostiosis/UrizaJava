package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import models.ChildComponent;
import models.Component;
import models.MediaObject;
import models.MediaObjectThumbnail;
import models.Page;
import models.PageComponent;
import models.Template;
import play.Logger;
import play.db.DB;
import play.mvc.*;
import utility.MediaHelpers;
import utility.UrizaHelpers;

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
    /**

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
    	
		
    	return ok(views.html.utility.longresult.render(getTemplate.id));
    }
    /**/
    
    public static Result updateComponent(Integer componentId, Integer parentId, String componentType, String classes, String code, Integer displayOrder)
    {
    	code = code.trim();
    	classes = UrizaHelpers.classCleanup(classes.trim());
    	
    	Component getComponent = null;
    	
    	Logger.info("\nBegin Row");
    	Logger.info("code: " + code.trim());
    	Logger.info("classes: " + classes.trim());
    	
    	Logger.info("parentId: " + parentId);
    	Logger.info("componentId: " + componentId);
    	Logger.info("componentType: " + componentType);  	
    	Logger.info("\nEnd Row");

    	/**/
		
		//getPage.save();
		
    	if (componentId <= 0)
    	{
    		getComponent = Component.create("", code, componentType, classes);
    		getComponent.save();
    	}
    	else
    	{
    		getComponent = Component.find.byId(componentId);
    		getComponent.update(code, classes);
    		getComponent.save();
    	}
		/**/

    	ChildComponent childComponent = ChildComponent
    			.find
    			.where()
    			.eq("child_id", getComponent.id)
    			.findUnique();
    	
    	if (childComponent != null)
    	{
    		childComponent.update(parentId, getComponent.id, displayOrder);
    		childComponent.save();
    	}
    	else
    	{
    		childComponent = ChildComponent.create(parentId, getComponent.id, displayOrder);
    		childComponent.save();
    	}
    	
    	childComponent.parentId = parentId;
    	
    	PageComponent pageComponent = PageComponent
    			.find
    			.where()
    			.eq("component_id", getComponent.id)
    			.findUnique();
    	if (pageComponent != null)
    	{
    		pageComponent.delete();
    	}
    	
    	return ok(views.html.utility.integerresult.render(getComponent.id));
    }
    
    public static Result updateTopLevelComponent(Integer componentId, Integer pageId, String componentType, String classes, String code, Integer displayOrder)
    {
    	code = code.trim();
    	classes = UrizaHelpers.classCleanup(classes.trim());
    	
    	Component getComponent = null;
    	
    	Logger.info("\nBegin Row");
    	Logger.info("code: " + code.trim());
    	Logger.info("classes: " + classes.trim());
    	
    	Logger.info("pageId: " + pageId);
    	Logger.info("componentId: " + componentId);
    	Logger.info("componentType: " + componentType);  	
    	Logger.info("\nEnd Row");

    	/**/
		
		//getPage.save();
		
    	if (componentId <= 0)
    	{
    		getComponent = Component.create("", code, componentType, classes);
    		getComponent.save();
    	}
    	else
    	{
    		getComponent = Component.find.byId(componentId);
    		getComponent.update(code, classes);
    		getComponent.save();
    	}
		/**/
    	
    	ChildComponent childComponent = ChildComponent
    			.find
    			.where()
    			.eq("child_id", getComponent.id)
    			.findUnique();
    	
    	if (childComponent != null)
    	{
    		childComponent.delete();
    	}
    	
    	PageComponent pageComponent = PageComponent
    			.find
    			.where()
    			.eq("component_id", getComponent.id)
    			.findUnique();
    	
    	if (pageComponent != null)
    	{
    		pageComponent.update(pageId, getComponent.id, displayOrder);
    		pageComponent.save();
    	}
    	else
    	{
    		pageComponent = PageComponent.create(pageId, getComponent.id, displayOrder);
    		pageComponent.save();
    	}
    	
    	return ok(views.html.utility.integerresult.render(getComponent.id));
    }
    
    public static Result updateOrder(String parentType, Integer parentId, String order) throws SQLException
    {
    	
    	Logger.info("parentType: " + parentType);
    	Logger.info("parentId: " + parentId);
    	Logger.info("order: " + order);
    	
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String table = null;
		String parentIdName = null;
		String childIdName = null;
		
		String elements[] = order.split(",");
    	
    	switch (parentType.toLowerCase())
    	{
			case "page":
			{
				table = "page_component";
				parentIdName = "page_id";
				break;
			}
			case "component":
			{
				table = "child_component";

				parentIdName = "parent_id";
				
				break;
			}
    	}
    	
		try
		{
			connection = DB.getConnection();
			
			String clear = "DELETE FROM " + table + " WHERE " + parentIdName + " = ?";
			preparedStatement = connection.prepareStatement(clear);
			preparedStatement.setLong(1, parentId);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();			
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			if (preparedStatement != null)
			{
				preparedStatement.close();
			}
			
			if (connection != null)
			{
				connection.close();
			}
		}
		
		for(int i = 0; i < elements.length; i++)
		{
			try
			{
				Integer value = Integer.parseInt(elements[i]);
				if ( value != null)
				{
					Logger.info("value: " + value);
			    	switch (parentType.toLowerCase())
			    	{
						case "page":
						{
							PageComponent c = new PageComponent(parentId, value, i);
							c.save();
							Logger.info("c" + c.toString());
							break;
						}
						case "component":
						{
							ChildComponent c = new ChildComponent(parentId, value, i);
							c.save();
							Logger.info("c" + c.toString());
							break;
						}
			    	}
					//create(Integer pageId, Integer componentId, Integer displayOrder)
					
				}
			}
			catch(Exception e)
			{
				
			}
		}
    	
    	return ok("updated");
    }

}
