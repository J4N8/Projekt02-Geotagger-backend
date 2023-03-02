package me.j4n8.projekt02backend.img_location;

import jakarta.persistence.*;
import me.j4n8.projekt02backend.user.User;

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
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, table = "img_location")
    private User user;
    
    public ImgLocation(String imageName, double latitude, double longitude, User user) {
        this.imageName = imageName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
    
    public ImgLocation(CreateImgLocationDto createImgLocationDto, User user) {
        this.imageName = createImgLocationDto.getImageName();
        this.longitude = createImgLocationDto.getLongitude();
        this.latitude = createImgLocationDto.getLatitude();
        this.user = user;
    }
    
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
