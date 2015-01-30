package cv.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Path("/fmea")
public class FmeaResource extends BaseResource
{
    private static Log log = LogFactory.getLog(FmeaResource.class);
    
    /* TODO: FMEA to consider:
     * 
     * (1) Inject docs:
     *     - improperly formatted.
     *     - wrong content type.
     *     - non UTF-8 content.
     * (2) Comm breakdown - stress REST.
     * (3) Boundary values, domain, and range.
     * (4) Browser and browser version wars.
     * (5) JSON - XML mapping.
     *     - typing [reliability].
     *     - performance.
     * (6) Sorting:
     *     - verification.
     *     - identify the known bug and fix or report it.
     * (7) Lazy initialization induction.
     * (8) Scaling:
     *     - stateless.
     *     - concurrency: users, save sort and file.
     *     - authentication and access controls.
     *     - session cleanup.
     * (9) //TODOs...including TDD / BDD.
     * 
     */
    
    @GET
    @Path("/echo/{pathParam}")
    public Response echo(@PathParam("pathParam") String pathParam)
    {
        String response = "echo() pathParam:" + pathParam;
        log.info(response);
        return Response.status(Status.OK).entity(response).build();
    }
    
    @GET
    @Path("/noop")
  //@Produces({"application/xml", "application/json"})  //TODO: Drive representation via .xml, .json: See 17.1. URL-based negotiation.
    public Response noop()
    {
        log.info("noop()");
        return Response.status(Status.OK).build();
    }
    
}
