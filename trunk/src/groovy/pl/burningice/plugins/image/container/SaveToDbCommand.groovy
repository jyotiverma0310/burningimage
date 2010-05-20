package pl.burningice.plugins.image.container

import pl.burningice.plugins.image.ast.intarface.DBImageContainer
import pl.burningice.plugins.image.ast.Image
import java.awt.image.BufferedImage

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 4:00:22 PM
 * To change this template use File | Settings | File Templates.
 */
class SaveToDbCommand implements SaveCommand {

  private DBImageContainer container

  private String size    

  SaveToDbCommand(DBImageContainer container, String size){
    this.container = container
    this.size = size
  }

  def void execute(byte[] source, String extension) {
    Image image = new Image(data:source, type: extension)
    image.save(flush:true)
    container.biImage[size] = image
  }
}
