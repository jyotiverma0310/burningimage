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

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.transform.*;
import org.codehaus.groovy.control.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import java.lang.reflect.Modifier;
import org.codehaus.groovy.syntax.*;
import org.objectweb.asm.Opcodes;
import org.apache.commons.lang.StringUtils;
import pl.burningice.plugins.image.ast.intarface.FileImageContainer;
import java.util.List;

/**
 * Object executer tranformation of object marked by FileImageContainer annotation
 *
 * @author pawel.gdula@burningice.pl
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class FileImageContainerTransformation implements ASTTransformation, Opcodes {

    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        for (ASTNode aSTNode : nodes) {
            if (!(aSTNode instanceof ClassNode)) {
                continue;
            }
            transform((ClassNode) aSTNode);
        }
    }

    private void transform(ClassNode node) {
        log("start transforming: " + node);
        // implements interface
        node.addInterface(new ClassNode(FileImageContainer.class));
        // imageExists field
        FieldNode imageExists = new FieldNode("imageExtension", Modifier.PRIVATE, new ClassNode(String.class), new ClassNode(node.getClass()), null);
        node.addField(imageExists);
        addGetter(imageExists, node);
        addSetter(imageExists, node);
        addNullableConstraint(node, "imageExtension");
    }

    public void addNullableConstraint(ClassNode classNode, String fieldName) {
        FieldNode closure = classNode.getDeclaredField("constraints");
        if (closure != null) {

            ClosureExpression exp = (ClosureExpression) closure.getInitialExpression();
            BlockStatement block = (BlockStatement) exp.getCode();

            if (!hasFieldInClosure(closure, fieldName)) {
                NamedArgumentListExpression namedarg = new NamedArgumentListExpression();
                namedarg.addMapEntryExpression(new ConstantExpression("nullable"), new ConstantExpression(true));
                namedarg.addMapEntryExpression(new ConstantExpression("blank"), new ConstantExpression(true));
                MethodCallExpression constExpr = new MethodCallExpression(
                        VariableExpression.THIS_EXPRESSION,
                        new ConstantExpression(fieldName),
                        namedarg);
                block.addStatement(new ExpressionStatement(constExpr));
            }
        }
    }

    public boolean hasFieldInClosure(FieldNode closure, String fieldName) {
        if (closure != null) {
            ClosureExpression exp = (ClosureExpression) closure.getInitialExpression();
            BlockStatement block = (BlockStatement) exp.getCode();
            List<Statement> ments = block.getStatements();
            for (Statement expstat : ments) {
                if (expstat instanceof ExpressionStatement && ((ExpressionStatement) expstat).getExpression() instanceof MethodCallExpression) {
                    MethodCallExpression methexp = (MethodCallExpression) ((ExpressionStatement) expstat).getExpression();
                    ConstantExpression conexp = (ConstantExpression) methexp.getMethod();
                    if (conexp.getValue().equals(fieldName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addGetter(FieldNode fieldNode, ClassNode owner) {
        ClassNode type = fieldNode.getType();
        String name = fieldNode.getName();
        String getterName = "get" + StringUtils.capitalize(name);
        owner.addMethod(getterName,
                ACC_PUBLIC,
                nonGeneric(type),
                Parameter.EMPTY_ARRAY,
                null,
                new ReturnStatement(new FieldExpression(fieldNode)));

    }

    private void addSetter(FieldNode fieldNode, ClassNode owner) {
        ClassNode type = fieldNode.getType();
        String name = fieldNode.getName();
        String setterName = "set" + StringUtils.capitalize(name);
        owner.addMethod(setterName,
                ACC_PUBLIC,
                ClassHelper.VOID_TYPE,
                new Parameter[]{new Parameter(nonGeneric(type), "value")},
                null,
                new ExpressionStatement(
                new BinaryExpression(
                new FieldExpression(fieldNode),
                Token.newSymbol(Types.EQUAL, -1, -1),
                new VariableExpression("value"))));
    }

    private ClassNode nonGeneric(ClassNode type) {
        if (type.isUsingGenerics()) {
            final ClassNode nonGen = ClassHelper.makeWithoutCaching(type.getName());
            nonGen.setRedirect(type);
            nonGen.setGenericsTypes(null);
            nonGen.setUsingGenerics(false);
            return nonGen;
        } else {
            return type;
        }
    }

    private void log(String message) {
        System.out.println("[Burining Image]: " + message);
    }
}

