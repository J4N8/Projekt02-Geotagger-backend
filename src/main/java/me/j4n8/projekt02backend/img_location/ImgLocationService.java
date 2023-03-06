package me.j4n8.projekt02backend.img_location;

import me.j4n8.projekt02backend.auth.GuessDto;
import me.j4n8.projekt02backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
	
	public ImgLocation createLocation(CreateImgLocationDto createImgLocationDto, User principal) {
		ImgLocation imgLocation = new ImgLocation(createImgLocationDto, principal);
		return imgLocationRepository.save(imgLocation);
	}
	
	public double guessLocation(GuessDto guessDto) {
		return imgLocationRepository.getGuessDistance(guessDto.getLatitude(), guessDto.getLongitude(), guessDto.getId());
	}
	
	public String uploadImage(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String keyName = UUID.randomUUID() + "-" + fileName;
		try {
			// Replace the directory below with the path to your desired directory
			Path path = Paths.get("/uploads/" + keyName);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return keyName;
	}
}
