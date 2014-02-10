package models;

import java.util.Map;

import javax.persistence.Entity;

import play.db.ebean.Model;
import utility.MediaObjectThumbnailType;

@Entity
public class MediaObjectThumbnail extends Model
{
	public Long parentId;
	public String label;
	public Long childId;
}
