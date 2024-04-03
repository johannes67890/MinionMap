package util;

/**
 * Helper class containing various mathematical methods.
 */
public final class MathUtil {

    /**
     * 
     * Clamps a value by ensuring, that it does not go below min, and above max.
     * 
     * @param val - The value to check
     * @param min - The minimum value
     * @param max - the maximum value
     * @return
     */
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
    
}
