package dev.ruster.spacetravel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class SpaceTravel {

    private static final double ROCKET_SPEED_IN_KM_PER_S = 4.0;

    public static void main(String[] args) {
        System.out.println("Welcome to Space Travel agency");
        boolean running = true;
        boolean planetSelected = false;
        Planet departure = null;
        Planet arrival = null;

        while (running) {
            System.out.println("What do you want to do? [h for help]");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                return;
            }

            switch (input.charAt(0)) {
                case 'h' -> {
                    System.out.println("h - help");
                    System.out.println("q - quit");
                    System.out.println("p - select planet");
                    System.out.println("t - travel to planet");
                }
                case 'l' -> {
                    System.out.println("List of the planets :");
                    System.out.println(planetsAsString());
                }
                case 'd' -> {
                    departure = choosePlanet(scanner);
                    System.out.println("Departure planet set to " + departure.getName());
                    arrival = choosePlanet(scanner);
                    System.out.println("Arrival planet set to " + arrival.getName());
                    planetSelected = true;

                    double distanceInUA = departure.distanceInUA(arrival);
                    double distanceInKM = departure.distanceInKM(arrival);
                    double travelTimeInSto = departure.travelTimeInSTo(arrival);
                    double travelTimeInKmPerS = departure.travelTimeInSTo(arrival, ROCKET_SPEED_IN_KM_PER_S);

                    // distance in UA
                    System.out.println("Distance between " + departure.getName() + " and " + arrival.getName() + " is " + distanceInUA + " UA");
                    // distance in KM reported in millions of KM if > 1 million
                    System.out.println("Distance between " + departure.getName() + " and " + arrival.getName() + " is " + (distanceInKM > 1000000 ? distanceInKM / 1000000 + " million" : distanceInKM) + " KM");
                    // travel time at the speed of light
                    System.out.println("At the speed of light it would take " + travelTimeInSto + " seconds to travel between " + departure.getName() + " and " + arrival.getName());
                    // travel time at the normal speed
                    System.out.println("At the speed of " + ROCKET_SPEED_IN_KM_PER_S + " KM per second it would take " + travelTimeInKmPerS + " seconds to travel between " + departure.getName() + " and " + arrival.getName());
                }
                case 't' -> {
                    if (!planetSelected) {
                        System.out.println("You must select a planet first");
                        break;
                    }
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = chooseDate(scanner, calendar);
                    double travelTimeInKmPerS = departure.travelTimeInSTo(arrival, ROCKET_SPEED_IN_KM_PER_S);
                    String travelDirection;

                    do {
                        System.out.println("Do you want to travel to " + arrival.getName() + " or from " + arrival.getName() + "? [a/f/q]");
                        travelDirection = scanner.nextLine();
                    } while (travelDirection != null && (!travelDirection.equals("a") && !travelDirection.equals("f") && !travelDirection.equals("q")));

                    if (travelDirection.equals("a")) {
                        calendar.add(Calendar.SECOND, (int) travelTimeInKmPerS);
                        System.out.println("You leave " + departure.getName() + " on " + dateFormat.format(date.getTime()) + " and arrive on " + dateFormat.format(new Date(date.getTime() + (long) travelTimeInKmPerS * 1000)));} else {
                        calendar.add(Calendar.SECOND, (int) -travelTimeInKmPerS);
                        System.out.println("You leave " + arrival.getName() + " on " + dateFormat.format(date.getTime()) + " and arrive on " + dateFormat.format(new Date(date.getTime() - (long) travelTimeInKmPerS * 1000)));
                    }
                }
                case 'q' -> {
                    System.out.println("Bye Bye!");
                    running = false;
                }
                default -> System.out.println("Unknown command. Type h for help");
            }
        }
    }

    private static Planet choosePlanet(@NotNull Scanner scanner) {
        System.out.println("Choose a planet : \n[" + planetsAsString() + "]");
        String input = scanner.nextLine();

        return Arrays.stream(Planet.values())
                .filter(p -> p.name().equalsIgnoreCase(input) || p.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElseGet(() -> SpaceTravel.choosePlanet(scanner));
    }

    private static @NotNull Date chooseDate(@NotNull Scanner scanner, @NotNull Calendar calendar) {
        System.out.println("When do you want to leave ?");

        int year = ask("Year", scanner, 0, 9999);
        int month = ask("Month", scanner, 1, Month.values().length);
        int day = ask("Day", scanner, 1, Month.of(month).maxLength());
        int hour = ask("Hour", scanner, 0, 23);
        int minute = ask("Minute", scanner, 0, 59);
        int second = ask("Second", scanner, 0, 59);

        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    private static int ask(@NotNull String question, @NotNull Scanner scanner, int min, int max) {
        System.out.print(question + " :");
        int value = Integer.parseInt(scanner.nextLine());

        if (value < min || value > max) {
            System.out.println("Invalid value, must be between " + min + " and " + max);
            return ask(question, scanner, min, max);
        }
        return value;
    }

    @Contract(" -> !null")
    private static String planetsAsString() {
        return Arrays.stream(Planet.values()).map(Planet::getName).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
    }
}