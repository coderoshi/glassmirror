package test.book.glass;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class PMF
{
  private static final PersistenceManagerFactory INSTANCE =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");
  
  private PMF() {}
  
  public static PersistenceManagerFactory get() {
    return INSTANCE;
  }

  @SuppressWarnings("unchecked")
  public static <T> T save( T c, Object o ) {
    PersistenceManager pm = get().getPersistenceManager();
    try {
      return (T)pm.makePersistent( o );
    } finally {
      pm.close();
    }
  }
}