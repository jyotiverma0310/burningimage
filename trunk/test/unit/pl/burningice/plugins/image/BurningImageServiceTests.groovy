package pl.burningice.plugins.image

import grails.test.*
import org.springframework.mock.web.MockMultipartFile

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class BurningImageServiceTests extends GrailsUnitTestCase {

    def burningImageService

    protected void setUp() {
        super.setUp()
        burningImageService = new BurningImageService()
    }

    protected void tearDown() {
        super.tearDown()
        burningImageService = null
    }

    void testLoadImageLocal() {
         shouldFail(IllegalArgumentException){
            burningImageService.loadImage(null)
        }

        shouldFail(FileNotFoundException){
            burningImageService.loadImage('not/existing/file')
        }

        assertTrue burningImageService.loadImage('./resources/testImages/jpgFile.jpg') instanceof BurningImageService
    }

    void testLoadImageMultipart() {
        shouldFail(IllegalArgumentException){
            burningImageService.loadImage(null)
        }

        def uploadedFile = new MockMultipartFile('empty', new byte[0])

        shouldFail(FileNotFoundException){
            burningImageService.loadImage(uploadedFile)
        }

        uploadedFile = new MockMultipartFile('uploaded', new FileInputStream('./resources/testImages/jpgFile.jpg'))
        assertTrue burningImageService.loadImage(uploadedFile) instanceof BurningImageService
    }

    void testResultDir() {
        shouldFail(IllegalArgumentException) {
            burningImageService.resultDir(null)
        }

        shouldFail(FileNotFoundException) {
            burningImageService.resultDir('not/exists/dir')
        }

        assertTrue burningImageService.resultDir('./resources/resultImages/') instanceof BurningImageService
    }

    void testScaleApproximateLocalFileJpg() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void testScaleApproximateLocalFileBmp() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/bmpFile.bmp')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpFile.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleApproximateLocalFileGif() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/gifFile.gif')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'gifFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleApproximateLocalFilePng() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/pngFile.png')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'pngFile.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

        void testScaleApproximateLocalFileJpgNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
                                        .resultDir('./resources/resultImages/')
                                        .execute ('jpgx200x200', {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        })

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgx200x200.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void testScaleApproximateLocalFileBmpNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/bmpFile.bmp')
                                        .resultDir('./resources/resultImages/')
                                        .execute('bmpx200x200', {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        })

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpx200x200.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleApproximateLocalFileGifNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/gifFile.gif')
                                        .resultDir('./resources/resultImages/')
                                        .execute('gifx200x200', {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        })

        assertTrue result instanceof BurningImageService
        assertEquals 'gifx200x200.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleApproximateLocalFilePngNameGived() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/pngFile.png')
                                        .resultDir('./resources/resultImages/')
                                        .execute('pngx200x200', {
                                            scaleResult = it.scaleApproximate(200, 200)
                                        })

        assertTrue result instanceof BurningImageService
        assertEquals 'pngx200x200.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleAccurateLocalFileJpg() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/jpgFile.jpg')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleAccurate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'jpgFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()

    }

    void testScaleAccurateLocalFileBmp() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/bmpFile.bmp')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleAccurate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'bmpFile.bmp', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleAccurateLocalFileGif() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/gifFile.gif')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleAccurate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'gifFile.jpg', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

    void testScaleAccurateLocalFilePng() {
        def scaleResult
        def result = burningImageService.loadImage('./resources/testImages/pngFile.png')
                                        .resultDir('./resources/resultImages/')
                                        .execute {
                                            scaleResult = it.scaleAccurate(200, 200)
                                        }

        assertTrue result instanceof BurningImageService
        assertEquals 'pngFile.png', scaleResult
        assertTrue new File("./resources/resultImages/${scaleResult}").exists()
    }

        void testScaleAccurateLocalFileJpgNameGived() {
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

    void testScaleAccurateLocalFileBmpNameGived() {
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

    void testScaleAccurateLocalFileGifNameGived() {
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

    void testScaleAccurateLocalFilePngNameGived() {
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

    void testScaleApproximateRemoteFileJpg() {
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

    void testScaleApproximateRemoteFileGif() {
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

    void testScaleApproximateRemoteFilePng() {
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

    void testScaleApproximateRemoteFileBmp() {
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

    void testWatermarkError() {
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

    void testWatermarkLocalJpg() {
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

    void testWatermarkLocalGif() {
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

    void testWatermarkLocalPng() {
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

    void testWatermarkLocalBmp() {
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

    void testWatermarkLocalJpgLocation() {
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

    void testWatermarkRemoteJpgLocation() {
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

    void testChaining() {
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
