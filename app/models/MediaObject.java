package models;
/**
 * TODO: refactor into this class and Image subclass
 */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.Logger;
import play.Play;
import play.db.ebean.Model;
import utility.MediaObjectThumbnailType;

/**
 * image process
 * TODO: all media objects
 * @author Philip Lipman
 *
 */
@Entity
public class MediaObject extends Model
{
	private static final long serialVersionUID = -7341090239191464746L;

	private static String uploadDir = 
			(Play.application().path().getAbsolutePath() 
					+ "\\public\\uploads\\");
	
	@Id
	public Long id;
	public String name;
	public String code;
	
	@ManyToOne
	User user;
	
	public Timestamp dateCreated;
	public Timestamp dateModified;
	
	public static Finder<Long, MediaObject> find 
	= new Finder<Long, MediaObject>(Long.class, MediaObject.class);
	
	/**
	 * 
	 * @param name
	 * @param code
	 */
	public MediaObject(String name, String code)
	{
		this.name = name;
		this.code = code;
		
		Calendar calendar = Calendar.getInstance();
		this.dateCreated = new java.sql.Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 * 
	 * @param fileName
	 * @param extension
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static MediaObject create(String fileName, 
			String extension, 
			File file) throws IOException
	{
		String fullFileName = fileName + "." + extension;
		
		BufferedImage image = ImageIO.read(file);
		
		if (ImageIO.write(image, "png", new File(uploadDir  + fullFileName)))
		{
			
			
			MediaObject mediaObject = 
					new MediaObject(fullFileName, fullFileName);
			
			mediaObject.save();
			
			Long parentId = mediaObject.id;
			
			for (MediaObjectThumbnailType thumbnailType: 
				MediaObjectThumbnailType.values())
			{
				createThumbnail(parentId, 
						thumbnailType, 
						fileName, 
						extension, 
						image);
			}
			
			return mediaObject;
		}
		
		return null;	
	}
	
	/**
	 * 
	 * @param parentId
	 * @param thumbnailType
	 * @param fileName
	 * @param extension
	 * @param image
	 * @throws IOException
	 */
	public static void createThumbnail(Long parentId, 
			MediaObjectThumbnailType thumbnailType, 
			String fileName, 
			String extension, 
			BufferedImage image) throws IOException
	{
		String thumbnailFileName = fileName 
				+ "_" 
				+ thumbnailType.name() 
				+ "." + extension;
		
		int type = image.getType();
		
		Integer scaleWidth = 0;
		Integer scaleHeight = 0;
		
		if (image.getType() == 0)
		{
			type = BufferedImage.TYPE_INT_ARGB;
		}
		
		if(image.getWidth() < image.getHeight())
		{
			
			scaleWidth = thumbnailType.getWidth();
			scaleHeight = (int)(image.getHeight() * 
					(thumbnailType.getWidth().floatValue()/(float)image.getWidth()));
		}
		else
		{
			scaleHeight = thumbnailType.getHeight();
			scaleWidth = (int)(image.getWidth() * 
					(thumbnailType.getHeight().floatValue()/(float)image.getHeight()));

			Logger.info("scaleWidth2: " + scaleWidth);
			Logger.info("scaleHeight2: " + scaleHeight);

		}
		
		BufferedImage thumbnail = resizeImage(image, 
				type, 
				scaleWidth, 
				scaleHeight);
		
		if (ImageIO.write(thumbnail, 
				"png", 
				new File(uploadDir  + thumbnailFileName)))
		{
			MediaObject mediaObject = 
					new MediaObject(thumbnailFileName, thumbnailFileName);
			
			mediaObject.save();
						
			MediaObjectThumbnail objectThumbnail = 
					new MediaObjectThumbnail(parentId, 
							thumbnailType.name(), 
							mediaObject.id);
			
			objectThumbnail.save();
		}
	}
	
	/**
	 * resize image
	 * @param originalImage
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 */
	private static BufferedImage resizeImage(BufferedImage originalImage, 
			int type, 
			Integer width, 
			Integer height)
	{
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D graphics = resizedImage.createGraphics();
		
		graphics.drawImage(originalImage, 0, 0, width, height, null);
		graphics.dispose();
		
		return resizedImage;
		
	}
	
	/**
	 * map key is label, value is MediaObject
	 * @return
	 * @throws SQLException
	 */
	public Map<String, MediaObject> thumbnails() throws SQLException
	{	
		Map<String, MediaObject> thumbnails = 
				new HashMap<String, MediaObject>();
		
		List<MediaObjectThumbnail> thumbnailList = 
				MediaObjectThumbnail
				.find.where()
				.eq("parent_id", this.id)
				.findList();
		
		for (MediaObjectThumbnail m: thumbnailList)
		{
			thumbnails.put(m.label.toLowerCase(), 
					MediaObject.find.byId(m.childId));
		}
		
		return thumbnails;	
	}
	
	/**
	 * finds thumbnail
	 * @param label - type of thumbnail to return
	 * @return
	 * @throws SQLException
	 */
	public MediaObject thumbnail(String label) throws SQLException
	{		
		MediaObject thumbnail = null;
		
		MediaObjectThumbnail reference = 
				MediaObjectThumbnail
				.find
				.where()
				.eq("parent_id", this.id)
				.eq("label", label)
				.findUnique();

		thumbnail = find
				.where()
				.eq("id", reference.childId)
				.findUnique();
		
		return thumbnail;
	}
	
	/**
	 * MediaObject's parent 
	 * @return
	 * @throws SQLException
	 */
	public MediaObject parent() throws SQLException
	{		
		MediaObject parent = null;
		
		MediaObjectThumbnail reference = 
				MediaObjectThumbnail
				.find
				.where()
				.eq("child_id", this.id)
				.findUnique();

		parent = find.where().eq("id", reference.parentId).findUnique();
		
		return parent;
	}
	
	/**
	 * returns image thumbnails
	 * @param label
	 * @return
	 * @throws SQLException
	 */
	public static List<MediaObject> thumbnailList(String label) 
			throws SQLException
	{		
		List<MediaObject> thumbnails = new ArrayList<MediaObject>();
		
		List<MediaObjectThumbnail> thumbnailList = 
				MediaObjectThumbnail
				.find
				.where()
				.eq("label", label)
				.findList();

		for (MediaObjectThumbnail m: thumbnailList)
		{
			thumbnails.add(MediaObject.find.byId(m.childId));
		}
		
		return thumbnails;
	}
}
