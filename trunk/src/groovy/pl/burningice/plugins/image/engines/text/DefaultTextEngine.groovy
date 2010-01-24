/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.burningice.plugins.image.engines.text

import pl.burningice.plugins.image.file.LocalImageFile
import pl.burningice.plugins.image.file.ImageFileFactory
import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Font

/**
 *
 * @author gdulus
 */
class DefaultTextEngine {

    def color

    def font

    def loadedImage

    def outputFilePath

    def fileToMark

    def graphics

    DefaultTextEngine(color, font, loadedImage, outputFilePath){
        this.color = color
        this.font = font
        this.loadedImage = loadedImage
        this.outputFilePath = outputFilePath

        fileToMark = ImageIO.read(loadedImage.source);
        graphics = fileToMark.createGraphics();

        if (color) {
            graphics.setColor(color);
        }

        if (font) {
            graphics.setFont(font);
        }
    }

    def write(text, deltaX, deltaY) {
        graphics.drawString(text, deltaX, deltaY);
    }

    def gerResult(){
        graphics.dispose();
        File outputfile = new File(outputFilePath);
        ImageIO.write(fileToMark, loadedImage.extension, outputfile);
        ImageFileFactory.produce(outputfile)
    }
}

