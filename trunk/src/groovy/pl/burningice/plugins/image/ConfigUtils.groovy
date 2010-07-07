package pl.burningice.plugins.image

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import pl.burningice.plugins.image.engines.RenderingEngine

class ConfigUtils {

    private static final String CONFIG_NAMESPACE = 'bi'

    private static final RenderingEngine DEFAULT_RENDERING_ENGINE = RenderingEngine.JAI 

    static Map getContainerConfig(String imageContainerName){
        CH.config?."${CONFIG_NAMESPACE}"?."${imageContainerName}"
    }

    static RenderingEngine getEngine(){
        return CH.config?."${CONFIG_NAMESPACE}"?.renderingEngine ?: DEFAULT_RENDERING_ENGINE
    }
}
