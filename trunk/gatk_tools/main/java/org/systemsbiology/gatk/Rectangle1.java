package org.systemsbiology.gatk;

/**
 * org.systemsbiology.gatk.Rectangle1
 * User: steven
 * Date: 6/18/12
 */
public class Rectangle1 {
    public static final Rectangle1[] EMPTY_ARRAY = {};

    private int width;
    private int height;

    public Rectangle1(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

class Square1 extends Rectangle1
{
    Square1(int width) {
        super(width, width);
    }

    /**
     * this is really ugly and does not meet LSP
      * @param width
     */
    @Override
    public void setWidth(int width) {
        if(getHeight() != width)
            throw new IllegalArgumentException("in a square width and height must ht the same");
        super.setWidth(width);
    }


    /**
     * this is really ugly and does not meet LSP
     * @param height
     */
    @Override
     public void setHeight(int height) {
        if(getWidth() != height)
             throw new IllegalArgumentException("in a square width and height must ht the same");
         super.setHeight(height);
    }
}
