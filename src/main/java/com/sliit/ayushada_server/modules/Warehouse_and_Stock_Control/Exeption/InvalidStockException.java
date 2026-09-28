package com.sliit.ayushada_server.modules.Warehouse_and_Stock_Control.Exeption;

public class InvalidStockException extends RuntimeException {
  public InvalidStockException(String message) {
    super(message);
  }
}
