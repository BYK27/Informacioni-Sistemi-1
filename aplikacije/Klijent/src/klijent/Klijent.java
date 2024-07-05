package klijent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Klijent
{
    private static Scanner scanner ;
    
    public static void main(String[] args)
    {
        scanner = new Scanner(System.in);
        while(true)
        {
            printChoices();
            int choice = getChoice();
            performChoice(choice);
        }
        
    }
    
    private static void printChoices()
    {
        String choices = "\n1.  Kreiranje grada \n" +
        "2.  Kreiranje korisnika \n" +
        "3.  Promena email adrese za korisnika \n" +
        "4.  Promena mesta za korisnika \n" +
        "5.  Kreiranje kategorije \n" +
        "6.  Kreiranje video snimka \n" +
        "7.  Promena naziva video snimka \n" +
        "8.  Dodavanje kategorije video snimku \n" +
        "9.  Kreiranje paketa \n" +
        "10. Promena mesečne cene za paket \n" +
        "11. Kreiranje pretplate korisnika na paket \n" +
        "12. Kreiranje gledanja video snimka od strane korisnika \n" +
        "13. Kreiranje ocene korisnika za video snimak \n" +
        "14. Menjanje ocene korisnika za video snimak \n" +
        "15. Brisanje ocene korisnika za video snimak \n" +
        "16. Brisanje video snimka od strane korisnika koji ga je kreirao \n" +
        "17. Dohvatanje svih mesta \n" +
        "18. Dohvatanje svih korisnika \n" +
        "19. Dohvatanje svih kategorija \n" +
        "20. Dohvatanje svih video snimaka \n" +
        "21. Dohvatanje kategorija za određeni video snimak \n" +
        "22. Dohvatanje svih paketa \n" +
        "23. Dohvatanje svih pretplata za korisnika \n" +
        "24. Dohvatanje svih gledanja za video snimak \n" +
        "25. Dohvatanje svih ocena za video snimak ";
        System.out.println("Hello! Please choose an option:");
        System.out.println(choices + "\n");
    }

    private static int getChoice()
    {
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        //debug
        if(choice == -1) return -1;
        
        if(choice < 1 || choice > 25)
        {
            System.err.println("Morate izabrati broj koji oznacava funkcionalnost.");
        }
        
        return choice;
    }

    private static void performChoice(int choice)
    {
        switch(choice)
        {
            case 1:
                createMesto();
                break;
            case 2:
                createKorisnik();
                break;
            case 3:
                updateEmail();
                break;
            case 4:
                updateMesto();
                break;
            case 5:
                createKategorija();
                break;
            case 6:
                createVideo();
                break;
            case 7:
                updateNaziv();
                break;
            case 8:
                updateKategorija();
                break;
            case 9:
                createPaket();
                break;
            case 10:
                updateCena();
                break;
            case 11:
                createPretplata();
                break;
            case 12:
                createGledanje();
                break;
            case 13:
                createOcena();
                break;
            case 14:
                updateOcena();
                break;
            case 15:
                deleteOcena();
                break;
            case 16:
                deleteVideo();
                break;
            case 17:
                getAllKorisnik();
                break;
            case 18:
                getAllMesto();
                break;
            case 19:
                getAllKategorija();
                break;
            case 20:
                getAllVideo();
                break;
            case 21:
                getAllKategorijaByVideo();
                break;
            case 22:
                getAllPaket();
                break;
            case 23:
                getAllPretplataByKorisnik();
                break;
            case 24:
                getAllGledanjeByVideo();
                break;
            case 25:
                getAllOcenaByVideo();
                break;
            case -1:
                test();
                break;
        }
    }
    
    private static void createMesto()
    {
        System.out.println("Kreiranje Mesta zapoceto. ");
        System.out.print("Naziv mesta: " );
        String naziv = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query1/" + naziv;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createKorisnik()
    {
       System.out.println("Kreiranje Korisnika zapoceto.");
        System.out.print("Ime korisnika: ");
        String ime = scanner.nextLine();
        
        System.out.print("Email adresa korisnika: ");
        String email = scanner.nextLine();
        
        System.out.print("Godiste korisnika: ");
        int godiste = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Pol korisnika (M / Z): ");
        String pol = scanner.nextLine();
        
        System.out.print("Mesto korisnika: ");
        String mesto = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query2/" + ime + "/" + email + "/" + godiste + "/" + pol + "/" + mesto;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateEmail()
    {
        System.out.println("Azuriranje Korisnickog Email-a zapoceto. ");
        System.out.print("Korisnicko ime: " );
        String Korisnik = scanner.nextLine();
        System.out.print("Nova email adresa: " );
        String Email = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query3/" + Korisnik + "/" + Email;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateMesto()
    {
        System.out.println("Azuriranje Korisnickog mesta zapoceto. ");
        System.out.print("Korisnicko ime: " );
        String Korisnik = scanner.nextLine();
        System.out.print("Novo mesto: " );
        String Mesto = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query4/" + Korisnik + "/" + Mesto;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createKategorija()
    {
        System.out.println("Kreiranje Kategorije zapoceto. ");
        System.out.print("Naziv kategorije: " );
        String naziv = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query5/" + naziv;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createVideo()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("Kreiranje Video zapoceto. ");
        System.out.print("Naziv videa: " );
        String naziv = scanner.nextLine();
        System.out.print("Trajanje videa: " );
        Double trajanje = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Korisnik videa: " );
        String korisnik = scanner.nextLine();
        System.out.print("Trajanje videa: " );

        System.out.print("Datum postavljanja videa (format: yyyy-MM-dd HH:mm:ss): ");
        String datumString = scanner.nextLine();

        Date datumPostavljanja = null;
        try
        {
            datumPostavljanja = dateFormat.parse(datumString);
            
        } catch (ParseException ex)
        {
            System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
            return;
        }
        
        String encodedNaziv = naziv;
        String encodedDatumString = datumString;
        
        try
        {
            encodedNaziv = URLEncoder.encode(naziv, StandardCharsets.UTF_8.toString());
            encodedDatumString = URLEncoder.encode(datumString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String urlString = "http://localhost:8080/Server/api/server/query6/" + encodedNaziv + "/" + trajanje + "/" + korisnik + "/" + encodedDatumString;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateNaziv()
    {
        System.out.println("Azuriranje Naziv zapoceto. ");
        System.out.print("Naziv videa: " );
        String nazivOld = scanner.nextLine();
        System.out.print("Novi naziv videa: " );
        String nazivNew = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query7/" + nazivOld + "/" + nazivNew;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateKategorija()
    {
        System.out.println("Dodavanje Kategorija zapoceto. ");
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        System.out.print("Naziv kategorija: " );
        String kategorija = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query8/" + video + "/" + kategorija;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createPaket()
    {
        System.out.println("Kreiranje Paket zapoceto. ");
        System.out.print("Naziv paketa: " );
        String naziv = scanner.nextLine();
        System.out.print("Cena paketa: " );
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query9/" + naziv + "/" + cena;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateCena()
    {
        System.out.println("Azuiranje Cena zapoceto. ");
        System.out.print("Naziv paketa: " );
        String naziv = scanner.nextLine();
        System.out.print("Nova cena paketa: " );
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query10/" + naziv + "/" + cena;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createPretplata()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("Kreiranje Pretplata zapoceto. ");
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        System.out.print("Naziv paketa: " );
        String paket = scanner.nextLine();
        
        System.out.print("Nova cena paketa: " );
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Datum pocetka pretplate (format: yyyy-MM-dd HH:mm:ss): ");
        String datumString = scanner.nextLine();

        Date datumPostavljanja = null;
        try
        {
            datumPostavljanja = dateFormat.parse(datumString);
            
        } catch (ParseException ex)
        {
            System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
            return;
        }
        
        String encodedDatumString = datumString;
        
        try
        {
            encodedDatumString = URLEncoder.encode(datumString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String urlString = "http://localhost:8080/Server/api/server/query11/" + korisnik + "/" + paket + "/" + cena + "/" + encodedDatumString;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createGledanje()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("Kreiranje Gledanje zapoceto. ");
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();

        
        System.out.print("Datum pocetka gledanja (format: yyyy-MM-dd HH:mm:ss): ");
        String datumString = scanner.nextLine();

        Date datumPostavljanja = null;
        try
        {
            datumPostavljanja = dateFormat.parse(datumString);
            
        } catch (ParseException ex)
        {
            System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
            return;
        }
        
        String encodedDatumString = datumString;
        
        try
        {
            encodedDatumString = URLEncoder.encode(datumString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.print("Sekund pocetka: " );
        Integer SekundPocetka = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Sekund odgledano: " );
        Integer SekundOdgledano = scanner.nextInt();
        scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query12/" + korisnik + "/" + video + "/" + encodedDatumString + "/" + SekundPocetka + "/" + SekundOdgledano;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void createOcena()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("Kreiranje Ocena zapoceto. ");
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        
        System.out.print("Ocena (1 - 5): " );
        Integer ocena = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Datum pocetka gledanja (format: yyyy-MM-dd HH:mm:ss): ");
        String datumString = scanner.nextLine();

        Date datumPostavljanja = null;
        try
        {
            datumPostavljanja = dateFormat.parse(datumString);
            
        } catch (ParseException ex)
        {
            System.out.println("Datum nije u ispravnom formatu. Pokušajte ponovo.");
            return;
        }
        
        String encodedDatumString = datumString;
        
        try
        {
            encodedDatumString = URLEncoder.encode(datumString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String urlString = "http://localhost:8080/Server/api/server/query13/" + korisnik + "/" + video + "/" + ocena + "/" + encodedDatumString;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void updateOcena()
    {
        
        System.out.println("Azuriranje Ocena zapoceto. ");
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        
        System.out.print("Nova ocena (1 - 5): " );
        Integer ocena = scanner.nextInt();
        scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query14/" + korisnik + "/" + video + "/" + ocena;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void deleteOcena()
    {
        
        System.out.println("Brisanje Ocena zapoceto. ");
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query15/" + korisnik + "/" + video;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void deleteVideo()
    {
        System.out.println("Brisanje Video zapoceto. ");
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        
        String urlString = "http://localhost:8080/Server/api/server/query16/" + video + "/" + korisnik;
        sendHttpRequest(urlString, "POST");
    }
    
    private static void getAllKorisnik()
    {
        String urlString = "http://localhost:8080/Server/api/server/query17";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllMesto()
    {
        String urlString = "http://localhost:8080/Server/api/server/query18";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllKategorija()
    {
        String urlString = "http://localhost:8080/Server/api/server/query19";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllVideo()
    {
        String urlString = "http://localhost:8080/Server/api/server/query20";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllKategorijaByVideo()
    {
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        String urlString = "http://localhost:8080/Server/api/server/query21/" + video;
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllPaket()
    {
        String urlString = "http://localhost:8080/Server/api/server/query22";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllPretplataByKorisnik()
    {
        System.out.print("Naziv korisnika: " );
        String korisnik = scanner.nextLine();
        String urlString = "http://localhost:8080/Server/api/server/query23/" + korisnik;
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllGledanjeByVideo()
    {
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        String urlString = "http://localhost:8080/Server/api/server/query24/" + video;
        sendHttpRequest(urlString, "GET");
    }
    
    private static void getAllOcenaByVideo()
    {
        System.out.print("Naziv videa: " );
        String video = scanner.nextLine();
        String urlString = "http://localhost:8080/Server/api/server/query25/" + video;
        sendHttpRequest(urlString, "GET");
    }
    
    private static void test()
    {
        String urlString = "http://localhost:8080/Server/api/server/test";
        sendHttpRequest(urlString, "GET");
    }
    
    private static void sendHttpRequest(String urlString, String method) {
        try 
        {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            //httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            //httpURLConnection.setRequestProperty("Accept", "application/json");

            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) 
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON response
                Gson gson = new Gson();
                JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
                ArrayList<String> korisnici = new ArrayList<>();

                for (JsonElement jsonElement : jsonArray) {
                    korisnici.add(gson.toJson(jsonElement));
                }

                // Print all Korisnik
                System.out.println("Lista:");
                korisnici.forEach(korisnik ->
                {
                    System.out.println(korisnik.replace("\"", ""));
                });
            }
            
        } catch (JsonSyntaxException | IOException e) 
        {
        }
    }

}
