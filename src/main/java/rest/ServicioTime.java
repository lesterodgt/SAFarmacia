
package rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/times")
public class ServicioTime {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Time> getTimes(){
        List<Time> times = new ArrayList<Time>();
        times.add(new Time(1,"gremio","RS"));
        times.add(new Time(2,"internacional","RS"));
        times.add(new Time(3,"guatemala","GT"));
        times.add(new Time(4,"USAC","RS"));
        times.add(new Time(5,"otro","OT"));
        
       return times; 
    }
    
    
    
}
