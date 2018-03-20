package ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by USER on 27.02.2018.
 */

public class Order implements Serializable {
    public Long orderId;
    public Long tradeOutletId;
    public String tradeOutletName;
    public Long orderDate;

    public Order(Long orderId, Long orderDate, String tradeOutletName, long tradeOutletId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.tradeOutletName = tradeOutletName;
        this.tradeOutletId = tradeOutletId;
        this.orderLines = new ArrayList<OrderLine>();
    }

    public Order(Long orderId, Long orderDate) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderLines = new ArrayList<OrderLine>();
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public String getOrderDate(Boolean toString) {
        Date dateOrderDate = new Date(orderDate);

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.LONG);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); //Установка временной зоны
        //String text = dateFormat.format(dateFormat);

        return dateFormat.format(dateOrderDate);
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public static ArrayList<OrderLine> orderLines;

    public String getTradeOutletName() {
        return tradeOutletName;
    }

    public void setTradeOutletName(String tradeOutletName) {
        this.tradeOutletName = tradeOutletName;
    }

    public void addLine(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTradeOutletId() {
        return tradeOutletId;
    }

    public void setTradeOutletId(Long tradeOutletId) {
        this.tradeOutletId = tradeOutletId;
    }

    public ArrayList<OrderLine> getOrderLines() {
        return orderLines;
    }

    public OrderLine getOrderLine(int id) {
        return orderLines.get(id);
    }
}
