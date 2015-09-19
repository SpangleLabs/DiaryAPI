package uk.co.coales.diary;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginService {

	@POST
	@Path("/salt")
	public Response generateSalt() {
		String output = "Newsalt";
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/token")
	public Response login() {
		String output = "Login session token";
		return Response.status(200).entity(output).build();
	}
}