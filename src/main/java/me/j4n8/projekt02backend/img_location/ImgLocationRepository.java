package me.j4n8.projekt02backend.img_location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImgLocationRepository extends JpaRepository<ImgLocation, Long> {
	/***
	 * Calculates the distance between the given coordinates and the coordinates of the image with the given id using PostGIS.
	 * @param latitude
	 * @param longitude
	 * @param img_id
	 * @return
	 */
	@Query(value = "SELECT ST_DistanceSphere(ST_MakePoint(:lat, :lon), ST_MakePoint(latitude, longitude)) as distance "
			+ "FROM img_location "
			+ "WHERE id = :img_id", nativeQuery = true)
	double getGuessDistance(@Param("lat") double latitude, @Param("lon") double longitude, @Param("img_id") long img_id);
}