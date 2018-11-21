/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "order_line")
@NamedQueries({
    @NamedQuery(name = "OrderLine.findAll", query = "SELECT o FROM OrderLine o"),
    @NamedQuery(name = "OrderLine.findByOrderlineid", query = "SELECT o FROM OrderLine o WHERE o.orderlineid = :orderlineid"),
    @NamedQuery(name = "OrderLine.findByRate", query = "SELECT o FROM OrderLine o WHERE o.rate = :rate"),
    @NamedQuery(name = "OrderLine.findByTotal", query = "SELECT o FROM OrderLine o WHERE o.total = :total")})
public class OrderLine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "orderlineid")
    private Integer orderlineid;
    @Column(name = "rate")
    private Float rate;
    @Column(name = "total")
    private Float total;
    @JoinColumn(name = "stockid", referencedColumnName = "stockid")
    @ManyToOne
    private Stock stock;
    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
    @ManyToOne
    private Oreder oreder;

    public OrderLine() {
    }

    public OrderLine(Integer orderlineid) {
        this.orderlineid = orderlineid;
    }

    public Integer getOrderlineid() {
        return orderlineid;
    }

    public void setOrderlineid(Integer orderlineid) {
        this.orderlineid = orderlineid;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Oreder getOreder() {
        return oreder;
    }

    public void setOreder(Oreder oreder) {
        this.oreder = oreder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderlineid != null ? orderlineid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderLine)) {
            return false;
        }
        OrderLine other = (OrderLine) object;
        if ((this.orderlineid == null && other.orderlineid != null) || (this.orderlineid != null && !this.orderlineid.equals(other.orderlineid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventory.beans.OrderLine[orderlineid=" + orderlineid + "]";
    }

}
