package cv.representation;

import java.io.Serializable;

public class SelectItem implements Serializable
{ 
    public String id;
    public String label;
    
    public SelectItem(String id, String label)
    {
        super();
        this.id = id;
        this.label = label;
        return;
    }

}
