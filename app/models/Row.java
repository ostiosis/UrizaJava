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
public class Row extends Model
{
	@Id
	public Long id;
	public String name;
	public String code;
	
	public Date dateCreated;
	public Date dateModified;
	
	@ManyToOne
	public Template template;
	
	@ManyToMany
	@JoinTable(
		      name="row_component",
		      joinColumns={@JoinColumn(name="row_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="component_id", referencedColumnName="id")})
	public List<Component> components;
}
