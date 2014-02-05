package models;


import javax.persistence.*;

import play.db.ebean.*;

import java.sql.Date;
import java.util.List;

@Entity
public class Template extends Model 
{
	@Id
	public Long id;
	public String name;
	public String code;
	
	@ManyToOne
	User user;
	
	public Date dateCreated;
	public Date dateModified;
	
	@OneToMany
	public List<Row> rows;
	
	/**
	@ManyToMany
	@JoinTable(
		      name="template_component",
		      joinColumns={@JoinColumn(name="template_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="component_id", referencedColumnName="id")})
	public List<Component> components;
	/**/
	
	@ManyToMany
	@JoinTable(
		      name="page_template",
		      joinColumns={@JoinColumn(name="template_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="page_id", referencedColumnName="id")})
	public List<Page> pages;
	
	
}
