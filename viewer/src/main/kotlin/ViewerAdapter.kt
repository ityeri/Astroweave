import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.ityeri.CameraScrollProcessor

abstract class ViewerAdapter : ApplicationAdapter() {

    lateinit var world: World
    lateinit var camera: OrthographicCamera
    lateinit var viewport: ExtendViewport
    lateinit var batch: SpriteBatch
    lateinit var shapeRenderer: ShapeRenderer

    var wheelSensitivity = 0.1f
    var zoomSoftness = 10f

    var lastTouchX = 0f
    var lastTouchY = 0f

    var goalZoom = 1f
    var isDragging = false

    override fun create() {
        world = World(Vector2(0f, 0f), true)

        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        viewport = ExtendViewport(500f, 500f, camera)

        camera.position.set(0f, 0f, 0f)
        camera.update()

        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()

        Gdx.input.inputProcessor = CameraScrollProcessor(this)

    }

    override fun render() {
        val dt = Gdx.graphics.deltaTime

        input()
        logic(dt)

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        draw()

        shapeRenderer.end()
    }

    open fun input() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

            // 처음 드래그를 시작하면
            if (!isDragging) {
                lastTouchX = Gdx.input.x.toFloat()
                lastTouchY = Gdx.input.y.toFloat()
                isDragging = true
            }
            // 드래그를 하는 도중이이면
            else {
                val currentScreen = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
                val currentWorld = viewport.unproject(currentScreen.cpy())

                val lastWorld = viewport.unproject(Vector2(lastTouchX, lastTouchY))

                val deltaX = lastWorld.x - currentWorld.x
                val deltaY = lastWorld.y - currentWorld.y

                camera.translate(deltaX, deltaY)

                lastTouchX = Gdx.input.x.toFloat()
                lastTouchY = Gdx.input.y.toFloat()
            }
        } else {
            isDragging = false
        }
    }

    open fun logic(dt: Float) {

        // 않이 카메라 중심으로 주민주마웃 어카함

//        val cameraX = camera.position.x
//        val cameraY = camera.position.y
//
//        val mouseScreen = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
//        val mouseWorld = viewport.unproject(mouseScreen.cpy())
//
//        val offsetDeltaX = mouseWorld.x - cameraX
//        val offsetDeltaY = mouseWorld.y - cameraY
//
//        camera.translate(offsetDeltaX, offsetDeltaY)
//        println("${offsetDeltaX}, ${offsetDeltaY}")
//
        camera.zoom += (goalZoom - camera.zoom) * zoomSoftness * dt
//        camera.translate(-offsetDeltaX, -offsetDeltaY)
//
        camera.update()

    }

    abstract fun draw()

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
        world.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}
