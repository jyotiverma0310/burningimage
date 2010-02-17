package pl.burningice.plugins.image.test

import grails.test.*
import org.springframework.mock.web.MockMultipartFile
import javax.imageio.ImageIO

/**
 *
 * @author pawel.gdula@burningice.pl
 */
abstract class BurningImageUnitTestCase extends GrailsUnitTestCase {

    protected static final def SOURCE_DIR = './resources/testImages/'

    protected void setUp() {
        super.setUp()
        cleanUpTestDir()
    }

    protected void cleanUpTestDir(){
        new File(RESULT_DIR).list().toList().each {
            if(it != '.svn'){
                def filePath = "${RESULT_DIR}${it}"
                println "Remove ${filePath}"
                new File(filePath).delete()
            }
        }
    }

    protected def fileExists(fileName){
        println "search for file ${RESULT_DIR}${fileName}"
        new File("${RESULT_DIR}${fileName}").exists()
    }

    protected def getFilePath(fileName){
        "${SOURCE_DIR}${fileName}"
    }

    protected def getFile(fileName, dir = null){
        ImageIO.read(new File("${dir ?: RESULT_DIR}${fileName}"))
    }

    protected def getEmptyMultipartFile(){
        new MockMultipartFile('empty', new byte[0])
    }

    protected def getMultipartFile(fileName){
        def fileNameParts = fileName.split(/\./)
        def contentTypes = ['jpg':'image/jpeg', 'png':'image/png', 'gif':'image/gif', 'bmp':'image/bmp']
        new MockMultipartFile(fileNameParts[0],
                              fileName,
                              contentTypes[fileNameParts[1]],
                              new FileInputStream(getFilePath(fileName)))
    }
}