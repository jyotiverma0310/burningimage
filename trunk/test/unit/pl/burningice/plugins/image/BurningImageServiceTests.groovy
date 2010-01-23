package pl.burningice.plugins.image

import grails.test.*
import org.springframework.mock.web.MockMultipartFile
import pl.burningice.plugins.image.engines.*
import javax.imageio.ImageIO

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class BurningImageServiceTests extends GrailsUnitTestCase {

    private static final def SOURCE_DIR = './resources/testImages/'

    private static final def RESULT_DIR = './resources/resultImages/'

    private def burningImageService

    protected void setUp() {
        super.setUp()
        cleanUpTestDir()
        burningImageService = new BurningImageService()
    }

    protected void tearDown() {
        super.tearDown()
        burningImageService = null
    }

    protected void cleanUpTestDir(){
        println "-" * 100
        new File(RESULT_DIR).list().toList().each {
            if(it != '.svn'){
                def filePath = "${RESULT_DIR}${it}"
                println "Remove ${filePath}"
                new File(filePath).delete()
            }
        }
    }

    protected def fileExists(fileName){
        new File("${RESULT_DIR}${fileName}").exists()
    }

    protected def getFilePath(fileName){
        "${SOURCE_DIR}${fileName}"
    }

    protected def getFile(fileName){
        ImageIO.read(new File("${RESULT_DIR}${fileName}"))
    }

    protected def getEmptyMultipartFile(){
        new MockMultipartFile('empty', new byte[0])
    }

    protected def getMultipartFile(fileName){
        new MockMultipartFile('uploaded', new FileInputStream(getFilePath(fileName)))
    }

    void testBaseSetupLocalFile(){
        shouldFail(IllegalArgumentException){
            burningImageService.doWith(null, null)
        }

        shouldFail(IllegalArgumentException){
            burningImageService.doWith('not/existing/file', null)
        }

        shouldFail(IllegalArgumentException){
            burningImageService.doWith(getFilePath('image.jpg'), null)
        }

        shouldFail(FileNotFoundException){
            burningImageService.doWith(getFilePath('image.jpg'), 'not/exists/dir')
        }

        shouldFail(FileNotFoundException){
            burningImageService.doWith('not/existing/file', RESULT_DIR)
        }

        assertTrue burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR) instanceof Worker
    }
    
    void testBaseSetupMultipart(){
        shouldFail(IllegalArgumentException){
            burningImageService.doWith(null, null)
        }

        shouldFail(IllegalArgumentException){
            burningImageService.doWith(getEmptyMultipartFile(), null)
        }

        shouldFail(IllegalArgumentException){
            burningImageService.doWith(getMultipartFile('image.jpg'), null)
        }

        shouldFail(FileNotFoundException){
            burningImageService.doWith(getMultipartFile('image.jpg'), 'not/exists/dir')
        }

        shouldFail(FileNotFoundException){
            burningImageService.doWith(getEmptyMultipartFile(), RESULT_DIR)
        }

        assertTrue burningImageService.doWith(getMultipartFile('image.jpg'), RESULT_DIR) instanceof Worker
    }

    void testScaleApproximateLocalFileJpg() {
        assertFalse fileExists('image.jpg')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
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

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
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

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
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

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

     void testScaleAccurateLocalFileJpgBig() {
        assertFalse fileExists('image2.jpg')
        def file

        burningImageService.doWith(getFilePath('image2.jpg'), RESULT_DIR).execute {
            it.scaleAccurate(178, 178)
        }
        
        assertTrue fileExists('image2.jpg')
        file = getFile('image2.jpg')
        assertTrue file.width == 178
        assertTrue file.height == 178

        cleanUpTestDir()
        assertFalse fileExists('image2.jpg')

        burningImageService.doWith(getFilePath('image2.jpg'), RESULT_DIR).execute {
            it.scaleAccurate(51, 62)
        }

        assertTrue fileExists('image2.jpg')
        file = getFile('image2.jpg')
        assertTrue file.width == 51
        assertTrue file.height == 62
    }

    void testScaleAccurateLocalFileJpg() {
        assertFalse fileExists('image.jpg')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 50
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 50
        assertTrue file.height == 2000

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 2000
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 2000
        assertTrue file.height == 2000
    }

    void testScaleApproximateLocalFileJpgWithName() {
        def scaleResult, result, file

        assertFalse fileExists('jpg-50x50.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute('jpg-50x50',{
            scaleResult = it.scaleApproximate(50, 50)
        })
    
        assertEquals 'jpg-50x50.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('jpg-50x50.jpg')
        file = getFile('jpg-50x50.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('jpg-50x2000.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute('jpg-50x2000',{
            scaleResult = it.scaleApproximate(50, 2000)
        })
        assertEquals 'jpg-50x2000.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('jpg-50x2000.jpg')
        file = getFile('jpg-50x2000.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('jpg-2000x50.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute('jpg-2000x50',{
            scaleResult = it.scaleApproximate(2000, 50)
        })
        assertEquals 'jpg-2000x50.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('jpg-2000x50.jpg')
        file = getFile('jpg-2000x50.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('jpg-2000x2000.jpg')

        result = burningImageService.doWith(getFilePath('image.jpg'), RESULT_DIR).execute('jpg-2000x2000',{
            scaleResult = it.scaleApproximate(2000, 2000)
        })
        assertEquals 'jpg-2000x2000.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('jpg-2000x2000.jpg')
        file = getFile('jpg-2000x2000.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleApproximateLocalFileBmp() {
        assertFalse fileExists('image.bmp')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 50)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 2000)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 50)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 2000)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleAccurateLocalFileBmp() {
        assertFalse fileExists('image.bmp')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 50)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width == 50
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 2000)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width == 50
        assertTrue file.height == 2000

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 50)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width == 2000
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 2000)
        }
        assertEquals 'image.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.bmp')
        file = getFile('image.bmp')
        assertTrue file.width == 2000
        assertTrue file.height == 2000
    }

    void testScaleApproximateLocalFileBmpWithName() {
        def scaleResult, result, file

        assertFalse fileExists('bmp-50x50.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute('bmp-50x50',{
            scaleResult = it.scaleApproximate(50, 50)
        })

        assertEquals 'bmp-50x50.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('bmp-50x50.bmp')
        file = getFile('bmp-50x50.bmp')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('bmp-50x2000.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute('bmp-50x2000',{
            scaleResult = it.scaleApproximate(50, 2000)
        })
        assertEquals 'bmp-50x2000.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('bmp-50x2000.bmp')
        file = getFile('bmp-50x2000.bmp')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('bmp-2000x50.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute('bmp-2000x50',{
            scaleResult = it.scaleApproximate(2000, 50)
        })
        assertEquals 'bmp-2000x50.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('bmp-2000x50.bmp')
        file = getFile('bmp-2000x50.bmp')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('bmp-2000x2000.bmp')

        result = burningImageService.doWith(getFilePath('image.bmp'), RESULT_DIR).execute('bmp-2000x2000',{
            scaleResult = it.scaleApproximate(2000, 2000)
        })
        assertEquals 'bmp-2000x2000.bmp', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('bmp-2000x2000.bmp')
        file = getFile('bmp-2000x2000.bmp')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleApproximateLocalFilePng() {
        assertFalse fileExists('image.png')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 50)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 2000)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 50)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 2000)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleAccurateLocalFilePng() {
        assertFalse fileExists('image.png')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 50)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width == 50
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 2000)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width == 50
        assertTrue file.height == 2000

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 50)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width == 2000
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 2000)
        }
        assertEquals 'image.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.png')
        file = getFile('image.png')
        assertTrue file.width == 2000
        assertTrue file.height == 2000
    }

    void testScaleApproximateLocalFilePngWithName() {
        def scaleResult, result, file

        assertFalse fileExists('png-50x50.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute('png-50x50',{
            scaleResult = it.scaleApproximate(50, 50)
        })

        assertEquals 'png-50x50.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('png-50x50.png')
        file = getFile('png-50x50.png')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('png-50x2000.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute('png-50x2000',{
            scaleResult = it.scaleApproximate(50, 2000)
        })
        assertEquals 'png-50x2000.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('png-50x2000.png')
        file = getFile('png-50x2000.png')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('png-2000x50.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute('png-2000x50',{
            scaleResult = it.scaleApproximate(2000, 50)
        })
        assertEquals 'png-2000x50.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('png-2000x50.png')
        file = getFile('png-2000x50.png')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('png-2000x2000.png')

        result = burningImageService.doWith(getFilePath('image.png'), RESULT_DIR).execute('png-2000x2000',{
            scaleResult = it.scaleApproximate(2000, 2000)
        })
        assertEquals 'png-2000x2000.png', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('png-2000x2000.png')
        file = getFile('png-2000x2000.png')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleApproximateLocalFileGif() {
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(50, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleApproximate(2000, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void testScaleAccurateLocalFileGif() {
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')
        def scaleResult, result, file

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 50
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(50, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 50
        assertTrue file.height == 2000

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 50)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 2000
        assertTrue file.height == 50

        cleanUpTestDir()
        assertFalse fileExists('image.gif')
        assertFalse fileExists('image.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute {
            scaleResult = it.scaleAccurate(2000, 2000)
        }
        assertEquals 'image.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('image.jpg')
        file = getFile('image.jpg')
        assertTrue file.width == 2000
        assertTrue file.height == 2000
    }

    void testScaleApproximateLocalFileGifWithName() {
        def scaleResult, result, file

        assertFalse fileExists('50x50.gif')
        assertFalse fileExists('50x50.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute('50x50',{
            scaleResult = it.scaleApproximate(50, 50)
        })

        assertEquals '50x50.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('50x50.jpg')
        file = getFile('50x50.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('50x2000.gif')
        assertFalse fileExists('50x2000.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute('50x2000',{
            scaleResult = it.scaleApproximate(50, 2000)
        })
        assertEquals '50x2000.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('50x2000.jpg')
        file = getFile('50x2000.jpg')
        assertTrue file.width <= 50
        assertTrue file.height <= 2000

        cleanUpTestDir()
        assertFalse fileExists('2000x50.gif')
        assertFalse fileExists('2000x50.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute('2000x50',{
            scaleResult = it.scaleApproximate(2000, 50)
        })
        assertEquals '2000x50.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('2000x50.jpg')
        file = getFile('2000x50.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 50

        cleanUpTestDir()
        assertFalse fileExists('2000x2000.gif')
        assertFalse fileExists('2000x2000.jpg')

        result = burningImageService.doWith(getFilePath('image.gif'), RESULT_DIR).execute('2000x2000',{
            scaleResult = it.scaleApproximate(2000, 2000)
        })
        assertEquals '2000x2000.jpg', scaleResult
        assertTrue result instanceof Worker
        assertTrue fileExists('2000x2000.jpg')
        file = getFile('2000x2000.jpg')
        assertTrue file.width <= 2000
        assertTrue file.height <= 2000
    }

    void xtestScaleAccurateLocalFileJpgNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute ('jpgx200x200', {
                scaleResult = it.scaleAccurate(200, 200)
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgx200x200.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestScaleAccurateLocalFileBmpNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/bmpFile.bmp')
        .resultDir('./resources/resultImages/')
        .execute('bmpx200x200', {
                scaleResult = it.scaleAccurate(200, 200)
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpx200x200.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestScaleAccurateLocalFileGifNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/gifFile.gif')
        .resultDir('./resources/resultImages/')
        .execute('gifx200x200', {
                scaleResult = it.scaleAccurate(200, 200)
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'gifx200x200.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestScaleAccurateLocalFilePngNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/pngFile.png')
        .resultDir('./resources/resultImages/')
        .execute('pngx200x200', {
                scaleResult = it.scaleAccurate(200, 200)
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'pngx200x200.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestScaleApproximateRemoteFileJpg() {
        def scaleResult

        def name = 'foto'
        def originalFilename = 'jpgFile.jpg'
        def contentType = 'image/jpeg'
        def file =  new FileInputStream('./resources/testImages/jpgFile.jpg')
        def uploadedFile = new MockMultipartFile(name, originalFilename, contentType, file)

        def result = burningImageService.loadImage(uploadedFile)
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.scaleApproximate(200, 200)
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestScaleApproximateRemoteFileGif() {
        def scaleResult

        def name = 'foto'
        def originalFilename = 'gifFile.gif'
        def contentType = 'image/gif'
        def file =  new FileInputStream('./resources/testImages/gifFile.gif')
        def uploadedFile = new MockMultipartFile(name, originalFilename, contentType, file)

        def result = burningImageService.loadImage(uploadedFile)
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.scaleApproximate(200, 200)
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'gifFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestScaleApproximateRemoteFilePng() {
        def scaleResult

        def name = 'foto'
        def originalFilename = 'pngFile.png'
        def contentType = 'image/png'
        def file =  new FileInputStream('./resources/testImages/pngFile.png')
        def uploadedFile = new MockMultipartFile(name, originalFilename, contentType, file)

        def result = burningImageService.loadImage(uploadedFile)
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.scaleApproximate(200, 200)
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'pngFile.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestScaleApproximateRemoteFileBmp() {
        def scaleResult

        def name = 'foto'
        def originalFilename = 'bmpFile.bmp'
        def contentType = 'image/png'
        def file =  new FileInputStream('./resources/testImages/bmpFile.bmp')
        def uploadedFile = new MockMultipartFile(name, originalFilename, contentType, file)

        def result = burningImageService.loadImage(uploadedFile)
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.scaleApproximate(200, 200)
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpFile.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestWatermarkError() {
        shouldFail(IllegalArgumentException){
            def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
            .resultDir('./resources/resultImages/')
            .execute {
                it.watermark(null, null)
            }
        }

        shouldFail(IllegalArgumentException){
            def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
            .resultDir('./resources/resultImages/')
            .execute {
                it.watermark('/not/exists', ['left':10, 'right': 10])
            }
        }

        shouldFail(IllegalArgumentException){
            def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
            .resultDir('./resources/resultImages/')
            .execute {
                it.watermark('/not/exists', ['top':10, 'bottom': 10])
            }
        }

        shouldFail(FileNotFoundException){
            def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
            .resultDir('./resources/resultImages/')
            .execute {
                it.watermark('/not/exists')
            }
        }
    }

    void xtestWatermarkLocalJpg() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.watermark('./resources/testImages/watermark.png')
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestWatermarkLocalGif() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/gifFile.gif')
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.watermark('./resources/testImages/watermark.png')
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'gifFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestWatermarkLocalPng() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/pngFile.png')
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.watermark('./resources/testImages/watermark.png')
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'pngFile.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void xtestWatermarkLocalBmp() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/bmpFile.bmp')
        .resultDir('./resources/resultImages/')
        .execute {
            scaleResult = it.watermark('./resources/testImages/watermark.png')
        }

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpFile.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestWatermarkLocalJpgLocation() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute('left-top-watermark', {
                scaleResult = it.watermark('./resources/testImages/watermark.png', ['left': 20, 'top': 20])
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'left-top-watermark.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

        
        result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute('right-bottom-watermark', {
                scaleResult = it.watermark('./resources/testImages/watermark.png', ['right': 20, 'bottom': 20])
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'right-bottom-watermark.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestWatermarkRemoteJpgLocation() {
        def scaleResult

        def name = 'foto'
        def originalFilename = 'pngFile.png'
        def contentType = 'image/png'
        def file =  new FileInputStream('./resources/testImages/pngFile.png')
        def uploadedFile = new MockMultipartFile(name, originalFilename, contentType, file)
        
        def result = burningImageService.loadImage(uploadedFile)
        .resultDir('./resources/resultImages/')
        .execute('remote-watermark', {
                scaleResult = it.watermark('./resources/testImages/watermark.png')
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'remote-watermark.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void xtestChaining() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute('watermark-scale', {
                it.watermark('./resources/testImages/watermark.png')
                scaleResult = it.scaleApproximate(200, 200)
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'watermark-scale.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

        result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
        .resultDir('./resources/resultImages/')
        .execute('scale-watermark', {
                it.scaleApproximate(200, 200)
                scaleResult = it.watermark('./resources/testImages/watermark.png')
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'scale-watermark.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

        result = burningImageService.loadImage('./resources/testImages/pngFile.png')
        .resultDir('./resources/resultImages/')
        .execute('first-scale-watermark', {
                it.scaleAccurate(300, 300)
                scaleResult = it.watermark('./resources/testImages/watermark.png')
            })

        assertTrue result instanceof BurningImageService
        assertEquals 'first-scale-watermark.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

        result.execute('second-watermark-scale', {
                it.scaleAccurate(200, 300)
                scaleResult = it.watermark('./resources/testImages/watermark.png')
            })

        assertEquals 'second-watermark-scale.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }
}
