/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * 3-D gear wheels. Originally by Brian Paul
 */
package org.lwjgl.opengles;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengles.util.*;
import org.lwjgl.opengles.util.Geometry;
import org.lwjgl.opengles.util.ImmediateModeBuffer;
import org.lwjgl.opengles.util.Shader;
import org.lwjgl.opengles.util.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import test.opengles.util.*;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.StringTokenizer;

import static org.lwjgl.opengles.GLES20.*;

/**
 * <p>
 * This is the OpenGL "standard" Gears demo, originally by Brian Paul
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 3276 $
 *          $Id: Gears.java 3276 2010-02-21 21:18:17Z matzon $
 */
public class Gears {

	private boolean run = true;

	private float view_rotx = 20.0f;

	private float view_roty = 30.0f;

	private float view_rotz = 0.0f;

	private Gear gear1;

	private Gear gear2;

	private Gear gear3;

	private float angle = 0.0f;

	private Shader vsh;
	private Shader fsh;

	private ShaderProgram program;

	private int LIGHT_POS;

	private int MVP;
	private int NM;

	private int GEAR_COLOR;

	private int vPosition;
	private int vNormal;

	private final Matrix4f p   = new Matrix4f();
	private final Matrix4f mv  = new Matrix4f();
	private final Matrix4f mvp = new Matrix4f();

	private final FloatBuffer m4fBuffer = BufferUtils.createFloatBuffer(4 * 4);
	private final FloatBuffer m3fBuffer = BufferUtils.createFloatBuffer(3 * 3);

	public static void main(String[] args) {
		new Gears().execute();
		System.exit(0);
	}

