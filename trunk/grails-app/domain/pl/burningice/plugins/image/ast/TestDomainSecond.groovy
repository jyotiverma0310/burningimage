/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.burningice.plugins.image.ast

/**
 *
 * @author gdulus
 */
@FileImageContainer(field = 'avatar')
class TestDomainSecond {

    String email

    String lastname

    static transients = ['lastname']

    static constraints = {
        email(email:true, blank:false)
    }
}

