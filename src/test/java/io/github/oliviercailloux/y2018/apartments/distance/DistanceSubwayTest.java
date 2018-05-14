package io.github.oliviercailloux.y2018.apartments.distance;


import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.google.maps.errors.ApiException;

import io.github.oliviercailloux.y2018.apartments.valuefunction.DistanceMode;

class DistanceSubwayTest {

	@Test
	void calculateDistanceAddressTest() throws ApiException, InterruptedException, IOException {
		
		String api_key = "AIzaSyDuFlzxo-Sbee0E6eMLnfTvxcADSKQzaNs";
		DistanceSubway dist = new DistanceSubway(api_key,"Paris","Ville d'Avray");
		//Assert.assertEquals(0.635, dist.calculateDistanceAddress(), 0);
		Assert.assertEquals(0.96833, dist.calculateDistanceAddress(DistanceMode.ADDRESS), 0.0001);
	}

}
