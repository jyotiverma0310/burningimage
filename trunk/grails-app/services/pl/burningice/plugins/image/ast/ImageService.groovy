package pl.burningice.plugins.image.ast

class ImageService {

    boolean transactional = true

    def get(imageId) {
        Image.get(imageId)
    }
}
