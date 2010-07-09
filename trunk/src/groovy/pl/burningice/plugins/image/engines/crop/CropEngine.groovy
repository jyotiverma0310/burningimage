package pl.burningice.plugins.image.engines.crop

import java.awt.image.BufferedImage
import pl.burningice.plugins.image.file.ImageFile

/**
 * Created by IntelliJ IDEA.
 * User: Gdulus
 * Date: 2010-07-07
 * Time: 23:06:09
 * To change this template use File | Settings | File Templates.
 */
public interface CropEngine {

    public BufferedImage execute(ImageFile loadedImage, int width, int height)

}