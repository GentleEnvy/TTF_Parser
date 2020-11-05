package task_3.graphics;

import task_3.graphics.canvas.Canvas;
import task_3.graphics.canvas.RenderParameters;


public abstract class Graphic {
    private final RenderParameters renderParameters = new RenderParameters();

    public final void render(Canvas canvas) {
        RenderParameters oldRenderParameters = canvas.addRenderParameters(
                renderParameters
        );
        _render(canvas);
        canvas.comeBack(oldRenderParameters);
    }

    abstract protected void _render(Canvas canvas);

    public RenderParameters getRenderParameters() {
        return renderParameters;
    }
}
