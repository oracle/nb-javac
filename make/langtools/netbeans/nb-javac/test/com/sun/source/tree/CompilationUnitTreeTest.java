/*
 * Copyright (c) 1999, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.source.tree;

import com.sun.tools.javac.api.JavacTaskImpl;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import junit.framework.TestCase;

/**
 *
 * @author Jan Lahoda
 */
public class CompilationUnitTreeTest extends TestCase {

    public CompilationUnitTreeTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    static class MyFileObject extends SimpleJavaFileObject {
        private String text;
        public MyFileObject(String text) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            this.text = text;
        }
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return text;
        }
    }

    private void performTest(String code, int lastLine) throws IOException {
        final String bootPath = System.getProperty("sun.boot.class.path"); //NOI18N
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        assert tool != null;

        JavacTaskImpl ct = (JavacTaskImpl)tool.getTask(null, null, null, Arrays.asList("-bootclasspath",  bootPath), null, Arrays.asList(new MyFileObject(code)));

        CompilationUnitTree cut = ct.parse().iterator().next();

        cut.getLineMap().getStartPosition(lastLine);

        boolean exceptionThrown = false;

        try {
            cut.getLineMap().getStartPosition(lastLine + 1);
        } catch (IndexOutOfBoundsException e) {
            //intentional:
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        ((JavacTaskImpl) ct).finish();

        //DocCommentScanner:
        ct = (JavacTaskImpl)tool.getTask(null, null, null, Arrays.asList("-bootclasspath",  bootPath), null, Arrays.asList(new MyFileObject(code)));

        cut = ct.parse().iterator().next();

        cut.getLineMap().getStartPosition(lastLine);

        exceptionThrown = false;

        try {
            cut.getLineMap().getStartPosition(lastLine + 1);
        } catch (IndexOutOfBoundsException e) {
            //intentional:
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        ((JavacTaskImpl) ct).finish();
    }

    public void testLineMap() throws IOException {
        performTest("public class Test {}\n//", 2);
        performTest("public class Test {}\n ", 2);
        performTest("public class Test {}\n", 2);
        performTest("public class Test {}", 1);
        performTest(" ", 1);
        performTest("", 1);
        performTest("\n", 2);
        performTest("\n\n", 3);
    }

}
