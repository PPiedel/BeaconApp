package pl.yahoo.pawelpiedel.data.place;


import java.util.Objects;

public class Place {
    private String placeType;
    private String name;
    private String description;

    public Place(String placeType, String name, String description) {
        this.placeType = placeType;
        this.name = name;
        this.description = description;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(placeType, place.placeType) &&
                Objects.equals(name, place.name) &&
                Objects.equals(description, place.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(placeType, name, description);
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeType='" + placeType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
