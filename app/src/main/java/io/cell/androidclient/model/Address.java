package io.cell.androidclient.model;

import java.util.Objects;
import java.util.UUID;


public class Address {
  private UUID id;
  private Integer regionIndex;
  private Integer x;
  private Integer y;


  public UUID getId() {
    return id;
  }

  public Address setId(UUID id) {
    this.id = id;
    return this;
  }

  public Integer getRegionIndex() {
    return regionIndex;
  }

  public Address setRegionIndex(Integer regionIndex) {
    this.regionIndex = regionIndex;
    return this;
  }

  public Integer getX() {
    return x;
  }

  public Address setX(Integer x) {
    this.x = x;
    return this;
  }

  public Integer getY() {
    return y;
  }

  public Address setY(Integer y) {
    this.y = y;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return regionIndex.equals(address.regionIndex) &&
        x.equals(address.x) &&
        y.equals(address.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionIndex, x, y);
  }

  @Override
  public String toString() {
    return "Address{" +
        regionIndex +
        "." + x +
        "." + y +
        "}";
  }
}
