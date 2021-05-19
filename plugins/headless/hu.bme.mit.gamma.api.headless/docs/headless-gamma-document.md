# Exporting Gamma as headless Eclipse



This document describes how to export Gamma as a headless Eclipse application.

## Step 1 - Setting up the environment

The processes and steps described in this document were executed on Ubuntu, version 20.04.

**Required applications**

 - Eclipse - this document uses the required plugins and Eclipse version detailed in the Gamma setup tutorial, which can be found here: https://github.com/ftsrg/gamma. Please note that installing Gamma is also required. The installation is detailed in the aformentioned link.
 - Docker - a tutorial can be found here: https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-18-04

 
Additionally, Postman can be installed to test requests to the webserver.

**Required packages**

These can be installed using the `apt-get install <package>` command.

 - Java 11 - `openjdk-11-jdk`, `openjdk-11-jre`
 - SWT - `libswt-gtk*`
 - Maven - `maven`

Additionally, cURL can be installed to test requests to the webserver, using the `curl` package.

## Step 2 - Importing projects


Import the `hu.bme.mit.gamma.headless.api` and `hu.bme.mit.gamma.headless.source.generate` projects to your workspace, which already contains the necessary Gamma plugins that you want to export.



The `hu.bme.mit.gamma.headless.api` creates the headless version of Gamma. This application can be exported using the product file found in the META-INF folder of the plugin, named `gamma.api.headless.product`.



The `hu.bme.mit.gamma.headless.source.generate` is used to generate Eclipse workspaces and import projects for the headless version of Gamma. This application can be exported using the product file found in the META-INF folder of the plugin, named `product.gamma.source.generate.product`. 


## Step 3 - Modifying Target Platform
Open the target platform via Window -> Preferences -> Plug-in Development -> Target Platform.

We have created a target platform (see `hu.bme.mit.gamma.headless.api`) that can be used to properly export and run the headless version of Gamma. Nevertheless, if you wish to create your own target platform, the necessary modifications are elaborated in the following paragraphs.

Edit the target platform by modifying its content. For the following plugins, select **only** the described version(s), and deselect other versions (remove the tick from the box next to them).

 - com.google.guava 30.1.0
 
 - org.antlr.runtime 3.2.0
 
 - org.apache.batik.css 1.13.0
 
 - org.apache.batik.util 1.13.0
 
 - org.apache.xerces 2.12.1
 
 

**If you have Gamma installed into your host Eclipse:**


Make sure to remove the Gamma plugins from the required plugin list of your target platform. You can do this by removing them from the Content list, or deleting the corresponding lines in the source file.

## Step 4 - Exporting the products

Select either product file to begin the exporting process. Both can be found in their corresponding META-INF folder (`gamma.api.headless.product` and `product.gamma.source.generate.product`).

In the Overview tab, under `Product Definition`, check if the appropriate `Application` is selected for the `Product`. The applications are `gamma.api.headless.application`  and `product.gamma.source.generate.application`, for  `gamma.api.headless.product` and `product.gamma.source.generate.product` respectively.

Still in the Overview tab, under `Exporting`, select the `Eclipse Product export wizard` option.

In the Export pop-up window, deselect the `Synchronise before exporting` and `Generate p2 repository` options.

To summarize, the selection of options in the `Exporting` tab should look like this:

 - [ ] Synchronise before exporting
 - [ ] Export source: [..]
 - [ ] Generate p2 repository
 - [x] Allow for binary cycles in target platform

Finally, select Finish, and the exporting process should begin.

## Notable errors

The following paragraphs include some notable errors users tend to stumble upon and the methods to resolve them.

**Unresolved requirement**

After exporting and running the Headless Eclipse, it is possible that an error will occur stating "An error has occured. See the log file [...]". After inspecting the log file, it is possible that the exported Eclipse can't resolve the module, because some bundles are missing ("Unresolved requirement: Require-Bundle: [...]"). An example of this is the `hu.bme.mit.gamma.composition.xsts.uppaal.transformation` Gamma plugin.

