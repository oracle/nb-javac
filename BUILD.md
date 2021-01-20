This projects shows a way to compile JDK 16+ javac to run on JDK 8+,
for the pruposes of NetBeans ("nb-javac").

### Steps to compile nb-javac:

```bash
JAVA_HOME8=/jdk-8/ JAVA_HOME14=/jdk-14/ ./build.sh
```

### Manual steps to compile nb-javac:

1. from JDK 16 (https://github.com/openjdk/jdk16), ideally commit 58dca9253d3ec7bc5745d5b814b33e1b4b8b08e8, copy `src/java.compiler` and `src/jdk.compiler` into `src/java.compiler` and `src/jdk.compiler` in this project
2. from a JDK 16 build, copy:
`com/sun/tools/javac/resources/CompilerProperties.java`
into:
`jdk.compiler/share/classes/com/sun/tools/javac/resources/CompilerProperties.java`
and:
`com/sun/tools/javac/resources/LauncherProperties.java`
into:
`jdk.compiler/share/classes/com/sun/tools/javac/resources/LauncherProperties.java`
3. apply `temporary-patches/language-changes` patch. This includes backport language changes,
which should eventually also be done using NetBeans/Jackpot
4. open the `make/langtools/netbeans/nb-javac` project in NetBeans. Neccessary setup (in Project Properties):
-in Libraries tab, set Java Platform to JDK 11+ (tested with JDK 16) (JDK 8 javac contains a bug that will prevent compilation of the sources)
-in Build/Compiling tab, make sure there is an "Additiona Compiler Options" entry specifying -bootclasspath from JDK 8 (i.e. rt.jar)
5. open `src/META-INF/upgrades/nbjavac.hint`, invoke Run File, select the project Custom Scope. Do the refactoring. (There should be 102 replacements done.)
6. apply temporary-patches/manual-workarounds to workaround a few mistakes in the transformation
7. the project should now be buildable. Not tried in NetBeans yet.
8. If you would want to test on the commandline, apply `filesystems-run-on-jdk8` and clean & build. Then it should be possible to run like:
```bash
java -Xbootclasspath/p:make/langtools/netbeans/nb-javac/dist/nb-javac-15-api.jar:make/langtools/netbeans/nb-javac/dist/nb-javac-15-impl.jar com.sun.tools.javac.Main --system <path-to-JDK16> TextBlock.java
```

Note step 8 is optional and only needed for experiments on command line, while running on JDK 8 and compiling for JDK 9+. Not needed in NetBeans.


TODO (incomplete):
-automatic build (including CompilerProperties, etc.)
-cleanup
-fix nbjavac wrappers
-fix Jackpot
-resolve language changes
-jdeps needed? (classfile library?)
-test - all javac, nb-javac and NetBeans (including updates)
