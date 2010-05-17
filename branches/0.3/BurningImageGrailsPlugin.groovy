class BurningImageGrailsPlugin {
    // the plugin version
    def version = "0.3.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/domain/pl/burningice/plugins/image/ast/**",
        "resources/**",
        "web-app/**"
    ]

    def author = "Pawel Gdula"
    def authorEmail = "pawel.gdula@burningice.pl"
    def title = "Burning Image"
    def description = "Image manipulation plugin"

    // URL to the plugin's documentation
    def documentation = "http://code.google.com/p/burningimage/"

    def doWithSpring = {
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def doWithWebDescriptor = { xml ->
    }

    def doWithDynamicMethods = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }
}
