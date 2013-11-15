package test.book.glass.blog.models;

import test.book.glass.PMF;

public abstract class Base<T>
{
  @SuppressWarnings("unchecked")
  public T save()
  {
    return (T)PMF.save( this.getClass(), this );
  }
}
