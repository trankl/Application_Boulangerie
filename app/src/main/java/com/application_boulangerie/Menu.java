package com.application_boulangerie;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.application_boulangerie.data.Categorie;
import com.application_boulangerie.utils.AppelDialog;
import com.application_boulangerie.utils.AppelIntent;
import com.application_boulangerie.utils.AppelToast;
import com.application_boulangerie.adapter.CustomListAdapter;
import com.application_boulangerie.utils.Fonctions;
import com.application_boulangerie.utils.MyHTTPConnection;
import com.application_boulangerie.utils.MyURL;

import java.util.List;

public class Menu extends AppCompatActivity {

    ListView listView_listCategories;
    Categorie categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.listView_listCategories = findViewById(R.id.listView_listCategories);
        this.listView_listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object o = listView_listCategories.getItemAtPosition(position);
                categorie = (Categorie) o;

                AppelDialog.showAlertDialogCategorie(Menu.this, categorie);

            }
        });
    }

    // Methode onclick sur bouton "Tous produits" pour afficher tout les produits dans stock
    public void act_listProduit(View view) {
        AppelToast.displayCustomToast(this, "Aller à la page liste des produits");
        AppelIntent.appelIntentSimple(this, PageListeProduits.class);
    }

    // Methode onclick sur bouton "Matière Première" pour afficher tout les Matière Premières dans stock
    public void act_listMP(View view) {
        AppelToast.displayCustomToast(this, "Aller à la page liste des matières premières");
        AppelIntent.appelIntentSimple(this, PageListeMP.class);
    }

    // Methode onclick sur triange rouge pour afficher tout les categories
    public void act_afficher_listeCategorie(View view) {
        AppelToast.displayCustomToast(this, "Ouvrir la liste des categories");

        // Get connection to HTTP server throw Thread
        String textUrl = MyURL.TITLE.toString()+ MyURL.LISTCATEGORIE.toString();

        SendHttpRequestTaskCategorie t = new SendHttpRequestTaskCategorie();

        String[] params = new String[]{textUrl};
        t.execute(params);
    }

    private class SendHttpRequestTaskCategorie extends AsyncTask<String, Void, String> {


        //out le code qui nécessite un temps d'exécution sera placé dans cette fonction.
        // Étant donné que cette fonction est exécutée dans un thread complètement séparé du thread d'interface utilisateur, vous n'êtes pas autorisé à mettre à jour l'interface ici.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected  String  doInBackground(String... params) {
            String url = params[0];

            String result = null;
            try {
                result = MyHTTPConnection.startHttpRequestGET(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        //la resultat de doInBackground est repris et afficher à la vue
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute( String result ) {

            List<Categorie> listCategorie = Fonctions.changefromJsonListCategorie(result);

            listView_listCategories.setAdapter(new CustomListAdapter(Menu.this, listCategorie));

        }



    }

    public void act_ajouter_nouveau_categorie(View view) {

        AppelIntent.appelIntentSimple(this, PageCategorie.class);


    }

    // Methode onclick sur image "Déconnexion" pour déconnecter
    public void act_retour_page_acceuil(View view) {

        AppelDialog.showAlerDialogDeconnexion(this);

    }


}