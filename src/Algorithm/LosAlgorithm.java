package Algorithm;

import GNSS.Sat;
import Geometry.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Roi on 1/7/2015.
 * Those set of functions return true for a LOS sattelite and false for NLOS satelite.
 */
public class LosAlgorithm {
    //Computes LOS between a point, a wall, and a satellite.
    public static boolean ComputeLos(Point3D pos, Wall wall, Sat sat)
    {
       Line3D ray = new Line3D(pos, sat.getAzimuth(),sat.getElevetion(), 300);

        boolean ans = wall.isIntersecting(ray);
        if(ans==true) // wall is intersection, hence NLOS
            return !ans;
        return !ans; //wall does not intersecting, hence LOS
    }
    /**
    receives a wall and satellite point,
    You find the line between the satellite and the point and calculate whether there is a point of intersection between this line and the wall.
    If none, returns -1
    If there is, returns the distance between the intersection point and the height of the wall.
     */
    public static double ComputeLos(Point3D pos, Wall wall, Sat sat){
        Line3D ray = new Line3D(pos, sat.getAzimuth(),sat.getElevetion(), 300);

        Point3D cutPoint = wall.intersectionPoint3D(ray);//intersection pos between a wall 
        if(ans==null) 
            return -1;
        return wall.distanceToTop(cutPoint);
    }





    //Computes LOS between a point, a building, and a satellite by iterating over the walls of the building.
    public static boolean ComputeLos(Point3D pos, Building building, Sat sat)
    {
        for(Wall wall : building.getWalls())
        {
            if(!ComputeLos(pos, wall, sat))
                return false;
        }
        return true;
    }
    //Computes LOS between a point, a list of buildings, and a satellite by iterating over the buildings and calling the previous function.
    public static boolean ComputeLos(Point3D pos,List<Building> buildings, Sat sat)
    {
        for(Building building : buildings)
        {
            if(!ComputeLos(pos, building, sat))
                return false;
        }
        return true;
    }



    public static Set<Building> findBuildings(Point2D base, double az, List<Building> allBuildings, int azimutResolution) {

        double minAz = az-azimutResolution/2;
        double maxAz = az + azimutResolution/2;
        Set<Building> resultSet = new HashSet<>();
        boolean added;
        for (Building building : allBuildings) {
            added = false;
            List<Wall> walls = building.getWalls();
            for (Wall wall : walls) {
                Point3D[] point3dArray = wall.getPoint3dArray();
                for (Point3D point3D : point3dArray) {
                    double angRandians = Math.atan2(point3D.getY() - base.getY(), point3D.getX() - base.getX());
                    double angDegrees = Math.toDegrees(angRandians);
                    if (angDegrees < 0){
                        angDegrees += 360;
                    }
                    if (angDegrees < 0 || angDegrees >= 360){
                        assert false;
                    }
                    double angDegNorthHead = 450 - angDegrees;
                    if (angDegNorthHead >= 360){
                        angDegNorthHead -= 360;
                    }
                    if (angDegNorthHead < 0 || angDegNorthHead >= 360){
                        assert false;
                    }
                    if (angDegNorthHead <= maxAz && angDegNorthHead >= minAz){
                        resultSet.add(building);
                        added = true;
                    }
                    if (added){
                        break;
                    }
                }
                if (added){
                    break;
                }
            }
        }
        return resultSet;
    }
}
