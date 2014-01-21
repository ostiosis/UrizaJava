package controllers;

import models.PasswordReset;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller 
{

	@Security.Authenticated(Secured.class)
    public static Result index() 
	{
        return ok(index.render("Your new application is ready."));
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
    
    
    public static Result tokenForm2()
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
    		//if (PasswordReset.authenticate(email, token) == null)
    		//{
    		//	return "Invalid email: " + email;
    		//}
    		
    		return "test";
    	}
    	
    }
    
    //Password
    
    public static Result password()
    {
    	return ok(password.render(Form.form(Password.class)));
    }
    
    
    public static Result passwordForm()
    {
    	System.out.println(session().get("email"));
    	
    	Form<Password> passwordForm = Form.form(Password.class).bindFromRequest();
    	
    	if (passwordForm.hasErrors())
    	{
    		return badRequest(password.render(passwordForm));
    	}
    	else
    	{
    		session().clear();
    		session("email", session().get("email"));
    		return redirect(routes.Application.login());
    	}
    }

    
    public static class Password
    {
    	public String password;
    	public String confirmPassword;
    	
    	
    	public String validate()
    	{
    		if (password.equals(confirmPassword))
    		{
    			return "passwords do not match";
    		}
    		
    		return null;
    	}
    	
    }
    
}
