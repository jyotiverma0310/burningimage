package pl.burningice.plugins.image

import org.springframework.mock.web.MockMultipartFile
import pl.burningice.plugins.image.engines.*
import java.awt.Color
import java.awt.Font
import grails.test.GrailsUnitTestCase
import pl.burningice.plugins.image.test.FileUploadUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

/**
 *
 * @author pawel.gdula@burningice.pl
 */
@Mixin(FileUploadUtils)
class BurningImageServiceImageMagickTests extends GrailsUnitTestCase {

    protected static final def RESULT_DIR = './resources/resultImages/'

    private def burningImageService

    protected void setUp() {
        super.setUp()
        cleanUpTestDir()
        burningImageService = new BurningImageService()
        CH.config = new ConfigObject()
        CH.config.bi.renderingEngine = RenderingEngine.IMAGE_MAGICK
    }

    protected void tearDown() {
        super.tearDown()
        burningImageService = null
    }

    void testScaleApproximateMultipartFile() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('image.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getMultipartFile('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getMultipartFile('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getMultipartFile('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleApproximateWidthBiggerBothSidesSmaller() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('width_bigger.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('width_bigger.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 50)
        }
        assertEquals 'width_bigger.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('width_bigger.jpg')
        file = getFile('width_bigger.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 50
    }

    void testScaleApproximateWidthBiggerWidthSmaller() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('width_bigger.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('width_bigger.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(100, 1000)
        }
        assertEquals 'width_bigger.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('width_bigger.jpg')
        file = getFile('width_bigger.jpg')
        assertTrue file.width <= 100
        assertTrue file.height <= 1000
    }

    void testScaleApproximateWidthBiggerHeightSmaller() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('width_bigger.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('width_bigger.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(1000, 100)
        }
        assertEquals 'width_bigger.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('width_bigger.jpg')
        file = getFile('width_bigger.jpg')
        assertTrue file.width <= 1000
        assertTrue file.height <= 100
    }

    void testScaleAccurateWidthBiggerBothSidesSmaller() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('width_bigger.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('width_bigger.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 50)
        }
        assertEquals 'width_bigger.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('width_bigger.jpg')
        file = getFile('width_bigger.jpg')
        assertTrue file.width == 50
        assertTrue file.height == 50
    }

    void testScaleAccurateHeightBiggerBothSidesSmaller() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('height_bigger.jpg'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('height_bigger.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(100, 100)
        }
        assertEquals 'height_bigger.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('height_bigger.jpg')
        file = getFile('height_bigger.jpg')
        assertTrue file.width == 100
        assertTrue file.height == 100
    }

    void testScaleAccurateBmp() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('image.bmp'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(100, 100)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width == 100
        assertTrue file.height == 100
    }

    void testScaleAccuratePng() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('image.png'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(100, 100)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width == 100
        assertTrue file.height == 100
    }

    void testScaleAccurateGif() {
        assertEquals(ConfigUtils.getEngine(), RenderingEngine.IMAGE_MAGICK)
        assertFalse(fileExists('image.gif'))

        def scaleResult, result, file

        result = burningImageService.doWith(getMultipartFile('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(100, 100)
        }
        assertEquals 'image.gif', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.gif')
        file = getFile('image.gif')
        assertTrue file.width == 100
        assertTrue file.height == 100
    }
}
