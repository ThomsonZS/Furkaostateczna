package wizut.tpsi.ogloszenia.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.services.OffersService;
import wizut.tpsi.ogloszenia.web.OfferFilter;


@Controller
public class HomeController {
    @Autowired
    private OffersService offersService;
    /*
    @GetMapping("/")
    //furka4you cz.1 zad 2 i 3
    public String home(Model model) {
        CarManufacturer cManuf1=offersService.getCarManufacturer(2);
        CarModel cMod1=offersService.getModel(3);     
        model.addAttribute("cManuf1", cManuf1);
        model.addAttribute("cMod1", cMod1);
        return "teeest";
    }
    */
    @GetMapping("/")  //furka4you cz.2
    public String home(Model model,OfferFilter offerFilter) { 
        /*
        WSZYSTKO W TYM KOMENTARZU TO SĄ ZADANIA WYKONYWANE W TRAKCIE ROBIENIA LABORKI
        
        List<CarManufacturer> carManufacturers = offersService.getCarManufacturers();
        model.addAttribute("carManufacturers", carManufacturers);

        List<BodyStyle> bodyStyles = offersService.getBodyStyles();// zadanie 1
        model.addAttribute("bodyStyles", bodyStyles);
        
        List<FuelType> fuelTypes = offersService.getFuelTypes();// zadanie 2
        model.addAttribute("fuelTypes", fuelTypes);
        
        List<CarModel> carModels = offersService.getCarModels(3);// test funkcji, ktora jest po zadaniu nr 1, czyli pobieranie wszystkich modeli dla producenta o id np.3
        model.addAttribute("carModels", carModels);
        
        List<Offer> offers = offersService.getOffers();// zadanie 2, lista wszystkich ofert
        model.addAttribute("offers", offers);
        
        List<Offer> offersByModelId = offersService.getOffersByModel(10);// //furka 4y cz.2 //zadanie 2, lista ofert sprzedaży określonego modelu samochodu np.2
        model.addAttribute("offersByModelId", offersByModelId);
        
        List<Offer> offersByManufacturerId = offersService.getOffersByManufacturer(12);// //furka 4y cz.2 //zadanie 2, lista ofert  ofert sprzedaży określonej marki np.12
        model.addAttribute("offersByManufacturerId", offersByManufacturerId);
        
        Offer oneOffer=offersService.getOffer(3); //furka 4y cz.2 //zadanie 2, zwracanie pojedynczej oferty na podstawie jej id np. 3
        model.addAttribute("oneOffer", oneOffer);
        return "offersList";
    */
        
        // Wyświetlanie listy ofert na stronie głównej
        List<CarManufacturer> carManufacturers = offersService.getCarManufacturers();
        List<CarModel> carModels = null;
        List<Offer> offers;
        if((offerFilter.getManufacturerId()!=null)&&(offerFilter.getModelId()==null)) { //furka4y cz.3. 
            offers = offersService.getOffersByManufacturer(offerFilter.getManufacturerId(),offerFilter.getDescription());
            carModels=offersService.getCarModels(offerFilter.getManufacturerId()); //furka f4y cz.3, Zadanie 3  DODATKOWE. 
        } 
        else if (offerFilter.getModelId()!=null) {//furka f4y cz.3, Zadanie 3  DODATKOWE. 
            offers = offersService.getOffersByModel(offerFilter.getModelId());
            carModels=offersService.getCarModels(offerFilter.getManufacturerId());
        }
        else { 
            offers = offersService.getOffers();
        }
        model.addAttribute("carManufacturers", carManufacturers);
        model.addAttribute("carModels", carModels);
        model.addAttribute("offers", offers);

        return "offersList";
    }
    @GetMapping("/offer/{id}") //metoda ta pobiera konkretną oferte i ją wyświetla, czyli akcja po naciśnięciu przycisku "pokaż" na stronie
    public String offerDetails(Model model, @PathVariable("id") Integer id) {
        Offer offer = offersService.getOffer(id);
        model.addAttribute("offer", offer);
        return "offerDetails";
    }
    @GetMapping("/newoffer") //wyświetlenie strony z formularzem dodawania nowego ogłoszenia
        public String newOfferForm(Model model, Offer offer) {
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();
            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("header", "Nowe ogłoszenie");//furka 4y cz.4
            model.addAttribute("action", "/newoffer");//furka 4y cz.4
            return "offerForm";
        }
    @PostMapping("/newoffer")//furka5y cz.3 metoda też odpowiada za zapis nowego ogłoszenai do bazy. Tutaj jako pierwszy trafia formularz wypełniony na stronie
    //jest on tutaj walidowany i jeśli walidacja się powiedzie to jest on zapisywany
    public String saveNewOffer(Model model, @Valid Offer offer, BindingResult binding) { 
        if(binding.hasErrors()) { 
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();
            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("header", "Nowe ogłoszenie");//furka 4y cz.4
            model.addAttribute("action", "/newoffer");//furka 4y cz.4
            return "offerForm";
        }
        offer = offersService.createOffer(offer);

        return "redirect:/offer/" + offer.getId();
    }   
    @RequestMapping("/deleteoffer/{id}")
        public String deleteOffer(Model model, @PathVariable("id") Integer id) { //furka4y cz.4 ta funkcja wywołuje sie gdy wciśnie sie na stronie przycisk, aby usunąć ogłoszenie
                                    
            Offer offer = offersService.deleteOffer(id); //tutaj wywołujemy konkretną metode która usuwa z  bazy danyc
            model.addAttribute("offer", offer);
            return "deleteOffer";
    }
    @GetMapping("/editoffer/{id}") //furka4y cz.4  edycja ogłoszenia. Zasada działania dokładnie taka sama jak wyżej przy usuwaniu
        public String editOffer(Model model, @PathVariable("id") Integer id) {
            Offer offer = offersService.getOffer(id);
            model.addAttribute("offer", offer);
            model.addAttribute("header", "Edycja ogłoszenia"); //furka4y cz.4
            model.addAttribute("action", "/editoffer/" + id);//furka4y cz.4
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();
            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);

            return "offerForm";
        }
        
        
        
    //furka4y cz.4 Ta funkcja  odpowiada za edytowane ogłoszenie, pododnie jak ta powyzej
        @PostMapping("/editoffer/{id}")
        public String saveEditedOffer(Model model, @PathVariable("id") Integer id, @Valid Offer offer, BindingResult binding) {
            if(binding.hasErrors()) {
                model.addAttribute("header", "Edycja ogłoszenia");
                model.addAttribute("action", "/editoffer/" + id);

                List<CarModel> carModels = offersService.getCarModels();
                List<BodyStyle> bodyStyles = offersService.getBodyStyles();
                List<FuelType> fuelTypes = offersService.getFuelTypes();

                model.addAttribute("carModels", carModels);
                model.addAttribute("bodyStyles", bodyStyles);
                model.addAttribute("fuelTypes", fuelTypes);

                return "offerForm";
            }

            offersService.saveOffer(offer);

            return "redirect:/offer/" + offer.getId();
        }
        
}
