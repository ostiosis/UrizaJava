package models;


import java.sql.Date;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
public class PasswordReset extends Model
{
	@OneToOne
	User user;
	
	public String token;
	
	public Date expires;
	
	public static Finder<Long, PasswordReset> find 
	= new Finder<Long, PasswordReset>(Long.class, PasswordReset.class);
	
	public static PasswordReset authenticate(String email, String token)
	{
		return find.where()
				.eq("email", email)
				.eq("token", token)
				.findUnique();
	}
	
	public static createToken(String email)
	{
		
	}
}
