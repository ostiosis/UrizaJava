package models;

import javax.persistence.*;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

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
	
	public Integer displayOrder;
	
	
	public static Finder<Integer, PageComponent> find 
	= new Finder<Integer, PageComponent>(Integer.class, PageComponent.class);
	
	public PageComponent(Integer pageId, Integer componentId, Integer displayOrder)
	{
		this.pageId = pageId;
		this.componentId = componentId;
		
		this.displayOrder = displayOrder;
	}
	
	public void update(Integer pageId, Integer componentId, Integer displayOrder)
	{
		this.pageId = pageId;
		this.componentId = componentId;		
		
		this.displayOrder = displayOrder;
	}
	
	public static PageComponent create(Integer pageId, Integer componentId, Integer displayOrder)
	{
		PageComponent component = new PageComponent(pageId, componentId, displayOrder);
		component.save();
		
		return component;
	}
}
