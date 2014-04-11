package models;


import javax.persistence.*;

import play.db.ebean.*;
import utility.UrizaHelpers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Philip Lipman
 *
 */
@Entity
public class Template extends Model 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2150265888934939399L;

	@Id
	public Integer id;

	public String name;
	public String code;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;
	
	public Template(String name, String code) 
	{
		this.name = name;
		this.code = code;
	}
	
	public static Finder<Long, Template> find 
	= new Finder<Long, Template>(Long.class, Template.class);

	public static Template create(String name, String code)
	{
		Template template = new Template(name, code);
		template.dateCreated = UrizaHelpers.getTime();
		template.save();
		
		Component main = Component.create(name + "Component", 
				"", 
				"template", 
				"custom-template");
		TemplateComponent.create(template.id, main.id);
		
		return template;
	}
	
	public void update(String name, String code)
	{
		this.name = name;
		this.code = code;
		
		this.dateModified = UrizaHelpers.getTime();
	}

	public static Template getTemplate(String name)
	{		
		/**/
		return find.where()
				.eq("name", name)
				.findUnique();
		/**/
	}
	
	public List<Component> components()
	{			
		List<Component> components = new ArrayList<Component>();
		
		List<TemplateComponent> componentIds = 
				TemplateComponent
				.find
				.where()
				.eq("page_id", this.id)
				.findList();
				
		for (TemplateComponent c: componentIds)
		{
			components.add(Component.find.byId(c.componentId));
		}
		return components;	
	}
}
