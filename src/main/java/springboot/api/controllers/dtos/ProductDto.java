package springboot.api.controllers.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import springboot.api.entities.Product;

public class ProductDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Price is required")
  @Min(value = 1, message = "Price must be greater than 1")
  private Integer price;

  public ProductDto() {}

  public ProductDto(String name, String description, Integer price) {
    this.name = name;
    this.description = description;
    this.price = price;
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

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Product toEntity() {
    return new Product(null, name, description, price);
  }
}
