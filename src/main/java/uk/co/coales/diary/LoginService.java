package uk.co.coales.diary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import uk.co.coales.utils.Config;
import uk.co.coales.utils.Database;

@Path("/login")
public class LoginService {

	@GET
	@Path("/salt")
    @Produces(MediaType.APPLICATION_JSON)
	public Response loginSalt() {
		//Connect to database
		Database db = new Database();
		//Generate unique salt
		String hexSalt = this.generateSalt(db);
		String privKey = this.getPrivateKey();
		//String output = "Newsalt:"+hexSalt;
		JSONObject outputJson = new JSONObject();
		try {
			outputJson.put("salt",hexSalt);
			outputJson.put("pub_key",privKey);
		} catch (JSONException e) {
			System.out.println("ERROR: login salt failed to construct JSON object.");
			e.printStackTrace();
			return Response.status(500).entity("FAILED TO CONSTRUCT JSON").build();
		}
		return Response.status(200).entity(outputJson).build();
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
	
	/**
	 * Loads private key from file, using configuration directory.
	 * Base64 encodes key before output.
	 * @return
	 */
	private String getPrivateKey() {
		//Load configuration
		Config conf = new Config();
		String keyDir = conf.getKeyDirectory();
		//Load file
		byte[] keyBytes = null;
		try {
			keyBytes = IOUtils.toByteArray(new FileInputStream(new File(keyDir+"/public_key.der")));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: PUBLIC KEY FILE NOT FOUND.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("ERROR: FAILED TO READ PUBLIC KEY.");
			e.printStackTrace();
			return null;
		}
		//Base 64 encode it
		byte[] keyEnc = Base64.encodeBase64(keyBytes);
		//Output
		return new String(keyEnc);
	}
	
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
		//Decrypt password
		//Check salt is valid
		//Get user data from database
		//Check if account is locked out
		//Hash password
		//Check password is correct
		//If incorrect, return authentication failure
		//Otherwise, generate token
		//Save token in database
		//Return token
		JSONObject output = new JSONObject();
		output.put("username",username);
		output.put("password",password);
		output.put("token","tokentoken");
		return Response.status(200).entity(output).build();
	}
	
	
}