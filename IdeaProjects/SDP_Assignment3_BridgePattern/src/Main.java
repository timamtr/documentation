public class Main {
    public static void main(String[] args) {
        Renderer vectorRenderer = new VectorRenderer();
        Renderer rasterRenderer = new RasterRenderer();

        Shape circle = new Circle(vectorRenderer);
        Shape square = new Square(rasterRenderer);

        System.out.println("--- Initial Drawing ---");
        circle.draw();
        square.draw();

        System.out.println("\n--- Switching Implementations at Runtime ---");
        Shape rasterCircle = new Circle(rasterRenderer);
        rasterCircle.draw();
    }
}