package uk.co.coales.diary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import org.codehaus.jettison.json.JSONObject;

import uk.co.coales.data.DiaryEntry;
import uk.co.coales.data.Login;
import uk.co.coales.utils.Database;

@Path("/entries")
public class EntriesService {

	@Context HttpServletRequest request;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listEntries() {
		String authToken = this.request.getHeader("Authentication");
		String ipAddr = this.request.getRemoteAddr();
		if(authToken == null) {
			return Response.status(401).entity("ACCESS DENIED").build();
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
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/{entryid}")
	public Response viewEntry(@PathParam("entryid") Integer entryId) {
		//Check auth token and get current login
		String authToken = this.request.getHeader("Authentication");
		String ipAddr = this.request.getRemoteAddr();
		if(authToken == null) {
			return Response.status(401).entity("ACCESS DENIED").build();
		}
		Database db = new Database();
		Login newLogin = Login.fromSessionToken(db,authToken,ipAddr);
		if(newLogin == null) {
			return Response.status(401).entity("ACCESS DENIED").build();
		}
		//Get specified diary entry
		DiaryEntry diaryEntry = newLogin.getDiaryEntryById(entryId);
		if(diaryEntry == null) {
			return Response.status(404).entity("ENTRY NOT FOUND").build();
		}
		//Return specified diary entry
		JSONObject outputJson;
		try {
			outputJson = diaryEntry.toJson();
		} catch (JSONException e) {
			System.out.println("ERROR: diary entry failed to construct JSON object.");
			e.printStackTrace();
			return Response.status(500).entity("FAILED TO CONSTRUCT JSON").build();
		} 
		return Response.status(200).entity(outputJson).build();
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{entryYear}-{entryMonth}-{entryDay}")
    public Response viewEntry(@PathParam("entryYear") Integer entryYear, @PathParam("entryMonth") Integer entryMonth, @PathParam("entryDay") Integer entryDay) {
        //Check auth token and get current login
        String authToken = this.request.getHeader("Authentication");
        String ipAddr = this.request.getRemoteAddr();
        if(authToken == null) {
            return Response.status(401).entity("ACCESS DENIED").build();
        }
        Database db = new Database();
        Login newLogin = Login.fromSessionToken(db,authToken,ipAddr);
        if(newLogin == null) {
            return Response.status(401).entity("ACCESS DENIED").build();
        }
        //Attempt to create Date
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.DAY_OF_MONTH,entryDay);
        cal.set(Calendar.MONTH,entryMonth-1);
        cal.set(Calendar.YEAR,entryYear);
        Date date = cal.getTime();
        //Get specified diary entry
        DiaryEntry diaryEntry = newLogin.getDiaryEntryByDate(date);
        if(diaryEntry == null) {
            return Response.status(404).entity("ENTRY NOT FOUND").build();
        }
        //Return specified diary entry
        JSONObject outputJson;
        try {
            outputJson = diaryEntry.toJson();
        } catch (JSONException e) {
            System.out.println("ERROR: diary entry failed to construct JSON object.");
            e.printStackTrace();
            return Response.status(500).entity("FAILED TO CONSTRUCT JSON").build();
        }
        return Response.status(200).entity(outputJson).build();
    }
}
