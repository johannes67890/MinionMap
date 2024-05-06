package gui;

/**
 * The ControllerInterface interface represents the interface for a controller in the application.
 * It defines the method for starting the controller with a specified view.
 */
public interface ControllerInterface {
    /**
     * Starts the controller with the specified view.
     *
     * @param view the view to be associated with the controller
     */
    public void start(View view);
}
