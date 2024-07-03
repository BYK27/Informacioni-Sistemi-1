package entities;
 
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "video")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findByIdVideo", query = "SELECT v FROM Video v WHERE v.idVideo = :idVideo"),
    @NamedQuery(name = "Video.findByNaziv", query = "SELECT v FROM Video v WHERE v.naziv = :naziv"),
    @NamedQuery(name = "Video.findByTrajanje", query = "SELECT v FROM Video v WHERE v.trajanje = :trajanje"),
    @NamedQuery(name = "Video.findByIdKorisnik", query = "SELECT v FROM Video v WHERE v.idKorisnik = :idKorisnik"),
    @NamedQuery(name = "Video.findByDatumPostavljanja", query = "SELECT v FROM Video v WHERE v.datumPostavljanja = :datumPostavljanja")
})
public class Video implements Serializable 
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idVideo")
    private Integer idVideo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Naziv")
    private String naziv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trajanje")
    private BigDecimal trajanje;
    @Column(name = "idKorisnik")
    private Integer idKorisnik;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumPostavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPostavljanja;
    @ManyToMany(mappedBy = "videoList")
    private List<Kategorija> kategorijaList;

    public Video()
    {
    }

    public Video(Integer idVideo)
    {
        this.idVideo = idVideo;
    }

    public Video(Integer idVideo, String naziv, BigDecimal trajanje, Date datumPostavljanja)
    {
        this.idVideo = idVideo;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datumPostavljanja = datumPostavljanja;
    }

    public Integer getIdVideo()
    {
        return idVideo;
    }

    public void setIdVideo(Integer idVideo)
    {
        this.idVideo = idVideo;
    }

    public String getNaziv()
    {
        return naziv;
    }

    public void setNaziv(String naziv)
    {
        this.naziv = naziv;
    }

    public BigDecimal getTrajanje()
    {
        return trajanje;
    }

    public void setTrajanje(BigDecimal trajanje)
    {
        this.trajanje = trajanje;
    }

    public Integer getIdKorisnik()
    {
        return idKorisnik;
    }

    public void setIdKorisnik(Integer idKorisnik)
    {
        this.idKorisnik = idKorisnik;
    }

    public Date getDatumPostavljanja()
    {
        return datumPostavljanja;
    }

    public void setDatumPostavljanja(Date datumPostavljanja)
    {
        this.datumPostavljanja = datumPostavljanja;
    }

    @XmlTransient
    public List<Kategorija> getKategorijaList()
    {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList)
    {
        this.kategorijaList = kategorijaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (idVideo != null ? idVideo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Video))
        {
            return false;
        }
        Video other = (Video) object;
        if ((this.idVideo == null && other.idVideo != null) || (this.idVideo != null && !this.idVideo.equals(other.idVideo)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entities.Video[ idVideo=" + idVideo + " ]";
    }

}
