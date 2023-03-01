package me.j4n8.projekt02backend.img_location;

public class CreateImgLocationDto {
	private String imageName;
	private double latitude;
	private double longitude;
	
	public CreateImgLocationDto() {
	}
	
	public CreateImgLocationDto(String imageName, double latitude, double longitude) {
		this.imageName = imageName;
		this.latitude = latitude;
		this.longitude = longitude;
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
}
