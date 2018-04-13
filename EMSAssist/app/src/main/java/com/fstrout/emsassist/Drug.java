package com.fstrout.emsassist;

public class Drug {
    private String PRODUCTID;
    private String PRODUCTNDC;
    private String PRODUCTTYPENAME;
    private String PROPRIETARYNAME;
    private String PROPRIETARYNAMESUFFIX;
    private String NONPROPRIETARYNAME;
    private String DOSAGEFORMNAME;

    public String getPRODUCTID() {
        return PRODUCTID;
    }

    public void setPRODUCTID(String PRODUCTID) {
        this.PRODUCTID = PRODUCTID;
    }

    public String getPRODUCTNDC() {
        return PRODUCTNDC;
    }

    public void setPRODUCTNDC(String PRODUCTNDC) {
        this.PRODUCTNDC = PRODUCTNDC;
    }

    public String getPRODUCTTYPENAME() {
        return PRODUCTTYPENAME;
    }

    public void setPRODUCTTYPENAME(String PRODUCTTYPENAME) {
        this.PRODUCTTYPENAME = PRODUCTTYPENAME;
    }

    public String getPROPRIETARYNAME() {
        return PROPRIETARYNAME;
    }

    public void setPROPRIETARYNAME(String PROPRIETARYNAME) {
        this.PROPRIETARYNAME = PROPRIETARYNAME;
    }

    public String getPROPRIETARYNAMESUFFIX() {
        return PROPRIETARYNAMESUFFIX;
    }

    public void setPROPRIETARYNAMESUFFIX(String PROPRIETARYNAMESUFFIX) {
        this.PROPRIETARYNAMESUFFIX = PROPRIETARYNAMESUFFIX;
    }

    public String getNONPROPRIETARYNAME() {
        return NONPROPRIETARYNAME;
    }

    public void setNONPROPRIETARYNAME(String NONPROPRIETARYNAME) {
        this.NONPROPRIETARYNAME = NONPROPRIETARYNAME;
    }

    public String getDOSAGEFORMNAME() {
        return DOSAGEFORMNAME;
    }

    public void setDOSAGEFORMNAME(String DOSAGEFORMNAME) {
        this.DOSAGEFORMNAME = DOSAGEFORMNAME;
    }

    public Drug (){

    }

    public Drug(String PRODUCTID, String PRODUCTNDC, String PRODUCTTYPENAME, String PROPRIETARYNAME, String PROPRIETARYNAMESUFFIX, String NONPROPRIETARYNAME, String DOSAGEFORMNAME) {
        this.PRODUCTID = PRODUCTID;
        this.PRODUCTNDC = PRODUCTNDC;
        this.PRODUCTTYPENAME = PRODUCTTYPENAME;
        this.PROPRIETARYNAME = PROPRIETARYNAME;
        this.PROPRIETARYNAMESUFFIX = PROPRIETARYNAMESUFFIX;
        this.NONPROPRIETARYNAME = NONPROPRIETARYNAME;
        this.DOSAGEFORMNAME = DOSAGEFORMNAME;
    }
}
