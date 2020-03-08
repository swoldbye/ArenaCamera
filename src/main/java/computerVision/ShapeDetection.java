package computerVision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class ShapeDetection {
//  source code credit goes to
    // https://docs.opencv.org/master/d4/d70/tutorial_hough_circle.html
    // https://laxmaredy.blogspot.com/2014/06/blog-post_6263.html
    // https://docs.opencv.org/master/javadoc/org/opencv/imgproc/Imgproc.html#HoughCircles(org.opencv.core.Mat,org.opencv.core.Mat,int,double,double,double)

    static ArrayList<Integer> radii = new ArrayList<>();


    public void initializeVision(){
        // TODO : Call the webcam and take a new picture (controllers job?)
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Load the image that is taken by the webcam (currently just a test image)
        String filename ="./src/main/java/computerVision/resources/balls.jpeg";
        Mat image = imread(filename,Imgcodecs.IMREAD_COLOR);

        processImg(image);
    }

    public void processImg(Mat src){
        Mat gray = new Mat();

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat circles = new Mat();

        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 1, 50); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles
        System.out.println("COL 1: "+ circles.col(1)+" And COL 2:"+circles.col(2));
        countCircles(src,gray,circles);

    }

    public void countCircles(Mat src, Mat gray, Mat circles){
        int count = 0;
        for (int x = 0; x < circles.cols(); x++) {
            count++;
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            radii.add(radius);
            System.out.println("Radius of ball "+count+": "+radius);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }
        saveProcessedImage(src);
    }

    public void saveProcessedImage(Mat src){
        // Create the destination file
        String filename = "shapewack123.png";
        String path = "./src/output/";
        String pathAndName = path + filename;
        Imgcodecs.imwrite(pathAndName, src);
        //HighGui.imshow("detected circles", src);
        // HighGui.waitKey();
        // System.exit(0);
    }

    public ArrayList<Integer> getRadius(){
        return radii;
    }
    }