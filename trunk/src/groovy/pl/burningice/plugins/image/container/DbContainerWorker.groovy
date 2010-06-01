package pl.burningice.plugins.image.container

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 2:40:00 PM
 * To change this template use File | Settings | File Templates.
 */
class DbContainerWorker extends ContainerWorker {

    boolean hasImage() {
        return (container.biImage != null && !container.biImage.isEmpty())
    }

    void delete() {
        if (!hasImage()){
            return
        }
        container.biImage.collect {it.value}.each {it.delete()}
        container.biImage = null
    }

    SaveCommand getSaveCommand(String size) {
        return new SaveToDbCommand(container, size)
    }
}
