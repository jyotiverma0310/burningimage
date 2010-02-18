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
package pl.burningice.plugins.image.engines.text

import javax.imageio.ImageIO

/**
 * Engine for typing text on image
 *
 * @author pawel.gdula@burningice.pl
 */
class DefaultTextEngine {

    /**
     * Object representing image file
     *
     */
    def fileToMark

    /**
     * Object representing image canvas
     *
     */
    def graphics

    /**
     * Default class constructor
     *
     * @param color Representing current color of text. Can be null
     * @param font Representing current font of text. Can be null
     * @param loadedImage Image to type on it
     */
    DefaultTextEngine(color, font, loadedImage){
        fileToMark = ImageIO.read(loadedImage.inputStream);
        graphics = fileToMark.createGraphics();

        if (color) {
            graphics.setColor(color);
        }

        if (font) {
            graphics.setFont(font);
        }
    }

    /**
     * Performs write actions
     *
     * @param text Text to type
     * @param deltaX Offset from left border of image
     * @param deltaY Offset from top border of image
     */
    def write(text, deltaX, deltaY) {
        graphics.drawString(text, deltaX, deltaY);
    }

    /**
     * Retutrns write result
     *
     * @return BufferedImage objects representing current image
     */
    def getResult(){
        graphics.dispose();
        fileToMark
    }
}
