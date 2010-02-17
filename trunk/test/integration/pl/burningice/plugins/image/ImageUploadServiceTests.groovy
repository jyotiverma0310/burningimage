package pl.burningice.plugins.image

import pl.burningice.plugins.image.test.BurningImageUnitTestCase
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import pl.burningice.plugins.image.engines.scale.ScaleType
import pl.burningice.plugins.image.ast.TestDomain

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class ImageUploadServiceTests extends BurningImageUnitTestCase {

    protected static final def RESULT_DIR = './web-app/upload/'

    protected static final def WEB_APP_RESULT_DIR = './upload/'

    def imageUploadService

    protected void setUp() {
        super.setUp()
        if (ConfigurationHolder.config == null){
            ConfigurationHolder.config = new ConfigObject()
        }
    }

    protected void tearDown() {
        super.tearDown()
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
