package klijent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
            case 17:
                getAllKorisnik();
                break;
            case 18:
                getAllMesto();
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
                System.out.println("Korisnici:");
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
