package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.DBImageContainer
import pl.burningice.plugins.image.ast.intarface.FileImageContainer
import pl.burningice.plugins.image.ast.intarface.ImageContainer
import pl.burningice.plugins.image.ResourcePathProvider

class UploadWorkerFactory {

    ResourcePathProvider resourcePathProvider

    public UploadWorker produce(ImageContainer container){
        if (container instanceof DBImageContainer){
            return new DbContainerUploadWorker(container:container)  
        }

        if (container instanceof FileImageContainer){
            return new FileContainerUploadWorker(container:container, resourcePathProvider:resourcePathProvider)  
        }

        throw new IllegalArgumentException("There is no upload worker for container ${container}")
    }
}
