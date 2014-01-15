package models;

import java.sql.Date;

import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class User extends Model
{
	@Id
	public Long id;
	public String username;
	public String email;
	public String passwordHash;
	
	public Date joinDate;
	
	public static Finder<Long, User> find 
		= new Finder<Long, User>(Long.class, User.class);
}
