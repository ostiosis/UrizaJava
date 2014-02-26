package models;


import javax.persistence.*;

import org.apache.commons.lang3.StringEscapeUtils;

import play.Logger;
import play.db.ebean.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Component extends Model 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7757858881784856402L;

	@Id
	public Long id;
	
	public String name;
	public String code;
	
	public String componentType;
	
	public Long width;
	public Long height;
	
	@ManyToOne
	User user;
	
	public Date dateCreated;
	public Date dateModified;
	
	/**/
	@ManyToMany
	@JoinTable(
		      name="template_component",
		      joinColumns={@JoinColumn(name="template_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="component_id", referencedColumnName="id")})
	public List<Template> templates;
	/**/
	
	/**
	@ManyToMany
	@JoinTable(
		      name="row_component",
		      joinColumns={@JoinColumn(name="row_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="component_id", referencedColumnName="id")})
	public List<Row> rows;
	/**/

	public Component(String name, String code, String componentType, Long width, Long height)
	{
		this.name = name;
		this.code = code;
		
		this.componentType = componentType.toLowerCase();
		
		this.width = width;
		this.height = height;
	}
	
	public static Finder<Long, Component> find 
	= new Finder<Long, Component>(Long.class, Component.class);

	public static Component create(String name, String code, String componentType, Long width, Long height)
	{
		Component component = new Component(name, code, componentType, width, height);
		component.save();
		component.saveManyToManyAssociations("templates");
		
		return component;

	}
	
	public void update(String name, String code, Long width, Long height)
	{
		this.name = name;
		this.code = code;
		
		this.width = width;
		this.height = height;
	}
	
	public void update(String code, Long width, Long height)
	{
		this.code = code;
		
		this.width = width;
		this.height = height;
	}

	public void update(Long width, Long height)
	{
		this.width = width;
		this.height = height;
	}
	
	public String unEscape()
	{
		Logger.info(StringEscapeUtils.unescapeHtml4(this.code));
		return "<b>TEST CODE</b>";
	}

}
