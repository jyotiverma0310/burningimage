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
package pl.burningice.plugins.image.engines

import javax.imageio.ImageIO

/**
 *
 *
 * @author gdulus
 */
class Worker {

    /**
     * Path to output directory
     *
     * @var String
     */
    private def resultDir

    /**
     * Object representin image to manipulate
     *
     * @var ImageFile
     */
    private def loadedImage

    /**
     * Methods execute action on image
     * It use as a output file name name of orginal image
     *
     * @param Closure chain Chain of action on image
     * @return BurningImageService
     */
    def execute (chain) {
        work("${resultDir}/${loadedImage.name}", loadedImage.name, chain)
        this
    }

    /**
     * Methods execute action on image
     * Allows to specify output name by the user
     *
     * @param String Name of output image (without extension)
     * @param Closure chain Chain of action on image
     * @return Worker
     */
    Worker execute (outputFileName, chain) {
        def fileName = "${outputFileName}.${loadedImage.extension}"
        work("${resultDir}/${fileName}", fileName, chain)
    }

    /**
     * Perform work
     *
     * @param outputFilePath Specify path to output image
     * @param fileName Specify file name
     * @param chain Specify work field
     * @return Self
     */
    private Worker work(outputFilePath, fileName, chain){
        chain(new Action(loadedImage:loadedImage, fileName: fileName))
        save(outputFilePath)
        loadedImage.restoreOginalFile()
        this
    }

    /**
     * Save changed image
     *
     * @param outputFilePath Specify path to output image
     */
    private void save(outputFilePath){
        ImageIO.write(ImageIO.read(loadedImage.inputStream),
                      loadedImage.extension,
                      new File(outputFilePath));
    }
}

