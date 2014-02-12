package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;

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
		Page getPage = find.setMaxRows(1).findUnique();
		
			
		/**
		return find.where()
				.eq("name", name)
				.findUnique();
		/**/
		return getPage;
	}
	
	public static Page create(String name, String title, String description)
	{
		Page page = new Page(name, title, description);
		page.save();
		page.saveManyToManyAssociations("templates");
		
		return page;
	}
	
	public static List<Page> pages()
	{
		return find.findList();
	}
}
