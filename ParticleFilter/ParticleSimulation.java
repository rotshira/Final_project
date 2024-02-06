package ParticleFilter;

import GNSS.Sat;

import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point2D;
import Geometry.Point3D;
import Parsing.nmea.NMEAProtocolParser;
//import Utils.GeoUtils;
import dataStructres.NMEAPeriodicMeasurement;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * Created by Roi on 5/23/2016.
 */
public class ParticleSimulation {


    public static void main(String[] args) {
     // simulationMain2();
//        testParse();
      //  test();
      // NMEAParser();
     //   NEMAFIlePF();
       // NEMAFIlePF2();
    //    NEMAFIlePF3();


        //   test3();




    }

    private static void testParse() {

        List<String> testString= new ArrayList<>();
        testString.add("$GPRMC,061916.400,A,3205.04218,N,03448.14630,E,2.2,178.0,170616,,A*46");
        testString.add("$GPGGA,061916.400,3205.04218,N,03448.14630,E,1,15,0.7,043.75,M,17.1,M,,*60");
        testString.add("$GNGSA,A,3,30,15,17,28,05,13,20,07,,,,,1.4,0.7,1.2*24");
        testString.add("$GNGSA,A,3,85,75,65,66,72,74,76,,,,,,1.4,0.7,1.2*26");
        testString.add("$GNGSA,A,3,,,,,,,,,,,,,1.4,0.7,1.2*2D");
        testString.add("$GPGSV,3,1,10,05,28,241,31,07,19,082,30,13,57,320,41,15,23,316,37*77");
        testString.add("$GPGSV,3,2,10,17,41,143,44,19,21,171,37,20,17,289,29,28,67,023,36*77");
        testString.add("$GPGSV,3,3,10,30,42,064,33,33,24,246,22,,,,,,,,*79");
        testString.add("$GLGSV,2,1,08,74,30,041,33,66,16,329,29,76,14,278,24,75,47,332,38*68");
        testString.add("$GLGSV,2,2,08,65,51,275,35,84,26,068,23,85,25,124,36,72,35,202,33*60");
        testString.add("06/17/2016 09:19:16.341	(255)	Cannot process message $GNGSA,A,3,,,,,,,,,,,,,1.4,0.7,1.2*2D");
        NMEAProtocolParser parser = new NMEAProtocolParser();
        NMEAPeriodicMeasurement ans = parser.ParseStringList(testString);
        System.out.println("end");


}

    private static void test3() {


            for(int j=1;j<6;j++)
                for(int i=0;i<400;i+=10)
                    System.out.println("Power is : "+j+". Dist is : "+Math.sqrt(2)*i+". Receivd SNR :"+SenseEden.GetSnr(0,0,i,i,j));

    }



