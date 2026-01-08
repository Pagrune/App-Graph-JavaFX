    package ensisa.tp;

    import ensisa.tp.model.Courbe;
    import ensisa.tp.model.ImageFilter;
    import ensisa.tp.model.Point;
    import javafx.fxml.FXML;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.image.WritableImage;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.paint.Color;
    import javafx.stage.FileChooser;

    import java.io.File;

    public class MainController {

        @FXML
        private Label welcomeText;

        private Image originalImage;

        private ImageFilter imageFilter = new ImageFilter();
        @FXML private Canvas canvas1;
        @FXML private Canvas canvas2;
        @FXML private Canvas canvas3;
        @FXML private ImageView image;

        private Courbe courbe1 = new Courbe(Color.RED);
        private Courbe courbe2 = new Courbe(Color.GREEN);
        private Courbe courbe3 = new Courbe(Color.BLUE);

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

            double newY = couper_si_depasse(event.getY(), 10, 265);
            courbeActive.getPoints().get(point_section).setY(newY);

            refresh_affichage(courbeActive, canvasActif);
        }

        @FXML
        private void openImage() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir une image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images JPEG", "*.jpg", "*.jpeg")
            );

            File selectedFile = fileChooser.showOpenDialog(canvas1.getScene().getWindow());

            if (selectedFile != null) {
                originalImage = new Image(selectedFile.toURI().toString());
                image.setImage(imageFilter.filter(originalImage, courbe1, courbe2, courbe3));
            }
        }

        @FXML
        private void onMouseReleased(MouseEvent event) {
            if (originalImage != null) {
                WritableImage filtered =
                        imageFilter.filter(originalImage, courbe1, courbe2, courbe3);
                image.setImage(filtered);
            }

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
            dessine_point(canva, c);
        }

        private void dessine_courbe(GraphicsContext canva, Courbe courbe) {
            canva.setStroke(courbe.color);
            double y_preced = couper_si_depasse(courbe.evaluate(10), 10, 265);

            for (int x = 11; x <= 265; x++) {
                double y = couper_si_depasse(courbe.evaluate(x), 10, 265);
                canva.strokeLine(x - 1, y_preced, x, y);
                y_preced = y;
            }
            canva.setStroke(Color.BLACK);

        }

        private void linearisation_courbe(){
            courbe1.linear_points();
            courbe2.linear_points();
            courbe3.linear_points();
        }

        private void dessine_point(GraphicsContext canva, Courbe courbe) {
            for (Point p : courbe.getPoints()) {
                canva.fillOval(p.getX()-(taille_point/2), p.getY()-(taille_point/2), taille_point, taille_point);
            }
        }

        private void dessine_axis(GraphicsContext canva,
                                  double x1, double x2,
                                  double y1, double y2) {
            canva.strokeLine(x1, y1, x2, y2);
        }

        public double couper_si_depasse(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }
    }
