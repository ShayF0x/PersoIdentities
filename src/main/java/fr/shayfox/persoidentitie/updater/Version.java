package fr.shayfox.persoidentitie.updater;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Version implements Comparable<Version>{
    private final int[] version;

    public Version(String version) {
        final int v = !version.isEmpty() && version.charAt(0) == 'v' ? 1 : 0;
        final int sep = version.indexOf('-');
        final String numbers = sep < 0 ? version.substring(v) : version.substring(v, sep);
        this.version = Arrays.stream(numbers.split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public Version(int... version) {
        this.version = version;
    }

    @Override
    public int compareTo(Version version) {
        final int length = Math.max(this.version.length, version.version.length);
        for (int i = 0; i < length; i++) {
            final int diff = Integer.compare(
                    this.version.length > i ? this.version[i] : 0,
                    version.version.length > i ? version.version[i] : 0
            );
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.compareTo((Version) o) == 0;
    }

    @Override
    public String toString() {
        return String.join(".", Arrays.stream(version).mapToObj(value -> String.valueOf(value)).collect(Collectors.toList()));
    }
}
