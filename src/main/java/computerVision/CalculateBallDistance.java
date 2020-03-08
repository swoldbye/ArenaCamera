package computerVision;

import java.io.IOException;
import java.util.ArrayList;
// Help and guides:
// https://www.techjini.com/blog/calculate-the-distance-to-object-from-camera/

public class CalculateBallDistance {
    static final double CAMERA_WIDTH = 1080; // pixels
    static final double FIELD_OF_VIEW = 78; // degrees
    static final double DEG_PER_PIXEL = FIELD_OF_VIEW/CAMERA_WIDTH;

    int ballDiameter = 40; // mm
    double pixelSize = 0.0000020; // 20 microns size
    int webcamFocalLength = 35; // mm
    int cameraHeight = 1080; // pixels
    double sensorHeight = 3.6;

    public void start() throws IOException {
        // takePicture();
        ShapeDetection getShapes = new ShapeDetection();
        getShapes.initializeVision();
        System.out.println("NJDSANDSANKDAS");
        calculateDistance();
    }

    public void calculateDistance(){
        ShapeDetection radius = new ShapeDetection();
        ArrayList<Integer> imageHeights = new ArrayList<>();
        imageHeights = radius.getRadius(); // pixels

        ArrayList<Double> distances = new ArrayList<>();
        int numberOfBalls = imageHeights.size();
        for(int i=0; i<numberOfBalls;i++){

            double distance =
                    (webcamFocalLength*imageHeights.get(i)*cameraHeight)/
                            (imageHeights.get(i)*sensorHeight);
            distances.add(distance);
            System.out.println("Distance to ball "+i+" is: "+distance);
        }


        // distance =
        // focal length * real diameter * camera frame height * image height * sensor height


    }
/*



    public void takePicture() throws IOException {
        // get default webcam and open it
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        // get image
        BufferedImage image = webcam.getImage();
        // save image to PNG file
            ImageIO.write(image, "PNG", new File("./src/main/java/computerVision/resources/WebcamPic.png"));

    }
 */



}
