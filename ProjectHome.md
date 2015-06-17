# Help needed #
**I am looking for people that would be interested in developing BI plugin. If you have experience in Grails/Groovy and want to help, please leave comment in "Roadmap" section.**

# About #

Grails plugin for image manipulation and image upload handling. Current version allows:

  * use of JAI or ImageMagick rendering engines
  * scale image with approximate width and height
  * scale image with accurate width and height
  * add image watermark
  * crop image
  * write text on image
  * mark domain class as file image container by using @FileImageContainer annotation
  * mark domain class as DB image container by using @DBImageContainer annotation
  * save image binded to domain class by using ImageUpladService
  * validate uploaded image

It allows to update files stored on server and uploaded as a MultipartFile. Operations could be chained and executed in random order.

# Documentation #

  * [Rendering engines ](Supported_rendering_engines.md)
  * [Image manipulation by using BurningImageService](http://code.google.com/p/burningimage/wiki/Images_manipulation_handling)
  * [Handling of uploaded images, marking domain classes, validation](http://code.google.com/p/burningimage/wiki/Images_upload_handling)
  * [Testing](Image_manipulation_testing.md)

# Other #

  * [Roadmap](http://code.google.com/p/burningimage/wiki/Roadmap)
  * [Features requests](http://code.google.com/p/burningimage/wiki/Requests)
