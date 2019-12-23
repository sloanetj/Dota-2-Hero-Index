# Project Idea: Dota 2 Hero Information Index


Create a "Dota 2 Hero Information Index" app, where the user signs-up/logs-in using firebase authentication. Then, by using the OpenDota API, the user is presented with a table/list of the Dota 2 hero icons/names. The user clicks on an icon and is brought to a hero information page. This page includes stats about the selected hero, and also the top payer of this hero (this information is available though the OpenDota API). The top player's total play-time of Dota 2 is also displayed using the Steam Web API.

Screens:
- Login Activity
- Signup Activity
- Hero Icon Table Activity
- Hero Stats Activity

API's
- OpenDota API: https://docs.opendota.com/#
  - free, andlimited to one hundred thousand (100,000) calls to the Steam Web API per day
- Steam Web API: https://steamcommunity.com/dev
  - this is the "added complexity"
  - free, and to 50,000 free calls per month and 60 requests/minute.
