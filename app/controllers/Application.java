package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.commons.mail.EmailException;

import models.PasswordReset;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import utility.Mailer;
import utility.PasswordHash;
import views.html.*;

public class Application extends Controller 
{
	static boolean TEST_MODE = true;

	@Security.Authenticated(Secured.class)
    public static Result index() 
	{
        return ok(index.render("Your new application is ready."));
    }
	
    public static Result javascriptRoutes()
    {
    	response().setContentType("test/javascript");
    	
    	return ok(Routes.javascriptRouter("jsRoutes", 
    			controllers.routes.javascript.Development.addPage(),
    			controllers.routes.javascript.Development.validatePage(),
    			controllers.routes.javascript.Development.openMenu(),
    			controllers.routes.javascript.Development.uploadAjax(),
    			controllers.routes.javascript.Development.showImageThumbnails(),
    			controllers.routes.javascript.Development.development(),
    			controllers.routes.javascript.Development.getImage(),
    			controllers.routes.javascript.Development.updateComponent(),
    			controllers.routes.javascript.Development.updateOrder(),
    			controllers.routes.javascript.Development.deleteComponent()
		));
    }
    	
    public static Result login()
    {
    	session().clear();
    	return ok(login.render(Form.form(Login.class)));
    }
    
    public static Result authenticate()
    {
    	Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
    	
    	if (loginForm.hasErrors())
    	{
    		return badRequest(login.render(loginForm));
    	}
    	else
    	{
    		session().clear();
    		session("email", loginForm.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    
    public static Result logout()
    {
    	session().clear();
    	flash("success", "You've been logged out");
    	return redirect(routes.Application.login());
    }

    public static class Login
    {
    	public String email;
    	public String passwordHash;
    	
    	public String validate()
    	{
    		if (User.authenticate(email, passwordHash) == null)
    		{
    			return "Invalid user or password: " + email + " " + passwordHash;
    		}
    		
    		return null;
    	}    	
    }
 
    //Password Reset
    
    public static Result reset()
    {
    	session().clear();
    	return ok(reset.render(Form.form(Reset.class)));
    }
    
    
    public static Result resetForm()
    {
    	Form<Reset> resetForm = Form.form(Reset.class).bindFromRequest();
    	
    	if (resetForm.hasErrors())
    	{
    		return badRequest(reset.render(resetForm));
    	}
    	else
    	{
			String email = resetForm.get().email;
    		String token = "";
    		
    		try
			{
    			token = PasswordReset.createToken(email);
    			Logger.debug(token);
			} 
    		catch (SQLException e)
			{
				// TODO Auto-generated catch block
				Logger.error(e.getMessage());
			}
    		
    		Mailer mailer = new Mailer();
    		
    		try
			{
    			if(TEST_MODE)
    			{
    				mailer.sendMail("prinnypayday@gmail.com", "prinnypayday@gmail.com", "Reset Token", token);    				
    			}
    			else
    			{
    				mailer.sendMail("prinnypayday@gmail.com", email, "Reset Token", token);    				    				
    			}
			} 
    		catch (EmailException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		session().clear();
    		session("email", resetForm.get().email);
    		return redirect(routes.Application.token());
    	}
    }

    
    public static class Reset
    {
    	public String email;
    	
    	public String validate()
    	{
    		if (User.authenticate(email) == null)
    		{
    			return "Invalid email: " + email;
    		}
    		
    		return null;
    	}   	
    }
    
    //Password Token
    
    public static Result token()
    {
    	System.out.println(session().get("email"));
    	session().clear();
    	return ok(token.render(Form.form(Token.class)));
    }
    
    public static Result tokenForm()
    {
    	Form<Token> tokenForm = Form.form(Token.class).bindFromRequest();
    	
    	if (tokenForm.hasErrors())
    	{
    		return badRequest(token.render(tokenForm));
    	}
    	else
    	{    		
    		session().clear();
    		session("email", tokenForm.get().email);
    		return redirect(routes.Application.password());
    	}
    }
    
    public static class Token
    {
    	public String email;
    	public String token;
    	    	
    	public String validate()
    	{
    		if (PasswordReset.authenticate(email, token) == null)
    		{
    			return "Invalid email: " + email;
    		}
    		
    		return null;
    	}   	
    }
    
    //Password
    
    public static Result password()
    {
    	if(session().get("email") == null)
    	{
    		return redirect(routes.Application.login());
    	}
    	return ok(password.render(Form.form(Password.class)));
    }
    
    
    public static Result passwordForm() throws NoSuchAlgorithmException, InvalidKeySpecException
    {    	    	
    	Form<Password> passwordForm = Form.form(Password.class).bindFromRequest();    	   	
    	
    	
    	
    	if (passwordForm.hasErrors())
    	{
    		return badRequest(password.render(passwordForm));
    	}
    	else
    	{
        	//System.out.println(passwordForm.field("password").value());
        	//System.out.println(session().get("email"));
        	
        	try
			{
        		String email = session().get("email");
        		String passwordHash = PasswordHash.createHash(PasswordHash.createHash(passwordForm.field("password").value()));
				User.resetPassword(email, passwordHash);
			} 
    		catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		flash("success", "password reset");
   		   		
    		session("email", session().get("email"));
    		return redirect(routes.Application.login());
    	}
    	/**/
    }
    
    public static class Password
    {
    	public String password;
    	public String confirmPassword;
    	
    	
    	public String validate()
    	{
    		//System.out.println(password);
    		//System.out.println(confirmPassword);
    		
    		if (!password.equals(confirmPassword) || password.isEmpty() || password == null)
    		{
    			return "passwords do not match";
    		}
    		
    		return null;
    	}
    	
    }    
}
