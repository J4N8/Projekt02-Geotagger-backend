package me.j4n8.projekt02backend.img_location;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class ImgLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date created_at;
    
    private String imageName;
    
    private double latitude;
    private double longitude;
    
    public ImgLocation() {
    }
    
    public ImgLocation(CreateImgLocationDto createImgLocationDto) {
        this.imageName = createImgLocationDto.getImageName();
        this.longitude = createImgLocationDto.getLongitude();
        this.latitude = createImgLocationDto.getLatitude();
    }
    
    public ImgLocation(Long id, Date createdAt, String imageName, double latitude, double longitude) {
        this.id = id;
        this.created_at = createdAt;
        this.imageName = imageName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getImageName() {
        return imageName;
    }
    
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public Date getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
