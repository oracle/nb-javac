/*
 * Copyright 2003-2004 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package nbjavac;

import junit.framework.TestCase;

public class StringWrapperTest extends TestCase {

    public StringWrapperTest(String testName) {
        super(testName);
    }

    public void testStripIndent() {
        String input =
"                          package t;\n" +
"                          \\n\\\n" +
"                          class Test {\n" +
"                              String.<error> <error>;\n" +
"                              \\n\\\n" +
"                              class <error> {\n" +
"                              }\n" +
"                              \\n\\\n" +
"                              class <error> {\n" +
"                              }\n" +
"                              \\n\\\n" +
"                              class A {\n" +
"                              }\n" +
"                              \\n\\\n" +
"                              public class B {\n" +
"                              }\n" +
"                          }";
        String output = StringWrapper.stripIndent(input);

        String[] inLines = input.split("\n");
        String[] outLines = output.split("\n");

        assertEquals("Same number of lines", inLines.length, outLines.length);
        for (int i = 0; i < inLines.length; i++) {
            assertEquals(i + ". line is the same after trimming", inLines[i].trim(), outLines[i].trim());
        }
    }

    public void testSlashS() {
        String space = StringWrapper.translateEscapes("\\s");
        assertEquals(" ", space);
    }

    public void testEscapes() {
        String textBlock = "jst\u001E\u0009";
        String output = StringWrapper.stripIndent(textBlock);
        assertEquals("jst", output);
    }

    public void testNewLines() {
        char NL = (char) 0x000A;
        String input = "jst" + NL + NL + "";
        String output = StringWrapper.stripIndent(input);
        assertEquals("jst\n\n", output);
    }

    public void testTabAtBegin() {
        char NL = (char) 0x000A;
        String input = NL + "\u0009jst";
        String output = StringWrapper.stripIndent(input);
        assertEquals("\njst", output);
    }

    public void test0x15() {
        char NL = (char) 0x000A;
        String input = "" + NL + '\u0015';

        String output = StringWrapper.stripIndent(input);
        assertEquals("\n\u0015", output);
    }
}