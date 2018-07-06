package com.q1.exception.bt;

public class IllegalQuantityException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  
  public IllegalQuantityException(String text)
  {
    super(text + ": Order quantity exceeeds available capital for trading.");
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/exception/bt/IllegalQuantityException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */