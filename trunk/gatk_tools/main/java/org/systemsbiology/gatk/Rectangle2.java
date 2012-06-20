package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.Rectangle2
 *  In this immutable form there is no way to reset the width of a square to be different fron the height
 * User: steven
 * Date: 6/18/12
 */
public class Rectangle2 {
    public static final Rectangle2[] EMPTY_ARRAY = {};

    private final int width;
    private final int height;

    public Rectangle2(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * return a new rectangle with the current height and the given width
     * this is what String replace does
     * @param width
     * @return
     */
    public Rectangle2 setWidth(int width) {
       return new Rectangle2(getHeight(),width);
    }

    public int getHeight() {
        return height;
    }

    /**
      * return a new rectangle with the current width and the given height
      * this is what String replace does
      * @param width
      * @return
      */
    public Rectangle2 setHeight(int height) {
        return new Rectangle2(height,getWidth());
    }
}

class Square2 extends Rectangle2
{
    Square2(int width) {
        super(width, width);
    }
}

