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

## How to render landscape.yml file locally
### Windows + WSL
Install landscap2 application as mentioned at https://github.com/cncf/landscape2
Most simple way is:
```shell
curl --proto '=https' --tlsv1.2 -LsSf https://github.com/cncf/landscape2/releases/download/v0.13.0/landscape2-installer.sh | sh
source $HOME/.cargo/env
```

As mentioned in the usage instructions - prepare new landscape instance:
```shell
cd ~
mkdir "landscape2-test-app"
cd landscape2-test-app
landscape2 new --output-dir output
cd output
```

Note, here ${landscape.yml}$ is a file which was mentioned in the template.propertes files as a "result-configuration-file-name" file.

Now upload new/updated ${landscape.yml}$ file into the "output" directory and compile it using:
```shell
landscape2 build --data-file ${landscape.yml}$ --settings-file settings.yml --logos-path logos --output-dir build
```
Note: you should comment out "groups" property in the settings.yml file, otherwise you may not see your results. Use "#" to comment the lines (or delete them)
The "screenshot_width" property is also recommended to be disabled for quick start (on the dev env)


If compilation is finished without significant errors (errors for absent pictures may be skip on dev env) run
```shell
landscape2 serve --landscape-dir build
```

Open browse at noted address, by defualt it should be
```
http://127.0.0.1:8000
```

### Changes uploading
In case you have updated pictures or data.yml - stop the service, by pressing Ctrl+C for instance.
Do recompilation using "landscape2 build..." command (see detailed instructions above)
Then run "landscape2" applicatio once again.
