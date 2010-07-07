/*
Copyright (c) 2009 Pawel Gdula <pawel.gdula@burningice.pl>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package pl.burningice.plugins.image.file

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import com.sun.media.jai.codec.SeekableStream
import com.sun.media.jai.codec.ByteArraySeekableStream
import javax.media.jai.*
import java.awt.Dimension

/**
 * Base class for all image sources (File, MultipartFile)
 *
 * @author pawel.gdula@burningice.pl
 */
abstract class ImageFile {

    /**
     * Gif image output format
     *
     * @const String
     */
    private static final def GIF_OUTPUT_FORMAT = 'jpg'

    /**
     * Stream of image
     *
     */
    SeekableStream stream

    /**
     * Name of the source file
     *
     * @var String
     */
    String sourceFileName

    /**
     * Mapping file extension >> JAI encoder
     *
     * @var [:]
     */
    @Lazy
    def extensionEncoderMapping = [
        'jpg': 'JPEG',
        'jpeg': 'JPEG',
        'gif': 'JPEG',
        'bmp': 'BMP',
        'png': 'PNG'
    ]

    /**
     * Default constructor
     *
     * @param name Original file name
     * @param fileStream Representing an image
     */
    ImageFile(String name, SeekableStream fileStream){
        stream = fileStream
        sourceFileName = name
    }

    /**
     * Returns file as JAI RenderedOp object
     *
     * @return RenderedOp
     */
    public RenderedOp getAsJaiStream() {
        JAI.create("stream", inputStream)
    }

    /**
     * Allows to get size of current image
     *
     * @return Dimension object representing size of current image
     */
    public Dimension getSize(){
        RenderedOp jaiStream = getAsJaiStream()
        return new Dimension(jaiStream.width, jaiStream.height)
    }

    /**
     * Returns uploaded image as byte array
     *
     * @return Uploaded image as byte array
     */
    public byte[] getAsByteArray(){
        return toByteArray(ImageIO.read(inputStream))
    }

    /**
     * Returns InputStream object representing current file
     *
     * @return InputStream
     */
    def getInputStream() {
        stream
    }

    /**
     * Method returns name of file
     * If file is gif, it will replace gif extension by
     * format specified by GIF_OUTPUT_FORMAT
     *
     * @return String
     */
    def getName() {
        // this action is done to fix name of output file
        // for gif images
        // @see
        // http://java.sun.com/products/java-media/jai/forDevelopers/jaifaq.html
        // "What image file formats are supported? What limitations do these have?"
        def parts = sourceFileName.split(/\./)
        parts[-1] = extension
        parts.join('.')
    }

    /**
     * Method returns file extension
     * If there is GIF file, it will be transformed into format specified by
     * GIF_OUTPUT_FORMAT const
     *
     * @return String
     */
    def getExtension() {
        def fileExtension = sourceFileName.split(/\./)[-1].toLowerCase()

        if (fileExtension == 'gif') {
            return GIF_OUTPUT_FORMAT
        }

        fileExtension
    }

    /**
     * Method returns encoder for file
     * Encoder is mapped by file extension
     *
     * @return String
     */
    def getEncoder() {
        extensionEncoderMapping[extension]
    }

    /**
     * Allows to update current image data
     *
     * @param image Changed image that should be used as new one
     */
    void update(BufferedImage image){
        stream = new ByteArraySeekableStream(toByteArray(image))
    }

    /**
     * Converts BufferedImage object pass as a parameter to byte array
     *
     * @param image BufferedImage object that should be transformed to byte array
     * @return Image as byte array
     */
    private byte[] toByteArray(BufferedImage image){
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        ImageIO.write(image, extension, output);
        return output.toByteArray()      
    }
}