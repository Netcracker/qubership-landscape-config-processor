# qubership-landscape-config-processor

## About
There is a web application which allows to render Cloud Native Computer Foundation landscape.
It is placed at https://github.com/cncf/landscape2

There is a production instance: https://landscape.cncf.io/

The configuration of landscape is defined by landscape.yml file at https://github.com/cncf/landscape/ repo.

You can set up your own landscape application with your own configuration of landscape.
But in case you need customize somehow original landscape (hide, highlight, add special marks)
you will require to keep your own copy of landscape.yml.
And the main problem here - is to merge regular updates from original landscape.yml file if required.

This tool "landscape-config-processor-X.Y.Z" provides ability of automatic merge of extra yaml-files into base one.
So, the base file can be always just a copy of original file. And all other changes can be defined by
a number of *.yml files.

## How to build
The "landscape-config-processor-X.Y.Z" is a generic Java application, which uses Java 11 API (can be easily ported to Java 8).
Call "mvn clean package assembly:single"
Find "./target/landscape-config-processor-X.Y.Z.jar"

## How to use
Step 1: prepare some *.properties file and set correct values for the properties.
See description of each property in the template file "./template.properties"

Step 2: call "java -jar landscape-config-processor-X.Y.Z.jar \$PATH_TO_PROPERTIES_FILE\$"