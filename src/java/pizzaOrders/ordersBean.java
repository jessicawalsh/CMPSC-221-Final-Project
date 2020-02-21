/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizzaOrders;

/**
 *
 * @author jessw
 */
// AddressBean.java
// Bean for interacting with the pizza database
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition; 
import javax.inject.Named;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

// define the data source
@DataSourceDefinition(
   name = "java:global/jdbc/pizzaWeb",
   className = "org.apache.derby.jdbc.ClientDataSource",
   url = "jdbc:derby://localhost:1527/pizzaWeb",
   databaseName = "pizzaWeb",
   user = "app",
   password = "app")

@ManagedBean(name="ordersBean")
@SessionScoped
public class ordersBean implements Serializable
{
   // instance variables that represent an order entry   
   private String orderTime;
   private String totalPrice;
   private String orderID;
   
   //instance variables that are used for customer input
   private int pizza;
   private int side;
   private int drink;
   private String phoneNumberInput;
   private String firstNameInput;
   private String lastNameInput;
   private String emailInput; 
   private int  pizzaQuantity;
   private int sideQuantity;
   private int  drinkQuantity;

   // allow the server to inject the DataSource
   @Resource(lookup="java:global/jdbc/pizzaWeb")
   DataSource dataSource;
   
  
   //get the order time
   public String getOrderTime()
   {
       return orderTime;
   }
   
   //set the order time
   public void setorderTime(String orderTime)
   {
       this.orderTime = orderTime;
   }
   
   //get the total price
   public String getTotalPrice()
   {
       return totalPrice;
   }
   
   //set the total price
   public void setTotalPrice(String totalPrice)
   {
       this.totalPrice = totalPrice;
   }
   
   // return the firstName String
   public String getFirstNameInput()
   {
      return firstNameInput;
   }

   // set the firstName String
   public void setFirstNameInput(String firstNameInput)
   {
      this.firstNameInput = firstNameInput;
   }
   
   //return the lastName String
   public String getLastNameInput()
   {
       return lastNameInput;
   }
   
   //set the lastName String
   public void setLastNameInput(String lastNameInput)
   {
       this.lastNameInput = lastNameInput;
   }   

   // return the email String
   public String getEmailInput()
   {
      return emailInput;
   }

   // set the email String
   public void setEmailInput(String emailInput)
   {
      this.emailInput = emailInput;
   }

   // return the phone String
   public String getPhoneNumberInput()
   {
      return phoneNumberInput;
   } 

   // set the phone String
   public void setPhoneNumberInput(String phoneNumberInput)
   {
      this.phoneNumberInput = phoneNumberInput;
   } 
   
   // return the pizza String
   public int getPizza()
   {
      return pizza;
   } 

   // set the pizza String
   public void setPizza(int pizza)
   {
      this.pizza = pizza;
   } 
   
   // return the pizza qunatitu String
   public int getPizzaQuantity()
   {
      return pizzaQuantity;
   } 

   // set the pizza quantity String
   public void setPizzaQuantity(int pizzaQuantity)
   {
      this.pizzaQuantity = pizzaQuantity;
   } 
   
   // return the side String
   public int getSide()
   {
      return side;
   } 

   // set the side String
   public void setSide(int side)
   {
      this.side = side;
   } 
   
   // return the side quantity String
   public int getSideQuantity()
   {
      return sideQuantity;
   } 

   // set the side quantity String
   public void setSideQuantity(int sideQuantity)
   {
      this.sideQuantity = sideQuantity;
   }   
   
   
   // return the drink String
   public int getDrink()
   {
      return drink;
   } 

   // set the drinl String
   public void setDrink(int drink)
   {
      this.drink = drink;
   } 
   
   // return the drink quantity String
   public int getDrinkQuantity()
   {
      return drinkQuantity;
   } 

   // set the drink quantity String
   public void setDrinkQuantity(int drinkQuantity)
   {
      this.drinkQuantity = drinkQuantity;
   } 
   
   // return the orderId String
   public String getOrderID()
   {
      return orderID;
   } 

   // set the drink quantity String
   public void setOrderID(String orderId)
   {
      this.orderID = orderId;
   } 
   
   //get current time
   public String getTime()
   {
     return DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
   }
   
