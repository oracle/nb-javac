set -x
set -e

if ! [ -f $JAVA_HOME8/bin/javac ]; then
  echo Specify JAVA_HOME8 environment variable!
  exit 1
fi

if ! [ -f $JAVA_HOME8/jre/lib/rt.jar ]; then
  echo Specify JAVA_HOME8 environment variable!
  exit 1
fi

if ! [ -f $JAVA_HOME14/bin/javac ]; then
  echo Specify JAVA_HOME14 environment variable!
  exit 2
fi

if ! [ -f $JAVA_HOME14/jmods/java.base.jmod ]; then
  echo Specify JAVA_HOME14 environment variable!
  exit 2
fi

export JAVA_HOME=$JAVA_HOME14
ant $ANT_ARGS_EXTRA -f make/langtools/netbeans/nb-javac clean jar


export JAVA_HOME=$JAVA_HOME8
ant $ANT_ARGS_EXTRA -f make/langtools/netbeans/nb-javac test
