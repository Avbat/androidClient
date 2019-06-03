package io.cell.androidclient.model;

import java.util.UUID;

public class Cell implements Comparable<Cell> {
  private UUID cellId;
  private Address address;
  private Float movementRate;
  private Boolean movable;
  private Float flightRate;
  private Boolean flyable;
  private String presentationImage;
  private String backgroundImage;

  public UUID getCellId() {
    return cellId;
  }

  public Cell setCellId(UUID cellId) {
    this.cellId = cellId;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Cell setAddress(Address address) {
    this.address = address;
    return this;
  }

  public Float getMovementRate() {
    return movementRate;
  }

  public Cell setMovementRate(Float movementRate) {
    this.movementRate = movementRate;
    return this;
  }

  public Boolean isMovable() {
    return movable;
  }

  public Cell setMovable(Boolean movable) {
    this.movable = movable;
    return this;
  }

  public Float getFlightRate() {
    return flightRate;
  }

  public Cell setFlightRate(Float flightRate) {
    this.flightRate = flightRate;
    return this;
  }

  public Boolean isFlyable() {
    return flyable;
  }

  public Cell setFlyable(Boolean flyable) {
    this.flyable = flyable;
    return this;
  }

  public String getPresentationImage() {
    return presentationImage;
  }

  public Cell setPresentationImage(String presentationImage) {
    this.presentationImage = presentationImage;
    return this;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public Cell setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Cell that = (Cell) o;

    if (!cellId.equals(that.cellId)) return false;
    if (!address.equals(that.address)) return false;
    if (movementRate != null ? !movementRate.equals(that.movementRate) : that.movementRate != null) return false;
    if (movable != null ? !movable.equals(that.movable) : that.movable != null) return false;
    if (flightRate != null ? !flightRate.equals(that.flightRate) : that.flightRate != null) return false;
    if (flyable != null ? !flyable.equals(that.flyable) : that.flyable != null) return false;
    if (presentationImage != null ? !presentationImage.equals(that.presentationImage) : that.presentationImage != null)
      return false;
    return backgroundImage != null ? backgroundImage.equals(that.backgroundImage) : that.backgroundImage == null;

  }

  @Override
  public int hashCode() {
    int result = cellId.hashCode();
    result = 31 * result + address.hashCode();
    result = 31 * result + (movable != null ? movable.hashCode() : 0);
    result = 31 * result + (flyable != null ? flyable.hashCode() : 0);
    result = 31 * result + (presentationImage != null ? presentationImage.hashCode() : 0);
    result = 31 * result + (backgroundImage != null ? backgroundImage.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Cell{" +
        "cellId=" + cellId +
        ", address=" + address +
        ", movementRate=" + movementRate +
        ", movable=" + movable +
        ", flightRate=" + flightRate +
        ", flyable=" + flyable +
        ", presentationImage='" + presentationImage + '\'' +
        ", backgroundImage='" + backgroundImage + '\'' +
        '}';
  }

  @Override
  public int compareTo(Cell cellState) {
    if (cellState.getAddress() == null) {
      return 1;
    }
    int compareToX = cellState.getAddress().getX();
    int compareToY = cellState.getAddress().getY();
    int result = this.getAddress().getX() - compareToX;
    if (result == 0) {
      result = this.getAddress().getY() - compareToY;
    }
    return result;
  }

}
