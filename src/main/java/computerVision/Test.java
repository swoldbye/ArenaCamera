package computerVision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;


class DetectFaceDemo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public void run() {
        System.out.println("\nRunning DetectFaceDemo");

        //Load Image
        String file = "C:/Users/markm/IdeaProjects/ArenaCamera/src/computerVision/resources/Biggie_Smalls1.jpg";
        Mat image = Imgcodecs.imread(file);

        // Create a face detector from the cascade file in the resources
        // directory.
        String xmlFile = "C:/Users/markm/IdeaProjects/ArenaCamera/src/computerVision/resources/lbpcascade_frontalface.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(xmlFile);



        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);


        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = "faceDetection.png";
        String path = "C:/Users/markm/IdeaProjects/ArenaCamera/src/output/";
        System.out.println(String.format("Writing %s", filename));
        String pathAndName = path + filename;
        Imgcodecs.imwrite(pathAndName, image);
    }
}


public class Test {
    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // new DetectFaceDemo().run();
        CalculateBallDistance webcam = new CalculateBallDistance();
        webcam.start();
    }
}