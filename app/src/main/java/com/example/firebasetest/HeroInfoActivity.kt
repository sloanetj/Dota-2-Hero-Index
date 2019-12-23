package com.example.firebasetest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync

class HeroInfoActivity : AppCompatActivity() {

    private lateinit var primary_attr: TextView
    private lateinit var attack_type: TextView
    private lateinit var roles: TextView
    private lateinit var base_health: TextView
    private lateinit var base_health_regen: TextView
    private lateinit var base_mana: TextView
    private lateinit var base_mana_regen: TextView
    private lateinit var base_armor: TextView
    private lateinit var base_mr: TextView
    private lateinit var base_attack_min: TextView
    private lateinit var base_attack_max: TextView
    private lateinit var base_str: TextView
    private lateinit var base_agi: TextView
    private lateinit var base_int: TextView
    private lateinit var str_gain: TextView
    private lateinit var agi_gain: TextView
    private lateinit var int_gain: TextView
    private lateinit var attack_range: TextView
    private lateinit var projectile_speed: TextView
    private lateinit var attack_rate: TextView
    private lateinit var move_speed: TextView
    private lateinit var turn_rate: TextView

    private lateinit var playerName: TextView
    private lateinit var score: TextView
    private lateinit var playTime: TextView
    private lateinit var playerIcon: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tells Android which XML layout file to use for this Activity
        // The "R" is short for "Resources" (e.g. accessing a layout resource in this case)
        setContentView(R.layout.activity_hero_info)

        primary_attr = findViewById(R.id.primary_attr)
        attack_type = findViewById(R.id.attack_type)
        roles = findViewById(R.id.roles)
        base_health = findViewById(R.id.base_health)
        base_health_regen = findViewById(R.id.base_health_regen)
        base_mana = findViewById(R.id.base_mana)
        base_mana_regen = findViewById(R.id.base_mana_regen)
        base_armor = findViewById(R.id.base_armor)
        base_mr = findViewById(R.id.base_mr)
        base_attack_min = findViewById(R.id.base_attack_min)
        base_attack_max = findViewById(R.id.base_attack_max)
        base_str = findViewById(R.id.base_str)
        base_agi = findViewById(R.id.base_agi)
        base_int = findViewById(R.id.base_int)
        str_gain = findViewById(R.id.str_gain)
        agi_gain = findViewById(R.id.agi_gain)
        int_gain = findViewById(R.id.int_gain)
        attack_range = findViewById(R.id.attack_range)
        projectile_speed = findViewById(R.id.projectile_speed)
        attack_rate = findViewById(R.id.attack_rate)
        move_speed = findViewById(R.id.move_speed)
        turn_rate = findViewById(R.id.turn_rate)

        playerName = findViewById(R.id.playerName)
        score = findViewById(R.id.score)
        playTime = findViewById(R.id.playTime)
        playerIcon = findViewById(R.id.playerIcon)


        val id : String? = intent.getStringExtra("HERO ID")
        // do networking - get info from HERO ID passed through intent
        // then, set layout elements
        doAsync {

            /* NETWORKING HERE */
            val steamkey = getString(R.string.steam_key)
            val networkManager = NetworkManager()
            val dataList: List<HeroData> = networkManager.retrieveHeroData(id)
            val playerInfo: List<String> = networkManager.retrieveTopPlayerData(id, steamkey)

            runOnUiThread{

                if (dataList.isEmpty() || playerInfo.isEmpty()){
                    // display networking error
                }
                else{
                    // set layout elements
                    title                   = dataList[0].statVal
                    primary_attr.text       = dataList[1].statVal
                    attack_type.text        = dataList[2].statVal
                    roles.text              = dataList[3].statVal
                    base_health.text        = dataList[4].statVal
                    base_health_regen.text  = dataList[5].statVal
                    base_mana.text          = dataList[6].statVal
                    base_mana_regen.text    = dataList[7].statVal
                    base_armor.text         = dataList[8].statVal
                    base_mr.text            = dataList[9].statVal
                    base_attack_min.text    = dataList[10].statVal
                    base_attack_max.text    = dataList[11].statVal
                    base_str.text           = dataList[12].statVal
                    base_agi.text           = dataList[13].statVal
                    base_int.text           = dataList[14].statVal
                    str_gain.text           = dataList[15].statVal
                    agi_gain.text           = dataList[16].statVal
                    int_gain.text           = dataList[17].statVal
                    attack_range.text       = dataList[18].statVal
                    projectile_speed.text   = dataList[19].statVal
                    attack_rate.text        = dataList[20].statVal
                    move_speed.text         = dataList[21].statVal
                    turn_rate.text          = dataList[22].statVal

                    playerName.text = playerInfo[0]
                    score.text = playerInfo[1]
                    Picasso
                        .get()
                        .load(playerInfo[2])
                        .error(R.drawable.abaddon_icon)
                        .into(playerIcon)
                    playTime.text = playerInfo[3]

                }
            }
        }
    }
}