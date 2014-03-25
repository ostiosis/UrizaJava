package models;

import javax.persistence.*;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import utility.UrizaHelpers;

@Entity
public class ChildComponent extends Model
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7517223511230695777L;

	
	@Id
	public Integer childId;
	public Integer parentId;
	
	public Integer displayOrder;

	
	
	public static Finder<Integer, ChildComponent> find 
	= new Finder<Integer, ChildComponent>(Integer.class, ChildComponent.class);
	
	public ChildComponent(Integer parentId, Integer childId, Integer displayOrder)
	{
		this.parentId = parentId;
		this.childId = childId;
		
		this.displayOrder = displayOrder;
	}
	
	public void update(Integer parentId, Integer childId, Integer displayOrder)
	{
		this.parentId = parentId;
		this.childId = childId;		
		
		this.displayOrder = displayOrder;
	}
	
	public static ChildComponent create(Integer parentId, Integer childId, Integer displayOrder)
	{
		ChildComponent component = new ChildComponent(parentId, childId, displayOrder);
		component.save();
		
		return component;
	}
}
