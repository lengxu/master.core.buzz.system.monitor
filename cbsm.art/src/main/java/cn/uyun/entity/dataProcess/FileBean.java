package cn.uyun.entity.dataProcess;

/**
 * Created by lihonghua on 2018/5/28.
 */
public class FileBean {

    private String filename;
    private int laterTime;
    private int time;
    private String shortFilename;

    public FileBean(){}

    public FileBean(String filename, int laterTime,int time, String shortFilename) {
        this.filename = filename;
        this.laterTime = laterTime;
        this.time = time;
        this.shortFilename = shortFilename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLaterTime() {
        return laterTime;
    }

    public void setLaterTime(int laterTime) {
        this.laterTime = laterTime;
    }

    public String getShortFilename() {
        return shortFilename;
    }

    public void setShortFilename(String shortFilename) {
        this.shortFilename = shortFilename;
    }

    public String toString(){
        return filename+"  "+laterTime+"  "+shortFilename;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
