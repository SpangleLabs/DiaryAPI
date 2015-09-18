package uk.co.coales.diary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/entries")
public class EntriesService {

	@GET
	@Path("/")
	public Response listEntries() {
 
		String output = "List of entries";
 
		return Response.status(200).entity(output).build();
 
	}
	
	@GET
	@Path("/{param}")
	public Response viewEntry(@PathParam("param") Integer entryId) {
		String output = "Viewing entry "+entryId.toString();
		return Response.status(200).entity(output).build();
	}

}
