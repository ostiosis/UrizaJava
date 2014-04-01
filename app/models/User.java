package models;

import javax.persistence.*;

import play.db.DB;
import play.db.ebean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Entity
public class User extends Model
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1798862127077049477L;
	@Id
	public Long id;
	public String username;
	public String email;
	public String passwordHash;
	
	public Timestamp joinDate;
	
	public static Finder<Long, User> find 
		= new Finder<Long, User>(Long.class, User.class);
	
	public User(String username, String email, String password)
	{
		this.username = username;
		this.email = email;
	}
	
	public static User authenticate(String email, String passwordHash)
	{
		return find.where()
				.eq("email", email)
				.eq("password_hash", passwordHash)
				.findUnique();
	}
	
	public static User authenticate(String email)
	{
		return find.where()
				.eq("email", email)
				.findUnique();
	}
	
	public static void resetPassword(String email, String password) throws SQLException
	{	
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		User user = User.find.where().eq("email", email).findUnique();
		String sql = "UPDATE user SET password_hash = ? WHERE id = ?";
		
		try
		{
			connection = DB.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, password);
			preparedStatement.setLong(2, user.id);
			
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
	}
}
