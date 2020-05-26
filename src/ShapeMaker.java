import javax.swing.*;
import java.awt.*;

/* ShapeMaker.java
*  Driving class for the ShapeMaker program. Initializes a JFrame object that contains the ShapeManager which in turn
*  manages the program's other functionality. */

public class ShapeMaker
{
    // Default Width and Height of the window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Initializes a JFrame for the program with an instance of ShapeManager, which inherits from JPanel
    private static JFrame initFrame()
    {
        JFrame frame = new JFrame("ShapeMaker");
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ShapeManager());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return frame;
    }

    public static void main(String[] args)
    {
        JFrame frame = initFrame();
    }

}
