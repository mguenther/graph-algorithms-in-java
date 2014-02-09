package de.mgu.algorithms.graphs.converter;

import java.awt.Point;

public class MinuteDistanceConverter {
	
	/**
	 * Factor used in the conversion to radiant and standard M.m format
	 */
	private static final double TO_STD_MM_FORMAT = 2.0 * Math.PI / 360.0 / 1000000;
	
	/**
	 * Represents the earth's major axis
	 */
	private static final double EARTH_MAJOR_AXIS = 6378137.0;
	
	/**
	 * Represents the earth's minor axis
	 */
	private static final double EARTH_MINOR_AXIS = 6356752.3142;
	
	/**
	 * Represents the earth's flattening constant
	 */
	private static final double EARTH_FLATTENING = (EARTH_MAJOR_AXIS - EARTH_MINOR_AXIS) /
			EARTH_MAJOR_AXIS;
	
	/**
	 * Represents the earth's eccentricity
	 */
	private static final double EARTH_ECCENTRICITY = 2.0 * EARTH_FLATTENING -
			EARTH_FLATTENING * EARTH_FLATTENING;

	public int distance(Point from, Point to) {
		double fromLon = from.x * TO_STD_MM_FORMAT;
		double fromLat = from.y * TO_STD_MM_FORMAT;
		double toLon = to.x * TO_STD_MM_FORMAT;
		double toLat = to.y * TO_STD_MM_FORMAT;

		// calculate some aux values for first coordinate
		double sinFromLat = Math.sin(fromLat);
		double cosFromLat = Math.cos(fromLat);

		double beta = EARTH_MAJOR_AXIS / Math.sqrt(1.0-EARTH_ECCENTRICITY * sinFromLat * sinFromLat);
		double x = beta * cosFromLat * Math.cos(fromLon);
		double y = beta * cosFromLat * Math.sin(fromLon);
		double z = beta * (1.0-EARTH_ECCENTRICITY) * sinFromLat;
		
		// calculate some aux values for second coordinate w.r.t. first coordinate
		double sinToLat = Math.sin(toLat);
		double cosToLat = Math.cos(toLat);
		
		beta = EARTH_MAJOR_AXIS / Math.sqrt(1.0-EARTH_ECCENTRICITY * sinToLat * sinToLat);
		x -= beta * cosToLat * Math.cos(toLon);
		y -= beta * cosToLat * Math.sin(toLon);
		z -= beta * (1.0-EARTH_ECCENTRICITY) * sinFromLat;
		
		double distance = Math.sqrt(x*x + y*y + z*z);
		
		// apply some factor in order to obtain admissible lower bounds
		return (int) lowerBoundFactor(distance);
	}
	
	private double lowerBoundFactor(double distance) {
		return distance * 7.0;
	}
}