	/**
	 *
	 */
	private void execute() {
		try {
			init();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.out.println("Failed to initialize Gears.");
			return;
		}

		System.out.println("\nGL RENDERER: " + glGetString(GL_RENDERER));
		System.out.println("GL VENDOR: " + glGetString(GL_VENDOR));
		System.out.println("GL VERSION: " + glGetString(GL_VERSION));
		System.out.println("GL_SHADING_LANGUAGE_VERSION: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
		System.out.println("GL_EXTENSIONS = " + glGetString(GL_EXTENSIONS));

		ContextCapabilities caps = GLContext.getCapabilities();

		System.out.println();

		// Check extension support
		Field[] field = ContextCapabilities.class.getFields();
		for ( Field f : field ) {
			if ( f.getName().startsWith("GL_") ) {
				try {
					System.out.println(f.getName() + " - " + f.getBoolean(caps));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println();

		// Check for extensions that LWJGL does not support
		final String extensions = glGetString(GL_EXTENSIONS);

		final StringTokenizer tokenizer = new StringTokenizer(extensions);
		while ( tokenizer.hasMoreTokens() ) {
			final String ext = tokenizer.nextToken();
			try {
				if ( !caps.getClass().getField(ext).getBoolean(caps) )
					System.out.println("-- Extension exposed but functions are missing: " + ext);
			} catch (NoSuchFieldException e) {
				System.out.println("-- No LWJGL support for extension: " + ext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		loop();

		destroy();
	}

	/**
	 *
	 */
	private void destroy() {
		program.destroy();

		fsh.destroy();
		vsh.destroy();

		gear3.destroy();
		gear2.destroy();
		gear1.destroy();

		Display.destroy();
	}

	/**
	 *
	 */
	private void loop() {
		long lastFrameTime = Sys.getTime();
		long startTime = System.currentTimeMillis() + 5000;
		long fps = 0;

		while ( run ) {
			if ( !Display.isVisible() )
				Thread.yield();
			else {
				// This is the current frame time.
				long frameStart = Sys.getTime();

				// How many seconds passed since last frame.
				final float frameTime = (float)((frameStart - lastFrameTime) / (double)Sys.getTimerResolution());
				lastFrameTime = frameStart;

				angle += frameTime * 120.0f;

				handleInput();

				//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_COVERAGE_BUFFER_BIT_NV);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				GLMatrix.glPushMatrix();
				GLMatrix.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
				GLMatrix.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
				GLMatrix.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

				GLMatrix.glPushMatrix();
				GLMatrix.glTranslatef(-3.0f, -2.0f, 0.0f);
				GLMatrix.glRotatef(angle, 0.0f, 0.0f, 1.0f);
				gear1.render();
				GLMatrix.glPopMatrix();

				GLMatrix.glPushMatrix();
				GLMatrix.glTranslatef(3.1f, -2.0f, 0.0f);
				GLMatrix.glRotatef(-2.0f * angle - 9.0f, 0.0f, 0.0f, 1.0f);
				gear2.render();
				GLMatrix.glPopMatrix();

				GLMatrix.glPushMatrix();
				GLMatrix.glTranslatef(-3.1f, 4.2f, 0.0f);
				GLMatrix.glRotatef(-2.0f * angle - 25.0f, 0.0f, 0.0f, 1.0f);
				gear3.render();
				GLMatrix.glPopMatrix();

				GLMatrix.glPopMatrix();

				try {
					Display.update();
					//Display.sync(60);
				} catch (PowerManagementEventException e) {
					e.printStackTrace();
				}
				if ( startTime > System.currentTimeMillis() ) {
					fps++;
				} else {
					long timeUsed = 5000 + (startTime - System.currentTimeMillis());
					startTime = System.currentTimeMillis() + 5000;
					System.out.println(fps + " frames in " + (timeUsed / 1000f) + " seconds = " + (fps / (timeUsed / 1000f)));
					fps = 0;
				}

				if ( Display.isCloseRequested() )
					break;
			}
		}
	}

	private void handleInput() {
		if ( Keyboard.getNumKeyboardEvents() != 0 ) {
			while ( Keyboard.next() ) {
				if ( Keyboard.getEventKeyState() )
					continue;

				final int key = Keyboard.getEventKey();
				switch ( key ) {
					case Keyboard.KEY_ESCAPE:
						run = false;
						break;
				}
			}
		}

		while ( Mouse.next() ) ;
	}

	/**
	 *
	 */
	private void init() throws LWJGLException {
		final int WIDTH = 640;
		final int HEIGHT = 480;

		Display.setLocation((Display.getDisplayMode().getWidth() - WIDTH) / 2,
		                    (Display.getDisplayMode().getHeight() - HEIGHT) / 2);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		} catch (PowerManagementEventException e) {
			e.printStackTrace();
		}
		Display.setTitle("Gears");
		Display.create(new PixelFormat());

		//glCoverageMaskNV(true);

		// setup ogl
		glViewport(0, 0, WIDTH, HEIGHT);
		glFrontFace(GL_CCW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		final Vector3f lp = new Vector3f(5.0f, 5.0f, 10.0f);
		lp.normalise();
		GLLight.glLight(GLLight.GL_LIGHT0, GLLight.GL_POSITION, lp.getX(), lp.getY(), lp.getZ(), 0.0f);

		/* make the gears */
		gear1 = new Gear(gear(1.0f, 4.0f, 1.0f, 20, 0.7f), new float[] { 0.8f, 0.1f, 0.0f, 1.0f });
		gear2 = new Gear(gear(0.5f, 2.0f, 2.0f, 10, 0.7f), new float[] { 0.0f, 0.8f, 0.2f, 1.0f });
		gear3 = new Gear(gear(1.3f, 2.0f, 0.5f, 10, 0.7f), new float[] { 0.2f, 0.2f, 1.0f, 1.0f });

		GLMatrix.glMatrixMode(GLMatrix.GL_PROJECTION);
		GLMatrix.glLoadIdentity();

		final float h = (float)300 / (float)300;
		GLMatrix.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);

		GLMatrix.glMatrixMode(GLMatrix.GL_MODELVIEW);
		GLMatrix.glLoadIdentity();
		GLMatrix.glTranslatef(0.0f, 0.0f, -40.0f);

		vsh = new Shader(GL_VERTEX_SHADER, "uniform highp vec4 LIGHT_POS;\n" +
		                                   "uniform highp mat4 MODEL_VIEW_PROJECTION_MATRIX;\n" +
		                                   "uniform mediump mat3 NORMAL_MATRIX;\n" +
		                                   "uniform lowp vec3 GEAR_COLOR;\n" +
		                                   "attribute highp vec3 vPosition;\n" +
		                                   "attribute mediump vec3 vNormal;\n" +
		                                   "varying lowp vec3 color;\n" +
		                                   "void main(void) {\n" +
		                                   "\tgl_Position = MODEL_VIEW_PROJECTION_MATRIX * vec4(vPosition, 1.0);\n" +
		                                   "\tvec3 normal = NORMAL_MATRIX * vNormal;\n" +
		                                   "\tcolor = max(dot(normal, vec3(LIGHT_POS)), 0.0) * GEAR_COLOR + vec3(0.05);\n" +
		                                   "}");

		fsh = new Shader(GL_FRAGMENT_SHADER, "varying lowp vec3 color;\n" +
		                                     "void main(void) {\n" +
		                                     "\tgl_FragColor = vec4(color, 1.0);\n" +
		                                     "}");

		program = new ShaderProgram(vsh, fsh);
		program.enable();

		LIGHT_POS = program.getUniformLocation("LIGHT_POS");

		MVP = program.getUniformLocation("MODEL_VIEW_PROJECTION_MATRIX");
		NM = program.getUniformLocation("NORMAL_MATRIX");

		GEAR_COLOR = program.getUniformLocation("GEAR_COLOR");

		vPosition = program.getAttributeLocation("vPosition");
		vNormal = program.getAttributeLocation("vNormal");

		glEnableVertexAttribArray(vNormal);
		glEnableVertexAttribArray(vPosition);
	}

	/**
	 * Draw a gear wheel.  You'll probably want to call this function when
	 * building a display list since we do a lot of trig here.
	 *
	 * @param inner_radius radius of hole at center
	 * @param outer_radius radius at center of teeth
	 * @param width        width of gear
	 * @param teeth        number of teeth
	 * @param tooth_depth  depth of tooth
	 */
	private static Geometry gear(float inner_radius, float outer_radius, float width, int teeth, float tooth_depth) {
		int i;
		float r0, r1, r2;
		float angle, da;
		float u, v, len;

		r0 = inner_radius;
		r1 = outer_radius - tooth_depth / 2.0f;
		r2 = outer_radius + tooth_depth / 2.0f;

		da = 2.0f * (float)Math.PI / teeth / 4.0f;

		final Geometry gear = new Geometry();
		final ImmediateModeBuffer imb = new ImmediateModeBuffer(1024);
		int lastDrawIndex = 0;

		//glShadeModel(GL_FLAT);

		// draw front face
		lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, teeth * 4 + 2);
		for ( i = 0; i <= teeth; i++ ) {
			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), width * 0.5f);

			if ( i < teeth ) {
				imb.glNormal3f(0.0f, 0.0f, 1.0f);
				imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), width * 0.5f);

				imb.glNormal3f(0.0f, 0.0f, 1.0f);
				imb.glVertex3f(r1 * Geometry.cos(angle + 3.0f * da), r1 * Geometry.sin(angle + 3.0f * da), width * 0.5f);
			}
		}

		// draw front sides of teeth
		for ( i = 0; i < teeth; i++ ) {
			lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, 4);

			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + da), r2 * Geometry.sin(angle + da), width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle + 3.0f * da), r1 * Geometry.sin(angle + 3.0f * da), width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + 2.0f * da), r2 * Geometry.sin(angle + 2.0f * da), width * 0.5f);
		}

		// draw back face
		lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, (teeth + 1) * 4);
		for ( i = 0; i <= teeth; i++ ) {
			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle + 3 * da), r1 * Geometry.sin(angle + 3 * da), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), -width * 0.5f);
		}

		// draw back sides of teeth
		for ( i = 0; i < teeth; i++ ) {
			lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, 4);

			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle + 3 * da), r1 * Geometry.sin(angle + 3 * da), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + 2 * da), r2 * Geometry.sin(angle + 2 * da), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), -width * 0.5f);

			imb.glNormal3f(0.0f, 0.0f, 1.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + da), r2 * Geometry.sin(angle + da), -width * 0.5f);
		}

		// draw outward faces of teeth
		// OpenGL ES 2.0 note: This needs to be converted to a triangle
		// list with face normals to get the flat look of the original.
		lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, teeth * 8 + 2);
		for ( i = 0; i < teeth; i++ ) {
			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(Geometry.cos(angle), Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), width * 0.5f);

			imb.glNormal3f(Geometry.cos(angle), Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle), r1 * Geometry.sin(angle), -width * 0.5f);

			u = r2 * Geometry.cos(angle + da) - r1 * Geometry.cos(angle);
			v = r2 * Geometry.sin(angle + da) - r1 * Geometry.sin(angle);
			len = (float)Math.sqrt(u * u + v * v);
			u /= len;
			v /= len;

			imb.glNormal3f(v, -u, 0.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + da), r2 * Geometry.sin(angle + da), width * 0.5f);

			imb.glNormal3f(v, -u, 0.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + da), r2 * Geometry.sin(angle + da), -width * 0.5f);

			imb.glNormal3f(Geometry.cos(angle), Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + 2 * da), r2 * Geometry.sin(angle + 2 * da), width * 0.5f);

			imb.glNormal3f(Geometry.cos(angle), Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r2 * Geometry.cos(angle + 2 * da), r2 * Geometry.sin(angle + 2 * da), -width * 0.5f);

			u = r1 * Geometry.cos(angle + 3 * da) - r2 * Geometry.cos(angle + 2 * da);
			v = r1 * Geometry.sin(angle + 3 * da) - r2 * Geometry.sin(angle + 2 * da);

			imb.glNormal3f(v, -u, 0.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle + 3 * da), r1 * Geometry.sin(angle + 3 * da), width * 0.5f);

			imb.glNormal3f(v, -u, 0.0f);
			imb.glVertex3f(r1 * Geometry.cos(angle + 3 * da), r1 * Geometry.sin(angle + 3 * da), -width * 0.5f);
		}
		imb.glNormal3f(Geometry.cos(0), Geometry.sin(0), 0.0f);
		imb.glVertex3f(r1 * Geometry.cos(0), r1 * Geometry.sin(0), width * 0.5f);

		imb.glNormal3f(Geometry.cos(0), Geometry.sin(0), 0.0f);
		imb.glVertex3f(r1 * Geometry.cos(0), r1 * Geometry.sin(0), -width * 0.5f);

		//glShadeModel(GL_SMOOTH);

		// draw inside radius cylinder
		lastDrawIndex += gear.addDrawCommand(GL_TRIANGLE_STRIP, lastDrawIndex, (teeth + 1) * 2);
		for ( i = 0; i <= teeth; i++ ) {
			angle = i * 2.0f * (float)Math.PI / teeth;

			imb.glNormal3f(-Geometry.cos(angle), -Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), -width * 0.5f);

			imb.glNormal3f(-Geometry.cos(angle), -Geometry.sin(angle), 0.0f);
			imb.glVertex3f(r0 * Geometry.cos(angle), r0 * Geometry.sin(angle), width * 0.5f);
		}

		gear.update(imb.getBuffer());
		return gear;
	}

	private class Gear {

		private final Geometry geom;

		private final float[] color;

		Gear(final Geometry geom, final float[] color) {
			this.geom = geom;
			this.color = color;
		}

		void render() {
			// Set gear color
			glUniform3f(GEAR_COLOR, color[0], color[1], color[2]);

			// Set Light position
			GLLight.setUniform4f(LIGHT_POS, GLLight.GL_LIGHT0, GLLight.GL_POSITION);

			// Get Projection and Modelview matrices
			GLMatrix.glMatrixMode(GLMatrix.GL_PROJECTION);
			GLMatrix.glGetMatrix(p);

			GLMatrix.glMatrixMode(GLMatrix.GL_MODELVIEW);
			GLMatrix.glGetMatrix(mv);

			// Set MVP uniform
			Matrix4f.mul(p, mv, mvp);
			mvp.store(m4fBuffer);
			m4fBuffer.flip();
			glUniformMatrix4(MVP, false, m4fBuffer);

			// Set normal matrix (upper-left 3x3 of the inverse transpose MV matrix)
			mv.invert();
			mv.transpose();
			mv.store3f(m3fBuffer);
			m3fBuffer.flip();
			glUniformMatrix3(NM, false, m3fBuffer);

			geom.bind();

			final int stride = (3 + 3) * 4;
			glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, stride, 0);
			glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, stride, 3 * 4);

			geom.draw();
		}

		void destroy() {
			geom.destroy();
		}

	}

}