    private static void NEMAFIlePF2() {



            String walls_file = "BoazMapJune8.kml";
            Particles ParticleList;
            Point3D pivot, pivot2;

            List<ActionFunction>  Actions;
            List<Building> bs = null;
            try {
                bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Number of buildings is " + bs.size());
            System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


            String Particle_path = "KaminData\\CombineHeadong";
            String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

            List<Particles> PointInTime=  new ArrayList<>();
            String Particle_ans_path = "NMEA_SmartPhones_Recordings\\Constructed_ABCD_Route_9_8_2016_V.kml";
            String Particle_ans_path2 = "KaminData\\400ParticlesTry2.kml";


        ParticleList = new Particles();
            pivot = new Point3D(670053, 3551100, 1);
            pivot2 =  new Point3D(pivot);
            pivot2.offset(100, 100, 0);


            //   pivot = new Point3D(670003, 3551050, 1);
            // pivot2 =  new Point3D(pivot);
            //pivot2.offset(130, 130, 0);

            ParticleList.initParticlesWithHeading(pivot, pivot2);

//        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);


            NMEAProtocolParser parser = new NMEAProtocolParser();
         //   String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
     // String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";
        String NMEAFIlePath = "NmeaRecordings\\April2015.txt";

        // String NMEAFIlePath = "NmeaRecordings\\Diagonal_A_C_D_B_A_Twice.txt";



         //   String NMEAFIlePath = "NMEA_SmartPhones_Recordings\\2.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
            try {
                NmeaList = parser.parse(NMEAFIlePath);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

      //  String Recons_Path= "NmeaRecordings\\ABCD_Twice2_17_6COG_SOG_rec.kml";
     //   String Recons_Path2= "NmeaRecordings\\ABCD_Twice2_17_6from_NMEA.kml";

     //   UtilsAlgorithms.GenerateKMLfromCOG_SOS(NmeaList, Recons_Path);
      //  UtilsAlgorithms.GenerateKMLfromNMEA_List(NmeaList, Recons_Path2);

            int NumberOfSamples = NmeaList.size();
            List<Sat> allSats;
        List<Point3D> PointsInTIme = new ArrayList<>();
            List<Particles> finalList= new ArrayList<>();
             finalList.add(ParticleList);

        List<Point3D> ans = new ArrayList<Point3D>();
            int j=0;

            for(int i=2;i<NumberOfSamples; i++)
            {

                System.out.println("Compute position for TimeStamp # "+i);
                double COG = NmeaList.get(i).getCOG();
                double SOG = NmeaList.get(i).getSOG();
                double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
                if(dt>=1)
                    dt=1;
              //  System.out.println("SOG: "+ SOG+"  .  COG: "+COG+"  dt:  "+dt );
               // ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
                ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

                allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));

                ParticleList.OutFfRegion(bs, pivot, pivot2);

                ParticleList.MessureSignalFromSats( bs,  allSats);

              //  ParticleList.ComputeWeight4KaminV0(allSats);
               ParticleList.ComputeWeight4KaminV1(allSats);

                //      ParticleList.printWeights();

                ParticleList.sort();

               // for(int x=0;x<10; x++)
                //    PointsInTIme.add(ParticleList.getParticleList().get(x).pos);

                Point3D tmp = ParticleList.GetBestParticle();
              //  Point3D tmp = ParticleList.GetOptimalLocation(7);
                ans.add(tmp);
               // PointInTime.add(i-2, ParticleList);

                 ParticleList.Resample();

                 //   System.out.println(ParticleList.getParticleList().get(k).pos);
              //   String Particle_path2=Particle_path+i+".kml";
              //  KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,12);
                //  KML_Point3D_List_Generator.Generate_kml_from_List(PointList,Particle_path2);
                //  ParticleList.ComputeAndPrintErrors(path.get(i));

            }

       //     KML_Generator.generateKMLfromParticles(PointsInTIme, Particle_ans_path2, 20);
//            KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);
            System.out.println("end of program");


        }


    private static void NEMAFIlePF3() {



        String walls_file = "BoazMapJune8.kml";
        Particles ParticleList;
        Point3D pivot, pivot2;

        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


        String Particle_path = "KaminData\\CombineHeadong";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

        List<Particles> PointInTime=  new ArrayList<>();
        String Particle_ans_path = "NMEA_SmartPhones_Recordings\\Constructed_ABCD_Route_Weguht0.kml";
        String Particle_ans_path2 = "KaminData\\400ParticlesTry2.kml";


        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);


        //   pivot = new Point3D(670003, 3551050, 1);
        // pivot2 =  new Point3D(pivot);
        //pivot2.offset(130, 130, 0);

        ParticleList.initParticlesWithHeading(pivot, pivot2);

//        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);


        NMEAProtocolParser parser = new NMEAProtocolParser();
        //   String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
        // String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";
        String NMEAFIlePath = "NmeaRecordings\\2.txt";

        // String NMEAFIlePath = "NmeaRecordings\\Diagonal_A_C_D_B_A_Twice.txt";



        //   String NMEAFIlePath = "NMEA_SmartPhones_Recordings\\2.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  String Recons_Path= "NmeaRecordings\\ABCD_Twice2_17_6COG_SOG_rec.kml";
        //   String Recons_Path2= "NmeaRecordings\\ABCD_Twice2_17_6from_NMEA.kml";

        //   UtilsAlgorithms.GenerateKMLfromCOG_SOS(NmeaList, Recons_Path);
        //  UtilsAlgorithms.GenerateKMLfromNMEA_List(NmeaList, Recons_Path2);

        int NumberOfSamples = NmeaList.size();
        List<Sat> allSats;
        List<Point3D> PointsInTIme = new ArrayList<>();
        List<Particles> finalList= new ArrayList<>();
        finalList.add(ParticleList);

        List<Point3D> ans = new ArrayList<Point3D>();
        int j=0;

