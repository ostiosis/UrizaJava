package models;

import javax.persistence.*;

import play.db.ebean.Model;

/**
 * 
 * @author Philip Lipman
 *
 */
@Entity
public class TemplateComponent extends Model
{
	private static final long serialVersionUID = 7517223511230695777L;

	public Integer templateId;
	
	@Id
	public Integer componentId;
	
	public static Finder<Integer, TemplateComponent> find 
	= new Finder<Integer, TemplateComponent>(Integer.class, 
			TemplateComponent.class);
	
	/**
	 * 
	 * @param templateId
	 * @param componentId
	 */
	public TemplateComponent(Integer templateId, Integer componentId)
	{
		this.templateId = templateId;
		this.componentId = componentId;
	}
	
	/**
	 * 
	 * @param templateId
	 * @param componentId
	 */
	public void update(Integer templateId, Integer componentId)
	{
		this.templateId = templateId;
		this.componentId = componentId;	
	}
	
	/**
	 * 
	 * @param templateId
	 * @param componentId
	 * @return
	 */
	public static TemplateComponent create(Integer templateId, 
			Integer componentId)
	{
		TemplateComponent component = 
				new TemplateComponent(templateId, componentId);
		component.save();
		
		return component;
	}
}
