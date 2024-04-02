package util;

import java.lang.Math;

import parser.TagNode;
public class MecatorProjection {
  
        private static final double RADIUS = 6378137.0; /* in meters on the equator */

        /**
         * Projects a node to the mercator projection.
         * This takes the {@link TagNode} and turns lat and lon into x and y.
         * <p>
         * The x and y are in meters.
         * </p>
         * @param node The node to project
         * @return The projected node
         */
        public static TagNode project(TagNode node){
            return new TagNode(
                node.getId(),
                lat2y(node.getLatDouble()),
                lon2x(node.getLonDouble())
            );
        }

        /**
         * Unprojects a node from the mercator projection.
         * This takes the {@link TagNode} and turns x and y into lat and lon.
         * <p>
         * The lat and lon is in degrees.
         * </p>
         * @param node The node to unproject
         * @return The unprojected node
         */
        public static TagNode unproject(TagNode node){
            return new TagNode(
                node.getId(),
                y2lat(node.getLatDouble()),
                x2lon(node.getLonDouble())
            );
        }

        // Projection
        /* These functions take their angle parameter in degrees and return a length in meters */
        public static double lat2y(double aLat) {
            return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS;
        }  
        public static double lon2x(double aLong) {
            return Math.toRadians(aLong) * RADIUS;
        }

        // Unprojection
        /* These functions take their length parameter in meters and return an angle in degrees */
        public static double x2lon(double aX) {
            return Math.toDegrees(aX / RADIUS);
        }
        public static double y2lat(double aY) {
            return Math.toDegrees(Math.atan(Math.exp(aY / RADIUS)) * 2 - Math.PI/2);
        }
}
