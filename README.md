# MovieTheaterChallenge

## Overview

This program simulates a movie theater ticket reservation system with social distancing measures.  It takes as an argument the full path of an input file containing a series of purchase attempts.

### Assumptions

- That keeping space on hand for large groups to sit unbroken is a higher priority than seating early reservations in the middle row.  Therefore, seating is based on the largest open row at the time.
- That the theater will be busy and likely fill. 
- That unfulfillable reservations are parsed from the standard output format elsewhere.  The system maintains the same output structure regardless of whether a booking was actually possible no special message.
- That a diagonal buffer exists between patrons.  Taken from the sample output where `R001 I1,I2` and `R004 J4,J5,J6` are returned.  Therefore, buffering looks like: 
`O O X X X O O`     `O O O X O O O`
`X X X * X X X` not `X X X * X X X`
`O O X X X O O`     `O O O X O O O`
- The the input file is formatted correctly.  There is not error checking on input.

### Implementation

- Searches for the row with the most openings from the middle row outward
- Places patrons into the lowest numbered available seats in selected row
- When necessary, splits groups into the largest subgroups possible (e.g. 8 searches for 7 + 1 before 4 + 4)

### Maximized Customer Satisfaction

Because it is assumed the theater will likely fill, I thought it more detrimental to allow the middle rows potentially fill sparsely than to move people to the far front and rear of the theater.  With a middle first strategy the worst case scenario, 5 individual purchases at the start, would completely fill one whole row and turn the neighboring rows in clusters of pairs.  I decided that net customer happiness would likely be higher if larger groups had a better chance of sitting together.

### Maximized Customer Safety

While the problem only explicitly states that 3 seats horizontal or 1 seat vertically needed to be kept as a buffer, the example output seemed to imply a diagonal buffer as well.  I thought this also fit logically with both the standard of 6 feet social distancing and some domain knowledge that at a theater diagonal seats are typically closer than 3 away in the same row.

### Building Instructions

1. Download all files from this repo into the same folder
3. Compile Theater.java with `javac Theater.java`
4. Compile Driver.java with `javac Driver.java`
5. Run with `java Driver [input file path]`

To Run Tests
1. Download [JUnit5 standalone console](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/) to the same folder as previous files
  - I used junit-platform-console-standalone-1.7.1.jar 
2. Compile TheaterTest.java with `javac -cp .\junit-platform-console-standalone-1.7.1.jar TheaterTests.java Theater.java`
3. Run with `java -jar junit-platform-console-standalone-1.7.1.jar --class-path . -c TheaterTests`
