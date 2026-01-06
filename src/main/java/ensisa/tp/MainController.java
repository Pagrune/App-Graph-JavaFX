package ensisa.tp;

import ensisa.tp.model.Courbe;
import ensisa.tp.model.Point;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MainController {
    @FXML
    private Label welcomeText;

    private Courbe courbe = new Courbe();

    @FXML
    private Canvas canvas;
    private int point_section = -1;

    private static final double taille_point = 6.0;

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        refresh_affichage();
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        for (int i = 0; i < courbe.getPoints().size(); i++) {
            double px = courbe.getPoints().get(i).getX();
            double py = courbe.getPoints().get(i).getY();

            if (Math.abs(mouseX - px) < taille_point &&
                    Math.abs(mouseY - py) < taille_point) {
                point_section = i;
                return;
            }
        }
    }



    @FXML
    private void onMouseDragged(MouseEvent event) {
        if (point_section == -1) return;

        double newY = couper_si_depasse(event.getY(), 0,265);
        courbe.getPoints().get(point_section).setY(newY);
        refresh_affichage();
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        point_section = -1;
    }

    private void refresh_affichage(){
        //je réupère le canvas 2d
        GraphicsContext canva = canvas.getGraphicsContext2D();

        // je le vide completement
        canva.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //axe x
        //axe y
        dessine_axis(canva, 10, 10, 10, 265);
        dessine_axis(canva, 10, 265, 265, 265);
        dessine_courbe(canva);
        dessine_point(canva);
    }

    private void dessine_courbe(GraphicsContext canva) {
        double baselineY = 265;

        double y_preced = baselineY- courbe.evaluate(0);
        for (int x = 11; x <= 265; x++) {
            double y = courbe.evaluate(x);
            y = Math.max(y,couper_si_depasse(y, 0,265));
            if(y > baselineY) y = 0;
            if(y < 10) y=10;
            canva.strokeLine(x - 1, y_preced, x, y);
            y_preced = y;
        }
    }

    private void dessine_point(GraphicsContext canva){
        for (Point p : courbe.getPoints()){
            canva.fillOval(p.getX(), p.getY(), taille_point, taille_point);
        }
    }

    private void dessine_axis(GraphicsContext canva, double x1,double  x2,double  y1,double  y2){
        canva.strokeLine(x1, y1, x2, y2);
    }


    private double couper_si_depasse(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}
