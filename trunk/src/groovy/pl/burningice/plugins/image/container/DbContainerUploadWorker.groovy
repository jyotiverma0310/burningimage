package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.DBImageContainer
import pl.burningice.plugins.image.ast.intarface.ImageContainer

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 2:40:00 PM
 * To change this template use File | Settings | File Templates.
 */
class DbContainerUploadWorker extends UploadWorker {

    boolean hasImage() {
        return (container.biImage != null && !container.biImage.isEmpty())
    }

    void delete() {
        
    }

    SaveCommand getSaveCommand(String size) {
        return new SaveToDbCommand(container, size)
    }
}
