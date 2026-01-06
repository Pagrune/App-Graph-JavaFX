package ensisa.tp.model;

import java.util.ArrayList;
import java.util.List;

public class Courbe {

    List<Point> points = new ArrayList<Point>();
    public Courbe(){
        double ecart = 255/4;
        for(double i =1; i<5 ; i++){
            points.add(new Point( i*ecart,262));
        }
    }

    public List<Point> getPoints(){
        return this.points;
    }


    //lagrange pris sur google
    public double evaluate(double x) {
        double result = 0.0;
        int n = getPoints().size();

        for (int i = 0; i < n; i++) {
            result += getPoints().get(i).getY() * lagrangeBasis(i, x);
        }

        return result;
    }

    private double lagrangeBasis(int i, double x) {
        double li = 1.0;
        double xi = getPoints().get(i).getX();

        for (int j = 0; j < getPoints().size(); j++) {
            if (j == i) continue;

            double xj = getPoints().get(j).getX();
            li *= (x - xj) / (xi - xj);
        }

        return li;
    }
}
