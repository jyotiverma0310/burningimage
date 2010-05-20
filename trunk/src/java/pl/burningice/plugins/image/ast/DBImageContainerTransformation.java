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
package pl.burningice.plugins.image.ast;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import pl.burningice.plugins.image.ast.intarface.DBImageContainer;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Object execute transformation of object marked by DBImageContainer annotation
 *
 * @author pawel.gdula@burningice.pl
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class DBImageContainerTransformation extends AbstractImageContainerTransformation {

    @Override
    protected void transformSpecified(ClassNode node, String fieldName) {
        // implements interface
        node.addInterface(new ClassNode(DBImageContainer.class));
        // add relation with images table
        FieldNode imageBindField = new FieldNode("biImage", Modifier.PRIVATE, new ClassNode(Map.class), new ClassNode(node.getClass()), null);
        node.addField(imageBindField);
        addGetter(imageBindField, node);
        addSetter(imageBindField, node);
        addNullableConstraint(node, "biImage");
        // add hasMany relation
        FieldNode hasManyField = getHasManyField(node);
        MapExpression mapValues = (MapExpression)hasManyField.getInitialExpression();
        mapValues.addMapEntryExpression(new ConstantExpression("biImage"), new ClassExpression(new ClassNode(Image.class)));
    }
}