        for(int i=2;i<NumberOfSamples; i++)
        {

            System.out.println("Compute position for TimeStamp # "+i);
            double COG = NmeaList.get(i).getCOG();
            double SOG = NmeaList.get(i).getSOG();
            double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
            if(dt>=1)
                dt=1;
            //  System.out.println("SOG: "+ SOG+"  .  COG: "+COG+"  dt:  "+dt );
            // ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
            ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

            allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));

            ParticleList.OutFfRegion(bs, pivot, pivot2);

            ParticleList.MessureSignalFromSats( bs,  allSats);

            //  ParticleList.ComputeWeight4KaminV0(allSats);
            ParticleList.ComputeWeight4KaminV1(allSats);

            //      ParticleList.printWeights();

            ParticleList.sort();

            // for(int x=0;x<10; x++)
            //    PointsInTIme.add(ParticleList.getParticleList().get(x).pos);

            Point3D tmp = ParticleList.GetBestParticle();
            //  Point3D tmp = ParticleList.GetOptimalLocation(7);
            ans.add(tmp);
            // PointInTime.add(i-2, ParticleList);

            ParticleList.Resample();

            //   System.out.println(ParticleList.getParticleList().get(k).pos);
            //   String Particle_path2=Particle_path+i+".kml";
            //  KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,12);
            //  KML_Point3D_List_Generator.Generate_kml_from_List(PointList,Particle_path2);
            //  ParticleList.ComputeAndPrintErrors(path.get(i));

        }

        //     KML_Generator.generateKMLfromParticles(PointsInTIme, Particle_ans_path2, 20);
       //NMEAPeriodicMeasurement parse(List<String> NmeaREcords)
       // {}

//        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);
        System.out.println("end of program");


    }

    private static void test2() {

        List<Point3D> ans = new ArrayList<Point3D>();
        Point3D tmp2 = new Point3D(670119, 3551134.6, 1);
        ans.add(tmp2);

        for (int i = 0; i < 60; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 0);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);

            ans.add(tmp2);
        }
        for (int i = 0; i < 30; i++) {

            Point2D tmp = Particles.randomPivotPoint(1, 270);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);
        }

        for (int i = 0; i < 60; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 180);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);

        }
        for (int i = 0; i < 30; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 90);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);

        }


        String Particle_ans_path = "KaminData\\ans_RouteTest.kml";
//        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);


    }

    private static void NEMAFIlePF() {

            String walls_file = "BoazMapJune8.kml";
            Particles ParticleList;
            Point3D pivot, pivot2;

            List<ActionFunction>  Actions;
            List<Building> bs = null;
            try {
                bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Number of buildings is " + bs.size());
        System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


            String Particle_path = "KaminData\\BayesianWeight900Par";
            String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";


            String Particle_ans_path = "KaminData\\August3.kml";

            ParticleList = new Particles();
           pivot = new Point3D(670053, 3551100, 1);
           pivot2 =  new Point3D(pivot);
            pivot2.offset(100, 100, 0);


     //   pivot = new Point3D(670003, 3551050, 1);
       // pivot2 =  new Point3D(pivot);
        //pivot2.offset(130, 130, 0);

            ParticleList.initParticles(pivot, pivot2);
//            KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);

            NMEAProtocolParser parser = new NMEAProtocolParser();
            //String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
          //  String NMEAFIlePath = "KaminData\\NMEAFiles\\route_ABCD_STM_1Hz_twice.txt";

       String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice2_17_6.txt";
       //  String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";

        List<NMEAPeriodicMeasurement> NmeaList = null;
            try {
                NmeaList = parser.parse(NMEAFIlePath);
            } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



       int NumberOfSamples = NmeaList.size();
        List<Sat> allSats;




            List<Point3D> ans = new ArrayList<Point3D>();

            for(int i=2;i<NumberOfSamples; i++)
            {

                System.out.println("compute for timestamp "+i);
                double COG = NmeaList.get(i).getCOG();
                double SOG = NmeaList.get(i).getSOG();
                double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
                if(dt>=1)
                    dt=1;
            //     ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
                ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

                allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));
                ParticleList.OutFfRegion(bs, pivot, pivot2);

                ParticleList.MessureSignalFromSats( bs,  allSats);


                ParticleList.ComputeWeight4KaminV1(allSats);
         //      ParticleList.printWeights();
                Point3D tmp = ParticleList.GetBestParticle();
                ans.add(tmp);
                ParticleList.Resample();


            }

