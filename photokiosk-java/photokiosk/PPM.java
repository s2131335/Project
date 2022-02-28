package photokiosk;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PPM {
  // instance fields
  private String imageName;
  private int width, height;
  private int maxValue;
  private Color[][] image;

  // Default constructor for creating an blank PPM image of 2 x 3
  // provided for reference, NEED NOT be modified
  public PPM() {
    imageName = "Simple";
    width = 2;
    height = 3;
    maxValue = 255;
    image = new Color[height][width];
    image[0][0] = new Color(0, 128, 255);

    int r = image[0][0].getRed();
    int g = image[0][0].getGreen();
    int b = image[0][0].getBlue();

    image[0][1] = new Color(r, g + 127, b - 128);
    image[1][0] = new Color(128, g, 128);
    image[1][1] = new Color(255, 0, 255);
    image[2][0] = new Color(255, 255, 255);
    image[2][1] = new Color(0, 0, 0);
  }

  // Constructor for creating an "orange" PPM image of width x height
  // All pixels shall carry Color(255, 165, 0) in RGB
  public PPM(String name, int w, int h) 
  {
      imageName = name;
      height = h;
      width = w;
      maxValue = 255;
      image = new Color [height][width];
      for (int i = 0; i < height; i++)
      {
          for (int j = 0; j < width; j++)
          {
              image[i][j] = new Color (255, 165, 0);
          }
      }
  }

  // Constructor for reading a PPM image file
  public PPM(String filename) 
  {
    imageName = filename;
    try{
    read(filename);
    }
    catch (Exception e){
        System.err.println(e);
    }
  }

  public int getWidth() 
  {
    return width;
  }

  public int getHeight() 
  {
    return height;
  }

  public Color[][] getImage() 
  {
    return image;
  }
  // Show image on screen
  // given and NEED NOT be modified
  public void showImage() {
    if (getHeight() <= 0 || getWidth() <= 0 || image == null) {
      JOptionPane.showConfirmDialog(null, "Width x Height = " + getWidth() + "x" + getHeight(), imageName + " corrupted!", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null);
      return;
    }

    BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

    for (int row = 0; row < getHeight(); row++) {
      for (int col = 0; col < getWidth(); col++) {
        img.setRGB(col, row, image[row][col].getRGB());
      }
    }

    JOptionPane.showConfirmDialog(null, "Width x Height = " + getWidth() + "x" + getHeight(), imageName, JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img));
  } 
  
  public PPM grayscale() {
      int Y;
      PPM newImage = new PPM("Grayscale",width,height);
      for (int i = 0; i < height; i++)
      {
          for (int j = 0; j < width; j++)
          {
              Y = (int)(image[i][j].getRed()*0.257 + image[i][j].getGreen()*0.504 + image[i][j].getBlue() * 0.098 +16);
              newImage.image[i][j] = new Color (Y, Y, Y);
          }
      }
      return newImage;
  }
  
  public PPM saturate() {
      PPM newImage = new PPM("Saturate",width,height);
      double Y, U, V;
      int R, G, B;
      for (int i = 0; i < height; i++)
      {
          for (int j = 0; j < width; j++)
          {
              Y = ((0.257 * image[i][j].getRed()) + (0.504 * image[i][j].getGreen()) + (0.098 * image[i][j].getBlue())) + 16;    //RGB to YUV
              U = (-(0.148 * image[i][j].getRed()) - (0.291 * image[i][j].getGreen()) + (0.439 * image[i][j].getBlue()))*2 + 128;
              V = ((0.439 * image[i][j].getRed()) - (0.368 * image[i][j].getGreen()) - (0.071 * image[i][j].getBlue()))*2 + 128;
              
              R = (int)(1.164 * (Y-16) + 1.596 * (V - 128));      // YUV to RGB
              G = (int)(1.164 * (Y-16) -0.813 * (V - 128) -0.391 * (U - 128));
              B = (int)(1.164 * (Y-16) + 2.018 * (U - 128));
              
              R = Math.min(Math.max(R,0), 255);   // Clamp RGB value
              G = Math.min(Math.max(G,0), 255);
              B = Math.min(Math.max(B,0), 255);
              
              newImage.image[i][j] = new Color(R,G,B);
          }
      }
      return newImage;
  }

  public boolean read(String filename) {
    try {
      File f = new File(filename);
      Scanner reader = new Scanner(f);
      String header = reader.nextLine();
      if (header == null || header.length() < 2 || header.charAt(0) != 'P' || header.charAt(1) != '3') {
        throw new Exception("Wrong PPM header!");
      }
      
      do { // skip lines (if any) start with '#'
        header = reader.nextLine();
      } while (header.charAt(0) == '#');

      Scanner readStr = new Scanner(header);  // get w, h from last line scanned instead of file
      width = readStr.nextInt();
      height = readStr.nextInt();
      maxValue = reader.nextInt();  // get the rest from file      

      System.out.println("Reading PPM image of size " + width + "x" + height);
      // Write your code here
      image = new Color[height][width];
  
      for (int i = 0; i < height; i++)
      {
          for (int j = 0; j < width; j++)
          {
              int R = reader.nextInt();        // Scan RGB value in a group of three
              int G = reader.nextInt();
              int B = reader.nextInt();
              image[i][j] = new Color (R, G, B);
          }
      }
      reader.close();
    } catch (Exception e) {
      System.err.println(e);
      image = null;
      width = -1;
      height = -1;
      return false;
    }
    return true;
  }

  public void write(String filename) {
    PrintStream ps;
    String content = "";
    try {
      ps = new PrintStream(filename);
      ps.println("P3\n" + width +" "+ height + "\n" + maxValue);
      
      for (int i = 0; i < height; i++) // build the image
      {
          for (int j = 0; j < width; j++)
          {
              content += image[i][j].getRed() +" "+ image[i][j].getGreen() +" "+ image[i][j].getBlue() +" ";
          }
          content += "\n";
      }
      ps.print(content);
      ps.close();
     
    } catch (Exception e) {
      System.err.println(e);
    }
  }

}