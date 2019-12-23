package com.example.firebasetest

import android.content.Context
import android.content.res.Resources
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.UnknownHostException

class NetworkManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        // This causes all network traffic to be logged to the console
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }

    fun retrieveTopPlayerData(id: String?, key: String?):List<String>{
        val playerData = mutableListOf<String>()

        val openDotaRequest = Request.Builder()
            .url("https://api.opendota.com/api/rankings?hero_id=$id")
            .build()
        val response: Response

        try{
            response = okHttpClient.newCall(openDotaRequest).execute()
        } catch (exception: UnknownHostException) {
            // return an indicator of error
            return emptyList()
        } catch (exception: IOException) {
            return emptyList()
        }
        val responseString = response.body?.string()

        if (!responseString.isNullOrEmpty()){
            val json = JSONObject(responseString)
            val rankings = json.getJSONArray("rankings")


            var i = 0
            while(true){

                // return error indicator if none of the top players have accessible steam info
                if (i >= rankings.length()){
                    return emptyList()
                }

                val topRanking = rankings.getJSONObject(i)
                val playerName = topRanking.getString("personaname")
                val score = topRanking.getString("score")

                // get info to be forwarded to steam api call
                val accountID = topRanking.getString("account_id")
                val openDotaRequest2 = Request.Builder()
                    .url("https://api.opendota.com/api/players/$accountID")
                    .build()
                val playerResponse: Response

                try{
                    playerResponse = okHttpClient.newCall(openDotaRequest2).execute()
                } catch (exception: UnknownHostException) {
                    // return an indicator of error
                    return emptyList()
                } catch (exception: IOException) {
                    return emptyList()
                }
                val playerResponseStr = playerResponse.body?.string()

                if (!playerResponseStr.isNullOrEmpty()){
                    // ADD COMPETITIVE RANK LATER IF TIME PERMITS
                    val playerJSON = JSONObject(playerResponseStr)
                    val profile = playerJSON.getJSONObject("profile")
                    val steamid = profile.getString("steamid")
                    val avatar = profile.getString("avatarfull")

                    // steam api request to get play time
                    val steamRequest = Request.Builder()
                        .url("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=$key&steamid=$steamid&include_played_free_games=true&format=json")
                        .build()
                    val steamResponse: Response
                    try{
                        steamResponse = okHttpClient.newCall(steamRequest).execute()
                    } catch (exception: UnknownHostException) {
                        // return an indicator of error
                        return emptyList()
                    } catch (exception: IOException) {
                        return emptyList()
                    }
                    val steamResponseStr = steamResponse.body?.string()

                    if (!steamResponseStr.isNullOrEmpty()){
                        val steamJSON = JSONObject(steamResponseStr)
                        val result = steamJSON.getJSONObject("response")
                        if (!result.has("game_count")){
                            i++
                            continue
                        }
                        else{
                            val games = result.getJSONArray("games")
                            for (j in 0 until games.length()) {
                                val game = games.getJSONObject(j)
                                val gameID = game.getInt("appid")
                                if (gameID == 570){
                                    val playTimeMinutes = game.getInt("playtime_forever")
                                    val playTimeHours = (playTimeMinutes / 60)
                                    val playTime = playTimeHours.toString()

                                    // we have all the info we need
                                    playerData.add(0, playerName)
                                    playerData.add(1, score)
                                    playerData.add(2, avatar)
                                    playerData.add(3, playTime)

                                    return playerData
                                }
                                else{
                                    continue
                                }
                            }
                            i++
                            continue
                        }
                    }
                    else {
                        return emptyList()
                    }


                }
                else{
                    return emptyList()
                }


            }










        }
        else{
            return emptyList()
        }


    }
    fun retrieveHeroData(id: String?):List<HeroData>{
        val heroStats = mutableListOf<HeroData>()

        val openDotaRequest = Request.Builder()
            .url("https://api.opendota.com/api/heroStats")
            .build()
        val response: Response

        try{
            response = okHttpClient.newCall(openDotaRequest).execute()
        } catch (exception: UnknownHostException) {
            // return an indicator of error
            return emptyList()
        } catch (exception: IOException) {
            return emptyList()
        }
        val responseString = response.body?.string()

        if (!responseString.isNullOrEmpty()){

            val heroArray = JSONArray(responseString)
            var hero: JSONObject = heroArray.getJSONObject(0)

            // get the right hero
            for (i in 0 until heroArray.length()) {

                hero = heroArray.getJSONObject(i)

                val heroId = hero.getInt("id")
                if (heroId == id!!.toInt())
                    break
            }

            // we have the right hero, now add the data to the list
            var localized_name: String
            localized_name = if (hero.has("localized_name")){
                hero.getString("localized_name")
            } else {
                hero.getString("name")
            }

            val primary_attr = hero.getString("primary_attr")
            val attack_type = hero.getString("attack_type")
            val roles = hero.getJSONArray("roles").toString()
            val base_health = hero.getString("base_health")
            val base_health_regen = hero.getString("base_health_regen")
            val base_mana = hero.getString("base_mana")
            val base_mana_regen = hero.getString("base_mana_regen")
            val base_armor = hero.getString("base_armor")
            val base_mr = hero.getString("base_mr")
            val base_attack_min = hero.getString("base_attack_min")
            val base_attack_max = hero.getString("base_attack_max")
            val base_str = hero.getString("base_str")
            val base_agi = hero.getString("base_agi")
            val base_int = hero.getString("base_int")
            val str_gain = hero.getString("str_gain")
            val agi_gain = hero.getString("agi_gain")
            val int_gain = hero.getString("int_gain")
            val attack_range = hero.getString("attack_range")
            val projectile_speed = hero.getString("projectile_speed")
            val attack_rate = hero.getString("attack_rate")
            val move_speed = hero.getString("move_speed")
            val turn_rate = hero.getString("turn_rate")

            heroStats.add(0,HeroData("localized_name", localized_name))
            heroStats.add(1,HeroData("primary_attr", primary_attr))
            heroStats.add(2,HeroData("attack_type", attack_type))
            heroStats.add(3,HeroData("roles", roles))
            heroStats.add(4,HeroData("base_health", base_health))
            heroStats.add(5,HeroData("base_health_regen", base_health_regen))
            heroStats.add(6,HeroData("base_mana", base_mana))
            heroStats.add(7,HeroData("base_mana_regen", base_mana_regen))
            heroStats.add(8,HeroData("base_armor", base_armor))
            heroStats.add(9,HeroData("base_mr", base_mr))
            heroStats.add(10,HeroData("base_attack_min", base_attack_min))
            heroStats.add(11,HeroData("base_attack_max", base_attack_max))
            heroStats.add(12,HeroData("base_str", base_str))
            heroStats.add(13,HeroData("base_agi", base_agi))
            heroStats.add(14,HeroData("base_int", base_int))
            heroStats.add(15,HeroData("str_gain", str_gain))
            heroStats.add(16,HeroData("agi_gain", agi_gain))
            heroStats.add(17,HeroData("int_gain", int_gain))
            heroStats.add(18,HeroData("attack_range", attack_range))
            heroStats.add(19,HeroData("projectile_speed", projectile_speed))
            heroStats.add(20,HeroData("attack_rate", attack_rate))
            heroStats.add(21,HeroData("move_speed", move_speed))
            heroStats.add(21,HeroData("turn_rate", turn_rate))

            return heroStats
        }
        // handle error where no results
        else {
            return emptyList()
        }

    }

    fun retrieveHeroIcons():List<HeroIcon>{

        val heroIcons = mutableListOf<HeroIcon>()
        // get heroes from OpenDota api
        val openDotaRequest = Request.Builder()
            .url("https://api.opendota.com/api/heroStats")
            .build()
        val response: Response
        try{
            response = okHttpClient.newCall(openDotaRequest).execute()
        } catch (exception: UnknownHostException) {
            // return an indicator of error
            return emptyList()
        } catch (exception: IOException) {
            return emptyList()
        }
        val responseString = response.body?.string()

        if (!responseString.isNullOrEmpty()){
            val heroArray = JSONArray(responseString)

            // add each review's data to the list
            for (i in 0 until heroArray.length()) {

                val hero = heroArray.getJSONObject(i)

                // get id
                val id = hero.getInt("id").toString()


                // get hero icon
                val imgUrl = "https://api.opendota.com" + hero.getString("img")
                Log.e("Network Manager", imgUrl)

                // add icon to list
                val heroIcon = HeroIcon(id, imgUrl)
                heroIcons.add(heroIcon)
            }
            return heroIcons
        }
        // handle error where no results
        else {
            return emptyList()
        }


    }
}