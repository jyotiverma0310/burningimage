**Note**: Due probles with text writing on bmp files with ImageMagick, currently, only for this file type, JAI rendering engine is used

# Introduction #

BurningImage support JAI and ImageMagick to all its actions (watermarking, cropping, thumbnailing and text writing).

## JAI ##
This engine is used by default, don't require any additional settings but produce images with quality lower than ImageMagick. This engine don't allow for any additional configuration.

## ImageMagick ##
BurningImage uses ImageMagick in version 6.3.9-Q16 through JMagick library. It require additional server configuration. To enable this engine in BI put this in your Config.groovy file:

```
...

bi.renderingEngine = RenderingEngine.IMAGE_MAGICK

...
```

You can also specify quality (0-100) and compresion (0-100) properties:

```
...

bi.imageMagickQuality = 50     
bi.imageMagickCompression= 50

...
```

By default **imageMagickQuality** equals 100 and **imageMagickCompression** equals 0