package com.example.uhealth;

import java.util.HashMap;

public class Medication {
        //private String Status;
        private String Medicine;
        private String InitDate;
        private int Repeats;
        private int InitStorage;
        private int Interval;
        private int Dosis;
        private String Username;
        private String Uid;
        //private int CurrentStorage;
        //--------Initializer------------
        public Medication(HashMap<String,Object> Initializer){
         //   this.Status  =(String)Initializer.get("status");
            this.Dosis = (int)Initializer.get("dosis");
;            this.Medicine = (String)Initializer.get("medicine");
            this.InitDate = (String)Initializer.get("initdate");
            this.InitStorage = (int)Initializer.get("initstorage");
            this.Interval = (int)Initializer.get("interval");
            this.Repeats = (int)Initializer.get("repeats");
            this.Username = (String)Initializer.get("username");
            this.Uid = (String)Initializer.get("uid");
            // no user id yet
            //this.CurrentStorage = (int)Initializer.get("currentstorage");
        }
    //----Get-------------
        //public String getStatus(){return Status;}
        public int getRepeats(){return Repeats;}
        public String getUsername(){return Username;}
        public String getUid(){return Uid;}
        public int getDosis(){return Dosis;}
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
        public void setRepeats(int Repeats){this.Repeats = Repeats;}
        public void setDosis(int Dosis){this.Dosis = Dosis;}
        public void setUid(String uid){this.Uid = Uid;};
        public void setUsername(String Username){this.Username = Username;}
        //public void setCurrentStorage(int CurrentStorage){this.CurrentStorage = CurrentStorage;}




}
