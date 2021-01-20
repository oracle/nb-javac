/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
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
package nbjavac;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ModuleWrapper {

    public static ModuleWrapper getModule(Class<?> clazz) {
        return new ModuleWrapper();
    }

    public static ModuleWrapper getUnnamedModule(ClassLoader loader) {
        return new ModuleWrapper();
    }

    public String getName() {
        return "jdk.compiler"; //XXX
    }

    public boolean isNamed() {
        return false;
    }

    public void addExports(String pack, ModuleWrapper to) {
    }

    public <S> void addUses(Class<S> service) {
    }

    public static class ModuleFinder {

        public static ModuleFinder of(Path... paths) {
            return new ModuleFinder();
        }

    }

    public static class ModuleLayer {

        public static ModuleLayer boot() {
            return new ModuleLayer();
        }

        public Configuration configuration() {
            return new Configuration();
        }

        public ModuleLayer defineModulesWithOneLoader(Configuration cf, ClassLoader systemClassLoader) {
            return new ModuleLayer();
        }

    }

    public static class Configuration {

        public Configuration resolveAndBind(ModuleFinder of, ModuleFinder finder, Set<?> emptySet) {
            return new Configuration();
        }

    }

    public static class ModuleDescriptor {
        public static class Version {
            public static void parse(String v) {
                //TODO: do validation
            }
        }
    }

}
