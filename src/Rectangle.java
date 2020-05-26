
/* Rectangle.java
*  Class for managing rectangle parameters in ShapeMaker */

public class Rectangle extends Shape
{
    private float width;
    private float height;
    private double diagonal;

    // Initializes rectangles
    private void initRectangle(float width, float height)
    {
        this.width = width;
        this.height = height;
        setArea(this.height * this.width);
        this.diagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height , 2));
        setPerimeter(2 * this.width + 2 * this.height);
    }

    // Constructors
    public Rectangle()
    {
       super("Default Rectangle #" + (ShapeManager.getShapesMade() + 1));
       initRectangle(1,1);
    }

    public Rectangle(float sideLength)
    {
        super("Default Rectangle #" + (ShapeManager.getShapesMade() + 1));
        initRectangle(sideLength,sideLength);
    }

    public Rectangle(float sideLength, String name) throws IllegalArgumentException
    {
        if(setName(name))
        {
            initRectangle(sideLength,sideLength);
        }
        else
        {
            throw new IllegalArgumentException("Name chosen for shape already taken.");
        }
    }

    public Rectangle(float width, float height, String name) throws IllegalArgumentException
    {
        if (setName(name))
        {
            initRectangle(width, height);
        }
        else
        {
            throw new IllegalArgumentException("Name chosen for same already taken.");
        }
    }

    public Rectangle(float width, float height)
    {
        super("Default Rectangle #" + (ShapeManager.getShapesMade() + 1));
        initRectangle(width, height);
    }

    // Accessors
    public float getWidth()
    {
        return this.width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public double getDiagonal() { return this.diagonal; }

    // Creates a RightTriangle from the rectangle passed
    public static RightTriangle exportRightTriangle(Rectangle rectangle)
    {
        String rightTriangleName = rectangle.getName() + " exported RightTriangle";
        return new RightTriangle(rectangle.width , rectangle.height, rightTriangleName);
    }

    // Mutators
    public void setWidth(float newWidth) throws IllegalArgumentException
    {
        if (newWidth < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            this.width = newWidth;
            update();
        }

    }

    public void setHeight(float newHeight) throws IllegalArgumentException
    {
        if (newHeight < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            this.height = newHeight;
            update();
        }

    }

    public void setDimensions(float newWidth, float newHeight)
    {
        setWidth(newWidth);
        setHeight(newHeight);
    }

    // Updater, Overrides the abstract function in the Shape class
    protected void update()
    {
        setArea(this.height * this.width);
        setPerimeter(2 * this.width + 2 * this.height);
        this.diagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height , 2));
    }


}
