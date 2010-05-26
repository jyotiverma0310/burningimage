package pl.burningice.plugins.image.ast

class Image {

    String type

    byte[] data

    static constraints = {
        type(nullable:false, blank:false)
        data(nullable:false, blank:false)
    }

    static mapping = {
		table 'bi_images'
    }
}
