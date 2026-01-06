package ensisa.tp.model;

public class Point {
    double x;
    double y;
    Point(double _x, double _y){
        this.x=_x;
        this.y=_y;
    }

    public double getX(){
        return this.x;
    }
    public  void setY(double _y){
        this.y = _y;
    }

    public double getY(){
        return this.y;
    }

    public  void setX(double _x){
         this.x = _x;
    }

}
