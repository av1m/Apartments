package io.github.oliviercailloux.y2018.apartments.valuefunction.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.Range;
import io.github.oliviercailloux.y2018.apartments.valuefunction.Criterion;
import io.github.oliviercailloux.y2018.apartments.valuefunction.LinearAVF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTest {

  Profile profile;

  /** Inits the profile object before each tests */
  @BeforeEach
  void initEach() {
    Profile.Builder profileBuilder = new Profile.Builder();
    LinearAVF lavf =
        new LinearAVF.Builder()
            .setTeleValueFunction(true)
            .setTerraceValueFunction(true)
            .setWifiValueFunction(true)
            .setFloorAreaTerraceValueFunction(10d, 20d)
            .setFloorAreaValueFunction(50d, 100d)
            .setNbBathroomsValueFunction(2, 4)
            .setNbBedroomsValueFunction(2, 4)
            .setNbSleepingValueFunction(2, 4)
            .setNbMinNightValueFunction(5, 10)
            .setPricePerNightValueFunction(30d, 60d)
            .build();
    profileBuilder.setLinearAVF(lavf);
    for (Criterion c : Criterion.values()) {
      profileBuilder.setWeightRange(c, Range.closed(Double.valueOf(0d), Double.valueOf(10d)));
    }

    profile = profileBuilder.build();
  }

  /** Function to test the basic Profile implementation */
  @Test
  void profileTest() {
    assertEquals(5d, profile.getLinearAVF().getWeight(Criterion.FLOOR_AREA), 0.0001);
    assertEquals(
        Range.closed(Double.valueOf(0d), Double.valueOf(10d)),
        profile.getWeightRange(Criterion.NB_BEDROOMS));
  }

  /** Function to test the Profile builder */
  @Test
  void builderTest() {
    Profile.Builder profileBuilder = new Profile.Builder();
    assertThrows(
        IllegalArgumentException.class,
        () -> profileBuilder.setWeightRange(Criterion.NB_SLEEPING, 10d, 0d));
  }
}
