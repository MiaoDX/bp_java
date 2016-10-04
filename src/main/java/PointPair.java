/**
 * Created by miao on 2016/10/4.
 */
public class PointPair {
    private Point leftPoint;
    private Point rightPoint;
    private double value;


    PointPair(Point leftPoint, Point rightPoint){
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
        this.value = 0;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Point getRightPoint() {
        return rightPoint;
    }

    public Point getLeftPoint() {
        return leftPoint;
    }


    @Override
    public String toString(){
        return leftPoint + "," + rightPoint + "\n";
    }
}
