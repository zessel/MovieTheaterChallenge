import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TheaterTests {

	Theater testTheater;
	
	@BeforeEach
	public void setup() {
		testTheater = new Theater(3,3);
	}
	
	@Test
	void shouldReserveSeatB1() {
		String expected = "R001 B1";
		assertEquals(expected, testTheater.bookSeats("R001 1"));
	}
	
	@Test
	void shouldNotReserve10seats() {
		String expected = "R001 ";
		assertEquals(expected, testTheater.bookSeats("R001 10"));
	}
	
	@Test
	void shouldReserveSeatA3andC3() {
		String expected = "R002 A3,C3";
		testTheater.bookSeats("R001 1");
		assertEquals(expected, testTheater.bookSeats("R002 2"));
	}

	@Test
	void shouldNotReserve1and3seats() {
		String expected = "R002 ";
		testTheater.bookSeats("R001 1");
		assertEquals(expected, testTheater.bookSeats("R002 3"));		
	}
	
	@Test
	void shouldReserveAllSeats() {
		String expected = "R001 A1,A2,A3,B1,B2,B3,C1,C2,C3";
		assertEquals(expected, testTheater.bookSeats("R001 9"));
	}
}
