set -x
set -e

if ! [ -f $JAVA_HOME8/bin/javac ]; then
  echo Specify JAVA_HOME8 environment variable!
  exit 1
fi

if ! [ -f $JAVA_HOME14/bin/javac ]; then
  echo Specify JAVA_HOME14 environment variable!
  exit 2
fi

if ! [ -d jdk ]; then
    git clone --depth=1 https://github.com/openjdk/jdk16 jdk
fi

if ! [ -f jackpot.jar ]; then
    wget -O jackpot.jar https://search.maven.org/remotecontent?filepath=org/apache/netbeans/modules/jackpot30/tool/11.1/tool-11.1.jar
fi

rm -rf src

mkdir src
cp -r jdk/src/java.compiler src
cp -r jdk/src/jdk.compiler src
$JAVA_HOME14/bin/javac `find jdk/make/langtools/tools/propertiesparser/ -type f -name "*.java"`
$JAVA_HOME14/bin/java -classpath jdk/make/langtools/tools/ propertiesparser.PropertiesParser -compile src/jdk.compiler/share/classes/com/sun/tools/javac/resources/compiler.properties src/jdk.compiler/share/classes/com/sun/tools/javac/resources/
$JAVA_HOME14/bin/java -classpath jdk/make/langtools/tools/ propertiesparser.PropertiesParser -compile src/jdk.compiler/share/classes/com/sun/tools/javac/resources/launcher.properties src/jdk.compiler/share/classes/com/sun/tools/javac/resources/
$JAVA_HOME14/bin/java -classpath jackpot.jar org.netbeans.modules.jackpot30.cmdline.Main -hint-file make/langtools/netbeans/nb-javac/src/META-INF/upgrade/nbjavac.hint  -sourcepath make/langtools/netbeans/nb-javac/src/:src/jdk.compiler/share/classes/:src/java.compiler/share/classes --apply src/java.compiler/share/classes src/jdk.compiler/share/classes
(cd src; patch -p1 -i ../temporary-patches/manual-workarounds)
(cd src; patch -p1 -i ../temporary-patches/filesystems-run-on-jdk8)

export JAVA_HOME=$JAVA_HOME14
ant -autoproxy -f make/langtools/netbeans/nb-javac "-Djavac.compilerargs=-bootclasspath $JAVA_HOME8/jre/lib/rt.jar" clean
ant -autoproxy -f make/langtools/netbeans/nb-javac "-Djavac.compilerargs=-bootclasspath $JAVA_HOME8/jre/lib/rt.jar" jar
export JAVA_HOME=$JAVA_HOME8
ant -autoproxy -f make/langtools/netbeans/nb-javac "-Djavac.compilerargs=-bootclasspath $JAVA_HOME8/jre/lib/rt.jar" test
