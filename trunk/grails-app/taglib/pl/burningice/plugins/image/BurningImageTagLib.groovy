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

import pl.burningice.plugins.image.container.ContainerUtils
import pl.burningice.plugins.image.ast.intarface.FileImageContainer

/**
 * Taglib for usage with annotated image container
 *
 * @author pawel.gdula@burningice.pl
 */
class BurningImageTagLib {

    static namespace = 'bi'

    /**
     * Display html img tag with path to stored image
     *
     * @param size Size of the image that should be displayed
     * @param bean Image container that hold information about image
     * @param id Value of id attribute
     * @param onclick Value of onclick attribute
     * @param name Value of name attribute
     * @param title Value of title attribute
     * @param alt Value of alt attribute
     */
    def img =  { attrs, body ->
        def path = resource(attrs, body)

        if (!attrs.bean.ident()){
            return null
        }

        def id = attrs.id ? " id=\"${attrs.id}\"" : ''
        def onclick = attrs.onclick ? " onclick=\"${attrs.onclick}\"" : ''
        def name = attrs.name ? " name=\"${attrs.name}\"" : ''
        def title = attrs.title ? " title=\"${attrs.title}\"" : ''
        def alt = attrs.alt ?: ''
        out << "<img src=\"${path}\" alt=\"${alt}\"${id}${onclick}${name}${title} />"
    }

    /**
     * Create link to image stored on the server
     *
     * @param size Size of the image that should be displayed
     * @param bean Image container that hold information about image
     */
    def resource = {attrs, body ->
        if (!attrs.size || !attrs.bean){
            throw new IllegalArgumentException("Atrribute bean and size can't be empty/null")
        }

        if (!attrs.bean.ident()){
            return null
        }

        out << g.resource(getResourceData(attrs.size, attrs.bean))
    }

    /**
     * Retrieve information about file name and storage directory on base
     * of image size name and image container object
     *
     * @param size Size of the image that should be displayed
     * @param bean Image container that hold information about image
     */
    private def getResourceData(size, FileImageContainer imageContainer){
        def config = ContainerUtils.getConfig(imageContainer)

        if (!config){
            throw new IllegalArgumentException("There is no config for ${imageContainer.class.name}")
        }

        [dir:config.outputDir, file:ContainerUtils.getFullName(size, imageContainer, config)]
    }
}
