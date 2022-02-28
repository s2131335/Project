package photokiosk;
  public class PhotoKiosk {

  public static void main(String[] args) {
    String filename;

    // Given code
    PPM imgDefault;
    imgDefault = new PPM();
    imgDefault.showImage();

    PPM imgBlank;
    imgBlank = new PPM("Orange", 40, 30);
    imgBlank.showImage();

    filename = "WRONG_FILENAME.ppm";
    PPM imgFileCorrupted;
    imgFileCorrupted = new PPM(filename);
    imgFileCorrupted.showImage();

    filename = "photokiosk/rgb_30x25.ppm";
    PPM imgFileSmall;
    imgFileSmall = new PPM(filename);
    imgFileSmall.showImage();

    filename = "photokiosk/peacock_256.ppm";
    PPM imgFile2;
    imgFile2 = new PPM(filename);
    imgFile2.showImage();

    PPM result = imgFile2.grayscale();
    result.showImage();
    result.write("grayscale.ppm");

    PPM result2 = imgFile2.saturate();
    result2.showImage();
    result2.write("saturate.ppm");
  }
}
