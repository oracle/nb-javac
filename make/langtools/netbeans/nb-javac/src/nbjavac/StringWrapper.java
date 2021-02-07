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

import java.util.Arrays;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class StringWrapper {

    public static String stripIndent(String str) {
        //prototype only, needs testing and performance improvements:
        String[] lines = str.split("\n");
        ToIntFunction<String> strIndent = s -> s.length() - s.replaceAll("^\\s*", "").length();
        int indent = Arrays.stream(lines)
                           .filter(l -> !isBlank(l))
                           .mapToInt(strIndent)
                           .min()
                           .orElse(Integer.MAX_VALUE);
        if (lines.length > 0 && isBlank(lines[lines.length - 1])) {
            indent = Math.min(indent, lines[lines.length - 1].length());
        }
        int indentFin = indent;
        return Arrays.stream(lines).map(line -> {
                String l = line.substring(indentFin);
                int at = l.length() - 1;
                while (at >= 0 && Character.isWhitespace(l.charAt(at))) {
                    at--;
                }
                return l.substring(0, at + 1);
            }).collect(Collectors.joining("\n"));
    }

    public static boolean isBlank(String str) {
        return str.trim().isEmpty();
    }

    //copied from nb-javac:
        public static String translateEscapes(String str) {
            if (str.isEmpty()) {
                return "";
            }
            char[] chars = str.toCharArray();
            int length = chars.length;
            int from = 0;
            int to = 0;
            while (from < length) {
                char ch = chars[from++];
                if (ch == '\\') {
                    ch = from < length ? chars[from++] : '\0';
                    switch (ch) {
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case 's':
                        ch = ' ';
                        break;
                    case '\'':
                    case '\"':
                    case '\\':
                        // as is
                        break;
                    case '0': case '1': case '2': case '3':
                    case '4': case '5': case '6': case '7':
                        int limit = Integer.min(from + (ch <= '3' ? 2 : 1), length);
                        int code = ch - '0';
                        while (from < limit) {
                            ch = chars[from];
                            if (ch < '0' || '7' < ch) {
                                break;
                            }
                            from++;
                            code = (code << 3) | (ch - '0');
                        }
                        ch = (char)code;
                        break;
                    case '\n': continue;
                    default: {
                        String msg = String.format(
                            "Invalid escape sequence: \\%c \\\\u%04X",
                            ch, (int)ch);
                        throw new IllegalArgumentException(msg);
                    }
                    }
                }

                chars[to++] = ch;
            }

            return new String(chars, 0, to);
        }

}
