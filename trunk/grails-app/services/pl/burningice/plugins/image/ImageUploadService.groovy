/*
Copyright (c) 2009 Pawel Gdula <pawel.gdula@burningice.pl>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package pl.burningice.plugins.image

import org.springframework.web.multipart.MultipartFile
import pl.burningice.plugins.image.container.*
import pl.burningice.plugins.image.engines.scale.ScaleType
import pl.burningice.plugins.image.ast.intarface.*;
import org.springframework.context.*

/**
 * Servicem for image upload handling
 *
 * @author pawel.gdula@burningice.pl
 */
class ImageUploadService implements ApplicationContextAware {

    boolean transactional = true

    ApplicationContext applicationContext

    def burningImageService

    /**
     * Perfrom save/update of image. Use configuration data to specify how many images
     * should be created and what type of actions should be performed.
     *
     * Exanple configuration
     *
     * CH.config.bi.MyDomain = [
     *     outputDir: '/path/to/outputDir', {nullable = false, blank = false, exists = true}
     *     prefix: '/path/to/outputDir', {nullable = true, blank = false}
     *     images: ['small':[scale:[width:xx, height:yy, type[e:SCALE_ENGINE]]
     *              'medium:[scale:[width:xx, height:yy, type:SCALE_ENGINE],
     *                       watermark:[sign:'/path/to/watermark', offset:[valid offset]]],
     *              'large':[scale:[width:xx, height:yy, type:SCALE_ENGINE],
     *                       watermark:[sign:'/path/to/watermark', offset:[valid offset]]]
     *     ]
     *  ]
     *
     * @param imageContainer Domain object marked by FileImageContainer annotation
     * @param shouldBeSaved Delineate if specifed domain object should be saved (optional)
     * @param actionWraper Closure that allow user to wrap prediefined action by some additional steps (optional)
     * @return FileImageContainer updated image container
     */
    def save(FileImageContainer imageContainer) {
        execute(imageContainer, imageContainer.getImage(), null)
    }

    def save(FileImageContainer imageContainer, boolean shouldBeSaved) {
        save(imageContainer, shouldBeSaved, null)
    }

    def save(FileImageContainer imageContainer, Closure actionWraper) {
        execute(imageContainer, imageContainer.getImage(), actionWraper)
    }

    def save(FileImageContainer imageContainer, boolean shouldBeSaved, Closure actionWraper) {
        execute(imageContainer, imageContainer.getImage(), actionWraper)

        if (shouldBeSaved){
            imageContainer.save(flush:true)
        }

        imageContainer
    }

    /**
     * Allows to delete images associated with specified domain object
     *
     * @param imageContainer Domain object marked by FileImageContainer annotation
     * @param shouldBeSaved Delineate if specifed domain object should be saved (optional)
     * @return FileImageContainer updated image container
     */
    def delete(FileImageContainer imageContainer, save = false) {
        removeFiles(imageContainer, getConfig(imageContainer))

        if (save){
            imageContainer.save(flush:true)
        }

        imageContainer
    }

    /**
     * Execute actins on image
     *
     * @param imageContainer Domain object marked by FileImageContainer annotation
     * @param uploadedImage Image that should be stored
     * @param actionWraper Closure that allow user to wrap prediefined action by some additional steps (optional)
     * @return FileImageContainer updated image container
     */
    private def execute(FileImageContainer imageContainer, MultipartFile uploadedImage, Closure actionWraper) {
        if (!imageContainer.ident()){
            throw new IllegalArgumentException("Image container ${imageContainer} should be persisten")
        }

        def config = getConfig(imageContainer)

        if (imageContainer.imageExtension != null){
            removeFiles(imageContainer, config)
        }

        def imageName, worker = burningImageService.doWith(uploadedImage, getPath(config.outputDir))

        config.images.each {subImageName, subImageOperations ->
            worker.execute(ContainerUtils.getName(subImageName, imageContainer, config), {image ->
                // execute in user specified wraper
                if (actionWraper) {
                    actionWraper(image, subImageName, {
                        executeOnImage(image, subImageOperations)
                    })
                }
                // execute directly
                else {
                    executeOnImage(image, subImageOperations)
                }
                // set file name - we will use it to get file extension
                imageName = image.fileName
            })
        }

        imageContainer.imageExtension = ContainerUtils.getImageExtension(imageName)
        imageContainer
    }

    /**
     * Perform specified chain of modyfication configuret by the user
     *
     * @param image Image that is moddified
     * @param subImageOperations Configuration of specified modyfications
     */
    private def executeOnImage(image, subImageOperations) {
        subImageOperations.each {operationName, params ->
            actionMapping[operationName](image, params)
        }
    }

    /**
     * Perform image deleting
     *
     * @param imageContainer Domain object marked by FileImageContainer annotation
     * @param config Configuration for specified domain object
     */
    private def removeFiles(FileImageContainer imageContainer, Map config) {
        def path = getPath(config.outputDir)
        config.images.each {subImageName, subImageOperations ->
            def file = new File("${path}/${ContainerUtils.getFullName(subImageName, imageContainer, config)}")
            if (file.exists()){
                file.delete()
            }
        }
    }

    /**
     * Returns configuration for specified domain object
     *
     * @param imageContainer Domain object marked by FileImageContainer annotation
     * @return Configuration for Domain object
     */
    private def getConfig(ImageContainer imageContainer){
        def config = ContainerUtils.getConfig(imageContainer)

        if (!config){
            throw new IllegalArgumentException("There is no configuration for ${imageContainer} class")
        }

        config
    }

    /**
     * Returns absolute path to resources
     *
     * @param relativePath Relative path to resources
     * @return Absolute path to resources
     */
    private def getPath(relativePath){
        applicationContext.getResource(relativePath).getFile().toString()
    }

    /**
     * Performs scaling on image
     *
     * @param image Image on witch scaling should be performed
     * @param params Scaling parameters
     */
    private def scaleImage = {image, params ->
        if (params.type == ScaleType.ACCURATE){
            image.scaleAccurate(params.width, params.height)
            return
        }

        if (params.type == ScaleType.APPROXIMATE){
            image.scaleApproximate(params.width, params.height)
        }
    }

    /**
     * Performs watermarking on image
     *
     * @param image Image on witch watermarking should be performed
     * @param params Scaling parameters
     */
    private def watermarkImage = {image, params ->
        image.watermark(getPath(params.sign), params.offset)
    }

    /**
     * Map configuration key to action in this service
     *
     */
    private def actionMapping = [
        scale:scaleImage,
        watermark:watermarkImage
    ]
}
