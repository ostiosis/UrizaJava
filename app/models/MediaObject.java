package models;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.DB;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MediaObject extends Model
{
	@Id
	public Long id;
	public String name;
	public String code;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;
	
	@ManyToMany
	@JoinTable(
		      name="component_mediaobject",
		      joinColumns={@JoinColumn(name="component_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="mediaobject_id", referencedColumnName="id")})
	public List<Component> components;
	
	public static Finder<Long, MediaObject> find 
	= new Finder<Long, MediaObject>(Long.class, MediaObject.class);
	
	public MediaObject(String name, String code)
	{
		this.name = name;
		this.code = code;
		
		Calendar calendar = Calendar.getInstance();
		this.dateCreated = new java.sql.Timestamp(calendar.getTimeInMillis());
	}
	
	public static MediaObject create(String fileName, String extension, File file)
	{
		/**
		Page page = new Page(name, title, description);
		page.save();
		page.saveManyToManyAssociations("templates");
		
		return page;
		/**/
		
		
		return null;	
	}
	
	public Map<String, MediaObject> thumbnails() throws SQLException
	{
		Map<String, MediaObject> thumbnails = new HashMap<String, MediaObject>();
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		ResultSet rs = null;
		
		String sql = "SELECT * FROM media_object_thumbnail WHERE parent_id = ?";
		
		try
		{
			connection = DB.getConnection();
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setLong(1, this.id);
			
			rs = preparedStatement.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			if (preparedStatement != null)
			{
				preparedStatement.close();
			}
			
			if (connection != null)
			{
				connection.close();
			}
		}
		
		while (rs.next())
		{
			thumbnails.put(rs.getString("label"), MediaObject.find.byId(rs.getLong("child_id")));
		}
		
		return thumbnails;	
	}
	
	public MediaObject thumbnail(String label) throws SQLException
	{
		MediaObject thumbnail = null;
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		ResultSet rs = null;
		
		String sql = "SELECT * FROM media_object_thumbnail WHERE parent_id = ? AND label = ?";
		
		try
		{
			connection = DB.getConnection();
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setLong(1, this.id);
			preparedStatement.setString(2, label);
			
			rs = preparedStatement.executeQuery();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			if (preparedStatement != null)
			{
				preparedStatement.close();
			}
			
			if (connection != null)
			{
				connection.close();
			}
		}

		thumbnail = find.where().eq("id", rs.getLong("child_id")).findUnique();
		
		return thumbnail;
	}
}
