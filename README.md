# About nb-javac!
"nb-javac" is a patched version of OpenJDK "javac", i.e., the Java compiler. This has long been part of NetBeans, providing a highly tuned Java compiler specifically for the Java editor i.e., parsing and lexing for features such as syntax coloring, code completion.

# Prerequisite
  - Git
  - Ant 1.9.9 or above
  - JDK 8 or above (to build nb-javac)

# Building nb-javac jar files
1.Obtain the code with the following command

    git clone https://github.com/oracle/nb-javac.git

2.To get a specific version use the following command

    git checkout <release_tag_name> 
    
3.Run the below command to build nb-javac.

    ant -f ./make/langtools/netbeans/nb-javac clean build jar
    
4.Run below command to zip the source code of nb-javac
    
    ant -f ./make/langtools/netbeans/nb-javac zip-nb-javac-sources
##### Note:
Build of nb-javac will generate two jars namely `javac-api.jar` and `javac-impl.jar` at location ./make/langtools/netbeans/nb-javac/dist/

# Installation/Usage

#### 1. Install nb-javac from jars

cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-api.jar netbeans/java/libs.javacapi/external/nb-javac-$ver-api.jar
cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-api.jar netbeans/nbbuild/build/testdist/extralibs/nb-javac-$ver-api.jar
cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-impl.jar netbeans/nbbuild/build/testdist/extralibs/nb-javac-$ver-impl.jar
cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-impl.jar netbeans/java/libs.javacimpl/external/nb-javac-$ver-impl.jar

#### 2. Open Netbeans and install nb-javac from plugins

cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-impl.jar netbeans/nbbuild/testuserdir/modules/ext/nb-javac-$ver-impl.jar
cp nb-java-x/make/langtools/netbeans/nb-javac/dist/nb-javac-$ver-api.jar netbeans/nbbuild/testuserdir/modules/ext/nb-javac-$ver-api.jar
touch netbeans/nbbuild/testuserdir/.lastmodified

# Documentation 

https://cwiki.apache.org/confluence/display/NETBEANS/Overview%3A+nb-javac
https://cwiki.apache.org/confluence/display/NETBEANS/Release+Schedule
https://confluence.oraclecorp.com/confluence/display/NB/nb-javac+JDK14+uptake
https://wiki.se.oracle.com/display/JPG/Behavior+without+NB-Javac

# Help
Subscribe or mail the users@netbeans.apache.org list - Ask questions, find answers, and also help other users.

Subscribe or mail the dev@netbeans.apache.org list - Join development discussions, propose new ideas and connect with contributors.

# Contributing
See the  [Contributing Policy](./CONTRIBUTING.md)

# Security
See the  [Security Policy](./SECURITY.md)
