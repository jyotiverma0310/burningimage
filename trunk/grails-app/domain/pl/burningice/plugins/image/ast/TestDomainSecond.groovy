/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.burningice.plugins.image.ast

/**
 *
 * @author gdulus
 */
@FileImageContainer
class TestDomainSecond {

    String email

    static constraints = {
        email(email:true, blank:false)
    }
}

