package me.j4n8.projekt02backend.img_location;

import me.j4n8.projekt02backend.auth.GuessDto;
import me.j4n8.projekt02backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping()
	public ImgLocation createLocation(@RequestBody CreateImgLocationDto createImgLocationDto, Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return imgLocationService.createLocation(createImgLocationDto, principal);
	}
	
	@PostMapping("/guess")
	public double guessLocation(@RequestBody GuessDto guessDto) {
		return imgLocationService.guessLocation(guessDto);
	}
	
	@PostMapping("/upload")
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		return imgLocationService.uploadImage(file);
	}
}
