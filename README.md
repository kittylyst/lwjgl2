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

* Requires OpenGL to be installed

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


