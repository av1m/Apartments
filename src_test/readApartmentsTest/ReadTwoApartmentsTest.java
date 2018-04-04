package readApartmentsTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.github.oliviercailloux.y2018.apartments.apartment.Apartment;
import io.github.oliviercailloux.y2018.apartments.readapartments.ReadApartmentsXMLFormat;

public class ReadTwoApartmentsTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {


		ReadApartmentsXMLFormat r = new ReadApartmentsXMLFormat();
		ReadApartmentsXMLFormat r1 = new ReadApartmentsXMLFormat();

		File f = new File("resources\\start-apartment-classpath.xml");

		String f1 = new File("start-apartment.xml").getAbsolutePath();


		try (InputStream input = new FileInputStream(f)){

			Apartment a = r.readApartment(input);

			System.out.println(a);

		}

		try (InputStream input = new FileInputStream(f1)){

			Apartment b = r1.readApartment(input);

			System.out.println(b);

		}






	}



}
