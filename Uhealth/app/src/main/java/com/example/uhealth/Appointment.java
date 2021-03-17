package com.example.uhealth;

import java.util.HashMap;

public class Appointment {
    //Status managed as int singals: PAST NEAR.TODAY
    private String Status;
    //private String PhysicianID;
    private String AppointmentType;
    private String PhysicianName;
    private String PatientID;
    private String PatientName;
    private String ApptLocation;
    private String ApptDate;
    //-----------------------
    public Appointment(HashMap<String,Object> Initializer){
        this.Status = Initializer.get("status").toString();
        this.AppointmentType = Initializer.get("type").toString();
        this.ApptDate = Initializer.get("date").toString();
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
        return ApptDate;
    }
    //public String getPhysicianID(){ return PhysicianID; }
    public String getPatientID(){ return PatientID; }
    public String getPatientName(){return PatientName; }
    public String getPhysicianName(){return PhysicianName;}
    public String getApptLocation(){return ApptLocation;}
    //------------Set---------------
    public void setStatus(String Status){this.Status = Status;}
    public void setAppointmentType(String appointmentType){this.AppointmentType = appointmentType;}
    public void setDate(String ApptDate){this.ApptDate = ApptDate;}
    public void setPatientID(String PatientID){this.PatientID = PatientID;}
    public void setPatientName(String PatientName){this.PatientName = PatientName;}
    //public void setPhysicianID(String PhysicianID){this.PhysicianID = PhysicianID;}
    public void setPhysicianName(String PhysicianName){this.PhysicianName= PhysicianName;}
    public void setApptLocation(String Location){this.ApptLocation = Location;}
}
