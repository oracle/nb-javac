# Hacking Guide for the automatically generated [nbjavac](README.md)

The idea of the new build system is to take the JDK 16+ `javac` sources and
automatically convert them to run on JDK 8+. As a result the sources come 
from real JDK repository. The `nbjavac` repository doesn't contain them. 
This repository only contains the build scripts and
description of [advanced refactorings](https://netbeans.apache.org/jackpot/HintsFileFormat.html).
Use:

```bash
$ JAVA_HOME=/jdk-14/ ant -f ./make/langtools/netbeans/nb-javac jar
```

to build everything at once. Read below to control individual steps of the build.


### Getting the JDK repository

The build requires JDK repository in `jdk` subdirectory of the root of `nb-javac` repository.
If such directory doesn't exist, the build checks out one:

```bash
$ JAVA_HOME=/jdk-14/ ant -f ./make/langtools/netbeans/nb-javac init \
    -Djdk.git.url=https://github.com/openjdk/jdk16 \
    -Djdk.git.commit=jdk-16+36
```

If the `jdk` directory is present the build leaves its content untouched. E.g.
a developer may clone the `jdk` repository manually, switch its content to any other tag,
make changes in the `jdk/src/java.compiler/` or `jdk/src/jdk.compiler/` directories,
etc. Bugfixes, features and other changes to `javac` sources are supposed to be done 
in the `jdk` subdirectory and integrated into the JDK's `javac` official repository.

One can discard any changes by `rm -rf jdk`. Then the subsequent build checks
a fresh copy of the `jdk` repository from scratch. The default values for
`jdk.git.url` and `jdk.git.commit` properties are in the
`./make/langtools/netbeans/nb-javac/nbproject/project.properties`
file.


### Automatically processing the sources

Once the JDK's `javac` sources are in the `jdk` subdirectory, it is necessary
to apply [advanced refactorings](./make/langtools/netbeans/nb-javac/src/META-INF/upgrade/nbjavac.hint)
to them. This is done by executing the [jackpot](https://netbeans.apache.org/jackpot/HintsFileFormat.html)
target:

```bash
$ JAVA_HOME=/jdk-14/ ant -f ./make/langtools/netbeans/nb-javac jackpot
```

This step copies the `javac` sources from the `jdk` subdirectory into a sibling
`src` subdirectory and applies necessary transformations to them.
The goal of such transformations is to eliminate usage of JDK9+ APIs
and replace them with JDK8 only APIs.

The sources under the `src/java.compiler` and `src/jdk.compiler` shall not
be edited manually. Rather than that edit the sources in the original
`jdk/src/java.compiler/` and `jdk/src/jdk.compiler/` directories. To apply
the refactorings again execute:

```bash
$ JAVA_HOME=/jdk-14/ ant -f ./make/langtools/netbeans/nb-javac clean jackpot
```

### The build

As described in [general documentation](README.md) use the following command to
generate the final JAR files:

```bash
$ JAVA_HOME=/jdk-14/ ant -f ./make/langtools/netbeans/nb-javac clean jar
```

JARs `nb-javac-*-api.jar` and `nb-javac-*-impl.jar` are going to appear
at location `./make/langtools/netbeans/nb-javac/dist/`.

### Debug & Develop 

Open the `nb-javac` project in NetBeans IDE with

```bash
$ netbeans --open make/langtools/netbeans/nb-javac/
```

and you should be able to debug a test (for example `StringWrapperTest`) with following command line:

```bash
$ JAVA_HOME=/jdk-8/ ant -f make/langtools/netbeans/nb-javac test \
    -Dincludes=**/StringWrapperTest* \
    -Drun.jvmargs=-agentlib:jdwp=transport=dt_socket,server=y,address=5005,suspend=y
```

Connect the NetBeans IDE to port 5005 and step through the `nb-javac`
generated code.
