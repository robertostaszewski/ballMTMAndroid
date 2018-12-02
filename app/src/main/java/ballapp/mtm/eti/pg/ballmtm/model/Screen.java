package ballapp.mtm.eti.pg.ballmtm.model;

public class Screen {
    private final float width;
    private final float height;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
