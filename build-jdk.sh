if ! [ -f $JAVA_HOME16/bin/javac ]; then
  echo Specify JAVA_HOME16 environment variable!
  exit 1
fi

rm -rf build/test
mkdir -p build/test
mkdir -p build/test/modules
cp $JAVA_HOME16/jmods/* build/test/modules
#clear ModuleHashes on java.base:
$JAVA_HOME16/bin/java --add-modules jdk.jdeps --add-exports jdk.jdeps/com.sun.tools.classfile=ALL-UNNAMED StripModuleHashes.java $JAVA_HOME16/jmods/java.base.jmod build/test/modules/java.base.jmod
rm build/test/modules/java.compiler.jmod
rm build/test/modules/jdk.compiler.jmod
mkdir -p build/test/src/java.compiler
cp src/java.compiler/share/classes/module-info.java build/test/src/java.compiler

patch -R build/test/src/java.compiler/module-info.java temporary-patches/test-java.compiler

mkdir -p build/test/src/jdk.compiler
cp src/jdk.compiler/share/classes/module-info.java build/test/src/jdk.compiler

patch build/test/src/jdk.compiler/module-info.java temporary-patches/test-jdk.compiler

mkdir -p build/test/out/java.compiler
cp -r make/langtools/netbeans/nb-javac/build/classes/javax build/test/out/java.compiler/
cp -r make/langtools/netbeans/nb-javac/build/classes/nbjavac build/test/out/java.compiler/

mkdir -p build/test/out/jdk.compiler
cp -r make/langtools/netbeans/nb-javac/build/classes/com build/test/out/jdk.compiler/
cp -r make/langtools/netbeans/nb-javac/build/classes/jdk build/test/out/jdk.compiler/

$JAVA_HOME16/bin/javac --module-source-path build/test/src/ -d build/test/out `find build/test/src/ -type f -name "*.java"`

$JAVA_HOME16/bin/jmod create --class-path build/test/out/java.compiler/ build/test/modules/java.compiler.jmod

mkdir -p build/test/expanded

$JAVA_HOME16/bin/jmod extract --dir=build/test/expanded $JAVA_HOME16/jmods/jdk.compiler.jmod

$JAVA_HOME16/bin/jmod create --class-path build/test/out/jdk.compiler/ --cmds build/test/expanded/bin/ --legal-notice build/test/expanded/legal/ --libs build/test/expanded/lib/ --man-pages build/test/expanded/man/ --module-version 16 build/test/modules/jdk.compiler.jmod

$JAVA_HOME16/bin/jlink -p build/test/modules --add-modules ALL-MODULE-PATH --output build/test/jdk
