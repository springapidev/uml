/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "oreder")
@NamedQueries({
    @NamedQuery(name = "Oreder.findAll", query = "SELECT o FROM Oreder o"),
    @NamedQuery(name = "Oreder.findByOrderId", query = "SELECT o FROM Oreder o WHERE o.orderId = :orderId"),
    @NamedQuery(name = "Oreder.findByOrderDate", query = "SELECT o FROM Oreder o WHERE o.orderDate = :orderDate"),
    @NamedQuery(name = "Oreder.findByTotalAmount", query = "SELECT o FROM Oreder o WHERE o.totalAmount = :totalAmount"),
    @NamedQuery(name = "Oreder.findByCommission", query = "SELECT o FROM Oreder o WHERE o.commission = :commission"),
    @NamedQuery(name = "Oreder.findByTax", query = "SELECT o FROM Oreder o WHERE o.tax = :tax")})
public class Oreder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "orderId")
    private Integer orderId;
    @Column(name = "orderDate")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Column(name = "totalAmount")
    private Float totalAmount;
    @Column(name = "commission")
    private Float commission;
    @Column(name = "tax")
    private Float tax;
    @OneToMany(mappedBy = "oreder")
    private List<OrderLine> orderLineList;
    @JoinColumn(name = "clientId", referencedColumnName = "clientId")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "orderTypeId", referencedColumnName = "orderTypeId")
    @ManyToOne
    private OrderType orderType;

    public Oreder() {
    }

    public Oreder(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Float getCommission() {
        return commission;
    }

    public void setCommission(Float commission) {
        this.commission = commission;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public List<OrderLine> getOrderLineList() {
        return orderLineList;
    }

    public void setOrderLineList(List<OrderLine> orderLineList) {
        this.orderLineList = orderLineList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oreder)) {
            return false;
        }
        Oreder other = (Oreder) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventory.beans.Oreder[orderId=" + orderId + "]";
    }

}
