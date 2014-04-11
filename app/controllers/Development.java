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
import play.Logger;
import play.db.DB;
import play.mvc.*;
import utility.MediaHelpers;
import utility.UrizaHelpers;

/**
 * Controls the development area
 * 
 * @author Philip Lipman
 *
 */
public class Development extends Controller
{
	/**
	 * returns current working page
	 * @param name - name of page to load
	 * @return
	 */
	private static boolean LOGGING = true;
	
    public static Result development(String name) 
	{
    	Page getPage = Page.getPage(name);
    	return ok(views.html.development.development.render(getPage));        
    }
    
    /**
     * if page exists send error message
     * @param name - name of page to check
     * @return
     */
    public static Result validatePage(String name)
	{    	    	
    	int check = Page.find.where().eq("name", name).findRowCount();
    	
		if (check > 0)
		{
			return ok("Error: page " + name + " already exists");  
		}
		return ok();
	}
    
    /**
     * creates new page
     * @param name - name of page
     * @param title - replaces text in <title> meta tag
     * @param description - replaces text in <description> meta tag 
     * @return - development with page loaded
     */
    public static Result addPage(String name, String title, String description)
	{    	    	
    	Page newPage = Page.create(
			name, 
			title, 
			description
		);
		
    	return ok(views.html.development.development.render(newPage));        

	}
    
    /**
     * opens a list of all existing pages
     * @return
     */
    public static Result openMenu()
    {
    	return ok(views.html.development.open.render(Page.pages()));
    }

    /**
     * upload file
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Result uploadAjax(String fileName) throws IOException
    {    	   	
    	String extension = FilenameUtils.getExtension(fileName);
    	String name = FilenameUtils.removeExtension(fileName);
    	
    	File file = null;

		file = request().body().asRaw().asFile();
		   	
    	InputStream is = null;
    	
		is = new BufferedInputStream(new FileInputStream(file));
		
		//String mimeType = URLConnection.guessContentTypeFromStream(is);
		String mimeType = MediaHelpers.guessContentTypeFromStream(is);
		
		if (LOGGING)
		{
			Logger.info(mimeType);
		}
		
		if (mimeType != null && mimeType.contains("image"))
		{
			MediaObject.create(name, extension, file);
		}

		file.delete();

		return ok("File uploaded");    	
		
    }
    
    /**
     * gets image thumbnail based on label
     * @param label
     * @return
     * @throws SQLException
     */
    public static Result showImageThumbnails(String label) throws SQLException
    {
    	List<MediaObject> thumbnails = MediaObject.thumbnailList(label);
    	
    	return ok(views.html.development.mediathumbnail.render(thumbnails));    	
    }
    
    /**
     * gets image thumbnail associated with it
     * @param imageId - parent image id
     * @param label - label based on utility.MediaObjectThumbnailType
     * @return
     */
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
    
    /**
     * updates component
     * @param componentId
     * @param parentId
     * @param componentType
     * @param classes
     * @param code
     * @param displayOrder
     * @return
     */
    public static Result updateComponent(Integer componentId, 
    		Integer parentId, 
    		String componentType, 
    		String classes, 
    		String code, 
    		Integer displayOrder)
    {
    	code = code.trim();
    	classes = UrizaHelpers.classCleanup(classes.trim());
    	
    	Component getComponent = null;
    	
    	if (LOGGING)
    	{
	    	Logger.info("\nBegin Row");
	    	Logger.info("code: " + code.trim());
	    	Logger.info("classes: " + classes.trim());
	    	
	    	Logger.info("parentId: " + parentId);
	    	Logger.info("componentId: " + componentId);
	    	Logger.info("componentType: " + componentType);  	
	    	Logger.info("\nEnd Row");
    	}
    	
    	if (componentId <= 0)
    	{
    		getComponent = Component.create("", code, componentType, classes);
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
    		childComponent = ChildComponent.create(parentId, 
    				getComponent.id, 
    				displayOrder);
    		
    		childComponent.save();
    	}
    	
    	childComponent.parentId = parentId;
    	
    	return ok(views.html.utility.integerresult.render(getComponent.id));
    }

    /**
     * update component display order
     * @param parentId
     * @param order
     * @return
     * @throws SQLException
     */
    public static Result updateOrder(Integer parentId, String order) 
    		throws SQLException
    {    	
    	if (LOGGING)
    	{
	    	Logger.info("parentId: " + parentId);
	    	Logger.info("order: " + order);
    	}
    	
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String elements[] = order.split(",");

		try
		{
			connection = DB.getConnection();
			
			String clear = "DELETE FROM child_component WHERE parent_id = ?";
			
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
			Integer value = Integer.parseInt(elements[i]);
			if ( value != null)
			{
				ChildComponent c = new ChildComponent(parentId, value, i);
				c.save();
			}
		}
    	
    	return ok("updated");
    }
    
    /**
     * delete component
     * @param id
     * @return
     * @throws SQLException
     */
    public static Result deleteComponent(Integer id) throws SQLException
    {
    	Component component = Component.find.byId(id);
    	
    	Component.delete(component);
    	
    	return ok("updated");
    }
    
    /**
     * open page
     * @param pageId
     * @param preview
     * @return
     */
    public static Result open(Integer pageId, Boolean preview)
    {
    	Page getPage = Page.find.byId(pageId);
    	
    	//development preview, shows what final page will look like
    	if (preview)
    	{
        	return ok(
    			views.html.development.developmentpreview.render(getPage));    		
    	}   	
    	
    	return ok(views.html.development.developmentpage.render(getPage));
    }
    
}
