package cv.representation;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Experiment implements Serializable
{
    @JacksonXmlProperty(isAttribute=true) public String GUID;
    @JacksonXmlProperty(isAttribute=true) public Integer DesignIndex;
    @JacksonXmlProperty(isAttribute=true) public Integer RunIndex;
    
    @JsonProperty
    @JacksonXmlText 
    private String value;
}
