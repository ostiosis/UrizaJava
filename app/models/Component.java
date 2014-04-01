package models;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import play.Logger;
import play.db.DB;
import play.db.ebean.Model;
import utility.UrizaHelpers;

@Entity
public class Component extends Model 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7757858881784856402L;

	@Id
	public Integer id;
	
	public String name;
	
	@Column(columnDefinition = "text")
	public String code;
	
	@Column(columnDefinition = "text")
	public String classes;
	
	public String componentType;
	
	public Long topPosition;
	public Long leftPosition;
	
	public Long width;
	public Long height;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;

	public Component(String name, String code, String componentType, Long width, Long height)
	{
		this.name = name;
		this.code = code;
		
		this.componentType = componentType.toLowerCase();
		
		this.width = width;
		this.height = height;
	}
	
	public Component(String name, String code, String componentType, String classes)
	{
		this.name = name;
		this.code = code;
		this.classes = classes;
		
		this.componentType = componentType.toLowerCase();
	}
	
	public static Finder<Integer, Component> find 
	= new Finder<Integer, Component>(Integer.class, Component.class);

	public static Component create(String name, String code, String componentType, String classes)
	{
		Component component = new Component(name, code, componentType, classes);
		component.dateCreated = UrizaHelpers.getTime();
		component.save();
		
		return component;
	}
	
	public static void delete(Component component) throws SQLException
	{	
		List<Integer> ids = new ArrayList<Integer>();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		ids.addAll(relatedIds(component.id));
		
		Logger.info("ids: " + ids.toString());
			
		connection = DB.getConnection();
		
		String down = "DELETE FROM child_component WHERE parent_id = ? OR child_id = ?";
		preparedStatement = connection.prepareStatement(down);
		
		for (int i = 0; i < ids.size(); i++)
		{
			preparedStatement.setLong(1, ids.get(i));
			preparedStatement.setLong(2, ids.get(i));	
			preparedStatement.addBatch();
		}
		preparedStatement.executeUpdate();
		
		down = "DELETE FROM component WHERE id = ?";
		preparedStatement = connection.prepareStatement(down);
		
		for (int i = 0; i < ids.size(); i++)
		{
			preparedStatement.setLong(1, ids.get(i));	
			preparedStatement.addBatch();
		}
		preparedStatement.executeUpdate();
	}
	
	public static List<Integer> relatedIds(Integer parentId)
	{
		List<Integer> ids = new ArrayList<Integer>();
		
		List<ChildComponent> childIds = ChildComponent.find.where().eq("parent_id", parentId).findList();
		
		for(ChildComponent c: childIds)
		{
			ids.addAll(relatedIds(c.childId));
		}
		
		ids.add(parentId);
		
		return ids;
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
	
	public void update(String code, String classes)
	{
		this.code = code;
		this.classes = classes;
						
		this.dateModified = UrizaHelpers.getTime();
	}


	public void update(Long width, Long height)
	{
		this.width = width;
		this.height = height;
		
		this.dateModified = UrizaHelpers.getTime();
	}
	
	public List<Component> children() throws SQLException
	{		
		List<Component> children = new ArrayList<Component>();
		
		List<ChildComponent> childIds = ChildComponent.find.where().eq("parent_id", this.id).orderBy("display_order asc").findList();
		
		for (ChildComponent c: childIds)
		{
			children.add(Component.find.byId(c.childId));
		}
	
		return children;	
	}

}
