package pl.burningice.plugins.image

import pl.burningice.plugins.image.ast.ImageService
import pl.burningice.plugins.image.ast.Image
import javax.servlet.http.HttpServletResponse

class DbContainerImageController {

    ImageService imageService

    def index = {
          Image image = imageService.get(params.imageId)

          if (!image){
              return response.sendError(HttpServletResponse.SC_NOT_FOUND)
          }

          response.setContentType(getContentType(image))
    	  response.outputStream << new ByteArrayInputStream(image.data)
    }

    protected String getContentType(Image image) {
        return "image/${getFileExtension(image)}"
    }

    protected  String getFileExtension(Image image){
        return (image.type == 'jpg' ? 'jpeg' : image.type)      
    }
}
