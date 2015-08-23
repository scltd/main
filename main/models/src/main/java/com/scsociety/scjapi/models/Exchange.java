package com.scsociety.scjapi.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Exchange implements Serializable {
  /**
	 * 
	 */
  private static final long serialVersionUID = -8188776611083482283L;
  private String name;
  private Integer id;
  private String description;

  public Exchange(Integer id) {
    this.id = id;
  }

  public Exchange() {

  }

  public void parse(ResultSet r) {
    try {
      this.setId(r.getInt("id"));
      this.setName(r.getString("name"));
      this.setDescription(r.getString("desc"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public final String getName() {
    return name;
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final Integer getId() {
    return id;
  }

  public final void setId(Integer id) {
    this.id = id;
  }

  public final String getDescription() {
    return description;
  }

  public final void setDescription(String description) {
    this.description = description;
  }
}
