package wizut.tpsi.ogloszenia.services;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.web.OfferFilter;

@Service 
@Transactional //furka4y cz.3
public class OffersService {
    @PersistenceContext
    private EntityManager em; 

    
    public CarManufacturer getCarManufacturer(int id) {
        return em.find(CarManufacturer.class, id);
    }
    //furka4you cz.1, zad 2
    public CarModel getModel(int id) {
        return em.find(CarModel.class,id);
    }
    public List<CarManufacturer> getCarManufacturers() {
        String jpql = "select cm from CarManufacturer cm order by cm.name"; 
        TypedQuery<CarManufacturer> query = em.createQuery(jpql, CarManufacturer.class);
        List<CarManufacturer> result = query.getResultList();
        return result;
    }
    public List<BodyStyle> getBodyStyles() { //furka 4y cz.2, zad 1, 
        String jpql = "select bs from BodyStyle bs order by bs.name";
        TypedQuery<BodyStyle> query = em.createQuery(jpql, BodyStyle.class);
        List<BodyStyle> result = query.getResultList();
        return result;
    }
    public List<FuelType> getFuelTypes() { //furka 4y cz.2, zad 1
        String jpql = "select ft from FuelType ft order by ft.name";
        TypedQuery<FuelType> query = em.createQuery(jpql, FuelType.class);
        List<FuelType> result = query.getResultList();
        return result;
    }
    public List<CarModel> getCarModels(int manufacturerId) { //furka 4y cz.2, metoda do testu po zadaniu nr 1
        String jpql = "select cm from CarModel cm where cm.manufacturer.id = :id order by cm.name";
        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        query.setParameter("id", manufacturerId);
        return query.getResultList();
    }
    public List<CarModel> getCarModels() {
        String jpql = "select cm from CarModel cm order by cm.name";
        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        List<CarModel> result = query.getResultList();
        return result;
    }
    public List<Offer> getOffers() { //furka 4y cz.2 //zadanie 2, lista wszystkich ofert
        String jpql = "select o from Offer o order by o.title";
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        List<Offer> result = query.getResultList();
        return result;
    }
    public List<Offer> getOffersByModel(int modelId) { //furka 4y cz.2 //zadanie 2, lista ofert sprzedaży określonego modelu samochodu np.10
        String jpql = "select o from Offer o where o.model.id = :id order by o.title";
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class); 
        query.setParameter("id", modelId);
        return query.getResultList();
    }
    public List<Offer> getOffersByManufacturer(int manufacturerId,String description) { //furka 4y cz.2 //zadanie 2, lista ofert ofert sprzedaży określonej marki np o id 59
        String jpql = "select o from Offer o JOIN o.model M where M.manufacturer.id = :id";
        if (description!=null && !description.equals(""))
            jpql+=" and o.description like '"+description+"' order by o.title";
        else
            jpql+=" order by o.title";
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class); 
        query.setParameter("id", manufacturerId);
        return query.getResultList();
    }
    public Offer getOffer(int id) {
        return em.find(Offer.class, id);
    }
    public Offer createOffer(Offer offer) { //furka4y cz.3 funkcja odpowiadająca za zapis nowej oferty do bazy. Jej argumentem jest tutaj "offer" czyli wypełniony formularz ze strony w przeglądarce
        em.persist(offer);
        return offer;
    }
    
    public Offer deleteOffer(Integer id) { //furka4y cz.4 ta funkcja usuwa ogłoszenie z bazy o konkretnym id ogłoszenia
        Offer offer = em.find(Offer.class, id);
        em.remove(offer);
        return offer;
    }
    public Offer saveOffer(Offer offer) {//furka4y cz.4  zapisujemy ponownie ogłoszenie jakie edytowaliśmy
        return em.merge(offer);
    }
//    public List<CarModel> getModelsByManufacturer(int manufacturerId) {
////        String jpql = "select o from Offer o JOIN o.model M where M.manufacturer.id = :id order by o.title";
//        String jpql = "select cm from CarModel where cm.manufacturer.id = :id order by cm.title";
//        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class); 
//        query.setParameter("id", manufacturerId);
//        return query.getResultList();
//    }

 
}
