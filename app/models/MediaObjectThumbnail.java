package models;

import java.util.Map;

import javax.persistence.Entity;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import utility.MediaObjectThumbnailType;

@Entity
public class MediaObjectThumbnail extends Model
{
	public Long parentId;
	public String label;
	public Long childId;
	
	public static Finder<Long, MediaObjectThumbnail> find 
	= new Finder<Long, MediaObjectThumbnail>(Long.class, MediaObjectThumbnail.class);
}
