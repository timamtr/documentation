public class Square extends Shape {

    public Square(Renderer renderer) {
        super(renderer);
    }

    @Override
    public void draw() {
        renderer.renderShape("Square");
    }
}
