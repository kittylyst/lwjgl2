# Modifications

Changed files from 2.9.3 baseline:

platform_build/macosx_ant/build-symbol-list
platform_build/macosx_ant/build.xml
src/native/common/org_lwjgl_opengl_AWTSurfaceLock.c
src/native/macosx/context.h
src/native/macosx/org_lwjgl_opengl_Display.m
src/native/macosx/org_lwjgl_opengl_MacOSXCanvasPeerInfo.m
src/native/macosx/org_lwjgl_opengl_MacOSXContextImplementation.m

Most of these are just conversions from DOS line endings to Unix, but there are some minor code changes.

The source / release of Java 6 has been updated to version 8.

In particular, a couple of functions require a postback onto the UI thread (see https://github.com/libsdl-org/SDL/issues/3507)

Also, the type JAWT_MacOSXDrawingSurfaceInfo is gone, and JAWT_DrawingSurfaceInfo should be used instead.

We also need to clean up the ASM handling. The dependency does not seem to correspond to any standard version of ASM.
In particular, it uses a generified ASM (which did not occur until ASM6) but also refers to a ClassAdapter type, which was removed in ASM 4. 

The code has been tidied up and baselined on ASM 6 as a first step.

src/java/org/lwjgl/util/mapped/MappedObjectTransformer.java
