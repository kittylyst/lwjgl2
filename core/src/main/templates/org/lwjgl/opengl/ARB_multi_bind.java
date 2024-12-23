/*
 * Copyright (c) 2002-2013 LWJGL Project
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
package org.lwjgl.opengl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.Const;
import org.lwjgl.util.generator.Reuse;

import java.nio.IntBuffer;

public interface ARB_multi_bind {

	@Reuse("GL44")
	void glBindBuffersBase(@GLenum int target, @GLuint int first, @GLsizei int count, @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer buffers);

	@Reuse("GL44")
	void glBindBuffersRange(@GLenum int target, @GLuint int first, @GLsizei int count,
	                        @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer buffers,
	                        @Check(value = "count", canBeNull = true) @Const @GLintptr PointerBuffer offsets,
	                        @Check(value = "count", canBeNull = true) @Const @GLsizeiptr PointerBuffer sizes);

	@Reuse("GL44")
	void glBindTextures(@GLuint int first, @GLsizei int count, @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer textures);

	@Reuse("GL44")
	void glBindSamplers(@GLuint int first, @GLsizei int count, @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer samplers);

	@Reuse("GL44")
	void glBindImageTextures(@GLuint int first, @GLsizei int count, @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer textures);

	@Reuse("GL44")
	void glBindVertexBuffers(@GLuint int first, @GLsizei int count,
	                         @Check(value = "count", canBeNull = true) @Const @GLuint IntBuffer buffers,
	                         @Check(value = "count", canBeNull = true) @Const @GLintptr PointerBuffer offsets,
	                         @Check(value = "count", canBeNull = true) @Const @GLsizei IntBuffer strides);

}