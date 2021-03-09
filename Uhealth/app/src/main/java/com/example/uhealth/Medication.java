package com.example.uhealth;

import java.util.HashMap;

public class Medication {
        //private String Status;
        private String Medicine;
        private String InitDate;

        private int InitStorage;
        private int Interval;
        //private int CurrentStorage;
        //--------Initializer------------
        public Medication(HashMap<String,Object> Initializer){
         //   this.Status  =(String)Initializer.get("status");
            this.Medicine = (String)Initializer.get("medicine");
            this.InitDate = (String)Initializer.get("initdate");
            this.InitStorage = (int)Initializer.get("initstorage");
            this.Interval = (int)Initializer.get("interval");
            //this.CurrentStorage = (int)Initializer.get("currentstorage");
        }
    //----Get-------------
        //public String getStatus(){return Status;}
        public String getMedicine(){return Medicine;}
        public String getInitDate(){return InitDate; }
        public int getInitStorage(){return InitStorage;}
        public int getInterval(){return Interval;}
       // public int getCurrentStorage(){return CurrentStorage;}
        //--Set-----------
      //  public void setStatus(String Status){this.Status = Status;}
        public void setMedicine(String Medicine){this.Medicine = Medicine;}
        public void setInitDate(String InitDate){this.InitDate = InitDate;}
        public void setInitStorage(int InitStorage){this.InitStorage = InitStorage;}
        public void setInterval(int Interval){this.Interval = Interval;}
        //public void setCurrentStorage(int CurrentStorage){this.CurrentStorage = CurrentStorage;}




}
