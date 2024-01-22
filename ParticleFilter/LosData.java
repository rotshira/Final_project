package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.Point3D;

import java.util.*;

/**
 * Created by Roi on 5/23/2016.
 */
public class LosData {

    private Map<Integer, Point3D> timeToLoc;
    private  Map<Point3D, Boolean[]> locToLos;

    public  LosData(List<Building> buildings, List<Point3D> path , List<Sat> satellites){
        timeToLoc = new HashMap<Integer, Point3D>();
        locToLos = new HashMap<Point3D, Boolean[]>();
        init(buildings, path, satellites);
    }




    // if intersection== null, there is no intersection poin3D, hence there is a LOS to sattelite.
    //so if los retun true, there is lOS
    public static boolean los(Sat sat ,Point3D pos, List<Building> roi) {

        boolean LOS = LosAlgorithm.ComputeLos(pos, roi, sat);
       return LOS;
    }

    public static boolean MachineLearningLOS(List<Sat> allsats, Point3D pos, List<Building> bs)
    {
        boolean tmp=true;
        for(Sat sat: allsats)
        {
            tmp= los(sat, pos, bs);
            if(tmp==false)
                return tmp;

        }
        return tmp;
    }
    private void init(List<Building> buildings, List<Point3D> path, List<Sat> satellites) {
        for (int i = 0; i< path.size(); i++){
            Point3D loc = path.get(i);
            Boolean[] los = new Boolean[satellites.size()];
            for (Sat sat : satellites){
                los[sat.getSatID()] = los(sat, loc, buildings);
            }
            timeToLoc.put(i, loc);
            locToLos.put(loc, los);
        }
    }

    public Set<Integer> getAllTimes(){
        return timeToLoc.keySet();
    }

    public Set<Point3D> getAllLocs(){
        return locToLos.keySet();
    }

    public Boolean[] getSatData(Point3D loc){
        return locToLos.get(loc);
    }

    public Boolean[] getSatData(Integer time){
        return locToLos.get(timeToLoc.get(time));
    }

    public Point3D getLoc(int time){
        return timeToLoc.get(time);
    }

}
