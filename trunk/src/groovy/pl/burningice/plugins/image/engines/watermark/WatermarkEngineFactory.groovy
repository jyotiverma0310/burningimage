package pl.burningice.plugins.image.engines.watermark

import pl.burningice.plugins.image.engines.RenderingEngine
import pl.burningice.plugins.image.engines.scale.*

/**
 * Created by IntelliJ IDEA.
 * User: Gdulus
 * Date: 2010-07-07
 * Time: 22:55:54
 * To change this template use File | Settings | File Templates.
 */

class WatermarkEngineFactory {

    public static WatermarkEngine produceEngine(RenderingEngine engineType){
        if (engineType == RenderingEngine.JAI){
            return new JaiWatermarkEngine()
        }

        if (engineType == RenderingEngine.IMAGE_MAGICK){
            return new ImageMagickWatermarkEngine()
        }

        throw new IllegalArgumentException("There is no watermark engine for type ${engineType}")
    }
}