package models;

import javax.persistence.Entity;

import play.db.ebean.Model;

/**
 * 
 * @author Philip Lipman
 *
 */
@Entity
public class MediaObjectThumbnail extends Model
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -968345188282491238L;
	
	public Long parentId;
	public String label;
	public Long childId;
	
	public static Finder<Long, MediaObjectThumbnail> find 
	= new Finder<Long, MediaObjectThumbnail>(Long.class, 
			MediaObjectThumbnail.class);
	
	/**
	 * 
	 * @param parentId
	 * @param label
	 * @param childId
	 */
	public MediaObjectThumbnail(Long parentId, String label, Long childId)
	{
		this.parentId = parentId;
		this.label = label;
		this.childId = childId;
	}
}
