package uk.co.coales.diary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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
		cal.setLenient(false);
        cal.clear();
		try {
            cal.set(Calendar.YEAR,entryYear);
            cal.set(Calendar.MONTH,entryMonth-1);
            cal.set(Calendar.DAY_OF_MONTH,entryDay);
		} catch (Exception e) {
			return Response.status(400).entity("INVALID DATE").build();
		}
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

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newEntry(JSONObject entryRequest) {
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
		//Get entry text and date
		String entryDate;
		String entryText;
		try {
			entryDate = entryRequest.getString("date");
			entryText = entryRequest.getString("text");
		} catch (JSONException e) {
			System.out.println("ERROR: Failed to read JSON object.");
			e.printStackTrace();
			return Response.status(500).entity("FAILED TO READ JSON").build();
		}
		//Check date and text are set
		if(entryDate == null || entryText == null) {
			return Response.status(400).entity("INVALID DATA FOR ENTRY.").build();
		}
        //Check for entry with current date
        Date newDate = this.dateFromString(entryDate);
		if(newDate == null) {
			return Response.status(400).entity("INVALID DATE FORMAT.").build();
		}
		//Attempt to add entry for this date
		DiaryEntry newEntry = newLogin.addDiaryEntry(newDate,entryText);
		if(newEntry == null) {
			return Response.status(409).entity("ENTRY ALREADY EXISTS WITH THIS DATE.").build();
		}
		Integer entryId = newEntry.getEntryId();
        return Response.status(201).header("Location","/entries/"+entryId.toString()).build();
    }

	/**
	 * Converts a user inputted ISO8601 date into a Date object
	 * @param inputDate String specifying ISO8601 date
	 * @return Date of the new entry
	 */
	private Date dateFromString(String inputDate) {
        //Check input is the right length
        if(inputDate.length() != 10) {
            return null;
        }
        //Check that 5th and 7th characters are hyphens
        if(!Character.toString(inputDate.charAt(4)).equals("-")) {
            return null;
        }
        if(!Character.toString(inputDate.charAt(6)).equals("-")) {
            return null;
        }
        //Get year, month and day as integers
        try {
            Integer inputYear = Integer.parseInt(inputDate.substring(0, 4));
            Integer inputMonth = Integer.parseInt(inputDate.substring(5, 7));
            Integer inputDay = Integer.parseInt(inputDate.substring(8));
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false);
            cal.clear();
            cal.set(Calendar.YEAR,inputYear);
            cal.set(Calendar.MONTH,inputMonth-1);
            cal.set(Calendar.DAY_OF_MONTH,inputDay);
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
	}

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{entry_id}")
    public Response editEntry(JSONObject entryRequest, @PathParam("entry_id") Integer entryId) {
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
        //Get entry text and date
        String entryDate;
        String entryText;
        try {
            entryDate = entryRequest.getString("date");
            entryText = entryRequest.getString("text");
        } catch (JSONException e) {
            System.out.println("ERROR: Failed to read JSON object.");
            e.printStackTrace();
            return Response.status(500).entity("FAILED TO READ JSON").build();
        }
        //Check date and text are set
        if(entryText == null) {
            return Response.status(400).entity("INVALID DATA FOR ENTRY.").build();
        }
        //Get current entry
        DiaryEntry oldEntry = newLogin.getDiaryEntryById(entryId);
        //Check that date is not different from current date
        if(this.dateFromString(entryDate) != null && this.dateFromString(entryDate) != oldEntry.getDate()) {
            return Response.status(400).entity("CANNOT CHANGE DATE OF ENTRY.").build();
        }
        //Update entry
    }
}