//             KML_Generator.Generate_kml_from_List(ans, Particle_ans_path);
        System.out.println("end of program");


        }



    private static void NMEAParser() {

        NMEAProtocolParser parser = new NMEAProtocolParser();
                String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Point3D> pointList = new ArrayList<>();
        for(int i=0; i<NmeaList.size();i++)
        {
            Point3D tmp= new Point3D(NmeaList.get(i).getLat(),NmeaList.get(i).getLon(),NmeaList.get(i).getAlt());
           // tmp = GeoUtils.convertLATLONtoUTM(tmp);
            pointList.add(tmp);

            List<Sat> allSat = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));
            for(int j=0; j< allSat.size(); j++)

            {
                System.out.println("id: "+allSat.get(j).getSatID()+" Az: "+allSat.get(j).getAzimuth()+" El: "+allSat.get(j).getElevetion()+" SNR: "+allSat.get(j).getSingleSNR());
            }
         //   System.out.println("Timestamp "+ i);
            System.out.println();
            System.out.println();
            System.out.println("Time : "+NmeaList.get(i).getTime()+" cog: "+NmeaList.get(i).getCOG()+" Sog : "+NmeaList.get(i).getSOG());
        }

        String Path = "NMEA2ABCD_route.kml";
//        KML_Generator.Generate_kml_from_List(pointList,Path);
        System.out.println("End of program");
    }




    public  static void simulationMain2() {

       // String walls_file = "EsriBuildingsBursaNoindentWithBoazBuilding.kml"; //todo Roi : Sparse this file to contain only 10 buildings!
        String walls_file = "Esri_v0.4.kml";


       // Buildings bs;
        List<Sat> allSats;

        List<Point3D> path;
        Particles ParticleList;
        Point3D pivot, pivot2;
        int CurrentGeneration;
     //   String Simulation_route_kml_path = "C:\\Users\\Roi\\Documents\\PHD\\Papers\\ParticleFilter\\Data\\Simulaton_route.kml";



        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        path = UtilsAlgorithms.createPath();

        allSats = UtilsAlgorithms.createSatDataList();
        String Simulation_route_3D_kml_path = "Simulaton__route_May_2016.kml";

        String Particle_path = "KaminData\\Simulaton_routeTest_FInal";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

//        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);
        String Particle_ans_path = ".\\Data\\ans_Simulation_route_100_particles2.kml";

        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        LosData losData = new LosData( bs, path, allSats);


        ParticleList.initParticles(pivot, pivot2);
//        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);

        NMEAProtocolParser parser = new NMEAProtocolParser();


        Actions = new ArrayList<ActionFunction>();
        List<Point3D> PointList;
        CurrentGeneration = 0;
        Random R1= new Random();
       for(int i=0;i<path.size()-1; i++)
        {
            ActionFunction tmp = new ActionFunction(path.get(i), path.get(i+1), 0 , 0,0);

            Actions.add(tmp);
        }

        List<Point3D> ans = new ArrayList<Point3D>();
        for(int i=1;i<path.size()-1; i++)
        {

           System.out.println("compute for timestamp "+i);
          //  ActionFunction currentAction = UtilsAlgorithms.getActionFromNMEA(NmeaList.get(i));
            ParticleList.MoveParticleWithError(Actions.get(i));
            //allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));

            ParticleList.OutFfRegion(bs, pivot, pivot2);

            //ParticleList.MessureSignalFromSats( bs,  allSats);
            ParticleList.MessureSignalFromSats( bs,  allSats);

          //  ParticleList.MoveParticleWithError(Actions.get(i));

            ParticleList.ComputeWeightsNoHistory(losData.getSatData(i));
            //ParticleList.ComputeWeights(losData.getSatData(i)); // compute weights with hisotry
            ParticleList.Resample();

           // Point3D tmp = ParticleList.GetParticleWithMaxWeight();
            //ans.add(tmp);
            String Particle_path2=Particle_path+i+".kml";

//            KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,5);
            //  KML_Point3D_List_Generator.Generate_kml_from_List(PointList,Particle_path2);
          //  ParticleList.ComputeAndPrintErrors(path.get(i));

        }

       // GUI.Generate_kml_from_List(ans, Particle_ans_path);


    }


}

