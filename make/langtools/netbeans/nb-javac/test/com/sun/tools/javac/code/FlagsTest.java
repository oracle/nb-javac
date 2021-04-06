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
package com.sun.tools.javac.code;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Ignore;

/**
 *
 * @author lahvac
 */
public class FlagsTest extends TestCase {
    
    public FlagsTest(String testName) {
        super(testName);
    }

    private static final Set<String> ignoredFields = new HashSet<String>(Arrays.asList("ACC_SUPER", "ACC_BRIDGE", "ACC_VARARGS", "ACC_MODULE", "ACC_DEFENDER", "BAD_OVERRIDE", "ReceiverParamFlags", "BODY_ONLY_FINALIZE"));
    @Ignore
    public void testCheckFlagsNotClashing() throws Exception {
        Map<Long, String> value2Name = new HashMap<Long, String>();

        for (Field f : Flags.class.getDeclaredFields()) {
            if (   !Modifier.isStatic(f.getModifiers())
                || !Modifier.isPublic(f.getModifiers())
                || ignoredFields.contains(f.getName())
                || Long.bitCount(f.getLong(null)) != 1) {
                continue;
            }

            long value = f.getLong(null);

            if (value2Name.containsKey(value)) {
                throw new IllegalStateException("Value clash between " + value2Name.get(value) + " and " + f.getName());
            }

            value2Name.put(value, f.getName());
        }
    }
}
