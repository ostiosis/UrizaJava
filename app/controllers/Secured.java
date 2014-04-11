package controllers;

import play.mvc.*;
import play.mvc.Http.Context;

public class Secured extends Security.Authenticator
{
	/**
	 * check username
	 */
	@Override
	public String getUsername(Context context)
	{
		return context.session().get("email");
	}
	
	/**
	 * check password
	 */
	@Override
	public Result onUnauthorized(Context context)
	{
		return redirect(routes.Application.login());
	}
}
