
/* Circle.java
*  Class that manages circle parameters in ShapeMaker. */

public class Circle extends Shape
{
    private float radius;

    // Initializes circles
    protected void initCircle(float radius)
    {
        this.radius = radius;
        setArea(Math.PI * Math.pow(this.radius , 2));
        setPerimeter(2*this.radius*Math.PI);
    }

    // Constructors
    public Circle()
    {
        super("Default Circle #" + (ShapeManager.getShapesMade() + 1));
        initCircle( 1);
    }

    public Circle(float radius)
    {
        super("Default Circle #" + (ShapeManager.getShapesMade() + 1));
        initCircle(radius);
    }

    public Circle(float radius, String name) throws IllegalArgumentException
    {
        if(setName(name))
        {
           initCircle(radius);
        }
        else
        {
            throw new IllegalArgumentException("Name chosen for shape already taken.");
        }
    }

    // Accessors
    public float getRadius()
    {
        return this.radius;
    }

    public double getDiameter()
    {
        return this.radius * 2;
    }

    // Mutators
    public void setRadius(float newRadius) throws IllegalArgumentException
    {
        if (newRadius < 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            this.radius = newRadius;
            update();
        }

    }

    // Updater. Overrides the update abstract function in the Shape class
    protected void update()
    {
        setArea(Math.PI * Math.pow(this.radius , 2));
        setPerimeter(2*this.radius*Math.PI);
    }

}
