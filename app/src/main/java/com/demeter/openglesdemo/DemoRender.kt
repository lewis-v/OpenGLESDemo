package com.demeter.openglesdemo

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: lewisywliu
 * @date: 2021/3/13
 * @Describe: render demo
 */

private const val V_SHADER = """#version 300 es
precision mediump float;


in vec4 a_position;

void main()
{
    gl_Position = a_position;
}
"""

private const val F_SHADER = """#version 300 es
precision mediump float;

out vec4 fragColor;
void main()
{
    fragColor = vec4(1.0,0.5,0.5,1.0);
}
"""

class DemoRender : GLSurfaceView.Renderer {
    private var aPosition = 0
    private var program = 0

    private var vertex = floatArrayOf(
        0f, 0.5f,
        -0.5f, -0.5f,
        0.5f, -0.5f
    )

    private val stride = (vertex.size) * 4

    private val buffer =
        ByteBuffer.allocateDirect(stride).order(ByteOrder.nativeOrder()).asFloatBuffer()

    init {
        buffer.put(vertex)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER)//建立指定类型的shader
        GLES30.glShaderSource(vShader, V_SHADER)//设置shader的程序字符串
        GLES30.glCompileShader(vShader)//编译
        val arr = IntArray(3)
        GLES30.glGetShaderiv(vShader, GLES30.GL_COMPILE_STATUS, arr, 0)
        Log.i("Render", "vShader arr:${arr[0]}  ${GLES30.glGetShaderInfoLog(vShader)}")//输出编译的日志

        val fShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER)
        GLES30.glShaderSource(fShader, F_SHADER)
        GLES30.glCompileShader(fShader)
        GLES30.glGetShaderiv(fShader, GLES30.GL_COMPILE_STATUS, arr, 1)
        Log.i("Render", "fShader arr:${arr[1]} ${GLES30.glGetShaderInfoLog(fShader)}")

        program = GLES30.glCreateProgram()//创建程序
        GLES30.glAttachShader(program, vShader)//将顶点着色器设置为程序的附件
        GLES30.glAttachShader(program, fShader)//将片元着色器设置为程序附件
        GLES30.glLinkProgram(program)//链接程序
        GLES30.glDeleteShader(vShader)
        GLES30.glDeleteShader(fShader)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, arr, 2)
        Log.i("Render", "program arr:${arr[2]} ${GLES30.glGetProgramInfoLog(program)}")


        aPosition = GLES30.glGetAttribLocation(program, "a_position")

        Log.i("Render", "aPosition:$aPosition")

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        Log.i("Render", "draw")
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)//清理颜色缓冲区
        GLES30.glClearColor(0f, 1f, 1f, 1f)//清除颜色缓冲，相当于用这个颜色值刷一下背景
        GLES30.glUseProgram(program)//指定当前使用的程序

        buffer.position(0)
        GLES30.glVertexAttribPointer(aPosition, 2, GLES30.GL_FLOAT, false, 0, buffer)//设置顶点数据
        GLES30.glEnableVertexAttribArray(aPosition)//使能顶点数据

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
    }
}