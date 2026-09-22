package es.upm.miw.devops.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;
  private String familyName;
  private String email;
  private String identity;
  private String address;
  private String city;
  private String province;
  private String postalCode;

  @Enumerated(EnumType.STRING)
  private Role role;

  private boolean active;

  protected User() {}

  public User(
      String firstName,
      String familyName,
      String email,
      String identity,
      String address,
      String city,
      String province,
      String postalCode,
      boolean active) {
    this(
        firstName,
        familyName,
        email,
        identity,
        address,
        city,
        province,
        postalCode,
        Role.USER,
        active);
  }

  public User(
      String firstName,
      String familyName,
      String email,
      String identity,
      String address,
      String city,
      String province,
      String postalCode,
      Role role,
      boolean active) {
    this.firstName = firstName;
    this.familyName = familyName;
    this.email = email;
    this.identity = identity;
    this.address = address;
    this.city = city;
    this.province = province;
    this.postalCode = postalCode;
    this.role = role;
    this.active = active;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isAdmin() {
    return Role.ADMIN == role;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isBillable() {
    return BillablePolicy.isBillable(this);
  }
}
