package ch.roomManager.dao;

import ch.roomManager.dao.annotations.*;
import ch.roomManager.db.MySqlDB;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @param <T>
 * @author Bastian Kappeler, help: Bissbert
 */

public class DynamicDAO<T> {

  private final Class<T> tClass;
  private final List<Field> attributes;
  private final Field idField;
  private final String tableName;

  public DynamicDAO(Class<T> tClass) {
    this.tClass = tClass;
    attributes = new ArrayList<>();

    final ArrayList<Field> tempList = new ArrayList<>(List.of(tClass.getFields()));
    idField = getIdField(tClass);

    if (idField == null) {
      throw new NoIdException(tClass);
    } else {
      tempList.remove(idField);
    }
    attributes.addAll(tempList);

    Table table = tClass.getAnnotation(Table.class);
    tableName = table == null ? tClass.getSimpleName() : table.name();
  }

  public T getEntity(Integer id) {
    String sqlQuery = "SELECT " + getFieldGetString() + " FROM " + tableName + " WHERE " + idField.getName() + "=?;";
    Map<Integer, Integer> values = new HashMap<>();
    values.put(1, id);
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery, values);
      if (resultSet.next()) {
        return entityFromResultSet(resultSet);
      }
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    } finally {
      MySqlDB.sqlClose();
    }
    return null;
  }

  public List<T> getAll() {
    String sqlQuery = "SELECT " + getFieldGetString() + " FROM " + tableName;
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      return listFromResultSet(resultSet);
    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    } finally {
      MySqlDB.sqlClose();
    }
    return null;
  }

  public Result save(T t) {
    String sqlQuery = "REPLACE " + tableName + " SET " + getFieldSetString();

    try {
      MySqlDB.setPrepStmt(sqlQuery).setInt(1, idField.getInt(t));

      for (int i = 0; i < attributes.size(); i++) {
        if (attributes.get(i).isAnnotationPresent(SubId.class)) {
          MySqlDB.getPrepStmt().setObject(i + 1, getIdField(attributes.get(i)));
          continue;
        }
        MySqlDB.getPrepStmt().setObject(i + 1, attributes.get(i).get(t));
      }

      MySqlDB.sqlUpdate();
    } catch (SQLException e) {
      MySqlDB.printSQLException(e);
      throw new RuntimeException();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } finally {
      MySqlDB.sqlClose();
    }
    return MySqlDB.getResult();
  }

  public Result delete(Integer id) {
    String sqlQuery = "DELETE FROM " + tableName + " WHERE " + idField.getName() + "=?;";
    Map<Integer, Integer> values = new HashMap<>();
    values.put(1, id);
    try {
      MySqlDB.sqlUpdate(sqlQuery, values);
    } catch (SQLException e) {
      MySqlDB.printSQLException(e);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return MySqlDB.getResult();
  }

  public Integer count() {
    String sqlQuery = "SELECT COUNT(*) FROM " + tableName;
    try {
      ResultSet resultSet = MySqlDB.sqlSelect(sqlQuery);
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }

    } catch (SQLException sqlEx) {
      MySqlDB.printSQLException(sqlEx);
      throw new RuntimeException();
    } finally {
      MySqlDB.sqlClose();
    }
    return -1;
  }

  private T entityFromResultSet(ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
    T t = tClass.getDeclaredConstructor(tClass).newInstance();

    idField.set(t, resultSet.getInt(idField.getName()));
    for (Field attribute : attributes) {
      if (attribute.isAnnotationPresent(Ignore.class)) continue;
      if (attribute.isAnnotationPresent(SubId.class)) {
        Field subIdField = getIdField(attribute);
        int subId = resultSet.getInt(subIdField.getName());
        attribute.set(t, new DynamicDAO<>(attribute.getType()).getEntity(subId));
        continue;
      }

      attribute.set(t, resultSet.getObject(attribute.getName(), attribute.getType()));
    }

    return t;
  }

  private List<T> listFromResultSet(ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
    List<T> tList = new ArrayList<>();

    while (resultSet.next()) {
      tList.add(entityFromResultSet(resultSet));
    }

    return tList;
  }

  private String getFieldGetString() {
    return getFieldString(", ");
  }

  private String getFieldString(String delimiter) {
    StringJoiner stringJoiner = new StringJoiner(delimiter);
    stringJoiner.add(idField.getName());
    for (Field attribute : attributes) {
      if (attribute.isAnnotationPresent(Ignore.class)) continue;
      if (attribute.isAnnotationPresent(SubId.class)) {
        Field id = getIdField(attribute);
        stringJoiner.add(id == null ? attribute.getName() : id.getName());
        continue;
      }

      stringJoiner.add(attribute.getName());
    }
    return stringJoiner.toString();
  }

  private String getFieldSetString() {
    return getFieldString("=?, ");
  }

  private Field getIdField(Field attribute) {
    return getIdField(attribute.getType());
  }

  private Field getIdField(Class<?> type) {
    final ArrayList<Field> tempList = new ArrayList<>(List.of(type.getFields()));
    for (Field field : tempList) {
      field.setAccessible(true);

      if (field.isAnnotationPresent(Id.class)) {
        return field;
      }
    }
    return null;
  }

}
