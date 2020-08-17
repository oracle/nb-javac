
Welcome to NB_JAVAC!
===================

Pre-requisite: Use JDK-8 for building nb-javac

1. Obtain the code with the following command

hg clone https://hg.netbeans.org/main/nb-java-x

2. To get a specific version check the revision number from file nb_javac_releses_tags(present in the cloned repo) and use the following command

hg update -r <REVISION NUMBER>

Run the below command to build nb-javac. Build of nb-javac will generate two jars 
namely javac-api.jar and javac-impl.jar at location ./make/langtools/netbeans/nb-javac/dist/

3. ant -f ./make/langtools/netbeans/nb-javac clean jar

Run below command to zip the source code of nb-javac

4. ant -f ./make/langtools/netbeans/nb-javac zip-nb-javac-sources