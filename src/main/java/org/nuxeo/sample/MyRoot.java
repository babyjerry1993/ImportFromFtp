
/*
* @title ${file_name}
* @package ${package_name}
* @author Shawn
* @update ${date} ${time}
* @version V1.0
*/

package org.nuxeo.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;


/**
 * The root entry for the WebEngine module.
 * @author Shawn
 */
@Path("/mysite")
@Produces("text/html;charset=UTF-8")
@WebObject(type="MyRoot")
public class MyRoot extends ModuleRoot {

    @GET
    public Object doGet() {

    	System.out.println("hello world");
        return getView("upload");
    }

    @GET
    @Path("upload")
    public Object upload() {

    	System.out.println("upload view");
        return getView("upload");
    }

}
