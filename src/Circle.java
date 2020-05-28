// License
/*Copyright 2020 Christopher W. Driggers-Ellis

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/

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
