package cv.representation;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ExperimentalSet implements Serializable
{
    @JacksonXmlProperty(isAttribute=true) public String GUID;
    public Identity Identity;
    public Experiments Experiments;
}
