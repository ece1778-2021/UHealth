package com.example.uhealth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Appointment {
    //Status managed as int singals: PAST NEAR.TODAY
    private String Status;
    //private String PhysicianID;
    private String AppointmentType;
    private String PhysicianName;
    private String PatientID;
    private String PatientName;
    private String ApptLocation;

    private int ApptDate;
    private String Note;
    private List<String> ImagePaths;
    //-----------------------
    public Appointment(HashMap<String,Object> Initializer){
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        this.Status = Initializer.get("status").toString();
        this.AppointmentType = Initializer.get("type").toString();
       // this.ApptDate = Initializer.get("date").toString();
        try{

            this.ApptDate = (int)(mdateformat.parse(Initializer.get("date").toString()).getTime()/1000);

        } catch(ParseException e){
            e.printStackTrace();
        }
        //this.PhysicianID = Initializer.get("physicianid");
        this.PhysicianName = Initializer.get("physicainname").toString();
        this.PatientID = Initializer.get("patientid").toString();
        this.PatientName = Initializer.get("patientname").toString();
        this.ApptLocation = Initializer.get("location").toString();
    }
    //----------Get-------------
    public String getStatus(){
       return Status;
    }
    public String getAppointmentType(){return AppointmentType;}
    public String getDate(){
        SimpleDateFormat mdateformat= new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Date d_Date = new Date();
        d_Date.setTime(this.ApptDate*1000);
        return  mdateformat.format( d_Date);
        //return ApptDate;
    }
    //public String getPhysicianID(){ return PhysicianID; }
    public String getPatientID(){ return PatientID; }
    public String getPatientName(){return PatientName; }
    public String getPhysicianName(){return PhysicianName;}
    public String getApptLocation(){return ApptLocation;}
    //------------Set---------------
    public void setStatus(String Status){this.Status = Status;}
    public void setAppointmentType(String appointmentType){this.AppointmentType = appointmentType;}
    public void setDate(String ApptDate){   SimpleDateFormat mdateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        try {
            this.ApptDate= (int) (mdateformat.parse(ApptDate).getTime() / 1000);
            ;
        } catch (ParseException e) {
            e.printStackTrace();
        }}
    public void setPatientID(String PatientID){this.PatientID = PatientID;}
    public void setPatientName(String PatientName){this.PatientName = PatientName;}
    //public void setPhysicianID(String PhysicianID){this.PhysicianID = PhysicianID;}
    public void setPhysicianName(String PhysicianName){this.PhysicianName= PhysicianName;}
    public void setApptLocation(String Location){this.ApptLocation = Location;}
}
