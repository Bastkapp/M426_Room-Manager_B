package ch.roomManager.dao.annotations;

public class NoIdException extends RuntimeException {
  public <T> NoIdException(Class<T> tClass) {
    super("The class " + tClass.getSimpleName() + " has no set ID");
  }
}
