package pl.burningice.plugins.image.container

import java.awt.image.BufferedImage

/**
 * Created by IntelliJ IDEA.
 * User: gdulus
 * Date: May 14, 2010
 * Time: 3:58:32 PM
 * To change this template use File | Settings | File Templates.
 */
interface SaveCommand {

    void execute(byte[] source, String extension);
}