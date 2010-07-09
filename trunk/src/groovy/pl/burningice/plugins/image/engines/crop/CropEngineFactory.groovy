package pl.burningice.plugins.image.engines.crop

import pl.burningice.plugins.image.engines.RenderingEngine
import pl.burningice.plugins.image.engines.scale.*

/**
 * Created by IntelliJ IDEA.
 * User: Gdulus
 * Date: 2010-07-07
 * Time: 22:55:54
 * To change this template use File | Settings | File Templates.
 */

class CropEngineFactory {

    public static ScaleEngine produceAccurateEngine(RenderingEngine engineType){
        if (engineType == RenderingEngine.JAI){
            return new JaiAccurateScaleEngine()
        }

        if (engineType == RenderingEngine.IMAGE_MAGICK){
            return new ImageMagickAccurateScaleEngine()
        }

        throw new IllegalArgumentException("There is no scale engine for type ${engineType}")
    }

    public static ScaleEngine produceApproximateEngine(RenderingEngine engineType){
        if (engineType == RenderingEngine.JAI){
            return new JaiApproximateScaleEngine()
        }

        if (engineType == RenderingEngine.IMAGE_MAGICK){
            return new ImageMagickApproximateScaleEngine()
        }

        throw new IllegalArgumentException("There is no scale engine for type ${engineType}")
    }
}