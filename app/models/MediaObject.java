package models;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.Play;
import play.db.DB;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import utility.MediaObjectThumbnailType;

@Entity
public class MediaObject extends Model
{
	private static String uploadDir = (Play.application().path().getAbsolutePath() + "\\public\\uploads\\");
	
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
	
	public static MediaObject create(String fileName, String extension, File file) throws IOException
	{
		/**
		Page page = new Page(name, title, description);
		page.save();
		page.saveManyToManyAssociations("templates");
		
		return page;
		/**/
		
		String fullFileName = fileName + "." + extension;
		
		BufferedImage image = ImageIO.read(file);
		
		if (ImageIO.write(image, "png", new File(uploadDir  + fullFileName)))
		{
			
			
			MediaObject mediaObject = new MediaObject(fullFileName, fullFileName);
			mediaObject.save();
			mediaObject.saveManyToManyAssociations("components");
			
			Long parentId = mediaObject.id;
			
			for (MediaObjectThumbnailType thumbnailType: MediaObjectThumbnailType.values())
			{
				createThumbnail(parentId, thumbnailType, fileName, extension, image);
			}
			
			return mediaObject;
		}
		
		return null;	
	}
	
	public static void createThumbnail(Long parentId, MediaObjectThumbnailType thumbnailType, String fileName, String extension, BufferedImage image) throws IOException
	{
		String thumbnailFileName = fileName + "_" + thumbnailType.name() + "." + extension;
		
		int type = image.getType();
		
		if (image.getType() == 0)
		{
			type = BufferedImage.TYPE_INT_ARGB;
		}
		
		BufferedImage thumbnail = resizeImage(image, type, thumbnailType.getWidth(), thumbnailType.getHeight());
		
		if (ImageIO.write(thumbnail, "png", new File(uploadDir  + thumbnailFileName)))
		{
			MediaObject mediaObject = new MediaObject(thumbnailFileName, thumbnailFileName);
			mediaObject.save();
			mediaObject.saveManyToManyAssociations("components");
						
			MediaObjectThumbnail objectThumbnail = new MediaObjectThumbnail(parentId, thumbnailType.name(), mediaObject.id);
			objectThumbnail.save();
		}
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer width, Integer height)
	{
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D graphics = resizedImage.createGraphics();
		
		graphics.drawImage(originalImage, 0, 0, width, height, null);
		graphics.dispose();
		
		return resizedImage;
		
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
