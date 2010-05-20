package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.ImageContainer

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 2:39:00 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class UploadWorker {

    ImageContainer container

    @Lazy
    public config = {
        return ContainerUtils.getConfig(container)
    }()

    abstract public boolean hasImage()

    abstract public void delete()

    abstract public SaveCommand getSaveCommand(String size)

    public String toString(){
        return "Image container: ${container.class.name}"      
    }

    public def isPersisted(){
        return this.container.ident() != null            
    }
}
