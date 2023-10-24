public class Vector2 {
    private final double EPSILON = 1E-5;
    private final double PI2 = 2 * Math.PI;

    double x = 0, y = 0;
    double magnitude = 0, theta = 0;

    public Vector2(){}

    static Vector2 ofMagnitudeAndAngle(double magnitude, double theta){
        Vector2 v = new Vector2();
        v.magnitude = magnitude;
        v.theta = theta;
        return v;
    }
    static Vector2 ofXAndYCoordinates(double x, double y){
        Vector2 v = new Vector2();
        v.x = x;
        v.y = y;
        return v;
    }

    public void changeMagnitudeBy(double v){
        magnitude += v;
        updateCartesian();
    }
    public void changeThetaBy(double v){
        theta += v;
        updateCartesian();
    }

    public void subtract(Vector2 v){
        x -= v.x;
        y -= v.y;
        updatePolar();
    }
    public void add(Vector2 v){
        x += v.x;
        y += v.y;
        updatePolar();
    }

    public void subtract(double v){
        x -= v;
        y -= v;
        updatePolar();
    }
    public void add(double v){
        x += v;
        y += v;
        updatePolar();
    }
    public void multiplyMagnitude(double v){
        magnitude *= v;
        updateCartesian();
    }


    private void updateCartesian(){
        this.x = magnitude * Math.cos(theta);
        this.y = magnitude * Math.sin(theta);
    }
    private void updatePolar(){
        this.theta = Math.atan2(y, x);
        this.magnitude = Math.sqrt((x*x)+(y*y));
    }

    public void updateVals(){
        if(Math.abs(this.magnitude) > 0 && Math.abs(this.magnitude) < EPSILON){
            this.magnitude = 0;
        }

        if(theta < 0) {
            theta += PI2;
        }else if (theta > PI2) {
            theta -= PI2;
        }

        if(Math.abs(this.y) > 0 && Math.abs(this.y) < EPSILON){
            this.y = 0;
        }
        if(Math.abs(this.x) > 0 && Math.abs(this.x) < EPSILON){
            this.x = 0;
        }
    }
}
