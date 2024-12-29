# Modifications for 2.9.4

Changed files from 2.9.3 baseline:

```
platform_build/macosx_ant/build-symbol-list
platform_build/macosx_ant/build.xml
src/native/common/org_lwjgl_opengl_AWTSurfaceLock.c
src/native/macosx/context.h
src/native/macosx/org_lwjgl_opengl_Display.m
src/native/macosx/org_lwjgl_opengl_MacOSXCanvasPeerInfo.m
src/native/macosx/org_lwjgl_opengl_MacOSXContextImplementation.m
```

Most of these are just conversions from DOS line endings to Unix, but there are some minor code changes.

The source / release of Java 6 has been updated to version 8.

In particular, a couple of functions require a postback onto the UI thread (see https://github.com/libsdl-org/SDL/issues/3507)

Also, the type JAWT_MacOSXDrawingSurfaceInfo is gone, and JAWT_DrawingSurfaceInfo should be used instead.

NOTE: There are also an increasing number of deprecated API calls in place -- and over time it seems likely that Apple will remove non-Metal APIs. This will likely limit the long-term viability of the library on MacOS.

We also needed to clean up the ASM handling. The dependency does not seem to correspond to any standard version of ASM.
In particular, it uses a generified ASM (which did not occur until ASM6) but also refers to a ClassAdapter type, which was removed in ASM 4. 

The code has been tidied up and baselined on ASM 6.2.1 as a first step.
We also need a version of jinput compiled for Java 8.
The ASM and jinput jars should be placed in the `libs` dir:

```
asm-6.2.1.jar
asm-analysis-6.2.1.jar
asm-tree-6.2.1.jar
asm-util-6.2.1.jar
jinput.jar
```

```
src/java/org/lwjgl/util/mapped/MappedObjectTransformer.java
```

## Linux 

```
sudo dnf -y install libXcursor-devel
sudo dnf -y install libXxf86vm-devel
```

## NOTES

There's a branch which contains the initial steps of a port to Maven, but there are some complexities involved in using the Maven Compiler Plugin which make the original Ant project structure hard to replicate, so that effort is on hold for now. 