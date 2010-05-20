package pl.burningice.plugins.image.ast

import pl.burningice.plugins.image.ast.intarface.DBImageContainer 

class Image {

    String type

    byte[] data

    static  belongsTo = [DBImageContainer]

    static constraints = {
        type(nullable:false, blank:false)
        data(nullable:false, blank:false)
    }

    static mapping = {
		table 'bi_images'
	}
}