   //method to calculate the total price of an order
   public void calcTotalPrice(){
       int[] pizzaPrices = new int[]{8,9,6}; 
       int[] drinkPrices = new int[]{2,1}; 
       int[] sidePrices = new int[]{4,6}; 
       
       
       int sideAmount = getSideQuantity();
       int pizzaAmount = getPizzaQuantity();
       int drinkAmount = getDrinkQuantity();
       
       int sideVal = getSide();
       int pizzaVal = getPizza();
       int drinkVal = getDrink();   
       
       int pizzaTotal = pizzaAmount*pizzaPrices[pizzaVal-1];
       int drinkTotal = drinkAmount*drinkPrices[drinkVal-1];
       int sideTotal = sideAmount*sidePrices[sideVal-1];
       
       int total = pizzaTotal+drinkTotal+sideTotal;
       
       String totalAmount = Integer.toString(total); 
       
       totalPrice = totalAmount;
       
       
   }
   // returns result for rendering on the client
   public String getResult()
   {
      if (firstNameInput != null && lastNameInput != null  && emailInput != null && phoneNumberInput != null)
         return "<p style=\"background-color:yellow;width:200px;" +
            "padding:5px\">First Name: " + getFirstNameInput() + "<br/>Last Name:" + getLastNameInput()+ "<br/>E-Mail: " +
            getEmailInput() + "<br/>Phone: " + getPhoneNumberInput() + "</p>";
      else
         return ""; // request has not yet been made
   } 

   
   // return a ResultSet of entries
   public ResultSet getAddresses() throws SQLException
   {
      // check whether dataSource was injected by the server
      if (dataSource == null)
      {
         throw new SQLException("Unable to obtain DataSource");
      }
      
      // obtain a connection from the connection pool
      Connection connection = dataSource.getConnection();

      // check whether connection was successful
      if (connection == null)
      {
         throw new SQLException("Unable to connect to DataSource");
      }
      
      try
      {
         // create a PreparedStatement to insert a new address book entry
         PreparedStatement getAddresses = connection.prepareStatement(
            "SELECT ORDEREDITEMS.ORDERID, CUSTOMERS.PHONENUMBER, ORDERTIME, TOTALPRICE\n"
                    +"FROM ORDERS\n"
                    +"inner join ORDEREDITEMS on ORDEREDITEMS.ORDERID=ORDERS.ORDERID\n"
                    +"inner join CUSTOMERS on CUSTOMERS.PHONENUMBER=ORDERS.PHONENUMBER\n"
                    +"order by ORDERTIME");
   
         CachedRowSet rowSet = 
            RowSetProvider.newFactory().createCachedRowSet();
         rowSet.populate(getAddresses.executeQuery());
         return rowSet; 
      } 
      finally
      {
         connection.close(); // return this connection to pool
      } 
   } 
   
   // save a new order to the orders table
   public String save() throws SQLException
   {
      //method call to calculate the total price
      calcTotalPrice();
      
      //get the orderTime 
      Date orderDate = new Date();
      orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDate);
      
      //get the orderID
      int orderId = (int) (orderDate.getTime() & 0x0000000000ffffffL);
      orderID = Integer.toString(orderId);
       
      // check whether dataSource was injected by the server
      if (dataSource == null)
      {
         throw new SQLException("Unable to obtain DataSource");
      }
      
      // obtain a connection from the connection pool
      Connection connection = dataSource.getConnection();
      
      // check whether connection was successful
      if (connection == null)
      {
         throw new SQLException("Unable to connect to DataSource");
      }
      
      try
      {
        //add data to the customer table
        PreparedStatement addCustomer =
                connection.prepareStatement("INSERT INTO CUSTOMERS " +
                        "(PHONENUMBER, FIRSTNAME, LASTNAME, EMAIL) \n" +
                        " ( SELECT ?, ?, ?, ? \n" +
                        "FROM CUSTOMERS \n" +
                        " WHERE PHONENUMBER = ?" +
                        " HAVING count(*)=0 )");        
        
        //specify the PreparedStatement's arguments
        addCustomer.setString(1, getPhoneNumberInput());
        addCustomer.setString(2, getFirstNameInput());
        addCustomer.setString(3, getLastNameInput());
        addCustomer.setString(4, getEmailInput());
        addCustomer.setString(5, getPhoneNumberInput());
        addCustomer.executeUpdate(); //insert the entry
        
        //add data to the ordered items table
        PreparedStatement addOrderedItems = 
            connection.prepareStatement("INSERT INTO ORDEREDITEMS " +
               "(ORDERID,PIZZAID,PIZZAQN,SIDESID,SIDESQN, DRINKID, DRINKQN)" +
               " VALUES (?, ?, ?, ?, ?, ?, ?)");

         // specify the PreparedStatement's arguments
         addOrderedItems.setString(1, getOrderID());
         addOrderedItems.setInt(2, getPizza());
         addOrderedItems.setInt(3, getPizzaQuantity());
         addOrderedItems.setInt(4, getSide());
         addOrderedItems.setInt(5, getSideQuantity());
         addOrderedItems.setInt(6, getDrink());
         addOrderedItems.setInt(7, getDrinkQuantity());
         addOrderedItems.executeUpdate(); // insert the entry
        
        //add data to the orders table 
        PreparedStatement addEntry = 
            connection.prepareStatement("INSERT INTO ORDERS " +
               "(ORDERID,PHONENUMBER,ORDERTIME,TOTALPRICE)" +
               " VALUES (?, ?, ?,? )");

         // specify the PreparedStatement's arguments
         addEntry.setString(1, getOrderID());
         addEntry.setString(2, getPhoneNumberInput());
         addEntry.setString(3, getOrderTime());
         addEntry.setString(4, getTotalPrice());
         addEntry.executeUpdate(); // insert the entry
         return "index.xhtml"; // go back to index.xhtml page   
         
      } 
      finally
      {
         connection.close(); // return this connection to pool
      } 
   } 
} 
   
