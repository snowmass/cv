package cv.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.james.mime4j.storage.DefaultStorageProvider;
import org.apache.james.mime4j.storage.MemoryStorageProvider;

import cv.resource.DocumentResource;
import cv.resource.FmeaResource;

@ApplicationPath("/rest")
public class CvApplication extends Application
{
    //
    // For Tomcat this can be an empty class.
    //
    // For Google AppEngine we need these explicit declarations:
    //
    
    static
    {
        DefaultStorageProvider.setInstance(new MemoryStorageProvider());
    }
    
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public CvApplication()
    {
        singletons.add(new DocumentResource());
        singletons.add(new FmeaResource());
        return;
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        return classes;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }

}
