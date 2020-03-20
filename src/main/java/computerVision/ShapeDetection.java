package computerVision;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class ShapeDetection {
//  source code credit goes to
    // https://docs.opencv.org/master/d4/d70/tutorial_hough_circle.html
    // https://laxmaredy.blogspot.com/2014/06/blog-post_6263.html
    // https://docs.opencv.org/master/javadoc/org/opencv/imgproc/Imgproc.html#HoughCircles(org.opencv.core.Mat,org.opencv.core.Mat,int,double,double,double)


    // Rectangle src code: http://androiderstuffs.blogspot.com/2016/06/detecting-rectangle-using-opencv-java.html

    static ArrayList<Integer> radii = new ArrayList<>();


    public void initializeVision(){
        // TODO : Call the webcam and take a new picture (controllers job?)
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Load the image that is taken by the webcam (currently just a test image)
        //String filename ="./src/main/java/computerVision/resources/solitaire_digital_board.png";
        String filename ="./src/main/java/computerVision/resources/shapeTest.png";

        Mat image = imread(filename,Imgcodecs.IMREAD_COLOR);

        try {
            findRectangle(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void wack(Mat src,  List<MatOfPoint> contours,int maxId){
        Rect rect = Imgproc.boundingRect(contours.get(maxId));
        Imgproc.rectangle(src, rect.tl(), rect.br(), new Scalar(255, 0, 0,.8), 2);
        saveProcessedImage(src);

    }

    private void findRectangle(Mat src) throws Exception {
        Mat blurred = src.clone();
        Imgproc.medianBlur(src, blurred, 9);

        Mat gray0 = new Mat(blurred.size(), CvType.CV_8U), gray = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();

        List<Mat> blurredChannel = new ArrayList<>();
        blurredChannel.add(blurred);
        List<Mat> gray0Channel = new ArrayList<>();
        gray0Channel.add(gray0);

        MatOfPoint2f approxCurve;

        double maxArea = 0;
        int maxId = -1;

        for (int c = 0; c < 3; c++) {
            int ch[] = { c, 0 };
            Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));

            int thresholdLevel = 1;
            for (int t = 0; t < thresholdLevel; t++) {
                if (t == 0) {
                    Imgproc.Canny(gray0, gray, 10, 20, 3, true); // true ?
                    Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1); // 1
                    // ?
                } else {
                    Imgproc.adaptiveThreshold(gray0, gray, thresholdLevel,
                            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                            Imgproc.THRESH_BINARY,
                            (src.width() + src.height()) / 200, t);
                }

                Imgproc.findContours(gray, contours, new Mat(),
                        Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
                for (MatOfPoint contour : contours) {
                    MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

                    double area = Imgproc.contourArea(contour);
                    approxCurve = new MatOfPoint2f();
                    Imgproc.approxPolyDP(temp, approxCurve,
                            Imgproc.arcLength(temp, true) * 0.02, true);

                    if (approxCurve.total() == 4 && area >= maxArea) {
                        System.out.println("found a card");
                        double maxCosine = 0;

                        List<Point> curves = approxCurve.toList();
                        for (int j = 2; j < 5; j++) {

                            double cosine = Math.abs(angle(curves.get(j % 4),
                                    curves.get(j - 2), curves.get(j - 1)));
                            maxCosine = Math.max(maxCosine, cosine);
                        }

                        if (maxCosine < 0.3) {
                            maxArea = area;
                            maxId = contours.indexOf(contour);
                        }
                    }
                }
            }
        }
        if (maxId >= 0) {
            Imgproc.drawContours(src, contours, maxId, new Scalar(255, 0, 0,
                    .8), 8);
            wack(src,contours,maxId);
        }
    }

    private double angle(Point p1, Point p2, Point p0) {
        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        return (dx1 * dx2 + dy1 * dy2)
                / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
                + 1e-10);
    }

    public void saveProcessedImage(Mat src){
        // Create the destination file
        String filename = "cardDetected.png";
        String path = "./src/output/";
        String pathAndName = path + filename;
        Imgcodecs.imwrite(pathAndName, src);
        HighGui.imshow("detected cards", src);
        HighGui.waitKey();
        System.exit(0);
    }
    }