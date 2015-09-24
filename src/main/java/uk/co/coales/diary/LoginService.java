package uk.co.coales.diary;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import uk.co.coales.data.Login;
import uk.co.coales.utils.Database;

@Path("/login")
public class LoginService {
	
	/**
	 * login/token resource, when supplied with username and password, it will respond with a session token
	 * @return
	 */
	@POST
	@Path("/token")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response loginToken(JSONObject loginRequest) {
		String username = null;
		String password = null;
		try {
			username = loginRequest.getString("username");
			password = loginRequest.getString("password");
		} catch (JSONException e) {
			System.out.println("ERROR: login token failed to read JSON object.");
			e.printStackTrace();
			return Response.status(500).entity("FAILED TO READ JSON").build();
		}
		//Check username and password are set
		if(username == null || password == null) {
			return Response.status(400).entity("INVALID DATA FOR TOKEN.").build();
		}
		//Get user data from database
		Database db = new Database();
		Login newLogin = Login.fromUsername(db,username);
		if(newLogin == null) {
			return Response.status(404).entity("USERNAME NOT FOUND.").build();
		}
		//Check if account is locked out
		if(newLogin.isLockedOut()) {
			return Response.status(403).build();
		}
		//Hash password
		//Check password is correct
		//If incorrect, return authentication failure
		//Otherwise, generate token
		//Save token in database
		//Return token
		JSONObject output = new JSONObject();
		try {
			output.put("username",username);
			output.put("password",password);
			output.put("token","tokentoken");
		} catch (JSONException e) {
			System.out.println("ERROR: login token failed to construct JSON object.");
			e.printStackTrace();
			return Response.status(500).entity("FAILED TO CONSTRUCT JSON").build();
		}
		return Response.status(200).entity(output).build();
	}
	
	
}