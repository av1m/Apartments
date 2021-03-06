package io.github.oliviercailloux.y2018.apartments.valuefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.apartment.Apartment.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApartmentValueFunctionTests {

  ApartmentValueFunction valueFunction = new ApartmentValueFunction();

  Apartment a;

  /** Inits the apartmentValueFunction object and the apartment before each tests */
  @BeforeEach
  void initEach() {
    a =
        new Builder()
            .setFloorArea(250)
            .setAddress("108 rue de chat-ville Ville-d'Avray 92410")
            .setNbBedrooms(1)
            .setNbSleeping(4)
            .setNbBathrooms(1)
            .setTerrace(true)
            .setFloorAreaTerrace(40)
            .setDescription(
                "Une ferme rustique en compagnie de Dwight Schrute, interdit à Jim Halpert")
            .setTitle("Une ferme")
            .setWifi(false)
            .setPricePerNight(3.3)
            .setNbMinNight(3)
            .setTele(false)
            .build();

    LinearValueFunction floorAreaV = new LinearValueFunction(0d, 200d);
    valueFunction.setFloorAreaValueFunction(floorAreaV);

    LinearValueFunction nbSleepingV = new LinearValueFunction(3d, 5d);
    valueFunction.setNbSleepingValueFunction(nbSleepingV);

    ReversedLinearValueFunction nbMinNightV = new ReversedLinearValueFunction(7d, 30d);
    valueFunction.setNbMinNightValueFunction(nbMinNightV);

    BooleanValueFunction terraceV = new BooleanValueFunction(true);
    valueFunction.setTerraceValueFunction(terraceV);

    LinearValueFunction nbBedroomsV = new LinearValueFunction(3d, 4d);
    valueFunction.setNbBedroomsValueFunction(nbBedroomsV);

    ReversedLinearValueFunction pricePerNightV = new ReversedLinearValueFunction(20d, 40d);
    valueFunction.setPricePerNightValueFunction(pricePerNightV);

    BooleanValueFunction teleV = new BooleanValueFunction(true);
    valueFunction.setTeleValueFunction(teleV);

    BooleanValueFunction wifiV = new BooleanValueFunction(true);
    valueFunction.setWifiValueFunction(wifiV);

    LinearValueFunction nbBathroomsV = new LinearValueFunction(2d, 3d);
    valueFunction.setNbBathroomsValueFunction(nbBathroomsV);

    LinearValueFunction floorAreaTerraceV = new LinearValueFunction(30d, 50d);
    valueFunction.setFloorAreaTerraceValueFunction(floorAreaTerraceV);
  }

  /** Function to test the some setters */
  @Test
  void testCheckValue() {

    LinearValueFunction lvf = (LinearValueFunction) valueFunction.getNbSleepingValueFunction();
    assertEquals(5d, lvf.getInterval().upperEndpoint());
    lvf = (LinearValueFunction) valueFunction.getNbBedroomsValueFunction();
    assertEquals(4d, lvf.getInterval().upperEndpoint());
    lvf = (LinearValueFunction) valueFunction.getNbBathroomsValueFunction();
    assertEquals(3d, lvf.getInterval().upperEndpoint());
  }

  /**
   * Function to test the computing of the subjective value of an apartment To compute the
   * subjective value of an apartment, here is the formula : sum(attributeSubjectiveValue *
   * attributeWeight)/sum(weight)
   */
  @Test
  void testApartmentValueFunction() {

    double subjectiveValueTele =
        valueFunction.getTeleValueFunction().getSubjectiveValue(a.getTele())
            * valueFunction.getWeightSubjectiveValue(Criterion.TELE);
    assertEquals(0, subjectiveValueTele);
    double subjectiveValueFloorArea =
        valueFunction.getFloorAreaValueFunction().getSubjectiveValue(a.getFloorArea())
            * valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA);
    assertEquals(0.1, subjectiveValueFloorArea, 0.0001);
    double subjectiveValuePrice =
        valueFunction.getPricePerNightValueFunction().getSubjectiveValue(a.getPricePerNight())
            * valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(0.1, subjectiveValuePrice, 0.0001);

    double sumWeight =
        valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
            + valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA_TERRACE)
            + valueFunction.getWeightSubjectiveValue(Criterion.NB_BEDROOMS)
            + valueFunction.getWeightSubjectiveValue(Criterion.NB_BATHROOMS)
            + valueFunction.getWeightSubjectiveValue(Criterion.NB_MIN_NIGHT)
            + valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT)
            + valueFunction.getWeightSubjectiveValue(Criterion.NB_SLEEPING)
            + valueFunction.getWeightSubjectiveValue(Criterion.TELE)
            + valueFunction.getWeightSubjectiveValue(Criterion.TERRACE)
            + valueFunction.getWeightSubjectiveValue(Criterion.WIFI);
    assertEquals(1, sumWeight, 0.0001);

    assertEquals(0.5, valueFunction.getSubjectiveValue(a), 0.0001);

    sumWeight =
        sumWeight
            - valueFunction.getWeightSubjectiveValue(Criterion.TELE)
            - valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
            - valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    valueFunction.setTeleSubjectiveValueWeight(4.3);
    valueFunction.setFloorAreaSubjectiveValueWeight(2);
    valueFunction.setPricePerNightSubjectiveValueWeight(3);
    sumWeight =
        sumWeight
            + valueFunction.getWeightSubjectiveValue(Criterion.TELE)
            + valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
            + valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(10, sumWeight, 0.0001);

    subjectiveValueTele =
        valueFunction.getTeleValueFunction().getSubjectiveValue(a.getTele())
            * valueFunction.getWeightSubjectiveValue(Criterion.TELE);
    assertEquals(0, subjectiveValueTele);
    subjectiveValueFloorArea =
        valueFunction.getFloorAreaValueFunction().getSubjectiveValue(a.getFloorArea())
            * valueFunction.getWeightSubjectiveValue(Criterion.FLOOR_AREA);
    assertEquals(2, subjectiveValueFloorArea, 0.0001);
    subjectiveValuePrice =
        valueFunction.getPricePerNightValueFunction().getSubjectiveValue(a.getPricePerNight())
            * valueFunction.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT);
    assertEquals(3, subjectiveValuePrice, 0.0001);

    assertEquals(0.53, valueFunction.getSubjectiveValue(a), 0.0001);
  }

  /** Test if the weight setter throw a Illegal Argument Exception when needed */
  @Test
  void testExceptionIllegalArgWeightSetter() {

    assertThrows(
        IllegalArgumentException.class, () -> valueFunction.setFloorAreaSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setNbBedroomsSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setNbSleepingSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setNbBathroomsSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class, () -> valueFunction.setTerraceSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setFloorAreaTerraceSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class, () -> valueFunction.setWifiSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setPricePerNightSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.setNbMinNightSubjectiveValueWeight(-1d));
    assertThrows(
        IllegalArgumentException.class, () -> valueFunction.setTeleSubjectiveValueWeight(-1d));
  }

  /** Function to test the adaptation to the subjective value weight of a criteria */
  @Test
  void testAdaptWeight() {

    assertThrows(
        IllegalArgumentException.class,
        () -> valueFunction.adaptWeight(Criterion.TELE, Criterion.TELE));
    valueFunction.setTeleSubjectiveValueWeight(7d);
    valueFunction.setTerraceSubjectiveValueWeight(3d);
    assertEquals(7d, valueFunction.getWeightSubjectiveValue(Criterion.TELE));
    assertEquals(3d, valueFunction.getWeightSubjectiveValue(Criterion.TERRACE));
    valueFunction = valueFunction.adaptWeight(Criterion.TERRACE, Criterion.TELE);
    assertEquals(9d, valueFunction.getWeightSubjectiveValue(Criterion.TERRACE));
    assertEquals(1d, valueFunction.getWeightSubjectiveValue(Criterion.TELE));
  }

  /** Function to test if the bounds of an interval adapt well when needed */
  @Test
  void testAdaptBounds() {

    assertThrows(
        IllegalArgumentException.class, () -> valueFunction.adaptBounds(Criterion.TELE, 0d, true));
    valueFunction = valueFunction.adaptBounds(Criterion.FLOOR_AREA_TERRACE, 25d, true);
    LinearValueFunction lvf =
        (LinearValueFunction) valueFunction.getFloorAreaTerraceValueFunction();
    assertEquals(25d, lvf.getInterval().lowerEndpoint());
    assertEquals(
        0.6,
        valueFunction
            .getFloorAreaTerraceValueFunction()
            .getSubjectiveValue(a.getFloorAreaTerrace()));
    valueFunction = valueFunction.adaptBounds(Criterion.NB_BEDROOMS, 8, false);
    double nbRooms = a.getNbBedrooms();
    assertEquals(0d, valueFunction.getNbBedroomsValueFunction().getSubjectiveValue(nbRooms));
    lvf = (LinearValueFunction) valueFunction.getNbBedroomsValueFunction();
    assertEquals(8, lvf.getInterval().upperEndpoint());
  }

  /** Function to test if a random apartment generated respects some criteria */
  @Test
  void testGetRandomApartmentValueFunction() {

    ApartmentValueFunction apart = ApartmentValueFunction.getRandomApartmentValueFunction();
    assertEquals(1d, apart.getFloorAreaValueFunction().getSubjectiveValue(a.getFloorArea()));
    LinearValueFunction lvf = (LinearValueFunction) apart.getFloorAreaTerraceValueFunction();
    assertTrue(lvf.getInterval().upperEndpoint() <= 101d);
    assertTrue(apart.getWeightSubjectiveValue(Criterion.TELE) <= 1d);
    double sum =
        apart.getWeightSubjectiveValue(Criterion.TELE)
            + apart.getWeightSubjectiveValue(Criterion.FLOOR_AREA)
            + apart.getWeightSubjectiveValue(Criterion.FLOOR_AREA_TERRACE)
            + apart.getWeightSubjectiveValue(Criterion.NB_BATHROOMS)
            + apart.getWeightSubjectiveValue(Criterion.NB_BEDROOMS)
            + apart.getWeightSubjectiveValue(Criterion.NB_SLEEPING)
            + apart.getWeightSubjectiveValue(Criterion.NB_MIN_NIGHT)
            + apart.getWeightSubjectiveValue(Criterion.PRICE_PER_NIGHT)
            + apart.getWeightSubjectiveValue(Criterion.TERRACE)
            + apart.getWeightSubjectiveValue(Criterion.WIFI);
    assertEquals(1d, sum, 0.00001);
  }
}
