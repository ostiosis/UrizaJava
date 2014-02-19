package models;


import javax.persistence.*;

import play.db.ebean.*;

import java.sql.Date;
import java.util.List;

@Entity
public class Component extends Model 
{
	@Id
	public Long id;
	public String name;
	public String code;
	
	
	public Long columnWidth;
	public Long offset;
	public Long height;
	
	@ManyToOne
	User user;
	
	public Date dateCreated;
	public Date dateModified;
	
	@ManyToMany
	@JoinTable(
		      name="row_component",
		      joinColumns={@JoinColumn(name="row_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="component_id", referencedColumnName="id")})
	public List<Row> rows;
	
	
}
