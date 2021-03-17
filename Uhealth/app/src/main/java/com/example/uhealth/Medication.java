package com.example.uhealth;

import java.util.HashMap;

public class Medication {
        private String Status;
        private String Medicine;
        private String InitDate;
        private long Repeats;
        private long InitStorage;
        private long Interval;
        private long Dosis;
        private String Username;
        private String Uid;
        private String LastUpdate;//time
        private String NextUpdate;//to be updated
        //private int CurrentStorage;
        //--------Initializer------------
        public Medication(HashMap<String,Object> Initializer){
           this.Status  =Initializer.get("status").toString();
            this.Dosis = Long.valueOf(Initializer.get("dosis").toString());
;            this.Medicine = Initializer.get("medicine").toString();
            this.InitDate = (String)Initializer.get("initdate");
            this.InitStorage =  Long.valueOf(Initializer.get("initstorage").toString());
            this.Interval =  Long.valueOf(Initializer.get("interval").toString());
            this.Repeats = Long.valueOf(Initializer.get("repeats").toString());
            this.LastUpdate = Initializer.get("lastupdate").toString();
            this.NextUpdate = Initializer.get("nextupdate").toString();
            this.Username =Initializer.get("username").toString();
            this.Uid = Initializer.get("uid").toString();
            // no user id yet
            //this.CurrentStorage = (int)Initializer.get("currentstorage");
        }
    //----Get-------------
        public String getStatus(){return Status;}
        public long getRepeats(){return Repeats;}
        public String getUsername(){return Username;}
        public String getUid(){return Uid;}
        public long getDosis(){return Dosis;}
        public String getMedicine(){return Medicine;}
        public String getInitDate(){return InitDate; }
        public long getInitStorage(){return InitStorage;}
        public long getInterval(){return Interval;}
        public String getLastUpdate(){return LastUpdate;}
        public String getNextUpdate(){return NextUpdate;}
       // public int getCurrentStorage(){return CurrentStorage;}
        //--Set-----------
        public void setStatus(String Status){this.Status = Status;}
        public void setLastUpdate(String LastUpdate){this.LastUpdate = LastUpdate;}
    public void setNextUpdate(String NextUpdate){this.NextUpdate = NextUpdate;}
        public void setMedicine(String Medicine){this.Medicine = Medicine;}
        public void setInitDate(String InitDate){this.InitDate = InitDate;}
        public void setInitStorage(long InitStorage){this.InitStorage = InitStorage;}
        public void setInterval(long Interval){this.Interval = Interval;}
        public void setRepeats(long Repeats){this.Repeats = Repeats;}
        public void setDosis(long Dosis){this.Dosis = Dosis;}
        public void setUid(String uid){this.Uid = Uid;};
        public void setUsername(String Username){this.Username = Username;}
        //public void setCurrentStorage(int CurrentStorage){this.CurrentStorage = CurrentStorage;}




}
