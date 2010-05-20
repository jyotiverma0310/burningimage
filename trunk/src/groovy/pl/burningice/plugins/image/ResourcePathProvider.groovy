package pl.burningice.plugins.image

import org.springframework.context.*

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 18, 2010
 * Time: 12:54:27 AM
 * To change this template use File | Settings | File Templates.
 */
class ResourcePathProvider implements ApplicationContextAware {

    ApplicationContext applicationContext

    public String  getPath(relativePath){
        return applicationContext.getResource(relativePath).getFile().toString()
    }

}
