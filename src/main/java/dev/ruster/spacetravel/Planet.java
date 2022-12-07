package dev.ruster.spacetravel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Planet {

    MERCURY("Mercure", 0.385),
    VENUS("Venus", 0.903),
    EARTH("Terre", 1.0),
    MARS("Mars", 1.524),
    JUPITER("Jupiter", 5.203),
    SATURN("Saturne", 9.539),
    URANUS("Uranus", 19.18),
    NEPTUNE("Neptune", 30.06),
    ;

    private static final double UA_IN_KM = 149597871.0;
    private static final double LIGHT_SPEED_IN_KM_PER_S = 299792.458;

    private final String name;
    private final double distanceFromSun;

    @Contract(pure = true)
    Planet(String name, double distanceFromSun) {
        this.name = name;
        this.distanceFromSun = distanceFromSun;
    }

    @Contract(pure = true)
    public double distanceInUA(@NotNull Planet planet) {
        return Math.abs(distanceFromSun - planet.distanceFromSun);
    }

    @Contract(pure = true)
    public double distanceInKM(@NotNull Planet planet) {
        return distanceInUA(planet) * UA_IN_KM;
    }

    @Contract(pure = true)
    public double travelTimeInSTo(@NotNull Planet planet) {
        return distanceInKM(planet) / LIGHT_SPEED_IN_KM_PER_S;
    }

    @Contract(pure = true)
    public double travelTimeInSTo(@NotNull Planet planet, double speedInKmPerS) {
        return distanceInKM(planet) / speedInKmPerS;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public double getDistanceFromSun() {
        return distanceFromSun;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", distanceFromSun=" + distanceFromSun +
                '}';
    }
}