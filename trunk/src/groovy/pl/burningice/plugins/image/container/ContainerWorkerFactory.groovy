package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.DBImageContainer
import pl.burningice.plugins.image.ast.intarface.FileImageContainer
import pl.burningice.plugins.image.ast.intarface.ImageContainer
import pl.burningice.plugins.image.ResourcePathProvider

class ContainerWorkerFactory {

    ResourcePathProvider resourcePathProvider

    public ContainerWorker produce(ImageContainer container){
        if (container instanceof DBImageContainer){
            return new DbContainerWorker(container:container)
        }

        if (container instanceof FileImageContainer){
            return new FileContainerWorker(container:container, resourcePathProvider:resourcePathProvider)
        }

        throw new IllegalArgumentException("There is no upload worker for container ${container}")
    }
}
