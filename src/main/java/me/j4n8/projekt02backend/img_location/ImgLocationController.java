package me.j4n8.projekt02backend.img_location;

import me.j4n8.projekt02backend.auth.GuessDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/location")
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
    
    //TODO: Add image upload
    @PostMapping()
    public ImgLocation createLocation(@RequestBody CreateImgLocationDto createImgLocationDto) {
        return imgLocationService.createLocation(createImgLocationDto);
    }
    
    @PostMapping("/guess")
    public double guessLocation(@RequestBody GuessDto guessDto) {
        return imgLocationService.guessLocation(guessDto);
    }
}
