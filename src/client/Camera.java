package client;

import javafx.scene.canvas.Canvas;

public class Camera {
    private final double maxZoom = 5;
    private final double minZoom = 0.5;
    private double scaleX = 1;
    private double scaleY = 1;
    private double zoomInFactor = 1.1;
    private double zoomOutFactor = 0.9;

    public void cameraZoomIn(Canvas canvas) {
        this.scaleX = clamp(this.scaleX * zoomInFactor, minZoom, maxZoom);
        this.scaleY = clamp(this.scaleY * zoomInFactor, minZoom, maxZoom);

        canvas.setScaleX(this.scaleX);
        canvas.setScaleY(this.scaleY);
    }

    public void cameraZoomOut(Canvas canvas) {
        this.scaleX = clamp(this.scaleX * zoomOutFactor, minZoom, maxZoom);
        this.scaleY = clamp(this.scaleY * zoomOutFactor, minZoom, maxZoom);

        canvas.setScaleX(this.scaleX);
        canvas.setScaleY(this.scaleY);
    }

    public double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0) return min;

        if (Double.compare(value, max) > 0) return max;

        return value;
    }
}