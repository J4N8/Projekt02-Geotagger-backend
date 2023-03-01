package me.j4n8.projekt02backend.img_location;

import me.j4n8.projekt02backend.auth.GuessDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ImgLocationService {
    @Autowired
    private ImgLocationRepository imgLocationRepository;
    
    public List<ImgLocation> getLatestLocations() {
        return imgLocationRepository.findAll();
    }
    
    public ImgLocation getRandomLocation() {
        List<ImgLocation> entities = imgLocationRepository.findAll();
        if (entities.isEmpty()) {
            throw new IllegalStateException("There are no locations in the database.");
        }
        int randomIndex = new Random().nextInt(entities.size());
        return entities.get(randomIndex);
    }
    
    public ImgLocation createLocation(CreateImgLocationDto createImgLocationDto) {
        ImgLocation imgLocation = new ImgLocation(createImgLocationDto);
        return imgLocationRepository.save(imgLocation);
    }
    
    public double guessLocation(GuessDto guessDto) {
        return imgLocationRepository.getGuessDistance(guessDto.getLatitude(), guessDto.getLongitude(), guessDto.getId());
    }
}
