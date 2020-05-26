import java.awt.*;

/*Shape.java
* Abstract bass class from which all shapes inherit, implements the comparable interface and overrides toString()
* to print the name of the shape. */

public abstract class Shape implements Comparable<Shape>
{

    private double area;
    private double perimeter;
    private String name;
    private Color color;


    // Constructors
    /* Shapes need unique names. However, the constructors for the shape class bypass this requirement by not using the setName() member.
       To ensure that the shape has a unique name anyway, the shapesMade member of the ShapeManager class. */
    public Shape()
    {
        this.name = "Default Shape #" + (ShapeManager.getShapesMade() + 1);
    }

    public Shape(String name)
    {
        this.name = name;
    }

    public double getArea()
    {
        return this.area;
    }

    public double getPerimeter()
    {
        return this.perimeter;
    }

    public String getName()
    {
        return this.name;
    }

    public Color getColor()
    {
        return this.color;
    }

    // mutators
    protected void setArea(double newArea)
    {
        this.area = newArea;
    }

    protected void setPerimeter(double newPerimeter)
    {
        this.perimeter = newPerimeter;
    }

    // sets the name but only if not already used to preserve the uniqueness of names
    public boolean setName(String name)
    {
        if(!checkDupeNames(name)) // uses the shapes ArrayList from ShapeManager
        {
            this.name = name;
            return true;
        }

        return false;

    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    private static boolean checkDupeNames(String name)
    {
        for (Shape shape : ShapeManager.getShapes())
        {
            if (name.equals(shape.getName()))
            {
                return true;
            }
        }

        return false;

    }

    // Abstract function for all shapes to override.
    // Each shape may have its own numeric data, and this should be called to update that data when the shape's parameters change
    protected abstract void update();

    public int compareTo(Shape shape)
    {
        if (this.name.equals(shape.name))
        {
            return 0;
        }
        else
        {
            return this.name.compareTo(shape.name);
        }
    }

    public String toString()
    {
        return getName();
    }

}
