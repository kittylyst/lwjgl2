/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_opengl_MacOSXContextImplementation */

#ifndef _Included_org_lwjgl_opengl_MacOSXContextImplementation
#define _Included_org_lwjgl_opengl_MacOSXContextImplementation
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nCreate
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nCreate
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    getCGLShareGroup
 * Signature: (Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_getCGLShareGroup
  (JNIEnv *, jobject, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nSwapBuffers
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nSwapBuffers
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nUpdate
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nUpdate
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nReleaseCurrentContext
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nReleaseCurrentContext
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    clearDrawable
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_clearDrawable
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    setView
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_setView
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nMakeCurrent
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nMakeCurrent
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nIsCurrent
 * Signature: (Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nIsCurrent
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nSetSwapInterval
 * Signature: (Ljava/nio/ByteBuffer;I)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nSetSwapInterval
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     org_lwjgl_opengl_MacOSXContextImplementation
 * Method:    nDestroy
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_MacOSXContextImplementation_nDestroy
  (JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif