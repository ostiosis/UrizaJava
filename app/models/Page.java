package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
public class Page extends Model 
{
	@Id
	public Long id;
	
	public String name;
	public String title;
	public String description;
	
	@ManyToOne
	User user;
	
	public Date dateCreated;
	public Date dateModified;
	
	@ManyToMany
	@JoinTable(
		      name="page_template",
		      joinColumns={@JoinColumn(name="page_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="template_id", referencedColumnName="id")})
	public List<Template> templates;

	public static Finder<Long, Page> find 
	= new Finder<Long, Page>(Long.class, Page.class);
	
	public static Page getPage(String name)
	{
		return find.where()
				.eq("name", name)
				.findUnique();
	}
}
