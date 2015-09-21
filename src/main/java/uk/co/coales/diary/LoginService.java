package uk.co.coales.diary;

import java.security.SecureRandom;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Hex;

import uk.co.coales.utils.Database;

@Path("/login")
public class LoginService {

	@GET
	@Path("/salt")
	public Response loginSalt() {
		//Connect to database
		Database db = new Database();
		//Generate unique salt
		String hexSalt = this.generateSalt(db);
		String output = "Newsalt:"+hexSalt;
		return Response.status(200).entity(output).build();
	}
	
	/**
	 * Generates a crypto-secure random salt, converts it to hexadecimal and stores it in the database.
	 * @return
	 */
	private String generateSalt(Database db) {
		//Generate salt
		Random r = new SecureRandom();
		byte[] newSalt = new byte[32];
		r.nextBytes(newSalt);
		//Convert to hex
		String hexSalt = Hex.encodeHexString(newSalt);
		//Store in database
		db.addUniqueSalt(hexSalt);
		return hexSalt;
	}
	
	@GET
	@Path("/token")
	public Response loginToken() {
		String output = "Login session token";
		return Response.status(200).entity(output).build();
	}
}