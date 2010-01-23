/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.burningice.plugins.image.engines

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
        chain(new Action(loadedImage:loadedImage,
                         outputFilePath: "${resultDir}/${loadedImage.name}",
                         fileName: loadedImage.name))
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
        chain(new Action(loadedImage:loadedImage,
                         outputFilePath: "${resultDir}/${fileName}",
                         fileName: fileName))
        this
    }
}