To resolve this, add the missing plugin(s) to the contents of the product file, in the Contents tab.

**Application could not be found in registry**

This error can occur upon launching the headless Eclipse.

In the workspace, select the product file which was exported. Under "Product Definition", check if the Application is corresponding to the product. After selecting the corresponding application, export again, and the headless Eclipse should run the correct application now.


**The product's defining plug-in could not be found**

This error occurs when the "Synchronise before exporting" option remains checked when exporting. Uncheck this option, and export again.

**"Export product" has encountered a problem**

This error occurs when the "Generate p2 repository" option remains checked when exporting. Uncheck this option, and export again.

**Problems with SWT**

This problem can occur mainly in the Docker version of Gamma. The two parts that make up the headless version of Gamma, the generator and the API can run into errors with SWT in a new Linux environment. These errors prevent normal functioning.

The first notable thing about SWT is that it is platform dependent. This means that in both product files (gamma.api.headless.product for the Headless Gamma API, and product.gamma.source.generate.product for the Headless Generator), the platform specific SWT plugins have to be imported (and possibly removed) according to the platform. For example, when moving from Windows to Linux, on the Content tab of the products, the Windows specific plugins will be missing, which is indicated with an error icon. These have to be replaced with the Linux specific plugins, which have the same name, with the operating system being a difference (instead of "win32",  "linux" is in the name).

The following solutions resolved these issues:

 - API
		 - Adding `org.eclipse.swt.browser.chromium.gtk.linux.x86_64.source` and `org.eclipse.swt.gtk.linux.x86_64.source` (or equivalent, in the case of a 32 bit system) fixed SWT related issues. These plugins have to be imported manually, after importing the required plugins with the "Add Required Plug-Ins" button.
 
 - Generator
		 - Some SWT errors can still persist even after removing the `hu.bme.mit.gamma.dialog` project. The following steps provided solution for this problem.
		 - Adding `org.eclipse.swt.browser.chromium.gtk.linux.x86_64.source` and `org.eclipse.swt.gtk.linux.x86_64.source` (or equivalent, in the case of a 32 bit system) fixed SWT related issues. These plugins have to be imported manually, after importing the reuqired plugins with the "Add Required Plug-Ins" button.
		 - In the Docker container, `libswt-gtk*` has to be installed, even with Java 11 installed in the container. This can be done with the `apt-get install libswt-gtk` command. This fixed the Docker specific SWT errors related to the Headless Generator.

## Setting up the Docker container

 Make sure that Docker is installed on the computer in use. The following tutorial was used to install Docker on Ubuntu: https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-18-04

 1. Create a working folder, and copy the Dockerfile inside.
 2. Inside the working folder, create the following folders: "eclipse", "generator", "server", "uppaal", "theta".
 3. Export the Headless Gamma API inside the "eclipse" folder.
 4. Export the Headless Generator inside the "generator" folder.
 5. Copy the "OpenApiGammaWrapper" folder, which contains the webserver, inside the "server" folder.
 6. Copy the contents of the UPPAAL folder installed on your computer inside the "uppaal" folder.
 7. Copy the `get-theta.sh` file inside the "theta" folder. This file can be found inside the `gamma/plugins/xsts/theta-bin` folder. 
 8. Open a terminal and build the docker image with the following command: `docker build -t gamma <path to working folder>` (the working folder is the folder created in the first step) . This will create a docker image named "gamma".
 9. In a terminal, run a docker container from the gamma image using the `docker run -it -p 8080:8080 --network host --name gamma_container gamma:latest` command. This command will bind the localhost:8080 address of the host machine to the Docker container, forwarding commands to the webserver running inside. The container is named "gamma_container", and starts in interactive mode, allowing for CLI access. The last parameter is the image, which is `gamma:latest`, which indicates that the latest Gamma image build is used.
 
It is possible to change the port binding if the user wishes, but the webserver listens to port 8080. In the port binding parameter, the part before the ":" stands for the host port, and the one after is the container port. So changing the parameter to "5555:8080" is valid, but "8080:5555" would result in communication failure.  Names of the image and container can also be changed.
