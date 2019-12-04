package com.revature.bean;

/**
 * A DTO (Data Transfer Object) is a java bean that is translated into a ready to transfer object
 * that is sent to the front end or visa-versa. DTO's allow us to pass data around the application
 * without making more Http requests or responses than necessary by passing the data in a single
 * request or response.
 */
public class CarDto {
  /**
   * The number of seats a user's car has.
   */
  private int seatNumber;

  public int getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(int seatNumber) {
    this.seatNumber = seatNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + seatNumber;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CarDto other = (CarDto) obj;
    if (seatNumber != other.seatNumber) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CarDto [seatNumber=" + seatNumber + "]";
  }

  public CarDto(int seatNumber) {
    super();
    this.seatNumber = seatNumber;
  }

  public CarDto() {
    super();
  }

}
