package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.ImageContainer
import pl.burningice.plugins.image.ResourcePathProvider

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 2:39:37 PM
 * To change this template use File | Settings | File Templates.
 */
class FileContainerUploadWorker extends UploadWorker {

    ResourcePathProvider resourcePathProvider 

    boolean hasImage() {
        container.imageExtension != null
    }

    void delete() {
        def path = resourcePathProvider.getPath(config.outputDir)
        config.images.each {subImageName, subImageOperations ->
            def file = new File("${path}/${ContainerUtils.getFullName(subImageName, container, config)}")
            if (file.exists()){
                file.delete()
            }
        }
    }

    SaveCommand getSaveCommand(String size) {
        new SaveToFileCommand(container, "${resourcePathProvider.getPath(config.outputDir)}/${ContainerUtils.getName(size, container, config)}")
    }
}
