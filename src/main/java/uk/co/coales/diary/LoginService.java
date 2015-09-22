package uk.co.coales.diary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import uk.co.coales.utils.Config;
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
	 * 
	 * @return
	 */
	@GET
	@Path("/token")
	public Response loginToken() {
		String output = "Login session token";
		return Response.status(200).entity(output).build();
	}
}