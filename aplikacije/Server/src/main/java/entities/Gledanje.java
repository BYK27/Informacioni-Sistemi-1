package entities;
 
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "gledanje")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Gledanje.findAll", query = "SELECT g FROM Gledanje g"),
    @NamedQuery(name = "Gledanje.findByIdGledanje", query = "SELECT g FROM Gledanje g WHERE g.idGledanje = :idGledanje"),
    @NamedQuery(name = "Gledanje.findByIdKorisnik", query = "SELECT g FROM Gledanje g WHERE g.idKorisnik = :idKorisnik"),
    @NamedQuery(name = "Gledanje.findByIdVideo", query = "SELECT g FROM Gledanje g WHERE g.idVideo = :idVideo"),
    @NamedQuery(name = "Gledanje.findByDatumPocetka", query = "SELECT g FROM Gledanje g WHERE g.datumPocetka = :datumPocetka"),
    @NamedQuery(name = "Gledanje.findBySekundPocetka", query = "SELECT g FROM Gledanje g WHERE g.sekundPocetka = :sekundPocetka"),
    @NamedQuery(name = "Gledanje.findBySekundOdgledano", query = "SELECT g FROM Gledanje g WHERE g.sekundOdgledano = :sekundOdgledano")
})
public class Gledanje implements Serializable 
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGledanje")
    private Integer idGledanje;
    @Column(name = "idKorisnik")
    private Integer idKorisnik;
    @Column(name = "idVideo")
    private Integer idVideo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumPocetka")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundPocetka")
    private int sekundPocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundOdgledano")
    private int sekundOdgledano;

    public Gledanje()
    {
    }

    public Gledanje(Integer idGledanje)
    {
        this.idGledanje = idGledanje;
    }

    public Gledanje(Integer idGledanje, Date datumPocetka, int sekundPocetka, int sekundOdgledano)
    {
        this.idGledanje = idGledanje;
        this.datumPocetka = datumPocetka;
        this.sekundPocetka = sekundPocetka;
        this.sekundOdgledano = sekundOdgledano;
    }

    public Integer getIdGledanje()
    {
        return idGledanje;
    }

    public void setIdGledanje(Integer idGledanje)
    {
        this.idGledanje = idGledanje;
    }

    public Integer getIdKorisnik()
    {
        return idKorisnik;
    }

    public void setIdKorisnik(Integer idKorisnik)
    {
        this.idKorisnik = idKorisnik;
    }

    public Integer getIdVideo()
    {
        return idVideo;
    }

    public void setIdVideo(Integer idVideo)
    {
        this.idVideo = idVideo;
    }

    public Date getDatumPocetka()
    {
        return datumPocetka;
    }

    public void setDatumPocetka(Date datumPocetka)
    {
        this.datumPocetka = datumPocetka;
    }

    public int getSekundPocetka()
    {
        return sekundPocetka;
    }

    public void setSekundPocetka(int sekundPocetka)
    {
        this.sekundPocetka = sekundPocetka;
    }

    public int getSekundOdgledano()
    {
        return sekundOdgledano;
    }

    public void setSekundOdgledano(int sekundOdgledano)
    {
        this.sekundOdgledano = sekundOdgledano;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idGledanje != null ? idGledanje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gledanje))
        {
            return false;
        }
        Gledanje other = (Gledanje) object;
        if ((this.idGledanje == null && other.idGledanje != null) || (this.idGledanje != null && !this.idGledanje.equals(other.idGledanje)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entities.Gledanje[ idGledanje=" + idGledanje + " ]";
    }

}
