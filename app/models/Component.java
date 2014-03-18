package models;


import javax.persistence.*;

import org.apache.commons.lang3.StringEscapeUtils;

import play.Logger;
import play.db.ebean.*;
import utility.UrizaHelpers;

import java.sql.Date;
import java.sql.Timestamp;
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
	public Long displayOrder;
	
	public String name;
	
	@Column(columnDefinition = "text")
	public String code;
	
	public String componentType;
	
	public Long topPosition;
	public Long leftPosition;
	
	public Long width;
	public Long height;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;
	
	@OneToMany(cascade=CascadeType.ALL)
	@OrderBy("display_order")
	public List<Component> children;

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
		component.dateCreated = UrizaHelpers.getTime();
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
		
		this.dateModified = UrizaHelpers.getTime();
	}
	
	public void update(String code, Long width, Long height)
	{
		this.code = code;
		
		this.width = width;
		this.height = height;
		
		this.dateModified = UrizaHelpers.getTime();
	}

	public void update(Long width, Long height)
	{
		this.width = width;
		this.height = height;
		
		this.dateModified = UrizaHelpers.getTime();
	}
}
