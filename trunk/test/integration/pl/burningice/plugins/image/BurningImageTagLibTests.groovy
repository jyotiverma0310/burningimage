package pl.burningice.plugins.image

import grails.test.*
import pl.burningice.plugins.image.ast.TestDomain
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.plugins.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException

/**
 *
 * @author pawel.gdula@burningice.pl
 */
class BurningImageTagLibTests extends GroovyPagesTestCase {

    protected void setUp() {
        super.setUp()
        ConfigurationHolder.config = new ConfigObject()
    }

    void testResource() {
        def template = '<bi:resource size="${size}" bean="${bean}" />'
        def bean = new TestDomain(imageExtension:'jpg')
        def size = 'small'

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:null, bean:null] )
        }

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:size, bean:null] )
        }

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:null, bean:bean] )
        }

        def result = applyTemplate(template, [size:size, bean:bean])
        assertEquals '', result

        bean.save(flush:true)

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:size, bean:bean])
        }

        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: '/relative/path/to/dir/',
            prefix: 'prefixName',
            images: ['small':[scale:[width:100, height:100, type:null]]]
        ]

        result = applyTemplate(template, [size:size, bean:bean])
        assertEquals "/relative/path/to/dir/prefixName-${bean.ident()}-small.jpg", result
    }

    void testImage() {
        def template = '<bi:img size="${size}" bean="${bean}" />'
        def bean = new TestDomain(imageExtension:'jpg')
        def size = 'small'

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:null, bean:null] )
        }

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:size, bean:null] )
        }

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:null, bean:bean] )
        }

        def result = applyTemplate(template, [size:size, bean:bean])
        assertEquals '', result

        bean.save(flush:true)

        shouldFail(GrailsTagException){
            applyTemplate(template, [size:size, bean:bean])
        }

        ConfigurationHolder.config.bi.TestDomain = [
            outputDir: '/',
            prefix: null,
            images: ['small':[scale:[width:100, height:100, type:null]]]
        ]

        result = applyTemplate(template, [size:size, bean:bean])
        assertEquals "<img src=\"/${bean.ident()}-small.jpg\" />", result

        template = '<bi:img size="${size}" bean="${bean}" alt="${alt}" id="${id}" onclick="${onclick}" title="${title}" name="${name}" />'
        result = applyTemplate(template, [size:size, bean:bean, alt:'img-alt', id:'img-id', onclick:'alert()', title:'img-title', name:'img-name'])
        assertEquals "<img src=\"/${bean.ident()}-small.jpg\" alt=\"img-alt\" id=\"img-id\" name=\"img-name\" onclick=\"alert()\" title=\"img-title\"/>", result
    }
}
