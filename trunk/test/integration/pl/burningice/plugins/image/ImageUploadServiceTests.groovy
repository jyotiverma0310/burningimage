package pl.burningice.plugins.image

import pl.burningice.plugins.image.test.BurningImageUnitTestCase
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import pl.burningice.plugins.image.engines.scale.ScaleType
import pl.burningice.plugins.image.ast.test.TestDomain
import pl.burningice.plugins.image.ast.test.TestDbContainerDomainFirst
import pl.burningice.plugins.image.ast.intarface.DBImageContainer

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class ImageUploadServiceTests extends BurningImageUnitTestCase {

    protected static final def RESULT_DIR = './web-app/upload/'

    protected static final def WEB_APP_RESULT_DIR = './upload/'

    ImageUploadService imageUploadService

    protected void setUp() {
        super.setUp()
        if (ConfigurationHolder.config == null){
            ConfigurationHolder.config = new ConfigObject()
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testScaleDbImageDefaultFiled() {
        def testDomain, result
        ConfigurationHolder.config.bi.TestDbContainerDomainFirst = null
        // instance not saved and there is no image
        testDomain = new TestDbContainerDomainFirst()
        shouldFail(IllegalArgumentException){
            imageUploadService.save(testDomain)
        }
        // image uploaded but instance not saved
        testDomain = new TestDbContainerDomainFirst(image:getMultipartFile('image.jpg'))
        shouldFail(IllegalArgumentException){
            imageUploadService.save(testDomain)
        }
        // should fail, there is no config provided for this domain object
        testDomain = new TestDbContainerDomainFirst()
        assertNotNull(testDomain.save(flush:true))
        testDomain.image = getMultipartFile('image.jpg')
        shouldFail(IllegalArgumentException){
            imageUploadService.save(testDomain)
        }
        // should be ok now
        ConfigurationHolder.config.bi.TestDbContainerDomainFirst = [
            images: [
                'small':[scale:[width:100, height:100, type:ScaleType.ACCURATE]],
                'medium':[scale:[width:300, height:300, type:ScaleType.ACCURATE]],
                'large':[scale:[width:800, height:600, type:ScaleType.APPROXIMATE]]
            ]
        ]
        testDomain = new TestDbContainerDomainFirst(image:getMultipartFile('image.jpg'))
        assertNotNull(testDomain.save(flush:true))
        def version = testDomain.version 
        imageUploadService.save(testDomain)

        assertEquals(version, testDomain.version) 
        assertNotNull(testDomain.biImage)
        assertEquals(3, testDomain.biImage.size())
        assertNotNull(testDomain.biImage.small)
        assertEquals('jpg', testDomain.biImage.small.type)
        assertNotNull(testDomain.biImage.medium)
        assertEquals('jpg', testDomain.biImage.medium.type)
        assertNotNull(testDomain.biImage.large)
        assertEquals('jpg', testDomain.biImage.large.type)

        testDomain.image = getMultipartFile('image.png')
        imageUploadService.save(testDomain)

        assertEquals(version, testDomain.version)
        assertNotNull(testDomain.biImage)
        assertEquals(3, testDomain.biImage.size())
        assertNotNull(testDomain.biImage.small)
        assertEquals('png', testDomain.biImage.small.type)
        assertNotNull(testDomain.biImage.medium)
        assertEquals('png', testDomain.biImage.medium.type)
        assertNotNull(testDomain.biImage.large)
        assertEquals('png', testDomain.biImage.large.type)

        testDomain.image = getMultipartFile('image.bmp')
        imageUploadService.save(testDomain, true)

        assertTrue(version < testDomain.version)
        assertNotNull(testDomain.biImage)
        assertEquals(3, testDomain.biImage.size())
        assertNotNull(testDomain.biImage.small)
        assertEquals('bmp', testDomain.biImage.small.type)
        assertNotNull(testDomain.biImage.medium)
        assertEquals('bmp', testDomain.biImage.medium.type)
        assertNotNull(testDomain.biImage.large)
        assertEquals('bmp', testDomain.biImage.large.type)
    }

    void testScale() {
        def testDomain = new TestDomain(image:getMultipartFile('image.jpg'))

        shouldFail(IllegalArgumentException){
            imageUploadService.save(testDomain)
        }
        assertNull testDomain.imageExtension

        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: WEB_APP_RESULT_DIR,
            prefix: 'prefixName',
            images: ['small':[scale:[width:100, height:100, type:ScaleType.ACCURATE]],
                     'medium':[scale:[width:300, height:300, type:ScaleType.ACCURATE]],
                     'large':[scale:[width:800, height:600, type:ScaleType.APPROXIMATE]]
            ]
        ]

        assertFalse fileExists('prefixName-1-large.jpg')
        assertFalse fileExists('prefixName-1-medium.jpg')
        assertFalse fileExists('prefixName-1-small.jpg')

        shouldFail(IllegalArgumentException){
            imageUploadService.save(testDomain)
        }
        assertNull testDomain.imageExtension

        assertFalse fileExists('prefixName-1-large.jpg')
        assertFalse fileExists('prefixName-1-medium.jpg')
        assertFalse fileExists('prefixName-1-small.jpg')

        testDomain.save(flush:true)
        def version = testDomain.version

        assertNotNull testDomain.ident()
        imageUploadService.save(testDomain)

        assertTrue testDomain.imageExtension == 'jpg'
        assertTrue testDomain.version == version

        assertTrue fileExists('prefixName-1-large.jpg')
        assertTrue fileExists('prefixName-1-medium.jpg')
        assertTrue fileExists('prefixName-1-small.jpg')

        version = testDomain.version

        testDomain.image = getMultipartFile('image.png')
        assertNotNull testDomain.ident()
        imageUploadService.save(testDomain, true)

        assertFalse fileExists('prefixName-1-large.jpg')
        assertFalse fileExists('prefixName-1-medium.jpg')
        assertFalse fileExists('prefixName-1-small.jpg')

        assertTrue fileExists('prefixName-1-large.png')
        assertTrue fileExists('prefixName-1-medium.png')
        assertTrue fileExists('prefixName-1-small.png')

        assertTrue testDomain.imageExtension == 'png'
        assertTrue testDomain.version > version
    }

    void testScaleAndWatermak() {
        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: WEB_APP_RESULT_DIR,
            prefix: null,
            images: ['large':[watermark:[sign:'images/watermark.png', offset:[top:10, left:10]]]]
        ]

        def testDomain = new TestDomain(image:getMultipartFile('image.jpg')).save(flush:true)
        assertFalse fileExists("${testDomain.ident()}-large.jpg")
        imageUploadService.save(testDomain)
        assertTrue fileExists("${testDomain.ident()}-large.jpg")
    }

    void testDelete() {
        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: WEB_APP_RESULT_DIR,
            prefix: 'prefixName',
            images: ['small':[scale:[width:100, height:100, type:ScaleType.ACCURATE]],
                     'medium':[scale:[width:300, height:300, type:ScaleType.ACCURATE]],
                     'large':[scale:[width:800, height:600, type:ScaleType.APPROXIMATE]]
            ]
        ]

        def testDomain = new TestDomain(image:getMultipartFile('image.jpg')).save(flush:true)

        assertFalse fileExists("prefixName-${testDomain.ident()}-large.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-medium.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-small.jpg")
        
        imageUploadService.save(testDomain)

        assertTrue fileExists("prefixName-${testDomain.ident()}-large.jpg")
        assertTrue fileExists("prefixName-${testDomain.ident()}-medium.jpg")
        assertTrue fileExists("prefixName-${testDomain.ident()}-small.jpg")

        def version = testDomain.version
        imageUploadService.delete(testDomain)

        assertTrue testDomain.version == version
        assertFalse fileExists("prefixName-${testDomain.ident()}-large.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-medium.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-small.jpg")

        imageUploadService.save(testDomain)

        assertTrue fileExists("prefixName-${testDomain.ident()}-large.jpg")
        assertTrue fileExists("prefixName-${testDomain.ident()}-medium.jpg")
        assertTrue fileExists("prefixName-${testDomain.ident()}-small.jpg")

        version = testDomain.version
        imageUploadService.delete(testDomain, true)

        assertTrue testDomain.version > version
        assertFalse fileExists("prefixName-${testDomain.ident()}-large.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-medium.jpg")
        assertFalse fileExists("prefixName-${testDomain.ident()}-small.jpg")
    }

    void testWatermak() {
        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: WEB_APP_RESULT_DIR,
            prefix: 'scale-and-waremark',
            images: ['large':[scale:[width:800, height:600, type:ScaleType.APPROXIMATE],
                              watermark:[sign:'images/watermark.png', offset:[top:10, left:10]]]]
        ]

        def testDomain = new TestDomain(image:getMultipartFile('image.jpg')).save(flush:true)
        assertFalse fileExists("scale-and-waremark-${testDomain.ident()}-large.jpg")
        imageUploadService.save(testDomain)
        assertTrue fileExists("scale-and-waremark-${testDomain.ident()}-large.jpg")
    }

    void testActionWraper() {
        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: WEB_APP_RESULT_DIR,
            prefix: 'action-wraped',
            images: ['large':[scale:[width:800, height:600, type:ScaleType.APPROXIMATE],
                              watermark:[sign:'images/watermark.png', offset:[top:10, left:10]]],
                      'small':[scale:[width:300, height:300, type:ScaleType.ACCURATE]]]
        ]

        def testDomain = new TestDomain(image:getMultipartFile('image.jpg')).save(flush:true)
        def version = testDomain.version

        assertNull testDomain.imageExtension
        assertFalse fileExists("action-wraped-${testDomain.ident()}-large.jpg")
        assertFalse fileExists("action-wraped-${testDomain.ident()}-small.jpg")

        imageUploadService.save(testDomain, {image, name, action ->
            action()

            if (name == 'large'){
                image.text({it.write("Text on large image", 300, 300)})
            }

            if (name == 'small'){
                image.text({it.write("Text on small image", 10, 50)})
            }
        })

        assertTrue version == testDomain.version
        assertTrue testDomain.imageExtension == 'jpg'
        assertTrue fileExists("action-wraped-${testDomain.ident()}-large.jpg")
        assertTrue fileExists("action-wraped-${testDomain.ident()}-small.jpg")

        testDomain.image = getMultipartFile('image.png')
        
        imageUploadService.save(testDomain, true, {image, name, action ->
            action()

            if (name == 'large'){
                image.text({it.write("Text on large image", 300, 300)})
            }

            if (name == 'small'){
                image.text({it.write("Text on small image", 10, 50)})
            }
        })

        assertTrue version < testDomain.version
        assertTrue testDomain.imageExtension == 'png'

        assertTrue fileExists("action-wraped-${testDomain.ident()}-large.png")
        assertTrue fileExists("action-wraped-${testDomain.ident()}-small.png")

        assertFalse fileExists("action-wraped-${testDomain.ident()}-large.jpg")
        assertFalse fileExists("action-wraped-${testDomain.ident()}-small.jpg")
    }
}
