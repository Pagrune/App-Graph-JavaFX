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

    @FXML private Canvas canvas1;
    @FXML private Canvas canvas2;
    @FXML private Canvas canvas3;

    private Courbe courbe1 = new Courbe();
    private Courbe courbe2 = new Courbe();
    private Courbe courbe3 = new Courbe();

    private Canvas canvasActif;
    private Courbe courbeActive;
    private int point_section = -1;

    private static final double taille_point = 6.0;

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        refresh_affichage(courbe1, canvas1);
        refresh_affichage(courbe2, canvas2);
        refresh_affichage(courbe3, canvas3);
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        canvasActif = (Canvas) event.getSource();

        if (canvasActif == canvas1) courbeActive = courbe1;
        else if (canvasActif == canvas2) courbeActive = courbe2;
        else if (canvasActif == canvas3) courbeActive = courbe3;
        else return;

        double mouseX = event.getX();
        double mouseY = event.getY();

        for (int i = 0; i < courbeActive.getPoints().size(); i++) {
            double px = courbeActive.getPoints().get(i).getX();
            double py = courbeActive.getPoints().get(i).getY();

            if (Math.abs(mouseX - px) < taille_point &&
                    Math.abs(mouseY - py) < taille_point) {
                point_section = i;
                return;
            }
        }
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        if (point_section == -1 || courbeActive == null || canvasActif == null)
            return;

        double newY = couper_si_depasse(event.getY(), 0, 265);
        courbeActive.getPoints().get(point_section).setY(newY);

        refresh_affichage(courbeActive, canvasActif);
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        point_section = -1;
        canvasActif = null;
        courbeActive = null;
    }

    private void refresh_affichage(Courbe c, Canvas _canva) {
        GraphicsContext canva = _canva.getGraphicsContext2D();

        canva.clearRect(0, 0, _canva.getWidth(), _canva.getHeight());

        dessine_axis(canva, 10, 10, 10, 265);
        dessine_axis(canva, 10, 265, 265, 265);

        dessine_courbe(canva, c);

        courbeActive = c;
        dessine_point(canva);
        courbeActive = null;
    }

    private void dessine_courbe(GraphicsContext canva, Courbe courbe) {
        double y_preced = courbe.evaluate(0);

        for (int x = 11; x <= 265; x++) {
            double y = couper_si_depasse(courbe.evaluate(x), 0, 265);
            canva.strokeLine(x - 1, y_preced, x, y);
            y_preced = y;
        }
    }

    private void dessine_point(GraphicsContext canva) {
        for (Point p : courbeActive.getPoints()) {
            canva.fillOval(p.getX(), p.getY(), taille_point, taille_point);
        }
    }

    private void dessine_axis(GraphicsContext canva,
                              double x1, double x2,
                              double y1, double y2) {
        canva.strokeLine(x1, y1, x2, y2);
    }

    private double couper_si_depasse(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
