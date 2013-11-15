package test.book.glass;

import java.util.List;

public class PageList<T>
{
  private List<T> list;
  private int current;
  private boolean more;

  public PageList( List<T> list, int current, boolean more) {
    this.list = list;
    this.current = current;
    this.more = more;
  }
  
  public List<T> getList()
  {
    return list;
  }
  public void setList(List<T> list)
  {
    this.list = list;
  }
  public int getCurrent()
  {
    return current;
  }
  public void setCurrent(int current)
  {
    this.current = current;
  }
  public boolean isMore()
  {
    return more;
  }
  public void setMore(boolean more)
  {
    this.more = more;
  }
}
