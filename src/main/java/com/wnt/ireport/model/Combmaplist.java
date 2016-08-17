package com.wnt.ireport.model;

public class Combmaplist  {

    private String code;

    private String name;

    private String pcode;

    private String lev;
    
    private String flag;
    
    public Combmaplist(String code, String name, String pcode,String lev,String flag) {
        this.code = code;
        this.name = name;
        this.pcode = pcode;
        this.lev = lev;
        this.flag = flag;
    }

    public Combmaplist() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getLev() {
        return this.lev;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }
    
    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
