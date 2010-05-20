package pl.burningice.plugins.image.container

import javax.imageio.ImageIO
import pl.burningice.plugins.image.ast.intarface.FileImageContainer

class SaveToFileCommand implements SaveCommand {

  private FileImageContainer container

  private String outputFilePath

  SaveToFileCommand(FileImageContainer container, String outputFilePath){
    this.container = container
    this.outputFilePath = outputFilePath
  }

  void execute(byte[] source, String extension) {
    ImageIO.write(ImageIO.read(new ByteArrayInputStream(source)), extension, new File("${outputFilePath}.${extension}"));
    // this will updated for every copy of image, but be always the same
    this.container.imageExtension = extension
  }
}
