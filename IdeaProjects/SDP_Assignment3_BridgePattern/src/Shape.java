public abstract class Shape {
    protected final Renderer renderer;

    protected Shape(Renderer renderer) {
        if (renderer == null) {
            throw new IllegalArgumentException("Renderer cannot be null.");
        }
        this.renderer = renderer;
    }

    public abstract void draw();
}

