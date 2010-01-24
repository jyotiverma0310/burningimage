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

import org.springframework.web.multipart.MultipartFile
import  pl.burningice.plugins.image.file.*
import  pl.burningice.plugins.image.engines.*

/**
 * Main entry for the plugin
 *
 * @author pawel.gdula@burningice.pl
 */
class BurningImageService {

    boolean transactional = false

    /**
     * Global setting for output direcotry
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

    def doWith(String filePath, String resultDir){
        if (!filePath || !resultDir) {
            throw new IllegalArgumentException('Source file and output directory paths must be provided')
        }

        def file = new File(filePath)

        if (!file.exists()) {
            throw new FileNotFoundException("There is no source file: ${filePath}")
        }

        getWorker(file, resultDir)
    }

   def doWith(MultipartFile file, String resultDir) {
        if (!file || !resultDir) {
            throw new IllegalArgumentException('Source file and output directory path must be provided')
        }

        if (file.isEmpty()) {
            throw new FileNotFoundException("Uploaded file ${file.originalFilename} is empty")
        }

        getWorker(file, resultDir)
    }

    private def getWorker(file, resultDir){
        if (!(new File(resultDir).exists())) {
            throw new FileNotFoundException("There is no output ${resultDir} directory")
        }
        
        if (resultDir[-1] == '/'){
            resultDir = resultDir[0..-2]
        }

        new Worker(ImageFileFactory.produce(file), resultDir)
    }
}
