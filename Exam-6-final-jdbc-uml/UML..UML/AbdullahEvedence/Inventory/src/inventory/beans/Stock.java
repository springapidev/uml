/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.beans;

import java.io.Serializable;
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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "stock")
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s"),
    @NamedQuery(name = "Stock.findByStockid", query = "SELECT s FROM Stock s WHERE s.stockid = :stockid"),
    @NamedQuery(name = "Stock.findByStockInHand", query = "SELECT s FROM Stock s WHERE s.stockInHand = :stockInHand"),
    @NamedQuery(name = "Stock.findByStocklevel", query = "SELECT s FROM Stock s WHERE s.stocklevel = :stocklevel")})
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "stockid")
    private Integer stockid;
    @Column(name = "stockInHand")
    private Integer stockInHand;
    @Column(name = "stocklevel")
    private Integer stocklevel;
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    @ManyToOne
    private Product product;
    @OneToMany(mappedBy = "stock")
    private List<OrderLine> orderLineList;

    public Stock() {
    }

    public Stock(Integer stockid) {
        this.stockid = stockid;
    }

    public Integer getStockid() {
        return stockid;
    }

    public void setStockid(Integer stockid) {
        this.stockid = stockid;
    }

    public Integer getStockInHand() {
        return stockInHand;
    }

    public void setStockInHand(Integer stockInHand) {
        this.stockInHand = stockInHand;
    }

    public Integer getStocklevel() {
        return stocklevel;
    }

    public void setStocklevel(Integer stocklevel) {
        this.stocklevel = stocklevel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<OrderLine> getOrderLineList() {
        return orderLineList;
    }

    public void setOrderLineList(List<OrderLine> orderLineList) {
        this.orderLineList = orderLineList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockid != null ? stockid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.stockid == null && other.stockid != null) || (this.stockid != null && !this.stockid.equals(other.stockid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventory.beans.Stock[stockid=" + stockid + "]";
    }

}
