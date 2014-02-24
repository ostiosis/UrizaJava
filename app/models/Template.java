package models;


import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import java.sql.Date;
import java.util.List;

@Entity
public class Template extends Model 
{
	@Id
	public Long id;

	public String name;
	public String code;
	
	public Long topPosition;
	public Long leftPosition;
	
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
	public List<Component> components;
	/**/
	
	/**
	@ManyToMany
	@JoinTable(
		      name="page_template",
		      joinColumns={@JoinColumn(name="template_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="page_id", referencedColumnName="id")})
	public List<Page> pages;
	/**/
	
	public Template(String name, String code, Long topPosition, Long leftPosition) 
	{
		this.name = name;
		this.code = code;
		
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}
	
	public static Finder<Long, Template> find 
	= new Finder<Long, Template>(Long.class, Template.class);

	public static Template create(String name, String code, Long topPosition, Long leftPosition)
	{
		Template template = new Template(name, code, topPosition, leftPosition);
		template.save();
		template.saveManyToManyAssociations("components");
		
		return template;
	}
	
	public void update(String name, String code, Long topPosition, Long leftPosition)
	{
		this.name = name;
		this.code = code;
		
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}
	
	public void update(String code, Long topPosition, Long leftPosition)
	{
		this.code = code;
		
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public void update(Long topPosition, Long leftPosition)
	{
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

}
