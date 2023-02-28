package me.j4n8.projekt02backend.img_location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/location")
public class ImgLocationController {
    @Autowired
    private ImgLocationService imgLocationService;
    
    @GetMapping()
    public List<ImgLocation> getLatestLocations() {
        return imgLocationService.getLatestLocations();
    }
    
    @GetMapping("/random")
    public ImgLocation getRandomLocation() {
        return imgLocationService.getRandomLocation();
    }
}
