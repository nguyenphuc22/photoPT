package nguyenphuc.vr.photo.model;

public class PhotoDetail {
    
    public static int KB = 1024;
    public static int MB = 1024 * 1024;
    
    private String date;
    private String size;
    private String pixel;
    private String path;
    private  String name;
    private String location;

    public PhotoDetail() {
    }

    public PhotoDetail(String date, String size, String pixel, String path, String name, String location) {
        this.date = date;
        this.size = size;
        this.pixel = pixel;
        this.path = path;
        this.name = name;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
