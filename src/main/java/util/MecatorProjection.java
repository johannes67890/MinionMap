package util;

import java.lang.Math;

import parser.TagBound;
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
        public static TagBound project(TagBound bound){ 
            TagNode max = project(bound.getMaxLon(), bound.getMaxLat());
            TagNode min = project(bound.getMinLon(), bound.getMinLat());
            return new TagBound(
                -max.getLat(),
                -min.getLat(),
                min.getLon(),
                max.getLon()
            );
        }

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
                lon2x(node.getLon()),
                lat2y(node.getLat())
            );
        }

         /**
         * Projects a node to the mercator projection.
         * This takes the {@link TagNode} and turns lat and lon into x and y.
         * <p>
         * The x and y are in meters.
         * </p>
         * @param node The node to project
         * @return The projected node
         */
        public static TagNode project(long id, double x, double y){
            return new TagNode(
                    id,
                    lon2x(x),
                    lat2y(y)
            );
        }
         /**
         * Projects a node to the mercator projection.
         * This takes the {@link TagNode} and turns lat and lon into x and y.
         * <p>
         * The x and y are in meters.
         * </p>
         * @param node The node to project
         * @return The projected node
         */
        public static TagNode project(double x, double y){
            return new TagNode(
                    0,
                    lon2x(x),
                    lat2y(y)
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
                y2lat(node.getLat()),
                x2lon(node.getLon())
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

        // Utility
        public static double getEarthRadius() {
            return RADIUS;
        }
}
