package com.github.ityeri

import ViewerAdapter
import com.badlogic.gdx.InputAdapter

class CameraScrollProcessor(val viewerAdapter: ViewerAdapter) : InputAdapter() {
    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        viewerAdapter.goalZoom *= 1 + (amountY * viewerAdapter.wheelSensitivity)
        return true
    }
}
