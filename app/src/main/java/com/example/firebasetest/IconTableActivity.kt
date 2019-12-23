package com.example.firebasetest

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync


class IconTableActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var button: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activity_icons)
        setContentView(R.layout.activity_icons)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 5)

        // get the list of icons. Networking done asynchronously
        doAsync {

            /* NETWORKING HERE */
            val networkManager = NetworkManager()
            val iconList: List<HeroIcon> = networkManager.retrieveHeroIcons()

            runOnUiThread{

                /* handle networking error with alert dialogue
                if (error) ...
                else
                 */
                if (iconList.isEmpty()){

                }
                else{
                    // set the adapter
                    recyclerView.adapter = IconListAdapter(iconList, this@IconTableActivity, savedInstanceState)

                    // set up button click listener here ..
                }
            }
        }

//        button = findViewById(R.id.imageButton)
//        button.setOnClickListener {
//            val intent = Intent(this, HeroInfoActivity::class.java)
//            //intent.putExtra("LOCATION", "Washington D.C.")
//            startActivity(intent)
//        }
    }
}