package cv.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;

import com.fasterxml.jackson.core.JsonProcessingException;

import cv.representation.Experiment;
import cv.representation.ExperimentalSet;
import cv.representation.ExperimentalSets;
import cv.representation.SelectItem;

@Path("/document")
public class DocumentResource
{
    private static Log log = LogFactory.getLog(DocumentResource.class);
    
    private static final String UPDATED_FILENAME_PREFIX = "new-";
    private static final String MULTIPART_FORM_DATA_NAME = "file";
    private static final String MULTIPART_FORM_DATA_FILENAME = "filename";
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest request, MultipartFormDataInput input)
    {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(MULTIPART_FORM_DATA_NAME);

        for (InputPart inputPart : inputParts)
        {
            try
            {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
              //InputStream inputStream = inputPart.getBody(InputStream.class, null);
                InputStream inputStream = inputPart.getBody(new GenericType<InputStream>() { });
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String expectedXml = new String(bytes, "UTF-8");
                
                ExperimentalSets experimentalSets = ExperimentalSets.hydrateDocumentInstance(expectedXml);
                experimentalSets.filename = extractFilename(header);
                setExperimentalSets(request, experimentalSets);

                log.debug("uploadFile() experimentalSets.toJson:" + experimentalSets.toJson());
                log.debug("uploadFile() experimentalSets.toXml:" + experimentalSets.toXml());
                log.debug("uploadFile() experimentalSets.filename:" + experimentalSets.filename);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        return Response.status(Status.OK).build();
    }

    private static String extractFilename(MultivaluedMap<String, String> header)
    {
        // Extracting filename from this header:
        //
        // ------WebKitFormBoundaryARgU65snaCKMiRPm
        // Content-Disposition: form-data; name="file"; filename="Experimental Sets.xml"
        // Content-Type: text/xml
        // ------WebKitFormBoundaryARgU65snaCKMiRPm--
        
        String filename = "unset";
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String name : contentDisposition)
        {
            if ((name.trim().startsWith(MULTIPART_FORM_DATA_FILENAME)))
            {
                String[] value = name.split("=");
                filename = value[1].trim().replaceAll("\"", "");
            }
        }
        
        return filename;
    }
    
    @GET @Path("/selectItem") 
    @Produces(MediaType.APPLICATION_JSON) 
    public Response selectItemList(@Context HttpServletRequest request)
    {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        ExperimentalSets experimentalSets = getExperimentalSets(request);
        
        for (ExperimentalSet experimentalSet : experimentalSets.experimentalSets)
            selectItems.add(new SelectItem(experimentalSet.GUID, experimentalSet.Identity.Name));
            
        return Response.status(Status.OK).entity(selectItems).build();
    }
    
    @GET @Path("/experimentalSet/{GUID}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public Response experimentalSetList(@Context HttpServletRequest request, @PathParam("GUID") String GUID)
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);

        for (ExperimentalSet experimentalSet : experimentalSets.experimentalSets)
        {   
            if (GUID.equals(experimentalSet.GUID))
                return Response.status(Status.OK).entity(experimentalSet).build();
        }
        
        return Response.status(Status.NOT_FOUND).build();
    }
    
    @GET @Path("/experimentalSet/experiments/{GUID}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public Response experimentList(@Context HttpServletRequest request, @PathParam("GUID") String GUID)
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);

        for (ExperimentalSet experimentalSet : experimentalSets.experimentalSets)
        {   
            if (GUID.equals(experimentalSet.GUID))
            {
                if (null != experimentalSet.Experiments 
                    && null != experimentalSet.Experiments.experiments)
                {
                    return Response.status(Status.OK).entity(experimentalSet.Experiments.experiments).build();
                }
                else
                {
                    return Response.status(Status.NO_CONTENT).build();
                }
            }
        }
        
        return Response.status(Status.NOT_FOUND).build();
    }
    
    @POST @Path("/experimentalSet/experiments/{GUID}/{OrderBy}") 
    @Consumes(MediaType.APPLICATION_JSON) 
    public Response experimentList(@Context HttpServletRequest request, @PathParam("GUID") String GUID, @PathParam("OrderBy") String OrderBy, List<Experiment> altExperiments)
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);

        for (ExperimentalSet experimentalSet : experimentalSets.experimentalSets)
        {   
            if (GUID.equals(experimentalSet.GUID))
            {
                experimentalSet.Experiments.OrderBy = OrderBy;
                experimentalSet.Experiments.experiments = altExperiments;
                return Response.status(Status.OK).build();
            }
        }
        
        return Response.status(Status.NOT_FOUND).build();
    }
    
    @GET @Path("/displayAsJson")  // For internal inspection.
    @Produces(MediaType.APPLICATION_JSON) 
    public Response shouldDisplayAsJsonString(@Context HttpServletRequest request)
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);
        return Response.status(Status.OK).entity(experimentalSets).build();
    }
    
    @GET @Path("/displayAsXml")  // For internal inspection.
    @Produces(MediaType.TEXT_XML) 
    public Response shouldDisplayAsXmlString(@Context HttpServletRequest request) throws JsonProcessingException
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);
        return Response.status(Status.OK).entity(experimentalSets.toXml()).build();
    }
    
    @GET 
    @Path("/download") 
    @Produces(MediaType.TEXT_XML) 
    public Response download(@Context HttpServletRequest request) throws JsonProcessingException
    {
        ExperimentalSets experimentalSets = getExperimentalSets(request);
        return Response.status(Status.OK)
                       .type(MediaType.APPLICATION_OCTET_STREAM)
                       .header("Content-Disposition", "inline;filename=" + UPDATED_FILENAME_PREFIX + experimentalSets.filename)
                       .entity(experimentalSets.toXml())
                       .build();
    }

    public ExperimentalSets getExperimentalSets(HttpServletRequest request)
    {
        //TODO: This can be a repository.

        HttpSession session = request.getSession();
        ExperimentalSets experimentalSets = (ExperimentalSets) session.getAttribute("ExperimentalSets");
        if (null == experimentalSets)
            throw new RuntimeException("No ExperimentalSets found in user's session");
        return experimentalSets;
    }

    public void setExperimentalSets(HttpServletRequest request, ExperimentalSets experimentalSets)
    {
        HttpSession session = request.getSession();
        session.setAttribute("ExperimentalSets", experimentalSets);
        return;
    }
}
