package com.alphabetlistview;

import com.alphabetlistview.AlphabetListView.AlphabetPositionListener;
import com.liuxiaofei.atest.R;

import android.app.Activity;
import android.os.Bundle;  
import android.widget.ArrayAdapter;  
  
public class AlphabetActivity extends Activity {  
    private AlphabetListView alphabetListView;  
      
    private String[] mStrings = {  
            "中文", "Adelost", "Affidelice au Chablis",  
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",  
            "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",  
            "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh",  
            "Anthoriro", "Appenzell", "Aragon", "Ardi Gasna", "Ardrahan",  
            "Armenian String", "Aromes au Gene de Marc", "Asadero", "Asiago",  
            "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss", "Babybel",  
            "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal",  
            "Banon", "Barry's Bay Cheddar", "Basing", "Basket Cheese",  
            "Bath Cheese", "Bavarian Bergkase", "Baylough", "Beaufort",  
            "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",  
            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir",  
            "Bierkase", "Bishop Kennedy", "Blarney", "Bleu d'Auvergne",  
            "Bleu de Gex", "Bleu de Laqueuille", "Bleu de Septmoncel",  
            "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",  
            "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",  
            "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase",  
            "Button (Innes)", "Buxton Blue", "Cabecou", "Caboc", "Cabrales",  
            "Cachaille", "Caciocavallo", "Caciotta", "Caerphilly",  
            "Cream Havarti", "Crema Agria", "Crema Mexicana", "Creme Fraiche",  
            "Crescenza", "Croghan", "Crottin de Chavignol",  
            "Crottin du Chavignol", "Crowdie", "Crowley", "Cuajada", "Curd",  
            "Cure Nantais", "Curworthy", "Cwmtawe Pecorino",  
            "Cypress Grove Chevre", "Danablu (Danish Blue)", "Danbo",  
            "Danish Fontina", "Daralagjazsky", "Dauphin", "Delice des Fiouves",  
            "Denhany Dorset Drum", "Derby", "Dessertnyj Belyj", "Devon Blue",  
            "Devon Garland", "Dolcelatte", "Doolin", "Doppelrhamstufel",  
            "Dorset Blue Vinney", "Double Gloucester", "Double Worcester",  
            "Dreux a la Feuille", "Dry Jack", "Duddleswell", "Dunbarra",  
            "Dunlop", "Dunsyre Blue", "Duroblando", "Durrus",  
            "Dutch Mimolette (Commissiekaas)", "Edam", "Edelpilz",  
            "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne",  
            "Esbareich", "Esrom", "Etorki", "Evansdale Farmhouse Brie",  
            "Evora De L'Alentejo", "Exmoor Blue", "Explorateur", "Feta",  
            "Feta (Australian)", "Figue", "Filetta", "Fin-de-Siecle",  
            "Finlandia Swiss", "Finn", "Fiore Sardo", "Fleur du Maquis",  
            "Flor de Guia", "Flower Marie", "Folded",  
            "Folded cheese with mint", "Fondant de Brebis", "Fontainebleau",  
            "Fontal", "Fontina Val d'Aosta", "Formaggio di capra", "Fougerus",  
            "Four Herb Gouda", "Fourme d' Ambert", "Fourme de Haute Loire",  
            "Fourme de Montbrison", "Fresh Jack", "Fresh Mozzarella",  
            "Fresh Ricotta", "Fresh Truffles", "Fribourgeois", "Friesekaas",  
            "Friesian", "Friesla", "Frinault", "Fromage a Raclette",  
            "Fromage Corse", "Fromage de Montagne de Savoie", "Fromage Frais",  
            "Fruit Cream Cheese", "Frying Cheese", "Fynbo", "Gabriel",  
            "Galette du Paludier", "Galette Lyonnaise",  
            "Galloway Goat's Milk Gems", "Gammelost", "Gaperon a l'Ail",  
            "Garrotxa", "Gastanberra", "Geitost", "Gippsland Blue", "Gjetost",  
            "Gloucester", "Golden Cross", "Gorgonzola", "Gornyaltajski",  
            "Gospel Green", "Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",  
            "Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",  
            "Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh",  
            "Greve", "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny",  
            "Halloumi", "Halloumy (Australian)", "Haloumi-Style Cheese",  
            "Harbourne Blue", "Havarti", "Heidi Gruyere", "Hereford Hop",  
            "Herrgardsost", "Herriot Farmhouse", "Herve", "Hipi Iti",  
            "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",  
            "Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu",  
            "Isle of Mull", "Jarlsberg", "Jermi Tortes", "Jibneh Arabieh",  
            "Jindi Brie", "Jubilee Blue", "Juustoleipa", "Kadchgall", "Kaseri",  
            "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",  
            "Kikorangi", "King Island Cape Wickham Brie", "King River Gold",  
            "Klosterkaese", "Knockalara", "Kugelkase", "L'Aveyronnais",  
            "L'Ecir de l'Aubrac", "La Taupiniere", "La Vache Qui Rit",  
            "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",  
            "Langres", "Lappi", "Laruns", "Lavistown", "Le Brin",  
            "Le Fium Orbo", "Le Lacandou", "Le Roule", "Leafield", "Lebbene",  
            "Leerdammer", "Leicester", "Leyden", "Limburger",  
            "Lincolnshire Poacher", "Lingot Saint Bousquet d'Orb", "Liptauer",  
            "Little Rydings", "Livarot", "Llanboidy", "Llanglofan Farmhouse",  
            "Loch Arthur Farmhouse", "Loddiswell Avondale", "Longhorn",  
            "Lou Palou", "Lou Pevre", "Lyonnais", "Maasdam", "Macconais",  
            "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",  
            "Manouri", "Manur", "Marble Cheddar", "Marbled Cheeses",  
            "Maredsous", "Margotin", "Maribo", "Maroilles", "Mascares",  
            "Mascarpone", "Mascarpone (Australian)", "Mascarpone Torta",  
            "Murol", "Mycella", "Myzithra", "Naboulsi", "Nantais",  
            "Neufchatel", "Neufchatel (Australian)", "Niolo", "Nokkelost",  
            "Northumberland", "Oaxaca", "Olde York", "Olivet au Foin",  
            "Olivet Bleu", "Olivet Cendre", "Orkney Extra Mature Cheddar",  
            "Orla", "Oschtjepka", "Ossau Fermier", "Ossau-Iraty", "Oszczypek",  
            "Oxford Blue", "P'tit Berrichon", "Palet de Babligny", "Paneer",  
            "Panela", "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)",  
            "Parmigiano Reggiano", "Pas de l'Escalette", "Passendale",  
            "Pasteurized Processed", "Pate de Fromage", "Patefine Fort",  
            "Pourly", "Prastost", "Pressato", "Prince-Jean",  
            "Processed Cheddar", "Provolone", "Provolone (Australian)",  
            "Pyengana Cheddar", "Pyramide", "Quark", "Quark (Australian)",  
            "Quartirolo Lombardo", "Quatre-Vents", "Quercy Petit",  
            "Queso Blanco", "Queso Blanco con Frutas --Pina y Mango",  
            "Queso de Murcia", "Queso del Montsec", "Queso del Tietar",  
            "Queso Fresco", "Queso Fresco (Adobera)", "Queso Iberico",  
            "Queso Jalapeno", "Queso Majorero", "Queso Media Luna",  
            "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette",  
            "Ragusano", "Raschera", "Reblochon", "Red Leicester",  
            "Regal de la Dombes", "Reggianito", "Remedou", "Requeson",  
            "Richelieu", "Ricotta", "Ricotta (Australian)", "Ricotta Salata",  
            "Ridder", "Rigotte", "Rocamadour", "Rollot", "Romano",  
            "Romans Part Dieu", "Roncal", "Roquefort", "Roule",  
            "Rouleau De Beaulieu", "Royalp Tilsit", "Rubens", "Rustinu",  
            "Saaland Pfarr", "Saanenkaese", "Saga", "Sage Derby",  
            "Sainte Maure", "Saint-Marcellin", "Saint-Nectaire",  
            "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",  
            "Sveciaost", "Swaledale", "Sweet Style Swiss", "Swiss",  
            "Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",  
            "Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea",  
            "Testouri", "Tete de Moine", "Tetilla", "Texas Goat Cheese",  
            "Tronchon", "Trou du Cru", "Truffe", "Tupi", "Turunmaa",  
            "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",  
            "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco",  
            "Vendomois", "Vieux Corse", "Vignotte", "Vulscombe",  
            "Waimata Farmhouse Blue", "Washed Rind Cheese (Australian)",  
            "Waterloo", "Weichkaese", "Wellington", "Wensleydale",  
            "White Stilton", "Whitestone Farmhouse", "Wigmore",  
            "Woodside Cabecou", "Xanadu", "Xynotyro", "Yarg Cornish",  
            "Yarra Valley Pyramid", "Yorkshire Blue", "Zamorano",  
            "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"};  
  
  
    private AlphabetPositionListener positionListener = new AlphabetPositionListener() {  
        @Override  
        public int getPosition(String letter) {  
            for(int i=0, count=mStrings.length; i<count; i++){  
                Character firstLetter = mStrings[i].charAt(0);  
                if(firstLetter.toString().equals(letter)){  
                    return i;  
                }  
            }  
            return UNKNOW;  
        }  
    };  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
          
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,  
                android.R.layout.simple_list_item_1, mStrings);  
  
        alphabetListView = (AlphabetListView)findViewById(R.id.list_view);  
        alphabetListView.setAdapter(mArrayAdapter, positionListener);  
    }  
}  