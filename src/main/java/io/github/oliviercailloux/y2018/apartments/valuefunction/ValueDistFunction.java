package io.github.oliviercailloux.y2018.apartments.valuefunction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;

import io.github.oliviercailloux.y2018.apartments.distance.DistanceSubway;
import io.github.oliviercailloux.y2018.apartments.localize.Location;

/**
 * This class enables the user to calculate the utility of a location by linear interpolation,
 * to have the maximum duration between the interest places.
 */
public class ValueDistFunction implements PartialValueFunction<Location> {
	
	private Map<Location, Double> interestlocation;
	private Location appartlocation;
	private double maxDuration;
	private Logger valueDistFunction = LoggerFactory.getLogger(ValueDistFunction.class);
	
	
	/**
	 * Initializes the different variables of the ValueDistFunction class.
	 * @param appartlocation Object Location which represents the apartment location.
	 */
	public ValueDistFunction(Location appartlocation){
		interestlocation = new HashMap<>();
		this.appartlocation = appartlocation;
		maxDuration = 0;
	}
	
	/**
	 * Add the apartment location and its utility to the HashMap.
	 * @param interest Object Location of an interest place of the user.
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ApiException 
	 */
	public void addInterestLocation(Location interest) throws ApiException, InterruptedException, IOException {
		double utility = setUtility(calculateDistanceLocation(interest));
		interestlocation.put(interest, 1-utility);
		valueDistFunction.info("The interest location has been had with success in the Map.");
	}
	
	/**
	 * 
	 * @return a double which corresponds to the maximum of the duration between an interest place and the apartment.
	 */
	public double getMaxDuration() {		
		return maxDuration;
	}
	
	/**
	 * Update the current distance between the apartment and an interest place.
	 * @param interest Object Location of an interest place of the user.
	 */
	public double calculateDistanceLocation(Location interest) throws ApiException, InterruptedException, IOException {
		DistanceSubway dist = new DistanceSubway(interest.getCoordinate(),appartlocation.getCoordinate());
		double currentdistance = dist.calculateDistanceAddress(DistanceMode.COORDINATE);
		valueDistFunction.info("The current distance between the interest place and the apartment has been updated.");
		if (currentdistance > maxDuration)
			maxDuration = currentdistance;
		valueDistFunction.info("The distance between "+interest.getCoordinate()+" and "+appartlocation.getCoordinate()+" has been calculated and is equal to "+ currentdistance);
		return currentdistance;

	}
	
	/**
	 * 
	 * @param currentdistance double distance in seconds.
	 * @return a double corresponding to the utility of the distance.
	 */
	public double setUtility(double currentdistance) {
		LinearValueFunction f = new LinearValueFunction(0,36000);
		return f.getSubjectiveValue(currentdistance);
	}

	@Override
	public double getSubjectiveValue(Location objectiveData) {
		return interestlocation.get(objectiveData);
	}
	
	@Override
	public Double apply(Location objectiveData) {
		return getSubjectiveValue(objectiveData);
	}
	
	public static void main(String args[]) throws ApiException, InterruptedException, IOException {
		Location loc = new Location("Ville d'Avray");
		Location loc1 = new Location("Paris");
		Location loc2 = new Location("Chaville");
		Location loc3 = new Location("Torcy");
		ValueDistFunction v = new ValueDistFunction(loc);
		
		v.addInterestLocation(loc1);
		v.addInterestLocation(loc2);
		v.addInterestLocation(loc3);
		
		System.out.println(v.getSubjectiveValue(loc1));
		System.out.println(v.getSubjectiveValue(loc2));
		System.out.println(v.getSubjectiveValue(loc3));
		System.out.println(v.getMaxDuration());
	}

}
