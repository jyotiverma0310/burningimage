/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.burningice.plugins.image.engines.crop

import java.awt.image.renderable.ParameterBlock
import javax.media.jai.*;
import com.sun.media.jai.codec.*;
import javax.imageio.ImageIO
import pl.burningice.plugins.image.file.ImageFileFactory

/**
 *
 * @author gdulus
 */
class DefaultCropEngine {

    def execute(loadedImage, outputFilePath, deltaX, deltaY, width, height){
        ParameterBlock cropParams = new ParameterBlock();
        cropParams.addSource(loadedImage.getAsJaiStream());
        cropParams.add((float)Math.floor(deltaX)) // delta x
        cropParams.add((float)Math.floor(deltaY)) // delta y
        cropParams.add((float)width) // width
        cropParams.add((float)height) // height

        def croppedImage = JAI.create('crop', cropParams)

        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath));
        ImageIO.write(croppedImage.getAsBufferedImage(), loadedImage.encoder, output);
        output.close();
        ImageFileFactory.produce(new File(outputFilePath))
    }
}

