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
		
		List<MediaObjectThumbnail> test = MediaObjectThumbnail.find.where().eq("parent_id", this.id).findList();
		
		for (MediaObjectThumbnail m: test)
		{
			thumbnails.put(m.label, MediaObject.find.byId(m.childId));
		}

		
		return thumbnails;	
	}
	
	public MediaObject thumbnail(String label) throws SQLException
	{		
		MediaObject thumbnail = null;
		
		MediaObjectThumbnail child = MediaObjectThumbnail.find.where().eq("parent_id", this.id).eq("label", label).findUnique();

		thumbnail = find.where().eq("id", child.childId).findUnique();
		
		return thumbnail;
	}
}
