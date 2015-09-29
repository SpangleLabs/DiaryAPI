package uk.co.coales.diary;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONArray;

import uk.co.coales.data.DiaryEntry;
import uk.co.coales.data.Login;
import uk.co.coales.utils.Database;

@Path("/entries")
public class EntriesService {

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listEntries(@Context HttpServletRequest request) {
		String authToken = request.getHeader("Authentication");
		String ipAddr = request.getRemoteAddr();
		if(authToken == null) {
			return Response.status(401).build();
		}
		Database db = new Database();
		Login newLogin = Login.fromSessionToken(db,authToken,ipAddr);
		if(newLogin == null) {
			return Response.status(401).entity("ACCESS DENIED").build();
		}
		ArrayList<DiaryEntry> listEntries = newLogin.listDiaryEntries();
		JSONArray output = new JSONArray();
		for(DiaryEntry entry : listEntries) {
			try {
				output.put(entry.toJson());
			} catch (JSONException e) {
				System.out.println("ERROR: diary entry failed to construct JSON object.");
				e.printStackTrace();
				return Response.status(500).entity("FAILED TO CONSTRUCT JSON").build();
			}
		}
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/{param}")
	public Response viewEntry(@PathParam("param") Integer entryId) {
		String output = "Viewing entry "+entryId.toString();
		return Response.status(200).entity(output).build();
	}

}
