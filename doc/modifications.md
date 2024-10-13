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

In particular, a couple of functions require a postback onto the UI thread (see https://github.com/libsdl-org/SDL/issues/3507)

Also, the type JAWT_MacOSXDrawingSurfaceInfo is gone, and JAWT_DrawingSurfaceInfo should be used instead.


