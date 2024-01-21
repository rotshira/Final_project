package Jamming;

import Geometry.Point2D;

/**
 * Created by Roi on 9/23/2016.
 */
public class TestMain {


    public static void main(String[] args) {

        Point2D jammerloc = new Point2D(50,50);
        JammerParticle realJammer  = new JammerParticle(50, jammerloc, 10);

        JamParticles JammerParticles = new JamParticles();
        JammerParticles.NaiveInit(0,0,200,200); // only one Tx Power. A stationary single Jammer.

     //   JammerParticles.getJamList().get(0).setJamLoc(new Point2D(50,51));
        JammerParticles.FindClosestJammer(realJammer);

        ClientList receiverList = new ClientList();
        receiverList.Init(0,0,200,200);

       // receiverList.PrintClinets();

        for(int i=0; i<40; i++)
        {
            receiverList.movebyCOGSOG();
            JammerParticles.FindClosestJammer(realJammer);
            receiverList.senseNoise(realJammer);
            JammerParticles.evalWeights(receiverList);
           // JammerParticles.getJamList().sort(JammerParticle::compareTo);
           // JammerParticles.PrintAll(realJammer);
            JammerParticles.PrintwithDistMargins(realJammer, 0, 20);
            JammerParticles.Resample();
           // JammerParticles.PrintResults(realJammer);



        }



    }
}
