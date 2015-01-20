package cv.representation;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class ExperimentalSets implements Serializable
{
    @JsonIgnore public String filename;  // Hydrated from this.
    
    @JacksonXmlProperty(localName="ExperimentalSet")
    @JacksonXmlElementWrapper(useWrapping=false)
    public List<ExperimentalSet> experimentalSets;
    
    @JsonIgnore public String toXml() throws JsonProcessingException
    {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
    
    @JsonIgnore public String toJson() throws JsonGenerationException, JsonMappingException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
    
    @JsonIgnore public static ExperimentalSets hydrateDocumentInstance(String xml) throws JsonParseException, com.fasterxml.jackson.databind.JsonMappingException, IOException
    {
        if (null == xml) 
            throw new RuntimeException("xml document to hydrate from cannot be null");
        
        XmlMapper xmlMapper = new XmlMapper();
        ExperimentalSets experimentalSets = xmlMapper.readValue(xml, ExperimentalSets.class);
        return experimentalSets;
    }
}
