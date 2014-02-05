package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class MediaObject extends Model
{
	@Id
	public Long id;
	public String name;
	public String code;
	
	@ManyToOne
	User user;
	
	public Date dateCreated;
	public Date dateModified;
	
	@ManyToMany
	@JoinTable(
		      name="component_mediaobject",
		      joinColumns={@JoinColumn(name="component_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="mediaobject_id", referencedColumnName="id")})
	public List<Component> components;

}
