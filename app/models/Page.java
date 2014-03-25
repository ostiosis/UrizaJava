package models;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import utility.UrizaHelpers;

@Entity
public class Page extends Model 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9065865930662266632L;

	@Id
	public Long id;
	
	public String name;
	public String title;
	public String description;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;

	public Page(String name, String title, String description)
	{
		this.name = name;
		this.title = title;
		this.description = description;
	}
	
	public static Finder<Long, Page> find 
	= new Finder<Long, Page>(Long.class, Page.class);
	
	public static Page getPage(String name)
	{		
		/**/
		return find.where()
				.eq("name", name)
				.findUnique();
		/**/
	}
	
	public static Page create(String name, String title, String description)
	{
		Page page = new Page(name, title, description);
		page.dateCreated = UrizaHelpers.getTime();
		page.save();
		
		return page;
	}
	
	public static List<Page> pages()
	{
		return find.findList();
	}
	
	public List<Component> components()
	{			
		List<Component> components = new ArrayList<Component>();
		
		List<PageComponent> componentIds = PageComponent.find.where().eq("page_id", this.id).orderBy("display_order asc").findList();
		
		Logger.info("Phil Test: " + componentIds.size());
		
		for (PageComponent c: componentIds)
		{
			components.add(Component.find.byId(c.componentId));
		}
		
		//Logger.info("Phil Test2: " + components.get(0).classes);
		
		return components;	
	}
}
