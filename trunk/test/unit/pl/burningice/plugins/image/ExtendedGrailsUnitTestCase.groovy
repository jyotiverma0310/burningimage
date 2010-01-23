package pl.burningice.plugins.image

import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.mock.web.MockMultipartFile
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.*

class ExtendedGrailsUnitTestCase extends GrailsUnitTestCase implements ApplicationContextAware  {

    def static final FILES_DIR = 'images/test'

    def static final WATERMARK_IMAGE = [dir:'images', file:'water-mark.png']

    private def configObject

    ApplicationContext applicationContext

    /**
     * Methods allows to mock generail image config
     *
     * @param maxFileSize Int Max allowed image size
     * @param allowedTypes [] List of allowed image types
     * @return void
     */
    def mockImageConfig = {maxFileSize, allowedTypes ->
        def config = getConfigObject()
        config.images.maxFileSize = maxFileSize
        config.images.allowedTypes = allowedTypes
        ConfigurationHolder.config = config
    }

    /**
     * Methods allows to mock category image config
     *
     * @param thumbnails [:] Map with thimbnails list
     * @return void
     */
    def mockCategoryImageConfig = {
        def config = getConfigObject()
        config.images.category.uploadDir = fullPath
        config.images.category.dir = FILES_DIR
        config.images.category.prefix = 'catgory'
        config.images.category.thumbnails = ['full':['width':100, 'height':100, 'scale': 'Approximate']]
        ConfigurationHolder.config = config
    }

    /**
     * Methods allows to mock product image config
     *
     * @param thumbnails [:] Map with thimbnails list
     * @return void
     */
    def mockProductImageConfig = {
        def config = getConfigObject()
        config.images.product.uploadDir = fullPath
        config.images.product.dir = FILES_DIR
        config.images.product.prefix = 'product'
        config.images.product.thumbnails = ['full':['width':100, 'height':100, 'scale': 'Approximate']]
        ConfigurationHolder.config = config
    }

    /**
     * Methods allows to mock empty image
     *
     * @param name String  File name
     * @param type String  File type (mime format)
     * @return MockMultipartFile
     */
    def mockEmptyImage = {name, type ->
        return new MockMultipartFile(name, name, type, new byte[0])
    }

    /**
     * Methods create mock image
     *
     * @param name String  File name
     * @param type String  File type (mime format)
     * @param path String  Path to the file
     * @return MockMultipartFile
     */
    def mockImage = {name, type, path ->
        def image = new File(path)
        assertTrue image.exists()
        return new MockMultipartFile(name, name, type, new FileInputStream(image))
    }

    /**
     * Helper method allows to mock category object
     *
     * @param category Category
     * @param parent Category
     */
    def mockCategory = {category, parent ->
        category.parent = parent
        category.save(flush:true)
        assertNotNull category.id
    }

    /**
     * Method returns mock config object
     *
     * @return ConfigObject
     */
    def getConfigObject(){
        if (!configObject){
            configObject = new ConfigObject()
            configObject.images.waterMark = WATERMARK_IMAGE
        }
        return configObject
    }

    /**
     * Methods allows to check if uploaded file exists
     *
     * @param imageContainer ImageContainer
     * @return boolean
     */
    /*
    def fileExists(ImageContainer domainObject){
        def config = domainObject.imageConfig
        def result = true

        config.thumbnails.each {thumbName, thumbConfig ->
            if (!applicationContext.getResource("${FILES_DIR}${File.separator}${ImageHelper.getFullFileName(domainObject, thumbName)}").file.exists()){
                result = false
            }

        }
        result
    }
    */

    /**
     * Method cleans test upload dir
     *
     * @return void
     */
    def cleanUpTestDir(){
         applicationContext.getResource("${FILES_DIR}").file.list().each {
             if(it != '.svn'){
                applicationContext.getResource("${FILES_DIR}${File.separatorChar}${it}").file.delete()
             }
         }
    }

    def getFullPath(){
        applicationContext.getResource("${FILES_DIR}").file.toString()
    }
}


