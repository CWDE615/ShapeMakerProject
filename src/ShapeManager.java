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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/* ShapeManager.java
*  A Class that extends JPanel to display shapes and interface with the user. This is where all the UI elements are made and handled.
*  This is also where shapes are created and stored into the shapes ArrayList for later editing. */

public class ShapeManager extends JPanel
{
    // Spacing of UI Elements. Some are constant others change according to the dimensions of the active shape.
    // They are set to these defaults to provide a definition for these parameters in the absence of an active shape
    // at the beginning of the program.
    private int shapeX = 50;
    private int shapeY = 75;
    private int nameX = shapeX;
    private int nameY = shapeY - 25;
    private int dataX = shapeX;
    private int dataY = shapeY + 120;
    private int verticalDataGap = 20;
    private int horizontalDataGap = 120;
    private int precision = 3;
    private int xUI = 300;
    private int yUI = 50;
    private static int shapesMade = 0;
    private static final int MIDDLE_GAP = 150;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int VERTICAL_GAP = 10;

    // Initializes a button in the program's UI. Buttons are stacked in a vertical column, and the vertical displacement parameter tells us
    // how much offset there is between the top of the button column, yUI, and the top of the current button as a multiple of the sum of the
    // BUTTON_HEIGHT and VERTICAL_GAP constants
    private void initButton(int verticalDisplacement, String label,Runnable runnable)
    {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The lambda passed into this function
                runnable.run();

                // Whenever a button is pressed the UI is repainted and the UI is potentially resized, depending on whether or not the active shape has changed size
                repaint();
                if (activeShape instanceof Rectangle)
                {
                    if (((Rectangle) activeShape).getHeight() > 100)
                        repositionData(0, (shapeY + Math.round(((Rectangle) activeShape).getHeight()) + verticalDataGap) - dataY, 0,0);
                    if (((Rectangle) activeShape).getWidth() > 100)
                        repositionAndResizeButtons((shapeX + Math.round(((Rectangle) activeShape).getWidth()) + MIDDLE_GAP) - xUI,0,0,0);
                    if (((Rectangle) activeShape).getHeight() <= 100)
                        repositionData(0,195-dataY,0,0);
                    if (((Rectangle) activeShape).getWidth() <= 100)
                        repositionAndResizeButtons(300-xUI, 0,0,0);
                }
                if (activeShape instanceof RightTriangle)
                {
                    if (((RightTriangle) activeShape).getHeight() > 100)
                        repositionData(0, (shapeY + Math.round(((RightTriangle) activeShape).getHeight()) + verticalDataGap) - dataY, 0,0);
                    if (((RightTriangle) activeShape).getWidth() > 100)
                        repositionAndResizeButtons((shapeX + Math.round(((RightTriangle) activeShape).getWidth()) + MIDDLE_GAP) - xUI,0,0,0);
                    if (((RightTriangle) activeShape).getHeight() <= 100)
                        repositionData(0,195-dataY,0,0);
                    if (((RightTriangle) activeShape).getWidth() <= 100)
                        repositionAndResizeButtons(300-xUI, 0,0,0);
                }
                else if (activeShape instanceof Circle)
                {
                    if (((Circle) activeShape).getRadius() > 50)
                        repositionAndResizeUI((shapeX + Math.round(((Circle) activeShape).getRadius() * 2) + MIDDLE_GAP) - xUI,0,0,0,
                            0, (shapeY + Math.round(((Circle) activeShape).getRadius() * 2) + verticalDataGap) - dataY, 0, 0);
                    else
                        repositionAndResizeUI(300-xUI, 0,0,0,0,195-dataY,0,0);
                }
            }
        });

        // During the initialization process, each button has its bounds placed using the parameters listed above and is added to the JPanel. Moreover, each button is placed in a HashMap
        button.setBounds(xUI, yUI + verticalDisplacement*(BUTTON_HEIGHT + VERTICAL_GAP), BUTTON_WIDTH, BUTTON_HEIGHT);
        add(button);
        buttonMap.put(label,button);
    }

    // Handles editing of Shape parameters by taking a shape as an argument as well as a member of the Parameter enumeration
    // Shapes each handle the updating of their Area's and their Perimeter's in the functions used to set their parameters.
    private boolean parameterDialogHandler(Shape shape, Parameter parameter)
    {
        try
        {
            // Display a dialog to get the user's input for what they'd like the new value of the parameter to be
            String newParameter = (String)JOptionPane.showInputDialog(ShapeManager.this, "What will be the " + parameter + " of\nthis " + shape.getClass().toString().substring(6) + " be?",
                    "Set Shape " + parameter, JOptionPane.PLAIN_MESSAGE, null,null, 100);

            // Handles the case that the user did not choose a new value at all (either by pressing cancel instead of OK or closing the dialog)
            // The return is used when creating new shapes but ignored when editing the parameters of active shapes
            if(newParameter != null)
            {
                editShapeParameter(shape, parameter, Float.parseFloat(newParameter));
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            // If the user did input something during the parameter changing dialog, but it was not an integer, a NumberFormatException will be thrown. This tells the user what they did wrong.
            JOptionPane.showMessageDialog(ShapeManager.this, "The formatting of your " + parameter + " is incorrect.\nPlease only enter integers or floating point values for the parameters of all shapes.",
                    "Error: Incorrect Number Format",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch (IllegalArgumentException e)
        {
            // It is possible to attempt changing the Radius of Rectangles and RightTriangles and the Height or Width of a Circle despite the fact that those shapes have no such parameters, respectively.
            // To handle such a case, the editShapeParameter function throws an IllegalArgumentException with a message corresponding to the shape type and parameter that the user attempted to edit.
            JOptionPane.showMessageDialog(ShapeManager.this, e.getMessage(),"Error: Illegal Argument", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Handles the changing of a Shape's name using the Shape parameter. Also takes a string for the Title of the name-changing dialog box
    private boolean shapeNameDialogHandler(Shape shape, String title)
    {
        // Prompt the user for a name
        String newName = (String)JOptionPane.showInputDialog(ShapeManager.this, "What will be the name of\nthe shape be?",
                title, JOptionPane.PLAIN_MESSAGE, null,null,activeShape.getName());

        // Check if the user actually entered a name.
        // The return is ignored when editing an active shape's name but is used during the creation of new shapes.
        if (newName != null)
        {
            if(!editShapeName(shape, newName))
            {
                JOptionPane.showMessageDialog(ShapeManager.this,"All shapes must have unique names. \nPick a unique name for your shape and try again.", "Error: Shape Name Taken", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else
            {
                return true;
            }
        }

        return false;

    }

    // initializes all buttons in the program
    private void initButtons()
    {
        // an int to iterate the vertical gap between each of the buttons
        int iter = 0;

        // Background color Button
        initButton(iter++,"Change Background Color", () -> {

            // Has the user choose the new background through the JColorChooser Dialog
            Color color = JColorChooser.showDialog(ShapeManager.this,"Choose the New Background Color", getBackground());

            // Not choosing a color and setting the background does so successfully without throwing an exception but with unexpected results.
            // Thus, we must check if the user actually chose a color. If they didn't, nothing happens.
            if (color != null)
            {
                setBackground(color);
            }
        });

        // Shape name Button
        initButton(iter++, "Change Shape Name", () -> shapeNameDialogHandler(activeShape, "Rename Shape"));

        // Shape Width, Height and Radius Buttons
        initButton(iter++, "Set Shape Width", () -> parameterDialogHandler(activeShape, Parameter.Width));
        initButton(iter++, "Set Shape Height", () -> parameterDialogHandler(activeShape, Parameter.Height));
        initButton(iter++, "Set Shape Radius", () -> parameterDialogHandler(activeShape, Parameter.Radius));

        // Add Shape Button
        initButton(iter++, "Add Shape", () -> {

            // Create a string array with all the types of shape that the user may create
            String[] options = new String[]{"Right Triangle", "Rectangle", "Circle"};
            // Display a dialog where those strings are options and the first is a default
            String option = (String)JOptionPane.showInputDialog(ShapeManager.this, "Add a shape to your collection", "Add A Shape",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            // The user may not have chosen an option, so check if they did
            if(option != null)
            {
                try
                {
                    // Initialize the shape reference that will be edited within the switch, case structure
                    Shape shape = null;

                    // Initialize a boolean to track whether the shape making dialogs go smoothly.
                    boolean goodInput = true;

                    // According to their shape option, the user will be given a different set of dialogs for creating their shape.
                    // This switch handles these dialogs through the Parameter dialog handler and uses the returns thereof to assess whether the process has gone correctly
                    switch (option)
                    {
                        case "Right Triangle":
                            shape = new RightTriangle(100);
                            if(!parameterDialogHandler(shape, Parameter.Width) || !parameterDialogHandler(shape, Parameter.Height))
                                goodInput = false;
                            break;
                        case "Rectangle":
                            shape = new Rectangle(100);
                            if(!parameterDialogHandler(shape, Parameter.Width) || !parameterDialogHandler(shape, Parameter.Height))
                                goodInput = false;
                            break;
                        case "Circle":
                            shape = new Circle(100);
                            if (!parameterDialogHandler(shape, Parameter.Radius))
                                goodInput = false;
                            break;
                    }

                    // If the input was good through the parameter input phase, the program moves on to setting the shape's name
                    if (goodInput)
                    {
                        // If this fails, usually because the user did not input a unique shape name, goodInput is switched to false
                        if (!shapeNameDialogHandler(shape, "Name New Shape"))
                            goodInput = false;
                    }

                    // If all else has gone correctly, the user can choose the color of their shape. However, if they do not choose one the default is the color of the currently active shape
                    if (goodInput)
                    {
                        Color color = JColorChooser.showDialog(ShapeManager.this, "Choose the color of your new Shape", activeShape.getColor());;

                        if (color == null)
                        {
                            color = activeShape.getColor();
                        }

                        // Sets the color of the new shape, makes the new, completed shape the active shape and adds it to the shapes ArrayList.
                        shape.setColor(color);
                        activeShape = shape;
                        addShape(shape);
                    }

                }
                catch (IllegalArgumentException e)
                {
                    JOptionPane.showMessageDialog(ShapeManager.this, e.getMessage(), "Error: Illegal Argument", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        // Delete Shape Button
        initButton(iter++, "Delete Shape", () -> {

            // The user cannot delete their last shape
            if (shapes.size() > 1)
            {
                // show the user a dialog where they may decide which shape to choose
                Shape shape = (Shape)JOptionPane.showInputDialog(ShapeManager.this, "Remove a shape from your collection", "Delete A Shape",
                        JOptionPane.PLAIN_MESSAGE, null, shapes.toArray(),shapes.toArray()[0]);

                // check whether the user actually chose a shape
                if (shape != null)
                {
                    // if they did, delete it and check whether it was the active shape
                    deleteShape(shape);

                    // if it was, set the first shape in the shapes ArrayList to the new Active Shape
                    if (shape.equals(activeShape))
                    {
                        activeShape = shapes.get(0);
                    }
                }

            }
            else
            {
                // shows an error message in the case that user would be deleting their only shape
                JOptionPane.showMessageDialog(ShapeManager.this, "You cannot delete your last shape.", "Error: Last Shape Will be Deleted", JOptionPane.ERROR_MESSAGE);
            }});

        // Change Active Shape Button
        // Changes the active shape
        initButton(iter++, "Change Active Shape", () -> {
            Shape shape = (Shape)JOptionPane.showInputDialog(ShapeManager.this, "Choose which shape you would like to work with", "Change Active Shape",
                    JOptionPane.PLAIN_MESSAGE, null, shapes.toArray(),shapes.toArray()[0]);

            if(shape != null)
            {
               setActiveShape(shape);
            }
        });

        // Change Active Shape Color Button
        // Changes the Color of the active shape
        initButton(iter++, "Change Shape Color", () -> {
            Color color = JColorChooser.showDialog(ShapeManager.this,"Choose the New Shape Color", activeShape.getColor());

            // Because setting a color does not throw a NullPointerException if the Color is null, this checks whether the user actually made a color selection
            if (color != null)
            {
                editShapeColor(activeShape, color);
            }
        });

    }

    // Default Constructor
    // Initializes the ShapeManager with a default shape as its active shape
    public ShapeManager()
    {
        setLayout(null);
        setBackground(new Color(50,50,50));

        activeShape = new Rectangle(100);
        activeShape.setColor(new Color(250, 150, 100));
        addShape(activeShape);


        initButtons();
    }

    // repositions and resizes the UI buttons
    private void repositionAndResizeButtons(int xOffset, int yOffset, int widthOffset, int heightOffset)
    {
        xUI += xOffset;
        yUI += yOffset;

        for (JButton button : buttonMap.values())
        {
            button.setBounds(button.getX() + xOffset, button.getY() + yOffset, button.getWidth() + widthOffset, button.getHeight() + heightOffset);
        }
    }

    // repositions the data under the shapes
    private void repositionData(int xOffset, int yOffset, int horizontalOffset, int verticalOffset)
    {
        this.dataX += xOffset;
        this.dataY += yOffset;
        this.horizontalDataGap += horizontalOffset;
        this.verticalDataGap += verticalOffset;
    }

    // repositions both the buttons and the data
    private void repositionAndResizeUI(int xOffset, int yOffset, int widthOffset, int heightOffset, int dataXOffset, int dataYOffset, int horizontalOffset, int verticalOffset)
    {
        repositionAndResizeButtons(xOffset,yOffset,widthOffset,heightOffset);
        repositionData(dataXOffset,dataYOffset,horizontalOffset,verticalOffset);
    }

    // Overrides, paintComponent in JPanel, which is a necessary formality for the ShapeManager to display anything.
    public void paintComponent(Graphics g)
    {
        // also a formality
        super.paintComponent(g);

        // Sets the color of the graphic object used to display the shape and data to the color of the active shape
        g.setColor(activeShape.getColor());

        // Draws the name of the Shape
        g.drawString(activeShape.getName(),nameX,nameY);

        // Displays the active shape and its data according to what type of shape it is
        if (activeShape instanceof Rectangle) {
            g.fillRect(shapeX, shapeY, Math.round(((Rectangle) activeShape).getWidth()), Math.round(((Rectangle) activeShape).getHeight()));
            g.drawString("Width: " + Math.round(((Rectangle) activeShape).getWidth() * Math.pow(10, precision)) / Math.pow(10 , precision), dataX, dataY);
            g.drawString("Height: " + Math.round(((Rectangle) activeShape).getHeight() * Math.pow(10, precision)) / Math.pow(10 , precision), dataX + horizontalDataGap, dataY);
            g.drawString("Diagonal: " + Math.round(((Rectangle) activeShape).getDiagonal() * Math.pow(10, precision)) / Math.pow(10, precision), dataX, dataY + verticalDataGap);
            g.drawString("Area: " + Math.round(activeShape.getArea() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY + verticalDataGap);
            g.drawString("Perimeter: " + Math.round(activeShape.getPerimeter() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY + verticalDataGap * 2);
        }
        else if (activeShape instanceof RightTriangle)
        {
            g.fillPolygon(new int[]{shapeX, shapeX, shapeX + Math.round(((RightTriangle) activeShape).getWidth())},
                    new int[]{shapeY,shapeY + Math.round(((RightTriangle) activeShape).getHeight()),shapeY + Math.round(((RightTriangle) activeShape).getHeight())}, 3);
            g.drawString("Width: " + Math.round(((RightTriangle) activeShape).getWidth() * Math.pow(10, precision) / Math.pow(10, precision)), dataX, dataY);
            g.drawString("Height: " +  Math.round(((RightTriangle) activeShape).getHeight() * Math.pow(10, precision) / Math.pow(10, precision)), dataX + horizontalDataGap , dataY);
            g.drawString("Hypotenuse: " + Math.round(((RightTriangle) activeShape).getHypotenuse() * Math.pow(10, precision)) / Math.pow(10, precision), dataX, dataY + verticalDataGap);
            g.drawString("Area: " + Math.round(activeShape.getArea() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY + verticalDataGap);
            g.drawString("Perimeter: " + Math.round(activeShape.getPerimeter() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY + verticalDataGap * 2);
        }
        else if (activeShape instanceof Circle)
        {
            g.fillOval(shapeX,shapeY, Math.round(((Circle) activeShape).getRadius() * 2), Math.round(((Circle) activeShape).getRadius() * 2));
            g.drawString("Radius: " + Math.round(((Circle) activeShape).getRadius() * Math.pow(10, precision)) / Math.pow(10, precision), dataX, dataY);
            g.drawString("Area: " + Math.round(activeShape.getArea() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY);
            g.drawString("Perimeter: " + Math.round(activeShape.getPerimeter() * Math.pow(10, precision)) / Math.pow(10, precision), dataX + horizontalDataGap, dataY + verticalDataGap);
        }

    }

    // An enumeration containing the names of the numeric parameters that shapes may have
    public enum Parameter
    {
        // For Rectangles and RightTriangles
        Width, Height,
        // For Circles
        Radius
    }

    // the shapes ArrayList, buttonMap HashMap and the ActiveShape, used frequently throughout the class
    private static ArrayList<Shape> shapes = new ArrayList<Shape>();
    private HashMap<String, JButton> buttonMap = new HashMap<String, JButton>();
    private static Shape activeShape;

    // returns an ArrayList of the current shapes
    public static ArrayList<Shape> getShapes()
    {
        return shapes;
    }

    // returns the number of shapes that have been made thus far
    public static int getShapesMade() {return shapesMade;}

    // returns the Active Shape
    public static void setActiveShape(Shape shape)
    {
        activeShape = shape;
    }

    // Gets a shape from shapes ArrayList using its name
    public static Shape getShape(String name) throws IllegalArgumentException
    {
        for (Shape shape: shapes)
        {
            if (shape.getName().equals(name))
            {
                return shape;
            }
        }

        throw new IllegalArgumentException("Your collection of Shapes contains no shape named " + name + ".");
    }

    // Gets a shape form the shapes ArrayList using its index therein
    public static Shape getShape(int index) throws IndexOutOfBoundsException
    {
        if (index > 0 && index < shapes.size())
        {
            return shapes.get(index);
        }

        throw new IndexOutOfBoundsException("Index " + index + "is out of the bounds of your collection.");
    }

    // Adds a shape to the shape's ArrayList, but only if it is a unique shape
    private static boolean addShape(Shape shape)
    {
        if(!checkDupeShape(shape) && shape != null)
        {
            shapes.add(shape);
            shapesMade++;
            return true;
        }

        return false;
    }

    // Removes a shape from the shapes ArrayList
    private static boolean deleteShape(Shape shape)
    {
        if(checkDupeShape(shape))
        {
            shapes.remove(shape);
            return true;
        }

        return false;
    }

    // Edits a parameter of a shape while also checking whether that parameter exists for that shape
    private static void editShapeParameter(Shape shape, Parameter parameter, float newVal) throws IllegalArgumentException
    {
        if (shape instanceof Circle)
        {
            switch (parameter)
            {
                case Height:
                    throw new IllegalArgumentException("Circles have no " + parameter + " parameter.");
                case Width:
                    throw new IllegalArgumentException("Circles have no " + parameter + " parameter.");
                case Radius:
                    ((Circle) shape).setRadius(newVal);
                    break;
            }
        }
        else if (shape instanceof Rectangle)
        {
            switch (parameter)
            {
                case Height:
                    ((Rectangle) shape).setHeight(newVal);
                    break;
                case Width:
                    ((Rectangle) shape).setWidth(newVal);
                    break;
                case Radius:
                    throw new IllegalArgumentException("Rectangles have no " + parameter + " parameter.");
            }
        }
        else if (shape instanceof RightTriangle)
        {
            switch (parameter)
            {
                case Height:
                    ((RightTriangle) shape).setHeight(newVal);
                    break;
                case Width:
                    ((RightTriangle) shape).setWidth(newVal);
                    break;
                case Radius:
                    throw new IllegalArgumentException("RightTriangles have no " + parameter + " parameter.");
            }
        }
    }

    // Edits the name of the passed shape
    private static boolean editShapeName(Shape shape, String name)
    {
        return shape.setName(name);
    }

    // edits the color of the passed shape
    private static void editShapeColor(Shape shape, Color color)
    {
        shape.setColor(color);
    }

    // checks whether the shape already exists in the shapes ArrayList
    private static boolean checkDupeShape(Shape shape)
    {
        for (Shape compareShape: shapes)
        {
            if (shape.compareTo(compareShape) == 0)
            {
                return true;
            }
        }

        return false;
    }

    // returns the size of the shapes ArrayList
    public static int numShapes()
    {
        return shapes.size();
    }



}
