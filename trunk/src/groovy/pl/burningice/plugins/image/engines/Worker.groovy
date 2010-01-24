/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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


    private Worker work(outputFilePath, fileName, chain){
        chain(new Action(loadedImage:loadedImage, fileName: fileName))
        save(outputFilePath)
        loadedImage.restoreOginalFile()
        this
    }

    private void save(outputFilePath){
        ImageIO.write(ImageIO.read(loadedImage.inputStream),
                      loadedImage.extension,
                      new File(outputFilePath));
    }
}

