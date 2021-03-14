package com.demeter.openglesdemo

import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<GLSurfaceView>(R.id.gl).apply {
            setEGLContextClientVersion(3)
            this.setRenderer(DemoRender())
            this.renderMode = RENDERMODE_WHEN_DIRTY
//            this.onResume()
        }

    }

}