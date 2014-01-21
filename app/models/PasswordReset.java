package models;


import java.sql.Date;

import javax.persistence.*;

import play.db.DB;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import java.sql.*;
import java.util.Calendar;

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
		System.out.println(email);
		System.out.println(token);
		
		User user = User.find.where().eq("email", email).findUnique();
		
		return find.where()
				.eq("user_id", user.id)
				.eq("token", token)
				.findUnique();
	}
	
	public static String createToken(String email) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		User user = User.find.where().eq("email", email).findUnique();
		String token = "test";
		String sql = "INSERT INTO password_reset (user_id, token, expires) VALUES (?, ?, ?)";
		
		try
		{
			connection = DB.getConnection();
			
			String clear = "DELETE FROM password_reset WHERE user_id = ?";
			preparedStatement = connection.prepareStatement(clear);
			preparedStatement.setLong(1, user.id);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setLong(1, user.id);
			preparedStatement.setString(2, token);
			preparedStatement.setTimestamp(3, getExpiresTimeStamp());
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			if (preparedStatement != null)
			{
				preparedStatement.close();
			}
			
			if (connection != null)
			{
				connection.close();
			}
		}
		
		return token;
	}
	
	private static java.sql.Timestamp getExpiresTimeStamp()
	{
		Integer timeToExpire = 15;
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, timeToExpire);
		//java.util.Date expires = calendar.getTime();
		
		return new java.sql.Timestamp(calendar.getTimeInMillis());
	}
}
