package pl.burningice.plugins.image.engines.watermark

import java.awt.image.BufferedImage
import pl.burningice.plugins.image.file.ImageFile

/**
 * Created by IntelliJ IDEA.
 * User: Gdulus
 * Date: 2010-07-07
 * Time: 23:06:09
 * To change this template use File | Settings | File Templates.
 */
public interface WatermarkEngine {

    public BufferedImage execute(File watermarkFile, ImageFile loadedImage, Map position, float alpha) 

}