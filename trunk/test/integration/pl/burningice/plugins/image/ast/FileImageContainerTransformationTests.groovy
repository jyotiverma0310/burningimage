package pl.burningice.plugins.image.ast

import grails.test.*
import pl.burningice.plugins.image.ast.intarface.FileImageContainer

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class FileImageContainerTransformationTests extends GrailsUnitTestCase {
    
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testFileImageContainerInterface() {

        println "fields: " + TestDomain.fields.name
        println "methods: " + TestDomain.methods.name
        println "interfaces: " + TestDomain.interfaces.name

        assertTrue !TestDomain.fields.name.contains('imageExtension')
        assertTrue TestDomain.methods.name.contains('getImageExtension')
        assertTrue TestDomain.methods.name.contains('setImageExtension')

        def testDomain = new TestDomain()
        assertTrue testDomain instanceof FileImageContainer
        assertNull testDomain.imageExtension

        testDomain = new TestDomain(imageExtension:'jpg')
        assertNotNull testDomain.imageExtension

        testDomain = new TestDomain()
        testDomain.imageExtension = 'gif'
        assertTrue testDomain.imageExtension == 'gif'
        testDomain.imageExtension = 'jpg'
        assertTrue testDomain.imageExtension == 'jpg'
    }
    
    void testFileImageContainerConstraints(){
        def testDomain = new TestDomain()
        assertFalse testDomain.hasErrors()
        assertTrue testDomain.validate()

        def testDomainSecond = new TestDomainSecond()
        assertFalse testDomainSecond.hasErrors()
        assertFalse testDomainSecond.validate()
        assertFalse testDomainSecond.errors.hasFieldErrors('imageExtension')
        assertTrue testDomainSecond.errors.hasFieldErrors('email')

        println testDomainSecond.errors
    }
}
