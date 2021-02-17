import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Theater {
	
	private static final int HORIZONTAL_BUFFER = 3;
	
	private final char[][] seats;
	private int totalVacancies;
	private final int[] rowVacancies;
	
	public Theater(int rows, int columns) {
		seats = new char[rows][columns];
		for (char[] row : seats) {
			Arrays.fill(row, 'o');
		}
		totalVacancies = rows * columns;
		rowVacancies = new int[rows];
		Arrays.fill(this.rowVacancies, columns);
	}
	
	public String bookSeats (String reservationRequest) {
		
		String[] tokens = reservationRequest.split(" ");
		
		String response = tokens[0] + " ";
		int numSeats = Integer.parseInt(tokens[1]);
		if (checkVacancy(numSeats)) {
			response = response + reserveSeats(numSeats);
		}
		return response;
	}
	
	private String reserveSeats(int numSeats) {
		
		String response;
		int row = findSingleRow(numSeats);
		
		if (row != -1) {
			response = reserveSingleRow(row, numSeats);
		} else {
			response = reserveSplitRows(numSeats);
		}
		return response;
	}
	
	private String reserveSingleRow(int row, int numSeats) {
		
		int startingSeat = seats[0].length - rowVacancies[row];
		
		for (int ii = 0; ii < numSeats; ii++) {
			seats[row][startingSeat + ii] = '*';
			rowVacancies[row]--;
			totalVacancies--;
		}
		
		char rowChar = (char)(row + 65);
		StringBuilder response = new StringBuilder();
		response.append("" + rowChar + (startingSeat + 1));
		for (int ii = 2; ii <= numSeats; ii++) {
			response.append("," + rowChar + (startingSeat + ii));
		}
		bufferSeats(row, numSeats, startingSeat);
		
		return response.toString();
	}
	
	private void bufferSeats(int row, int numSeats, int startingSeat) {
		for (int ii = 0; ii < HORIZONTAL_BUFFER; ii++) {
			if (rowVacancies[row] > 0) {
				seats[row][seats[0].length - rowVacancies[row]] = 'x';
				rowVacancies[row]--;
				totalVacancies--;
			}
		}
		if (row - 1 >= 0) {
			for (int ii = -1; ii < numSeats + 1; ii++) {
				if ((ii + startingSeat) >= 0 && (ii + startingSeat < seats[0].length)) {
					if (seats[row-1][ii+startingSeat] == 'o') {
						rowVacancies[row-1]--;
						totalVacancies--;
					}
					seats[row-1][ii+startingSeat] = 'x';
				}
			}
		}
		if (row + 1 < seats.length) {
			for (int ii = -1; ii < numSeats + 1; ii++) {
				if ((ii + startingSeat) >= 0 && (ii + startingSeat < seats[0].length)) {
					if (seats[row+1][ii+startingSeat] == 'o') {
						rowVacancies[row+1]--;
						totalVacancies--;
					}
					seats[row+1][ii+startingSeat] = 'x';
				}
			}
		}
	}

	private void bufferSeats(Set<String> seatsUsed) {
		
		for (String entry : seatsUsed) {
			String[] tokens = entry.split("-");
			int row = Integer.parseInt(tokens[0]);
			int col = Integer.parseInt(tokens[1]);
			if (row - 1 >= 0 && col -1 >= 0) {
				if (seats[row-1][col-1] == 'o') {
					seats[row-1][col-1] = 'x';
					rowVacancies[row-1]--;
					totalVacancies--;
				}
			}
			if (row - 1 >= 0) {
				if (seats[row-1][col] == 'o') {
					seats[row-1][col] = 'x';
					rowVacancies[row-1]--;
					totalVacancies--;
				}
			}
			if (row - 1 >= 0 && col +1 < seats[0].length) {
				if (seats[row-1][col+1] == 'o') {
					seats[row-1][col+1] = 'x';
					rowVacancies[row-1]--;
					totalVacancies--;
				}
			}
			for (int ii = 1; ii <= HORIZONTAL_BUFFER; ii++) {
				if (col + ii < seats[0].length) {
					if (seats[row][col+ii] == 'o') {
						seats[row][col+ii] = 'x';
						rowVacancies[row]--;
						totalVacancies--;
					}
				}
			}	
			if (row + 1 < seats.length && col -1 >= 0) {
				if (seats[row+1][col-1] == 'o') {
					seats[row+1][col-1] = 'x';
					rowVacancies[row+1]--;
					totalVacancies--;
				}
			}
			if (row + 1 < seats.length) {
				if (seats[row+1][col] == 'o') {
					seats[row+1][col] = 'x';
					rowVacancies[row+1]--;
					totalVacancies--;
				}
			}
			if (row + 1 < seats.length && col +1 < seats[0].length) {
				if (seats[row+1][col+1] == 'o') {
					seats[row+1][col+1] = 'x';
					rowVacancies[row+1]--;
					totalVacancies--;
				}
			}
		}
	}
	
	private String reserveSplitRows(int numSeats) {
		
		Set<String> seatsUsed = new TreeSet<>();
		int remainingSeats = numSeats;
		int group = numSeats - 1;
		int row;
		while(remainingSeats > 0) {
			row = findSingleRow(group);
			if (row != -1) {
				int startingSeat = seats[0].length - rowVacancies[row];
				remainingSeats -= group;
				for (int ii = 0; ii < group; ii++) {
					seats[row][startingSeat + ii] = '*';
					rowVacancies[row]--;
					totalVacancies--;
					seatsUsed.add("" + row + "-" + (startingSeat + ii)); 
				}
				group = remainingSeats + 1;
			}
			group--;
		}
		
		bufferSeats(seatsUsed);
		
		StringBuilder response = new StringBuilder();
		for (String entry : seatsUsed) {
			String[] tokens = entry.split("-");
			response.append( (char)(Integer.parseInt(tokens[0]) + 65) + "" + (Integer.parseInt(tokens[1]) + 1) + ",");
		}
		response.deleteCharAt(response.length()-1);
		return response.toString();
	}
	
	private int findSingleRow(int numSeats) {

		int row = -1;
		int largest = -1;  
		int ii = rowVacancies.length / 2;
		for (int stepper = 0; stepper < rowVacancies.length; stepper++) {
			ii += (stepper % 2 == 0 ? stepper : (-1 * stepper));
			if (numSeats <= rowVacancies[ii]) {
				if (largest < rowVacancies[ii]) {
					row = ii;
					largest = rowVacancies[ii];
				}
			}
		}
		return row;
	}
	
	private boolean checkVacancy (int numSeats) {
		boolean available = false;
		if (numSeats <= totalVacancies) {
			available = true;
		}
		return available;
	}
	
	public void testPrint() {
		System.out.print("                    ");
		System.out.println("[[     SCREEN     ]]");
		System.out.print("                    ");
		System.out.println("--------------------");
		for (int ii = 0; ii < seats.length; ii++)
			System.out.println(Arrays.toString(seats[ii]));
		System.out.println("Total vacancies - " + totalVacancies);
		System.out.println("Row Vacancies - " + Arrays.toString(rowVacancies));
	}
	
}
