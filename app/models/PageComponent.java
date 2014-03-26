package models;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class PageComponent extends Model
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7517223511230695777L;

	public Integer pageId;
	
	@Id
	public Integer componentId;
	
	public static Finder<Integer, PageComponent> find 
	= new Finder<Integer, PageComponent>(Integer.class, PageComponent.class);
	
	public PageComponent(Integer pageId, Integer componentId)
	{
		this.pageId = pageId;
		this.componentId = componentId;
	}
	
	public void update(Integer pageId, Integer componentId)
	{
		this.pageId = pageId;
		this.componentId = componentId;	
	}
	
	public static PageComponent create(Integer pageId, Integer componentId)
	{
		PageComponent component = new PageComponent(pageId, componentId);
		component.save();
		
		return component;
	}
}
