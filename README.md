# README

This project is a continuation of the LWJGLv2 library, which was EOL'd in 2015 (https://legacy.lwjgl.org/index.php.html).
 
Modern systems, notably modern MacOS cannot run games built on the older library, so this is a port to bring it up to date and hopefully make a bunch of old, fun games able to run.

## Supported Systems

* MacOS (tested on 14 & 15)
* Linux (tested on Fedora 39 & 40)
* ?? Windows

## Building

Follow the system-specific instructions. General notes:

* Uses ant (ugh).

* Must be built with Java 8 (and no higher)

* Requires OpenGL to be installed

The ASM and jinput jars should be placed in the `libs` dir:

```
asm-6.2.1.jar
asm-analysis-6.2.1.jar
asm-tree-6.2.1.jar
asm-util-6.2.1.jar
jinput.jar
```

Note that JInput relies upon native code, which is not needed to build LWJGL, but will need to be present at runtime.

### MacOS

Install XCode (why yes, I would like to do a 3G download to install a few KB of header files for my build.)

Build using Java 8 (Apple Silicon macs will need a native JDK 8 - currently only available from Azul)

OpenGL should be already present for Macs.

### Fedora

Check for OpenGL via: 

```bash
ls /usr/include/GL/gl.h /usr/include/GL/glu.h
```

and if not then install via:

```bash 
sudo dnf install freeglut-devel mesa-libGL-devel mesa-libGLU-devel
```

Then install a couple of additional libraries:

```bash
sudo dnf -y install libXcursor-devel libXt-devel libXxf86vm-devel xrandr
```

After building using ant, you will then need to install the native library:

```bash
sudo cp libs/linux/liblwjgl64.so /lib64
```

and then install the jar file to local Maven, as above.

### Raspberry Pi (AArch32 or AArch64)

Check for OpenGL via:

```bash
ls /usr/include/GL/gl.h /usr/include/GL/glu.h
```

If not found then:

```bash
FIXME
```

Install some more headers:

```bash
sudo apt-get install libxxf86vm-dev
sudo apt-get install libxrandr-dev # X11/extensions/Xrandr.h
#sudo apt-get install x11-xserver-utils # Do we need this?
sudo apt-get install libxcursor-dev # Do we need this?
```

After building using ant, you will then need to install the native library:

```bash
sudo cp libs/linux/liblwjgl64.so /usr/lib
```

and then install the jar file to local Maven, as below.

You will also need to ensure that the RPi boots into X11 mode, not Wayland, or `XOpenDisplay()` will hang.

### Ubuntu

FIXME

### Windows

FIXME

## Installing

After building, you may need to install the library locally to make it available to games built with Maven or Gradle:

```bash
mvn install:install-file -Dfile=libs/lwjgl.jar -DgroupId=org.lwjgl -DartifactId=lwjgl -Dversion=2.9.4 -Dpackaging=jar

mvn install:install-file -Dfile=libs/lwjgl_util.jar -DgroupId=org.lwjgl -DartifactId=lwjgl-util -Dversion=2.9.4 -Dpackaging=jar
```