public class RightTriangle extends Shape
{

    private float width;
    private float height;
    private double hypotenuse;

    // Initializes right triangles
    private void initRightTriangle(float width, float height)
    {
        this.width = width;
        this.height = height;
        setArea((this.height * this.width) / 2);
        this.hypotenuse = Math.sqrt(Math.pow(width, 2) + Math.pow(height , 2));
        setPerimeter(this.width + this.height + this.hypotenuse);
    }

    // Constructors
    public RightTriangle()
    {
        super("Default Triangle #" + (ShapeManager.getShapesMade() + 1));
        initRightTriangle(1,1);
    }

    public RightTriangle(float sideLength)
    {
        super("Default Triangle #" + (ShapeManager.getShapesMade() + 1));
        initRightTriangle(sideLength,sideLength);
    }

    public RightTriangle(float sideLength, String name) throws IllegalArgumentException
    {
        if(setName(name))
        {
            initRightTriangle(sideLength,sideLength);
        }
        else
        {
            throw new IllegalArgumentException("Name chosen for shape already taken.");
        }
    }

    public RightTriangle(float width, float height, String name) throws IllegalArgumentException
    {
        if (setName(name))
        {
            initRightTriangle(width,height);
        }
        else
        {
            throw new IllegalArgumentException("Name chosen for shape already taken.");
        }
    }

    public RightTriangle(float width, float height)
    {
        super("Default Triangle#" + (ShapeManager.getShapesMade() + 1));
        initRightTriangle(width,height);
    }

    // Accessors
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    public double getHypotenuse() { return this.hypotenuse; }

   // Return the cosine, sine and hypotenuse of the Right Triangle
    public double cos() { return this.width / this.hypotenuse; }
    public double sin() { return this.height / this.hypotenuse; }
    public double tan() { return this.height / this.width; }

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

    // Mutators
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

    // Updater, overrides the abstract function in the Shape class
    protected void update()
    {
        setArea(this.height * this.width);
        this.hypotenuse = Math.sqrt(Math.pow(width, 2) + Math.pow(height , 2));
        setPerimeter(this.width + this.height + this.hypotenuse);
    }



}
