package cv.representation;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Experiments implements Serializable
{
    @JacksonXmlProperty(isAttribute=true) 
    public String OrderBy;
    
    @JacksonXmlElementWrapper(useWrapping=false)
    @JacksonXmlProperty(localName="Experiment") 
    public List<Experiment> experiments;
}
