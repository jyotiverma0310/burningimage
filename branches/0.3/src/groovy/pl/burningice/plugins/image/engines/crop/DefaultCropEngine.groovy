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
package pl.burningice.plugins.image.engines.crop

import java.awt.image.renderable.ParameterBlock
import javax.media.jai.*;

/**
 * Engine to cropping image
 *
 * @author pawel.gdula@burningice.pl
 */
class DefaultCropEngine {

    /**
     * Performs crop action on image
     *
     * @param loadedImage Object representing current image
     * @param deltaX Offset from left border of image
     * @param deltaY Offset from top border of image
     * @param width Size (horizontal) of crop region
     * @param width Size (vertical) of crop region
     */
    def execute(loadedImage, deltaX, deltaY, width, height){
        ParameterBlock cropParams = new ParameterBlock();
        cropParams.addSource(loadedImage.getAsJaiStream());
        cropParams.add((float)deltaX) // delta x
        cropParams.add((float)deltaY) // delta y
        cropParams.add((float)width) // width
        cropParams.add((float)height) // height

        def croppedImage = JAI.create('crop', cropParams)
        croppedImage.getAsBufferedImage()
    }
}

