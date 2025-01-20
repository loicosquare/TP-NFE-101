package tp.fstl.nfe101.pharmacies.streams;

public class Pharmacie {
    private String streetType;
    private String postalCode;
    private String city;
    private String openingDate;
    private String authorizationDate;

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getAuthorizationDate() {
        return authorizationDate;
    }

    public void setAuthorizationDate(String authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    public Pharmacie() {
    }

    public Pharmacie(String streetType, String postalCode, String city, String openingDate, String authorizationDate) {
        this.streetType = streetType;
        this.postalCode = postalCode;
        this.city = city;
        this.openingDate = openingDate;
        this.authorizationDate = authorizationDate;
    }
}
