package GNSS;

import Geometry.Point3D;

/**
 * Created by Roi on 1/7/2015.
 */
public class Sat {

    Point3D satPosInECEF;
    Point3D satVelocity;
    double azimuth, elevetion;
    int satID;
    public int[] snr;
    public int SingleSNR;


    public Sat(Point3D satPosInECEF, double azimuth, double elevetion, int satID) {
        this.satPosInECEF = satPosInECEF;
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
    }

    public int getSingleSNR() {
        return SingleSNR;
    }

    public void setSingleSNR(int singleSNR) {
        SingleSNR = singleSNR;
    }

    public Sat(double azimuth, double elevetion, int satID)
    {
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
    }

    public Sat(double azimuth, double elevetion, int satID, int SNR)
    {
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
        this.SingleSNR = SNR;
    }


    public double distanceFromSatToPos(Point3D pos)
    {
        return this.satPosInECEF.distance(pos);
    }

    public void computeAnglesfromSatToPos(Point3D pos)
    {
        this.azimuth = 0;
        this.elevetion = 0;
    }

    public Point3D getSatPosInECEF() {
        return satPosInECEF;
    }

    public void setSatPosInECEF(Point3D satPosInECEF) {
        this.satPosInECEF = satPosInECEF;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getElevetion() {
        return elevetion;
    }

    public void setElevetion(double elevetion) {
        this.elevetion = elevetion;
    }

    public int getSatID() {
        return satID;
    }

    public void setSatID(int satID) {
        this.satID = satID;
    }

    public int[] getSnr() {
        return snr;
    }

    public void setSnr(int[] snr) {
        this.snr = snr;
    }

    public double getXposECEF(){return this.satPosInECEF.getX();}

    public double getYposECEF(){return this.satPosInECEF.getY();}

    public double getZposECEF(){return this.satPosInECEF.getZ();}

}
