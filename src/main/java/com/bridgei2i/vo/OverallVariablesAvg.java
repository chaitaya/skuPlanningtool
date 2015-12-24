package com.bridgei2i.vo;
 
import java.math.BigDecimal;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name="overallvariablesavg")
public class OverallVariablesAvg {
 
       @Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
       
       @Column(name="columnName")
       private String columnName;
         
       @Column(name="value")
       private BigDecimal value;
 
       public Integer getId() {
              return id;
       }
 
       public void setId(Integer id) {
              this.id = id;
       }
 
       public String getColumnName() {
              return columnName;
       }
 
       public void setColumnName(String columnName) {
              this.columnName = columnName;
       }
 
       public BigDecimal getValue() {
              return value;
       }
 
       public void setValue(BigDecimal value) {
              this.value = value;
       }
       
 
}
 
